package com.domzky.gymbooking.Sessions.GymOwner.pages.Membership.ModifyMembership;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.domzky.gymbooking.Helpers.FieldSyntaxes.FieldValidations;
import com.domzky.gymbooking.Helpers.FieldSyntaxes.MoneyTextWatcher;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Things.Membership;
import com.domzky.gymbooking.Helpers.Users.Gym;
import com.domzky.gymbooking.Helpers.Users.GymOwner;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.GymOwner.OwnerSessionActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ModifyMembershipActivity extends AppCompatActivity {

    private DatabaseReference dbread = new FirebaseHelper().getMembershipReference();
    private DatabaseReference dbwrite = new FirebaseHelper().getMembershipReference();

    private TextView banner;
    private EditText nameField,priceField,descriptionField;
    private Button addBtn,resetBtn;

    private String name,description,price,reset_name,reset_description,reset_price,reset_uid;

    private SharedPreferences preferences;
    private FieldValidations fieldVal = new FieldValidations();
    private ProgressDialog progress;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_membership_form);

        reset_uid = getIntent().getStringExtra("membership_uid");
        reset_name = getIntent().getStringExtra("membership_name");
        reset_description = getIntent().getStringExtra("membership_description");
        reset_price = getIntent().getStringExtra("membership_price");

        preferences = this.getSharedPreferences("owner",MODE_PRIVATE);
        progress = new ProgressDialog(ModifyMembershipActivity.this);
        progress.setCancelable(false);
        progress.setMessage("Updating Membership");

        banner = findViewById(R.id.membership_form_banner);
        banner.setText("Add Program Form");

        nameField = findViewById(R.id.membership_form_field_name);
        priceField = findViewById(R.id.membership_form_field_price);
        descriptionField = findViewById(R.id.membership_form_field_description);
        //priceField.addTextChangedListener(new MoneyTextWatcher(priceField));

        resetFieldsByDefault();

        addBtn = findViewById(R.id.membership_form_button_submit);
        resetBtn = findViewById(R.id.membership_form_button_reset);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFieldsByDefault();
            }
        });

        addBtn.setText("update Membership");
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFieldValidations();
                progress.show();
                name = nameField.getText().toString().trim();
                price = priceField.getText().toString().trim();
                description = descriptionField.getText().toString().trim();

                if (checkIfFieldsAreNotDefaultState(name,description,price)) {
                    Toast.makeText(ModifyMembershipActivity.this,"No Changes been Made.",Toast.LENGTH_SHORT).show();
                }
                if (name.isEmpty()) {
                    nameField.setError("This Field is Required.");
                }
                if (price.isEmpty()) {
                    priceField.setError("This Field is Required.");
                }

                if (name.isEmpty() || price.isEmpty() || checkIfFieldsAreNotDefaultState(name,description,price)) {
                    progress.dismiss();
                    return;
                }

                dbread.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (
                                fieldVal.isMembershipNameExists(snapshot,name,preferences.getString("userid",""))
                                && !name.equals(reset_name)
                        ) {
                            nameField.setError("This Membership Exists.");
                            progress.dismiss();
                            return;
                        }
                        progress.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(ModifyMembershipActivity.this);
                        builder.setTitle("Membership Check");
                        builder.setMessage("Are you sure you want to change information of the membership?");
                        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // DO NOTHING
                            }
                        });
                        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbwrite.child(reset_uid).setValue(new Membership(
                                        name,
                                        preferences.getString("userid",""),
                                        Double.parseDouble(price),
                                        description,
                                        false
                                )).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progress.dismiss();
                                        Toast.makeText(ModifyMembershipActivity.this,"Membership Added Successfully.",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("FIREBASE ERROR",""+ e.getMessage());
                                        Toast.makeText(getBaseContext(),"Membership Changing Failed. Please Try Again",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        builder.create().show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FIREBASE ERROR",""+ error.getMessage());
                        Toast.makeText(getBaseContext(),"Membership Changing Failed. Please Try Again",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void clearAllFieldValidations() {
        nameField.setError(null);
        priceField.setError(null);
    }

    private boolean checkIfFieldsAreNotDefaultState(String nameGet,String descriptionGet,String priceGet) {
        return reset_name.equals(nameGet)
                && reset_price.equals(priceGet)
                && reset_description.equals(descriptionGet);
    }

    private void resetFieldsByDefault() {
        nameField.setText(reset_name);
        descriptionField.setText(reset_description);
        priceField.setText(reset_price);
    }


}