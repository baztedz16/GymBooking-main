package com.domzky.gymbooking.Helpers.Providers.SIM;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SIMHelper {

    public Context context;

    public SIMHelper(Context context) {
        this.context = context;
    }
    public void callNumber(String phoneNumber) {
        Intent intent_call = new Intent(Intent.ACTION_DIAL);
        intent_call.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent_call);
    }

    public void textNumber(String phoneNumber) {
        Intent intent_sms = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:" + phoneNumber));
        context.startActivity(intent_sms);
    }

}
