package com.domzky.gymbooking.Sessions.Admin.pages.Videos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.domzky.gymbooking.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

public class CustomListAdapter extends ArrayAdapter<LinkItem> {

    private Context mContext;
    private int mResource;

    public CustomListAdapter(Context context, int resource, ArrayList<LinkItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
        }

        LinkItem currentItem = getItem(position);

        ImageView imagePreview = convertView.findViewById(R.id.imagePreview);
        TextView titleTextView = convertView.findViewById(R.id.titleTextView);

        // Load image preview using Picasso library
        Picasso.get().load(currentItem.getImageUrl()).into(imagePreview);

        titleTextView.setText(currentItem.getTitle());

        imagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the URL when imagePreview is clicked
                String url = currentItem.getvideoUrl(); // Replace this with the actual URL you want to open
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(intent);
            }
        });


        return convertView;
    }
}
