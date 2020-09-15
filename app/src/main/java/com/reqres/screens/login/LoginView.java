package com.reqres.screens.login;

import android.widget.Button;
import android.widget.EditText;

import com.reqres.R;

public class LoginView {

    LoginActivity loginActivity;

    EditText edt_enter_email, edt_enter_password;
    Button btn_login;

    public LoginView(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
        init();
    }

    public void init() {

        edt_enter_email = loginActivity.findViewById(R.id.edt_enter_email);
        edt_enter_password = loginActivity.findViewById(R.id.edt_enter_password);
        btn_login = loginActivity.findViewById(R.id.btn_login);
    }
}
