package com.domzky.gymbooking.Sessions.GymOwner.pages.CoachesList.AddCoach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.domzky.gymbooking.Helpers.Users.GymCoach;
import com.domzky.gymbooking.Helpers.Users.GymStaff;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.GymOwner.pages.StaffsList.AddStaff.AddStaffActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AddCoachActivity extends AppCompatActivity {

    private DatabaseReference fread = new FirebaseHelper().getUserReference("Coaches");
    private DatabaseReference fwrite = new FirebaseHelper().getUserReference("Coaches");
    private FieldValidations fieldVal = new FieldValidations();

    private TextView banner;
    private EditText fullnameField,emailField,phoneField,usernameField,passwordField;
    private Button signupBtn,resetBtn;
    private Switch activationSwt;
    private CheckBox showPassword;

    private String fullname,email,phone,username,password;

    private ProgressDialog progress;

    private SharedPreferences preferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_employee_form);

        preferences = this.getSharedPreferences("owner",MODE_PRIVATE);

        progress = new ProgressDialog(AddCoachActivity.this);
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

        activationSwt.setVisibility(View.INVISIBLE);
        resetBtn.setVisibility(View.INVISIBLE);
        signupBtn.setText("Register Coach");
        banner.setText("ADDING COACH FORM");

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
                        if (fieldVal.isUsernameExists(snapshot,username) || fieldVal.isFullnameExists(snapshot,fullname)) {
                            if (fieldVal.isUsernameExists(snapshot,username)) {
                                usernameField.setError("This Username cannot used when Existed.");
                            }
                            if (fieldVal.isFullnameExists(snapshot,fullname)) {
                                fullnameField.setError("This Full Name cannot used when Existed.");
                            }
                            progress.dismiss();
                        } else {
                            clearAllFieldValidations();
                            progress.dismiss();
                            fwrite.push().setValue(new GymCoach(fullname,email,phone,username,password,true,preferences.getString("userid","")))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(),"Coach Added Successfully",Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),"Staff Added FAILED!!!",Toast.LENGTH_SHORT).show();
                                            Log.e("FAILED SIGNUP",""+e.getMessage().toString().trim());
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


    private void clearAllFieldValidations() {
        fullnameField.setError(null);
        emailField.setError(null);
        phoneField.setError(null);
        usernameField.setError(null);
        passwordField.setError(null);
    }
}