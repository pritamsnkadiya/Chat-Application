package engineer.babu.chatapplication.data.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class MyTextWatcher implements TextWatcher {

    private View view;

    public MyTextWatcher(View view) {
        this.view = view;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
