package com.domzky.gymbooking.Sessions.GymStaff.pages.Account;

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
import com.domzky.gymbooking.Helpers.Users.GymStaff;
import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.GymStaff.StaffSessionActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    private TextView gymNameText,gymAddressText,employeeBanner;
    private EditText fullnameField,emailField,phoneField,usernameField,passwordField;
    private Button updateBtn,resetBtn;
    private CheckBox showPassword;

    private DatabaseReference dbread = new FirebaseHelper().getUserReference("Staffs");
    private DatabaseReference dbwrite = new FirebaseHelper().getUserReference("Staffs");

    private FieldValidations fieldVal = new FieldValidations();

    private String fullname,email,phone,username,password;

    private ProgressDialog progress;

    private SharedPreferences preferences;
    private SharedPreferences.Editor prefEditor;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = getActivity().getSharedPreferences("staff", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();

        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage("Changing Account");

        View view = inflater.inflate(R.layout.fragment_employee_account, container, false);

        employeeBanner = view.findViewById(R.id.employee_account_textview_employee_of);
        gymNameText = view.findViewById(R.id.employee_account_textview_gym_name);
        gymAddressText = view.findViewById(R.id.employee_account_textview_gym_address);

        employeeBanner.setText("STAFF OF");
        gymNameText.setText(preferences.getString("gymname",""));
        gymAddressText.setText(preferences.getString("gymaddress",""));

        fullnameField = view.findViewById(R.id.employee_account_update_field_fullname);
        emailField = view.findViewById(R.id.employee_account_update_field_email);
        phoneField = view.findViewById(R.id.employee_account_update_field_phone);
        usernameField = view.findViewById(R.id.employee_account_update_field_username);
        passwordField = view.findViewById(R.id.employee_account_update_field_password);

        fullnameField.setEnabled(false);
        emailField.setEnabled(false);
        phoneField.setEnabled(false);

        updateBtn = view.findViewById(R.id.employee_account_update_button_submit);
        resetBtn = view.findViewById(R.id.employee_account_update_button_reset);
        showPassword = view.findViewById(R.id.employee_account_update_checkbox_password);

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
                        checkIfFieldsAreSame()
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
                                fieldVal.isUsernameExists(snapshot, username) && !username.equals(preferences.getString("username", "")) ||
                                        fieldVal.isFullnameExists(snapshot, fullname) && !fullname.equals(preferences.getString("fullname", ""))
                        ) {
                            if (fieldVal.isUsernameExists(snapshot, username) && !username.equals(preferences.getString("username", ""))) {
                                usernameField.setError("This Username cannot used when Existed.");
                            }
                            if (fieldVal.isFullnameExists(snapshot, fullname) && !fullname.equals(preferences.getString("fullname", ""))) {
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
                                    dbwrite.child(preferences.getString("userid", "")).setValue(new GymStaff(fullname, email, phone, username, password,
                                                    preferences.getBoolean("activated",true),
                                                    preferences.getString("gymid","")))
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getActivity(), "Account Changed Successfully", Toast.LENGTH_SHORT).show();
                                                    prefEditor.putString("username", username);
                                                    prefEditor.putString("password", password);
                                                    prefEditor.putString("fullname", fullname);
                                                    prefEditor.putString("phone", phone);
                                                    prefEditor.putString("email", email);
                                                    prefEditor.commit();

                                                    StaffSessionActivity sessionActivity = (StaffSessionActivity) getActivity();
                                                    sessionActivity.headUserName.setText(preferences.getString("fullname", ""));

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getActivity(), "Account Changing Failed. Please Try Again", Toast.LENGTH_SHORT).show();
                                                    Log.e("FAILED SIGNUP", "" + e.getMessage().toString().trim());
                                                }
                                            });
                                }
                            });
                            builder.create().show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FIREBASE ERROR", "" + error.getMessage());
                        Toast.makeText(getActivity(), "Account Changing Failed. Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
    }

    private void resetAllFieldsByDefault() {
        clearAllFieldValidations();
        fullnameField.setText(preferences.getString("fullname",""));
        emailField.setText(preferences.getString("email",""));
        phoneField.setText(preferences.getString("phone",""));
        usernameField.setText(preferences.getString("username",""));
        passwordField.setText(preferences.getString("password",""));
    }

    private Boolean checkIfFieldsAreSame() {
        return fullname.equals(preferences.getString("fullname","")) &&
                email.equals(preferences.getString("email","")) &&
                phone.equals(preferences.getString("phone","")) &&
                username.equals(preferences.getString("username","")) &&
                password.equals(preferences.getString("password",""));
    }


    private void clearAllFieldValidations() {
        fullnameField.setError(null);
        emailField.setError(null);
        phoneField.setError(null);
        usernameField.setError(null);
        passwordField.setError(null);
    }


}