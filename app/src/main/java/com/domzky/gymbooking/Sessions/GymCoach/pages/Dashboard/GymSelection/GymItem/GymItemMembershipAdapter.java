package com.domzky.gymbooking.Sessions.GymCoach.pages.Dashboard.GymSelection.GymItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.domzky.gymbooking.Helpers.FieldSyntaxes.MoneyTextWatcher;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Membership;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.UsersActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GymItemMembershipAdapter extends RecyclerView.Adapter<GymItemMembershipAdapter.ViewHolder> {

    public DatabaseReference dbMember = new FirebaseHelper().getUserReference("Members");

    public List<Membership> list;
    public String member_id;
    public Context wholeContext;
    public ProgressDialog progress;

    public GymItemMembershipAdapter(List<Membership> list,Context wholeContext,String member_id) {
        this.list = list;
        this.wholeContext = wholeContext;
        this.member_id = member_id;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView membership_name;
        ImageButton infoBtn;
        LinearLayout itemClick,portionHidden;
        public ViewHolder(View itemView) {
            super(itemView);
            infoBtn = itemView.findViewById(R.id.membership_adapter_item_btn_info);
            itemClick = itemView.findViewById(R.id.membership_adapter_click);
            portionHidden = itemView.findViewById(R.id.membership_adapter_o_hidden);
            membership_name = itemView.findViewById(R.id.membership_adapter_item_name);

            portionHidden.setVisibility(View.GONE);
        }

    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.membership_adapter_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Membership membership = list.get(position);

        holder.membership_name.setText(membership.name);

        holder.itemClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(holder.itemClick.getContext());
                progress.setCancelable(false);
                progress.setMessage("Joining Membership");

                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemClick.getContext());
                builder.setMessage("Are You Sure you want to Join this " + membership.name + " Membership?");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progress.show();
                        dbMember.child(member_id).child("Membership").setValue(new Membership(
                                            membership.membership_id,
                                            membership.gym_id
                                    ))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        progress.dismiss();
                                        AlertDialog.Builder buildExit = new AlertDialog.Builder(wholeContext);
                                        buildExit.setMessage("To have overall changes, please login again to the app and proceed.")
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        wholeContext.startActivity(new Intent(wholeContext,UsersActivity.class));
                                                        ((Activity) wholeContext).finishAffinity();
                                                    }
                                                });
                                        buildExit.setCancelable(false).create().show();
                                    }
                                });
                    }
                });builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // NOTHING
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
            }
        });

        holder.infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemClick.getContext());
                builder.setTitle(membership.name + " Membership");
                builder.setMessage(new MoneyTextWatcher().convertToCurrency(membership.price) + " per month\n\n\nDescription:\n" + membership.description);
                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // NOTHING
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
