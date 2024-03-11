package com.domzky.gymbooking.Sessions.Admin.pages.Videos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.CoachBooking;
import com.domzky.gymbooking.Helpers.Things.Videos;
import com.domzky.gymbooking.Helpers.Users.GymCoach;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.Members.pages.CoachesList.CoachesListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViedosFragment extends Fragment {
    private DatabaseReference dbWriteVideo = new FirebaseHelper().getVideos();
    ArrayList<LinkItem> linkItems = new ArrayList<>();
    private ListView listView;
    private CustomListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_vieos, container, false);

        listView = view.findViewById(R.id.listView);
        // Sample JSON data
//        String jsonData = "[\n" +
//                "    {\n" +
//                "        \"title\": \"sample video\",\n" +
//                "        \"video\": \"https://www.youtube.com/watch?v=4TuXhWF78rY\",\n" +
//                "        \"thumbnail\":\"https://img.youtube.com/vi/4TuXhWF78rY/default.jpg\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "        \"title\": \"sample video\",\n" +
//                "        \"video\": \"https://www.youtube.com/watch?v=4TuXhWF78rY\",\n" +
//                "        \"thumbnail\":\"https://img.youtube.com/vi/4TuXhWF78rY/default.jpg\"\n" +
//                "    }\n" +
//                "    \n" +
//                "]";
//        // Parse JSON and populate ArrayList of LinkItems
//        ArrayList<LinkItem> linkItems = parseJsonData(jsonData);
        // Create custom adapter and bind it to ListView
        adapter = new CustomListAdapter(getActivity(), R.layout.list_items_videos, linkItems);
        listView.setAdapter(adapter);

        dbWriteVideo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                linkItems.clear();
                for (DataSnapshot snap: snapshot.getChildren()) {
                    linkItems.add(new LinkItem(
                            snap.child("title").getValue(String.class),
                            snap.child("link").getValue(String.class),
                            snap.child("thumbnail").getValue(String.class)
                    ));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FIREBASE ERR", error.getMessage());
            }
        });


        FloatingActionButton fabAddVideo = view.findViewById(R.id.fabAddVideo);
        fabAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddVideoDialog();
            }
        });

        return view;
    }

    private ArrayList<LinkItem> parseJsonData(String jsonData) {


        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String videoUrl = jsonObject.getString("video");
                String thumbnail = jsonObject.getString("thumbnail");

                // Extract image URL from video URL (for simplicity, assuming it's available in the JSON)
                linkItems.add(new LinkItem(title,videoUrl, thumbnail));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return linkItems;
    }

    private String extractImageUrl(String videoUrl) {
        // Logic to extract image URL from video URL
        // For simplicity, let's assume you have the image URL directly in the JSON.
        // You can use a library like YouTube API to fetch thumbnail URLs if needed.
        return "Image URL";
    }

    private void showAddVideoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_videos, null);

        EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        EditText editTextLink = dialogView.findViewById(R.id.editTextLink);
        EditText editTextThumbnail = dialogView.findViewById(R.id.editTextThumbnail);

        builder.setView(dialogView)
                .setTitle("Add Video")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = editTextTitle.getText().toString().trim();
                        String link = editTextLink.getText().toString().trim();
                        String thumbnail = "https://img.youtube.com/vi/"+editTextThumbnail.getText().toString().trim()+"/default.jpg";

                        // Add the new video to your list
                        linkItems.add(new LinkItem(title, link, thumbnail));

                        AlertDialog.Builder successBuilder = new AlertDialog.Builder(getContext());
                        successBuilder.setMessage("Video add successfully! "+title+ "")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Do something when the user clicks OK
                                        String uuid = dbWriteVideo.push().getKey();
                                        dbWriteVideo.child(uuid).setValue(new Videos(
                                                title,
                                                link,
                                                thumbnail
                                        )).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("FIREBASE ERROR",""+ e.getMessage());

                                            }
                                        });
                                    }
                                });
                        successBuilder.create().show();
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.create().show();
    }

}