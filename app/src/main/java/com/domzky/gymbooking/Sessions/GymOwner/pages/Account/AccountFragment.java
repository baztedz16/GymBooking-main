package com.domzky.gymbooking.Sessions.GymOwner.pages.Account;

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
import android.widget.Toast;

import com.domzky.gymbooking.Helpers.FieldSyntaxes.FieldValidations;
import com.domzky.gymbooking.Helpers.Firebase.FirebaseHelper;
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

public class AccountFragment extends Fragment {

    private EditText gymnameField,gymaddressField,fullnameField,emailField,phoneField,usernameField,passwordField;
    private Button updateBtn,resetBtn;
    private CheckBox showPassword;

    private DatabaseReference dbread = new FirebaseHelper().getRootReference();
    private DatabaseReference dbwrite = new FirebaseHelper().getRootReference();

    private FieldValidations fieldVal = new FieldValidations();

    private String gymname,gymaddress,fullname,email,phone,username,password,uid;
    private Boolean gymactivated,gymstatus;

    private ProgressDialog progress;

    private SharedPreferences preferences;
    private SharedPreferences.Editor prefEditor;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        preferences = getActivity().getSharedPreferences("owner", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();

        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage("Changing Account");

        View view = inflater.inflate(R.layout.fragment_owner_account, container, false);

        gymnameField = view.findViewById(R.id.owner_account_update_field_gym_name);
        gymaddressField = view.findViewById(R.id.owner_account_update_field_gym_address);
        fullnameField = view.findViewById(R.id.owner_account_update_field_fullname);
        emailField = view.findViewById(R.id.owner_account_update_field_email);
        phoneField = view.findViewById(R.id.owner_account_update_field_phone);
        usernameField = view.findViewById(R.id.owner_account_update_field_username);
        passwordField = view.findViewById(R.id.owner_account_update_field_password);

        gymnameField.setEnabled(false);
        gymaddressField.setEnabled(false);
        fullnameField.setEnabled(false);
        emailField.setEnabled(false);
        phoneField.setEnabled(false);

        updateBtn = view.findViewById(R.id.owner_account_update_button_submit);
        resetBtn = view.findViewById(R.id.owner_account_update_button_reset);
        showPassword = view.findViewById(R.id.owner_account_update_checkbox_password);

        resetAllFieldsByDefault();

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
                resetAllFieldsByDefault();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.show();
                clearAllFields();

                gymname = gymnameField.getText().toString().trim();
                gymaddress = gymaddressField.getText().toString().trim();
                gymactivated = preferences.getBoolean("gymactivated",true);
                gymstatus = preferences.getBoolean("gymstatus",true);

                fullname = fullnameField.getText().toString().trim();
                email = emailField.getText().toString().trim();
                phone = phoneField.getText().toString().trim();
                username = usernameField.getText().toString().trim();
                password = passwordField.getText().toString().trim();

                if (checkIfFieldsAreTheSame()) {
                    Toast.makeText(getActivity(),"No Changes have been made.",Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    return;
                }

                if (gymname.isEmpty()) {
                    gymnameField.setError("Please fill this field.");
                }

                if (gymaddress.isEmpty()) {
                    gymaddressField.setError("Please fill this field.");
                } else if (!fieldVal.isValidAddressSyntax(gymaddress)) {
                    gymaddressField.setError("Names should be Alphabetic.");
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
                        gymname.isEmpty() ||
                        gymaddress.isEmpty() || !fieldVal.isValidAddressSyntax(gymaddress) ||
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
                        DataSnapshot snapowners = snapshot.child("Users").child("Owners");
                        DataSnapshot snapgyms = snapshot.child("Gyms");
                        if (
                                fieldVal.isUsernameExists(snapowners,username)
                                        && !username.equals(snapowners.child(preferences.getString("userid","")).child("username").getValue(String.class))
                                        || fieldVal.isFullnameExists(snapowners,fullname)
                                        && !fullname.equals(snapowners.child(preferences.getString("userid","")).child("fullname").getValue(String.class))
                                        || fieldVal.isGymNameExists(snapgyms,gymname)
                                        && !gymname.equals(snapgyms.child(preferences.getString("userid","")).child("gym_name").getValue(String.class))
                        )
                        {
                            if (fieldVal.isUsernameExists(snapowners,username)
                                    && !username.equals(snapowners.child(preferences.getString("userid","")).child("username").getValue(String.class))) {
                                usernameField.setError("This Username cannot used when Existed.");
                            }
                            if (fieldVal.isFullnameExists(snapowners,fullname)
                                    && !fullname.equals(snapowners.child(preferences.getString("userid","")).child("fullname").getValue(String.class))) {
                                fullnameField.setError("This Full Name cannot used when Existed.");
                            }
                            if (fieldVal.isGymNameExists(snapgyms,gymname)
                                    && !gymname.equals(snapgyms.child(preferences.getString("userid","")).child("gym_name").getValue(String.class))) {
                                gymnameField.setError("This Gym Name cannot used when Existed.");
                            }
                            progress.dismiss();
                            clearAllFields();

                        } else {
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
                                    dbwrite.child("Gyms").child(preferences.getString("userid","")).setValue(new Gym(gymname,gymaddress,gymactivated,gymstatus));
                                    dbwrite.child("Users").child("Owners").child(preferences.getString("userid","")).setValue(new GymOwner(fullname,email,phone,username,password))
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

                                                    OwnerSessionActivity memberSessionActivity = (OwnerSessionActivity) getActivity();
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

    private Boolean checkIfFieldsAreTheSame() {
        return gymname.equals(preferences.getString("gymname","")) &&
                gymaddress.equals(preferences.getString("gymaddress","")) &&
                fullname.equals(preferences.getString("fullname","")) &&
                email.equals(preferences.getString("email","")) &&
                phone.equals(preferences.getString("phone","")) &&
                username.equals(preferences.getString("username","")) &&
                password.equals(preferences.getString("password",""));
    }

    private void resetAllFieldsByDefault() {
        gymnameField.setText(preferences.getString("gymname",""));
        gymaddressField.setText(preferences.getString("gymaddress",""));
        fullnameField.setText(preferences.getString("fullname",""));
        emailField.setText(preferences.getString("email",""));
        phoneField.setText(preferences.getString("phone",""));
        usernameField.setText(preferences.getString("username",""));
        passwordField.setText(preferences.getString("password",""));
    }

    private void clearAllFields() {
        gymnameField.setError(null);
        gymaddressField.setError(null);
        fullnameField.setError(null);
        emailField.setError(null);
        phoneField.setError(null);
        usernameField.setError(null);
        passwordField.setError(null);
    }
}