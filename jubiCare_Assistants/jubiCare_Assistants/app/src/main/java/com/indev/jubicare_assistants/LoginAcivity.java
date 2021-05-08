package com.indev.jubicare_assistants;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginAcivity extends AppCompatActivity {
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.tv_sign_up)
    TextView tv_signup;
    @BindView(R.id.tv_forgot_password)
    TextView tv_forgot_password;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.cb_showPassword)
    CheckBox cb_showPassword;
    public static RelativeLayout rl_technology_partner;
    /*normal widgets*/
    private ProgressDialog mProgressDialog;
    private Context context = this;
    private SharedPrefHelper sharedPrefHelper;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = LoginAcivity.class.getSimpleName();
    /*--for validation--*/
    private EditText flagEditfield;
    private String msg = "";
    boolean mPushTokenIsRegistered;

    private String mUserId;
    private long mSigningSequence = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login_acivity);

        ButterKnife.bind(this);
        sharedPrefHelper = new SharedPrefHelper(this);
        //  btn_login=findViewById(R.id.btn_login);
        getSupportActionBar().hide();
        displayPassword();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    callLoginApi(view);
                }
//                Intent intent = new Intent(LoginAcivity.this, HomeActivity.class);
//                startActivity(intent);
//                finish();
            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginAcivity.this, ForgetPassword.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void displayPassword() {
        cb_showPassword.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                // show password
                et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // hide password
                et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }

    private void callLoginApi(View view) {
        String username = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        Snackbar.make(view, "Authenticating online" + "!!!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        if (checkInternetConnection(context) == false) {
            new AlertDialog.Builder(context)
                    .setTitle("Alert !")
                    .setMessage("Network Error, check your network connection.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            mProgressDialog = ProgressDialog.show(context, "Login", "Please Wait...", true);
            LoginModel mLoginModel = new LoginModel();
            mLoginModel.setPassword(password);
            mLoginModel.setUser_name(username);
//                mLoginModel.setFirebase_token(sharedPrefHelper.getString("Token", ""));
//                mLoginModel.setApp_version(FINAL_VAR.app_version);
            Gson mGson = new Gson();
            String data = mGson.toJson(mLoginModel);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, data);
            APIClient.getClient().create(TELEMEDICINE_API.class).login(body).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        mProgressDialog.dismiss();
                        if (jsonObject.optString("success").equalsIgnoreCase("1")) {
                            sharedPrefHelper.setString("is_login", "1");
                            String user_id = jsonObject.optString("user_id");
                            String message = jsonObject.optString("message");
                            String success = jsonObject.optString("success");
                            String role_id = jsonObject.optString("role_id");
                            String full_name = jsonObject.optString("full_name");
                            String profile_pic = jsonObject.optString("profile_pic");
                            String mobile_token = jsonObject.optString("mobile_token");
                            sharedPrefHelper.setString("user_id", user_id);
                            sharedPrefHelper.setString("role_id", role_id);
                            sharedPrefHelper.setString("full_name", full_name);
                            sharedPrefHelper.setString("profile_pic", profile_pic);
                            sharedPrefHelper.setString("userName", username);
                            sharedPrefHelper.setString("mobile_token", mobile_token);

//                           // if (success.equalsIgnoreCase("1") && role_id.equalsIgnoreCase("8")) {
//                                Intent intent = new Intent(LoginAcivity.this, HomeActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                    /*.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
//                                startActivity(intent);
//                                finish();
//                            }
                            if (success.equalsIgnoreCase("1") && role_id.equalsIgnoreCase("8")) {
                                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                                Intent intent = new Intent(LoginAcivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }


                        } else {
                            mProgressDialog.dismiss();
                            Toast.makeText(context, "Invalid Credential", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        mProgressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    mProgressDialog.dismiss();
                }
            });
        }
    }
    public static boolean checkInternetConnection(Context context) {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean checkValidation() {
        if (et_email.getText().toString().trim().length() == 0) {
            flagEditfield = et_email;
            msg = "Please enter username";
            flagEditfield.setError(msg);
            et_email.requestFocus();
            return false;
        } else if (et_password.getText().toString().trim().length() == 0) {
            flagEditfield = et_password;
            msg = "Please enter password";
            flagEditfield.setError(msg);
            et_password.requestFocus();
            return false;
        }
        return true;
    }
}