package com.domzky.gymbooking.Helpers.FieldSyntaxes;

import com.google.firebase.database.DataSnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldValidations {

    private final int NAME_LENGTH_MIN = 5;
    private final int PHONE_LENGTH_MIN = 10;
    private final int PHONE_LENGTH_MAX = 13;
    public final int USERNAME_LENGTH_MIN = 4;
    public final int PASSWORD_LENGTH_MIN = 6;

    private final String EMAIL_PATTERN = "^(.+)@(\\S+)$";
    private final String NAME_PATTERN = "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$";
    private final String CONTACT_PATTERN = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
     private final String ADDRESS_PATTERN = "^[#.0-9a-zA-Z\\s,-]+$";

    private final Pattern pattern_email = Pattern.compile(EMAIL_PATTERN);
    private final Pattern pattern_name = Pattern.compile(NAME_PATTERN);
    private final Pattern pattern_contact = Pattern.compile(CONTACT_PATTERN);
     private final Pattern pattern_address = Pattern.compile(ADDRESS_PATTERN);

    public boolean isValidEmailSyntax(String email) {
        Matcher matcher = pattern_email.matcher(email);
        return matcher.matches();
    }
    public boolean isValidContactSyntax(String contact) {
        Matcher matcher = pattern_contact.matcher(contact);
        return matcher.matches();
    }
    public boolean isValidNameSyntax(String name) {
        Matcher matcher = pattern_name.matcher(name);
        return matcher.matches();
    }
    public boolean isValidAddressSyntax(String address) {
        Matcher matcher = pattern_address.matcher(address);
        return matcher.matches();
    }

    public boolean isValidNameLength(String name) {
        return name.length() > NAME_LENGTH_MIN;
    }

    public boolean isValidPhoneLength(String phone) {
        return phone.length() >= PHONE_LENGTH_MIN && phone.length() <= PHONE_LENGTH_MAX;
    }

    public boolean isValidUsername(String username) {
        return username.length() >= USERNAME_LENGTH_MIN;
    }

    public boolean isValidPassword(String password) {
        return password.length() >= PASSWORD_LENGTH_MIN;
    }

    public boolean isUsernameExists(DataSnapshot snapshot,String username) {
        for (DataSnapshot snap : snapshot.getChildren()) {
            if (snap.child("username").getValue(String.class).equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean isGymNameExists(DataSnapshot snapshot,String username) {
        for (DataSnapshot snap : snapshot.getChildren()) {
            if (snap.child("gym_name").getValue(String.class).equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFullnameExists(DataSnapshot snapshot,String fullname) {
        for (DataSnapshot snap : snapshot.getChildren()) {
            if (snap.child("fullname").getValue(String.class).equals(fullname)) {
                return true;
            }
        }
        return false;
    }

    public boolean isMembershipNameExists(DataSnapshot snapshot,String name,String gym_id) {
        for (DataSnapshot snap : snapshot.getChildren()) {
            if (snap.child("name").getValue(String.class).equals(name)
                    && !snap.child("deleted").getValue(Boolean.class)
                    && snap.child("gym_id").getValue(String.class).equals(gym_id)
            ) {
                return true;
            }
        }
        return false;
    }
    public boolean isExerciseNameExists(DataSnapshot snapshot,String name,String coach_id) {
        for (DataSnapshot snap : snapshot.getChildren()) {
            if (snap.child("name").getValue(String.class).equals(name)
                    && !snap.child("deleted").getValue(Boolean.class)
                    && snap.child("coach_id").getValue(String.class).equals(coach_id)
            ) {
                return true;
            }
        }
        return false;
    }


}
