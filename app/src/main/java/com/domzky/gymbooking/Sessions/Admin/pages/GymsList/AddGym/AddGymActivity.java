package com.domzky.gymbooking.Sessions.Admin.pages.GymsList.AddGym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.domzky.gymbooking.Helpers.FieldSyntaxes.FieldValidations;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Users.Gym;
import com.domzky.gymbooking.Helpers.Users.GymOwner;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.Admin.AdminSignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AddGymActivity extends AppCompatActivity {

    private TextView gym_form;
    private EditText gym_name,gym_address,owner_name,owner_email,owner_phoneNum,owner_username,owner_password;
    private Button add_button,reset_button;
    private CheckBox check_password;
    private Switch gym_activation;

    private String gymname,gymaddress,ownername,owneremail,ownerphoneNum,ownerusername,ownerpassword;

    private DatabaseReference fdbread = new FirebaseHelper().getRootReference();
    private DatabaseReference fowner = new FirebaseHelper().getUserReference("Owners");
    private DatabaseReference fgym = new FirebaseHelper().getGymReference();
    
    private FieldValidations fieldVal = new FieldValidations();

    private ProgressDialog progress;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_gym_form);


        progress = new ProgressDialog(AddGymActivity.this);
        progress.setCancelable(false);
        progress.setMessage("Please Wait");

        gym_form = (TextView) findViewById(R.id.admin_gym_form_textview_gym);
        gym_form.setText("Gym Registration Form");

        gym_name = (EditText) findViewById(R.id.admin_gym_field_gym_name);
        gym_address = (EditText) findViewById(R.id.admin_gym_field_gym_address);
        owner_name = (EditText) findViewById(R.id.admin_gym_field_fullname);
        owner_email = (EditText) findViewById(R.id.admin_gym_field_email);
        owner_phoneNum = (EditText) findViewById(R.id.admin_gym_field_phone);
        owner_username = (EditText) findViewById(R.id.admin_gym_field_username);
        owner_password = (EditText) findViewById(R.id.admin_gym_field_password);

        gymname = gym_name.getText().toString().trim();
        gymaddress = gym_address.getText().toString().trim();
        ownername = owner_name.getText().toString().trim();
        owneremail = owner_email.getText().toString().trim();
        ownerphoneNum = owner_phoneNum.getText().toString().trim();
        ownerusername = owner_username.getText().toString().trim();
        ownerpassword = owner_password.getText().toString().trim();

        add_button = (Button) findViewById(R.id.admin_gym_button_submit);
        reset_button = (Button) findViewById(R.id.admin_gym_button_reset);
        check_password = (CheckBox) findViewById(R.id.admin_gym_checkbox_password);
        gym_activation = (Switch) findViewById(R.id.admin_gym_field_activation); 
        
        gym_activation.setVisibility(View.INVISIBLE);

        check_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    owner_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    owner_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        reset_button.setVisibility(View.GONE);
        add_button.setText("Add Gym");
        
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                clearAllFieldValidations();

                gymname = gym_name.getText().toString().trim();
                gymaddress  = gym_address.getText().toString().trim();
                ownername = owner_name.getText().toString().trim();
                owneremail = owner_email.getText().toString().trim();
                ownerphoneNum = owner_phoneNum.getText().toString().trim();
                ownerusername = owner_username.getText().toString().trim();
                ownerpassword = owner_password.getText().toString().trim();
                
                if (gymname.isEmpty()) {
                    gym_name.setError("This Field is Required.");
                }
                if (gymaddress.isEmpty()) {
                    gym_address.setError("This Field is Required.");
                } else if (!fieldVal.isValidAddressSyntax(gymaddress)) {
                    gym_address.setError("Invalid Address Format.");
                }
                if (ownername.isEmpty()) {
                    owner_name.setError("This Field is Required.");
                } else if (!fieldVal.isValidNameSyntax(ownername)) {
                    owner_name.setError("Names should be Alphabetic.");
                } else if (!fieldVal.isValidNameLength(ownername)) {
                    owner_name.setError("Name is too short.");
                }
                if (owneremail.isEmpty()) {
                    owner_email.setError("This Field is Required.");
                } else if (!fieldVal.isValidNameSyntax(ownername)) {
                    owner_name.setError("Invalid Email Format.");
                }
                if (ownerphoneNum.isEmpty()) {
                    owner_phoneNum.setError("This Field is Required.");
                } else if (!fieldVal.isValidPhoneLength(ownerphoneNum)) {
                    owner_phoneNum.setError("Invalid Phone Format.");
                }
                if (ownerusername.isEmpty()) {
                    owner_username.setError("This Field is Required.");
                } else if (!fieldVal.isValidUsername(ownerusername)) {
                    owner_username.setError("This username should not be short.");
                }
                if (ownerpassword.isEmpty()) {
                    owner_password.setError("This Field is Required.");
                } else if (!fieldVal.isValidPassword(ownerpassword)) {
                    owner_password.setError("This Password is "+ fieldVal.PASSWORD_LENGTH_MIN +" characters short.");
                }

                
                if (
                        gymname.isEmpty()
                        || gymaddress.isEmpty() || !fieldVal.isValidAddressSyntax(gymaddress)
                        || ownername.isEmpty() || !fieldVal.isValidNameSyntax(ownername) ||  !fieldVal.isValidNameLength(ownername)
                        || owneremail.isEmpty() || !fieldVal.isValidEmailSyntax(owneremail)
                        || ownerphoneNum.isEmpty() || !fieldVal.isValidPhoneLength(ownerphoneNum)
                        || ownerusername.isEmpty() || !fieldVal.isValidUsername(ownerusername)
                        || ownerpassword.isEmpty() || !fieldVal.isValidPassword(ownerpassword)
                        
                ) {
                    progress.dismiss();
                    return;
                }

                fdbread.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot snapowners = snapshot.child("Users").child("Owners");
                        DataSnapshot snapgyms = snapshot.child("Gyms");
                        if (
                                fieldVal.isUsernameExists(snapowners,ownerusername)
                                || fieldVal.isFullnameExists(snapowners,ownername)
                                || fieldVal.isGymNameExists(snapgyms,gymname)
                            )
                        {
                            if (fieldVal.isUsernameExists(snapowners,ownerusername)) {
                                owner_username.setError("This Username cannot used when Existed.");
                            }
                            if (fieldVal.isFullnameExists(snapowners,ownername)) {
                                owner_name.setError("This Full Name cannot used when Existed.");
                            }
                            if (fieldVal.isGymNameExists(snapgyms,gymname)) {
                                gym_name.setError("This Gym Name cannot used when Existed.");
                            }
                            progress.dismiss();
                            clearAllFieldValues();
                        } else {
                            String user_id = fowner.push().getKey();
                            fgym.child(user_id).setValue(new Gym(gymname,gymaddress,false,true));
                            fowner.child(user_id).setValue(new GymOwner(ownername,owneremail,ownerphoneNum,ownerusername,ownerpassword))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(),"Gym Added Successfully",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),"Add Gym FAILED!!!",Toast.LENGTH_SHORT).show();
                                            Log.e("FAILED SIGNUP",""+e.getMessage().toString().trim());
                                        }
                                    });
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),"Add Gym FAILED!!!",Toast.LENGTH_SHORT).show();
                        Log.e("FIREBASE ERR", error.getMessage());
                    }
                });
            }
        });
    }


    private void clearAllFieldValidations() {
        gym_name.setError(null);
        gym_address.setError(null);
        owner_name.setError(null);
        owner_email.setError(null);
        owner_phoneNum.setError(null);
        owner_username.setError(null);
        owner_password.setError(null);
    }
    
    private void clearAllFieldValues() {
        gymname = "";
        gymaddress = "";
        ownername = "";
        owneremail = "";
        ownerphoneNum = "";
        ownerusername = "";
        ownerpassword = "";
    }
}