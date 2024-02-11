package com.domzky.gymbooking.Sessions.Members.pages.Dashboard.GymSelection.GymItem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Membership;
import com.domzky.gymbooking.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class GymItemActivity extends AppCompatActivity {

    private String gymid;

    private DatabaseReference db = new FirebaseHelper().getRootReference();

    private RecyclerView recview;

    private List<Membership> list;

    private TextView gymName,gymOwner,gymAddress;

    private SharedPreferences preferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_gym_item);

        gymName = findViewById(R.id.member_act_gym_item_name);
        gymOwner = findViewById(R.id.member_act_gym_item_owner);
        gymAddress = findViewById(R.id.member_act_gym_item_addresa);

        gymid = getIntent().getStringExtra("gym_id");

        preferences = this.getSharedPreferences("member",MODE_PRIVATE);

        recview = findViewById(R.id.member_gym_membership_recview);
        list = new ArrayList<>();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                DataSnapshot snapGym = snapshot.child("Gyms").child(gymid);
                DataSnapshot snapOwner = snapshot.child("Owners").child(gymid);

                gymName.setText(snapGym.child("gym_name").getValue(String.class));
                gymOwner.setText(snapOwner.child("fullname").getValue(String.class));
                gymAddress.setText(snapGym.child("gym_address").getValue(String.class));
                list.clear();
                for ( DataSnapshot snapMem: snapshot.child("Memberships").getChildren() ) {

                    if (
                            snapMem.child("gym_id").getValue(String.class).equals(gymid)
                            && snapMem.child("deleted").getValue(Boolean.class) == false
                    ) {
                        list.add(new Membership(
                                snapMem.getKey(),
                                snapMem.child("name").getValue(String.class),
                                snapMem.child("gym_id").getValue(String.class),
                                snapMem.child("price").getValue(Double.class),
                                snapMem.child("description").getValue(String.class),
                                snapMem.child("deleted").getValue(Boolean.class)
                        ));
                    }


                }
                recview.setAdapter(new GymItemMembershipAdapter(
                        list,GymItemActivity.this,
                        getSharedPreferences("member",MODE_PRIVATE).getString("userid","")
                ));
                recview.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("FIREBASE ERR", error.getMessage());
            }
        });


    }
}