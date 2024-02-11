package com.domzky.gymbooking.Sessions.Admin.pages.GymsList.ModifyGym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.domzky.gymbooking.Helpers.Users.GymMember;
import com.domzky.gymbooking.Helpers.Users.GymOwner;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.Admin.AdminSessionActivity;
import com.domzky.gymbooking.Sessions.Admin.pages.GymsList.AddGym.AddGymActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class ModifyGymActivity extends AppCompatActivity {

    private TextView gym_form;
    private EditText gym_name,gym_address,owner_name,owner_email,owner_phoneNum,owner_username,owner_password;
    private Button modify_button,reset_button;
    private CheckBox check_password;
    private Switch gym_activation;

    private String gymname,gymaddress,ownername,owneremail,ownerphoneNum,ownerusername,ownerpassword;
    private Boolean gymactivated,gymstatus;

    private DatabaseReference fdb = new FirebaseHelper().getRootReference();
    private DatabaseReference fowner = new FirebaseHelper().getUserReference("Owners");
    private DatabaseReference fgym = new FirebaseHelper().getGymReference();

    private FieldValidations fieldVal = new FieldValidations();

    private ProgressDialog progress;
    
    private Intent intentGym;
    private String uid,reset_gymname,reset_gymaddress,reset_ownername,reset_owneremail,reset_ownerphoneNum,reset_ownerusername,reset_ownerpassword;
    private Boolean reset_gymactivated;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_gym_form);
        
        intentGym = this.getIntent();

        uid = intentGym.getStringExtra("gymuid");
        reset_gymname = intentGym.getStringExtra("gymname");
        reset_gymaddress = intentGym.getStringExtra("gymaddress");
        reset_gymactivated = intentGym.getBooleanExtra("gymactivated",true);
        gymstatus = intentGym.getBooleanExtra("gymstatus",true);
        reset_ownername = intentGym.getStringExtra("ownername");
        reset_owneremail = intentGym.getStringExtra("owneremail");
        reset_ownerphoneNum = intentGym.getStringExtra("ownerphone");
        reset_ownerusername = intentGym.getStringExtra("ownerusername");
        reset_ownerpassword = intentGym.getStringExtra("ownerpassword");
        
        progress = new ProgressDialog(ModifyGymActivity.this);
        progress.setCancelable(false);
        progress.setMessage("Please Wait");

        gym_form = (TextView) findViewById(R.id.admin_gym_form_textview_gym);
        gym_form.setText("Modify Gym Form");

        gym_name = (EditText) findViewById(R.id.admin_gym_field_gym_name);
        gym_address = (EditText) findViewById(R.id.admin_gym_field_gym_address);
        owner_name = (EditText) findViewById(R.id.admin_gym_field_fullname);
        owner_email = (EditText) findViewById(R.id.admin_gym_field_email);
        owner_phoneNum = (EditText) findViewById(R.id.admin_gym_field_phone);
        owner_username = (EditText) findViewById(R.id.admin_gym_field_username);
        owner_password = (EditText) findViewById(R.id.admin_gym_field_password);

        modify_button = (Button) findViewById(R.id.admin_gym_button_submit);
        reset_button = (Button) findViewById(R.id.admin_gym_button_reset);
        check_password = (CheckBox) findViewById(R.id.admin_gym_checkbox_password);
        gym_activation = (Switch) findViewById(R.id.admin_gym_field_activation);

        resetAllFieldValuesByDefault();

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

        modify_button.setText("Proceed");


        modify_button.setOnClickListener(new View.OnClickListener() {
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
                gymactivated = gym_activation.isChecked();

                if (checkIfAllChanged()) {
                    Toast.makeText(ModifyGymActivity.this,"No Changes have been made.",Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    return;
                }

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

                fdb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DataSnapshot snapowners = snapshot.child("Users").child("Owners");
                        DataSnapshot snapgyms = snapshot.child("Gyms");
                        if (
                                fieldVal.isUsernameExists(snapowners,ownerusername)
                                        && !ownerusername.equals(snapowners.child(uid).child("username").getValue(String.class))
                                        || fieldVal.isFullnameExists(snapowners,ownername)
                                        && !ownername.equals(snapowners.child(uid).child("fullname").getValue(String.class))
                                        || fieldVal.isGymNameExists(snapgyms,gymname)
                                        && !gymname.equals(snapgyms.child(uid).child("gym_name").getValue(String.class))
                        )
                        {
                            if (fieldVal.isUsernameExists(snapowners,ownerusername)
                                    && !ownerusername.equals(snapowners.child(uid).child("username").getValue(String.class))) {
                                owner_username.setError("This Username cannot used when Existed.");
                            }
                            if (fieldVal.isFullnameExists(snapowners,ownername)
                                && !ownername.equals(snapowners.child(uid).child("fullname").getValue(String.class))) {
                                owner_name.setError("This Full Name cannot used when Existed.");
                            }
                            if (fieldVal.isGymNameExists(snapgyms,gymname)
                                    && !gymname.equals(snapgyms.child(uid).child("gym_name").getValue(String.class))) {
                                gym_name.setError("This Gym Name cannot used when Existed.");
                            }
                            progress.dismiss();
                            clearAllFieldValues();
                        } else {
                            progress.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(ModifyGymActivity.this);
                            builder.setTitle("Owner Information Check");
                            builder.setMessage("Are you sure you want to change your information to the owner?");
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
                                    fgym.child(uid).setValue(new Gym(gymname,gymaddress,gymactivated,gymstatus));
                                    fowner.child(uid).setValue(new GymOwner(ownername,owneremail,ownerphoneNum,ownerusername,ownerpassword))
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(),"Modified Successfully",Toast.LENGTH_SHORT).show();
                                                    progress.dismiss();
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
                                }
                            });
                            builder.create().show();

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

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAllFieldValuesByDefault();
            }
        });



    }
    
    private void resetAllFieldValuesByDefault() {
        clearAllFieldValidations();
        gym_name.setText(reset_gymname);
        gym_address.setText(reset_gymaddress);
        gym_activation.setChecked(reset_gymactivated);
        owner_name.setText(reset_ownername);
        owner_email.setText(reset_owneremail);
        owner_phoneNum.setText(reset_ownerphoneNum);
        owner_username.setText(reset_ownerusername);
        owner_password.setText(reset_ownerpassword);
    }

    private Boolean checkIfAllChanged() {
        return gymname.equals(reset_gymname) &&
        gymaddress.equals(reset_gymaddress) &&
        gymactivated == reset_gymactivated &&
        ownername.equals(reset_ownername) &&
        owneremail.equals(reset_owneremail) &&
        ownerphoneNum.equals(reset_ownerphoneNum) &&
        ownerusername.equals(reset_ownerusername) &&
        ownerpassword.equals(reset_ownerpassword);
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