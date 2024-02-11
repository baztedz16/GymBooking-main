package com.domzky.gymbooking.Sessions.GymOwner.pages.Membership;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.FieldSyntaxes.MoneyTextWatcher;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Membership;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.GymOwner.pages.Membership.ModifyMembership.ModifyMembershipActivity;
import com.domzky.gymbooking.Sessions.GymOwner.pages.Membership.ShowJoined.ShowJoinedmembershipActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MembershipListAdapter extends RecyclerView.Adapter<MembershipListAdapter.ViewHolder> {

    List<Membership> list;
    Context wholeContext;

    DatabaseReference db = new FirebaseHelper().getMembershipReference();

    public MembershipListAdapter(List<Membership> list,Context wholeContext) {
        this.list = list;
        this.wholeContext = wholeContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView membership_name,members_joined;
        ImageButton editBtn,deletebtn,infoBtn;
        LinearLayout joinedBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            membership_name = itemView.findViewById(R.id.membership_adapter_item_name);
            members_joined = itemView.findViewById(R.id.membership_adapter_item_joined);
            editBtn = itemView.findViewById(R.id.membership_adapter_item_btn_edit);
            deletebtn = itemView.findViewById(R.id.membership_adapter_item_btn_delete);
            infoBtn = itemView.findViewById(R.id.membership_adapter_item_btn_info);
            joinedBtn = itemView.findViewById(R.id.membership_adapter_item_btn_joined);

            infoBtn.setVisibility(View.GONE);

        }
    }

    @NonNull
    @Override
    public MembershipListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.membership_adapter_item, parent, false);
        MembershipListAdapter.ViewHolder viewHolder = new MembershipListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Membership membership = list.get(position);

        holder.membership_name.setText(membership.name);
        holder.members_joined.setText(String.valueOf(membership.joined));

        holder.joinedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wholeContext.startActivity(new Intent(wholeContext,ShowJoinedmembershipActivity.class));
            }
        });

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progress = new ProgressDialog(wholeContext);
                progress.setCancelable(false);
                progress.setMessage("Removing Membership");
                AlertDialog.Builder builder = new AlertDialog.Builder(wholeContext);
                builder.setTitle("Delete " + membership.name + " Membership");
                builder.setMessage("Are you sure you want to remove this membership?");
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // DO NOTHING
                    }
                });
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progress.show();
                        db.child(membership.membership_id).child("deleted").setValue(true)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progress.dismiss();
                                        Toast.makeText(wholeContext,"Membership Removed.",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("FIREBASE ERROR",""+ e.getMessage());
                                        Toast.makeText(wholeContext,"Membership Removing Failed. Please Try Again",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                builder.create().show();
            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wholeContext.startActivity(new Intent(wholeContext, ModifyMembershipActivity.class)
                        .putExtra("membership_uid",membership.membership_id)
                        .putExtra("membership_name",membership.name)
                        .putExtra("membership_description",membership.description)
                        .putExtra("membership_price",new MoneyTextWatcher().convertToCurrency(membership.price))
                );

//                Log.d("membership_uid",membership.membership_id);
//                Log.d("membership_name",membership.name);
//                Log.d("membership_description",membership.description);
//                Log.d("membership_price",new MoneyTextWatcher().convertToCurrency(membership.price));
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
