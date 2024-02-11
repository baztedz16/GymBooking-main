package com.domzky.gymbooking.Sessions.GymOwner.pages.CoachesList.ModifyCoach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.domzky.gymbooking.Helpers.Users.GymStaff;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.GymOwner.pages.StaffsList.ModifyStaff.ModifyStaffActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ModifyCoachActivity extends AppCompatActivity {


    private DatabaseReference fread = new FirebaseHelper().getUserReference("Coaches");
    private DatabaseReference fwrite = new FirebaseHelper().getUserReference("Coaches");
    private FieldValidations fieldVal = new FieldValidations();

    private TextView banner;
    private EditText fullnameField,emailField,phoneField,usernameField,passwordField;
    private Button signupBtn,resetBtn;
    private Switch activationSwt;
    private CheckBox showPassword;

    private String fullname,email,phone,username,password;
    private Boolean activated;

    private ProgressDialog progress;

    private SharedPreferences preferences;

    private Intent intentGym;
    private String reset_uid,reset_gymid,reset_fullname,reset_email,reset_phoneNum,reset_username,reset_password;
    private Boolean reset_activated;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_employee_form);

        intentGym = this.getIntent();

        reset_uid = intentGym.getStringExtra("coachuid");
        reset_gymid = intentGym.getStringExtra("gymuid");
        reset_fullname = intentGym.getStringExtra("coachfullname");
        reset_fullname = intentGym.getStringExtra("coachfullname");
        reset_email = intentGym.getStringExtra("coachemail");
        reset_phoneNum = intentGym.getStringExtra("coachphone");
        reset_username = intentGym.getStringExtra("coachusername");
        reset_password = intentGym.getStringExtra("coachpassword");
        reset_activated = intentGym.getBooleanExtra("coachactivated",true);

        preferences = this.getSharedPreferences("owner",MODE_PRIVATE);

        progress = new ProgressDialog(ModifyCoachActivity.this);
        progress.setCancelable(false);
        progress.setMessage("Account Check");

        banner = findViewById(R.id.employee_account_view_banner);

        fullnameField = findViewById(R.id.employee_account_field_fullname);
        emailField = findViewById(R.id.employee_account_field_email);
        phoneField = findViewById(R.id.employee_account_field_phone);
        usernameField = findViewById(R.id.employee_account_field_username);
        passwordField = findViewById(R.id.employee_account_field_password);

        signupBtn = findViewById(R.id.employee_account_button_submit);
        resetBtn = findViewById(R.id.employee_account_button_reset);
        showPassword = findViewById(R.id.employee_account_checkbox_password);
        activationSwt = findViewById(R.id.employee_account_switch_activated);

        signupBtn.setText("Change");
        banner.setText("MODIFYING COACH FORM");

        resetAllFieldsByDefault();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAllFieldsByDefault();
            }
        });

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (!checked) {
                    passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clearAllFieldValidations();

                progress.show();


                fullname = fullnameField.getText().toString().trim();
                email = emailField.getText().toString().trim();
                phone = phoneField.getText().toString().trim();
                username = usernameField.getText().toString().trim();
                password = passwordField.getText().toString().trim();
                activated = activationSwt.isChecked();

                if (checkIfAllChanged()) {
                    Toast.makeText(ModifyCoachActivity.this,"No Changes have been made.",Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    return;
                }

                if (fullname.isEmpty()) {
                    fullnameField.setError("Please fill this field.");
                } else if (!fieldVal.isValidNameSyntax(fullname)) {
                    fullnameField.setError("Names should be Alphabetic.");
                } else if (!fieldVal.isValidNameLength(fullname)) {
                    fullnameField.setError("This name is too short.");
                }

                if (email.isEmpty()) {
                    emailField.setError("Please fill this field.");
                } else if (!fieldVal.isValidEmailSyntax(email)) {
                    emailField.setError("Please enter valid email.");
                }

                if (phone.isEmpty()) {
                    phoneField.setError("Please fill this field.");
                } else if (!fieldVal.isValidPhoneLength(phone)) {
                    phoneField.setError("Invalid Phone Format");
                }
                if (username.isEmpty()) {
                    usernameField.setError("Please fill this field.");
                } else if (!fieldVal.isValidUsername(username)) {
                    usernameField.setError("This username should not be short.");
                }

                if (password.isEmpty()) {
                    passwordField.setError("Please fill this field.");
                } else if (!fieldVal.isValidPassword(password)) {
                    passwordField.setError("This Password is "+ fieldVal.PASSWORD_LENGTH_MIN +" characters short.");
                }

                if (
                        fullname.isEmpty() || !fieldVal.isValidNameSyntax(fullname) ||  !fieldVal.isValidNameLength(fullname)
                                || email.isEmpty() || !fieldVal.isValidEmailSyntax(email)
                                || phone.isEmpty() || !fieldVal.isValidPhoneLength(phone)
                                || username.isEmpty() || !fieldVal.isValidUsername(username)
                                || password.isEmpty() || !fieldVal.isValidPassword(password)
                ) {
                    progress.dismiss();
                    return;
                }



                fread.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if ((fieldVal.isUsernameExists(snapshot,username)
                                && !username.equals(snapshot.child(reset_uid).child("username").getValue(String.class)))
                                || (fieldVal.isFullnameExists(snapshot,fullname)
                                && !fullname.equals(snapshot.child(reset_uid).child("fullname").getValue(String.class)))) {
                            if (fieldVal.isUsernameExists(snapshot,username)) {
                                usernameField.setError("This Username cannot used when Existed.");
                            }
                            if (fieldVal.isFullnameExists(snapshot,fullname)
                                    && !username.equals(snapshot.child(reset_uid).child("fullname").getValue(String.class))) {
                                fullnameField.setError("This Full Name cannot used when Existed.");
                            }
                            progress.dismiss();
                            clearAllFieldValues();
                        } else {
                            clearAllFieldValidations();
                            progress.dismiss();
                            fwrite.child(reset_uid).setValue(new GymStaff(fullname,email,phone,username,password,activated,preferences.getString("userid","")))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(),"Coach Info Changed Successfully",Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),"Coach Info Changed FAILED!!!",Toast.LENGTH_SHORT).show();
                                            Log.e("FAILED CHANGE",""+e.getMessage().toString().trim());
                                        }
                                    });
                        }





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FIREBASE ERR",""+error.getMessage());
                        Toast.makeText(getApplicationContext(),"Staff Added FAILED!!!",Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });


            }
        });


    }

    private Boolean checkIfAllChanged() {
        return activated == reset_activated &&
                fullname.equals(reset_fullname) &&
                email.equals(reset_email) &&
                phone.equals(reset_phoneNum) &&
                username.equals(reset_username) &&
                password.equals(reset_password);
    }


    private void resetAllFieldsByDefault() {
        clearAllFieldValidations();
        activationSwt.setChecked(reset_activated);
        fullnameField.setText(reset_fullname);
        emailField.setText(reset_email);
        phoneField.setText(reset_phoneNum);
        usernameField.setText(reset_username);
        passwordField.setText(reset_password);
    }

    private void clearAllFieldValidations() {
        fullnameField.setError(null);
        emailField.setError(null);
        phoneField.setError(null);
        usernameField.setError(null);
        passwordField.setError(null);
    }

    private void clearAllFieldValues() {
        fullname = "";
        email = "";
        phone = "";
        username = "";
        password = "";
    }

}