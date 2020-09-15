package com.reqres.screens.login;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.reqres.R;
import com.reqres.retrofit.RetrofitService;
import com.reqres.retrofit.ServiceGenerator;
import com.reqres.screens.home.HomeActivity;
import com.reqres.screens.login.model.LoginResponse;
import com.reqres.screens.utils.Prefs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public class LoginController implements View.OnClickListener {
    LoginActivity loginActivity;
    LoginView loginView;

    public LoginController(LoginActivity loginActivity, LoginView loginView) {
        this.loginActivity = loginActivity;
        this.loginView = loginView;
        setClick();
    }

    private void setClick() {
        loginView.btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_login:
                if (checkforvalidation()) {
                    loginActivity.customProgressDialog.show();
                    RetrofitService apiService = ServiceGenerator.getAPIClient();
                    Call<LoginResponse> call = apiService.userLogin(loginView.edt_enter_email.getText().toString(), loginView.edt_enter_password.getText().toString());
                    call.enqueue(new retrofit2.Callback<LoginResponse>() {

                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            loginActivity.customProgressDialog.dismiss();
                            if (response.body() != null) {
                                if (!response.body().getToken().equals("")) {
                                    Prefs.getInstance().setString(Prefs.PREFS_KEY_ACCESS_TOKEN, response.body().getToken());
                                    loginActivity.startActivity(new Intent(loginActivity, HomeActivity.class));
                                    loginActivity.finishAffinity();
                                    loginActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                } else {
                                    Toast.makeText(loginActivity, "Login Unsuccessful!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            loginActivity.customProgressDialog.dismiss();
                        }
                    });
                }


                break;
        }
    }

    private boolean checkforvalidation() {
        if (loginView.edt_enter_email.getText().toString().length() == 0) {
            loginView.edt_enter_email.setError("Email ID is required");
            return false;
        } else if (loginView.edt_enter_password.getText().toString().length() == 0) {
            loginView.edt_enter_password.setError("Password is required");
            return false;
        } else if (!isValidEmail(loginView.edt_enter_email.getText().toString())) {
            Toast.makeText(loginActivity, "Invalid Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }


    public boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
