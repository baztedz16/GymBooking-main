package com.domzky.gymbooking.Sessions.Members.pages.Account;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.domzky.gymbooking.Helpers.FieldSyntaxes.FieldValidations;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
import com.domzky.gymbooking.Helpers.Users.GymMember;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.Members.MemberLoginActivity;
import com.domzky.gymbooking.Sessions.Members.MemberSessionActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    private EditText fullnameField,emailField,phoneField,usernameField,passwordField;
    private Button updateBtn,resetBtn;
    private CheckBox showPassword;

    private DatabaseReference dbread = new FirebaseHelper().getUserReference("Members");
    private DatabaseReference dbwrite = new FirebaseHelper().getUserReference("Members");

    private FieldValidations fieldVal = new FieldValidations();

    private String fullname,email,phone,username,password;
    
    private ProgressDialog progress;

    private SharedPreferences preferences;
    private SharedPreferences.Editor prefEditor;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        preferences = getActivity().getSharedPreferences("member",Context.MODE_PRIVATE);
        prefEditor = preferences.edit();

        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage("Changing Account");

        View view = inflater.inflate(R.layout.fragment_member_account, container, false);

        fullnameField = view.findViewById(R.id.member_account_update_field_fullname);
        emailField = view.findViewById(R.id.member_account_update_field_email);
        phoneField = view.findViewById(R.id.member_account_update_field_phone);
        usernameField = view.findViewById(R.id.member_account_update_field_username);
        passwordField = view.findViewById(R.id.member_account_update_field_password);

        fullnameField.setText(preferences.getString("fullname",""));
        emailField.setText(preferences.getString("email",""));
        phoneField.setText(preferences.getString("phone",""));
        usernameField.setText(preferences.getString("username",""));
        passwordField.setText(preferences.getString("password",""));

        updateBtn = view.findViewById(R.id.member_account_update_button_submit);
        resetBtn = view.findViewById(R.id.member_account_update_reset_button_fields);
        showPassword = view.findViewById(R.id.member_account_update_checkbox_password);

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

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFieldValidations();
                fullnameField.setText(preferences.getString("fullname",""));
                emailField.setText(preferences.getString("email",""));
                phoneField.setText(preferences.getString("phone",""));
                usernameField.setText(preferences.getString("username",""));
                passwordField.setText(preferences.getString("password",""));
            }
        });
        
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.show();
                clearAllFieldValidations();

                fullname = fullnameField.getText().toString().trim();
                email = emailField.getText().toString().trim();
                phone = phoneField.getText().toString().trim();
                username = usernameField.getText().toString().trim();
                password = passwordField.getText().toString().trim();
                
                if (
                    fullname.equals(preferences.getString("fullname","")) &&
                    email.equals(preferences.getString("email","")) &&
                    phone.equals(preferences.getString("phone","")) &&
                    username.equals(preferences.getString("username","")) &&
                    password.equals(preferences.getString("password",""))
                ) {
                    Toast.makeText(getActivity(),"No Changes have been made.",Toast.LENGTH_SHORT).show();
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
                
                
                
                dbread.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (
                                fieldVal.isUsernameExists(snapshot,username) && !username.equals(preferences.getString("username","")) ||
                                fieldVal.isFullnameExists(snapshot,fullname) && !fullname.equals(preferences.getString("fullname",""))
                        ) {
                            if (fieldVal.isUsernameExists(snapshot,username) && !username.equals(preferences.getString("username",""))) {
                                usernameField.setError("This Username cannot used when Existed.");
                            }
                            if (fieldVal.isFullnameExists(snapshot,fullname) && !fullname.equals(preferences.getString("fullname",""))) {
                                fullnameField.setError("This Full Name cannot used when Existed.");
                            }
                            progress.dismiss();
                            return;
                        } else {
                            clearAllFieldValidations();
                            progress.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Account Check");
                            builder.setMessage("Are you sure you want to change your information to your account session?");
                            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // DO NOTHING
                                }
                            });
                            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dbwrite.child(preferences.getString("userid","")).setValue(new GymMember(fullname,email,phone,username,password,true))
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getActivity(),"Account Changed Successfully",Toast.LENGTH_SHORT).show();
                                                    prefEditor.putString("username",username);
                                                    prefEditor.putString("password",password);
                                                    prefEditor.putString("fullname",fullname);
                                                    prefEditor.putString("phone",phone);
                                                    prefEditor.putString("email",email);
                                                    prefEditor.commit();

                                                    MemberSessionActivity memberSessionActivity = (MemberSessionActivity) getActivity();
                                                    memberSessionActivity.headUserName.setText(preferences.getString("fullname",""));

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(),"Account Changing Failed. Please Try Again",Toast.LENGTH_SHORT).show();
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
                        Log.e("FIREBASE ERROR",""+ error.getMessage());
                        Toast.makeText(getActivity(),"Account Changing Failed. Please Try Again",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
    }

    private void clearAllFieldValidations() {
        fullnameField.setError(null);
        emailField.setError(null);
        phoneField.setError(null);
        usernameField.setError(null);
        passwordField.setError(null);
    }
}