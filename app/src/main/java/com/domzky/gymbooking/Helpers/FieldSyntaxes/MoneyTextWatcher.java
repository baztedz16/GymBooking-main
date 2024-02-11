package com.domzky.gymbooking.Helpers.FieldSyntaxes;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class MoneyTextWatcher implements TextWatcher {
    private WeakReference<EditText> editTextWeakReference;

    public MoneyTextWatcher() {}

    public MoneyTextWatcher(EditText editText) {
        this.editTextWeakReference = new WeakReference<EditText>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        if (editText == null) {
            return;
        }
        String s = editable.toString();
        editText.removeTextChangedListener(this);
        String cleanString = s.replaceAll("[$,.]","");
        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR)
                .divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String format = NumberFormat.getCurrencyInstance().format(parsed)
                .replace("$","");
        String formatted = format.replaceAll(",","");
        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }

    public String convertToCurrency(Double price) {
        String s = String.valueOf(price*10);
        String cleanString = s.replaceAll("[$,.]","");
        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR)
                .divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String format = NumberFormat.getCurrencyInstance().format(parsed)
                .replace("$","");
        return format.replaceAll(",","");
    }
}
