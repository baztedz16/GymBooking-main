package com.domzky.gymbooking.Sessions.GymOwner.pages.Membership.AddMembership;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.GymOwner.OwnerLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AddMembershipActivity extends AppCompatActivity {

    private DatabaseReference dbread = new FirebaseHelper().getMembershipReference();
    private DatabaseReference dbwrite = new FirebaseHelper().getMembershipReference();

    private TextView banner;
    private EditText nameField,priceField,descriptionField;
    private Button addBtn,resetBtn;

    private String name,description,price;

    private SharedPreferences preferences;
    private FieldValidations fieldVal = new FieldValidations();
    private ProgressDialog progress;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_membership_form);


        preferences = this.getSharedPreferences("owner",MODE_PRIVATE);
        progress = new ProgressDialog(AddMembershipActivity.this);
        progress.setCancelable(false);
        progress.setMessage("Adding Membership");

        banner = findViewById(R.id.membership_form_banner);

        nameField = findViewById(R.id.membership_form_field_name);
        priceField = findViewById(R.id.membership_form_field_price);
        descriptionField = findViewById(R.id.membership_form_field_description);

        //priceField.addTextChangedListener(new MoneyTextWatcher(priceField));

        addBtn = findViewById(R.id.membership_form_button_submit);
        resetBtn = findViewById(R.id.membership_form_button_reset);

        resetBtn.setVisibility(View.GONE);
        addBtn.setText("Add to Membership List");

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFieldValidations();
                progress.show();
                name = nameField.getText().toString().trim();
                price = priceField.getText().toString().trim();
                description = descriptionField.getText().toString().trim();

                if (name.isEmpty()) {
                    nameField.setError("This Field is Required.");
                }
                if (price.isEmpty()) {
                    priceField.setError("This Field is Required.");
                }

                if (name.isEmpty() || price.isEmpty()) {
                    progress.dismiss();
                    return;
                }

                dbread.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (fieldVal.isMembershipNameExists(snapshot,name,preferences.getString("userid",""))) {
                            nameField.setError("This Membership Exists.");
                            progress.dismiss();
                            return;
                        }
                        dbwrite.push().setValue(new Membership(
                                name,
                                preferences.getString("userid",""),
                                Double.parseDouble(price),
                                description,
                                false
                        )).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progress.dismiss();
                                Toast.makeText(AddMembershipActivity.this,"Membership Added Successfully.",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("FIREBASE ERROR",""+ e.getMessage());
                                Toast.makeText(getBaseContext(),"Membership Adding Failed. Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FIREBASE ERROR",""+ error.getMessage());
                        Toast.makeText(getBaseContext(),"Membership Adding Failed. Please Try Again",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void clearAllFieldValidations() {
        nameField.setError(null);
        priceField.setError(null);
    }


}