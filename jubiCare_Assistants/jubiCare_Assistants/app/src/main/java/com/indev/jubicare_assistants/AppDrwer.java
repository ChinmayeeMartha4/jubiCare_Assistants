package com.indev.jubicare_assistants;

import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppDrwer extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    public Toolbar toolbar;
ProgressDialog progressDialog;
    private View view;
    private Menu menu;
    private Menu navMenu;
    private FrameLayout frame;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Context context = this;
    TextView tv_name, tv_email;
    private SharedPrefHelper sharedPrefHelper;
    android.app.Dialog change_language_alert;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        view = getLayoutInflater().inflate(R.layout.activity_app_drwer, null);
        frame = view.findViewById(R.id.frame);
        getLayoutInflater().inflate(layoutResID, frame, true);

        super.setContentView(view);
        sharedPrefHelper = new SharedPrefHelper(context);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nvView);
        navigationView.inflateMenu(R.menu.new_menu);
        menu = navigationView.getMenu();
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);

        View header = navigationView.getHeaderView(0);
        tv_name = (TextView) header.findViewById(R.id.name);
        tv_email = (TextView) header.findViewById(R.id.email);

        getSupportActionBar().hide();
        tv_name.setText("Arun");
        tv_email.setText("arun@gmail.com");

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }
        };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);//when using our custom drawer icon
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //call method
        initializeView();
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        navMenu = navigationView.getMenu();
    }

    private void initializeView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//                drawerLayout.closeDrawers();
//                int id = item.getItemId();
//                switch (id) {
//                    case R.id.option_home:
//                        break;
//
//                    case R.id.option_aboutUs:
//                        break;
//
//                    case R.id.option_change_password:
//                        break;
//
//                    case R.id.option_logout:
//                        logoutDialog();
//                        break;
//
//                    case R.id.option_settings:
//                        break;
//                       case R.id.option_privacy_policy:
//                        break;
//
//                }
//                return true;
//            }
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();
                int id = item.getItemId();
                switch (id) {
                    case R.id.option_home:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.option_change_password:
                        Intent intent = new Intent(context, ChangePassword.class);
                        startActivity(intent);
                          finish();
                          Toast.makeText(AppDrwer.this, "Change Password", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
//                    case R.id.option_notifications:
//                        //Toast.makeText(AppDrawer.this, "Notifications", Toast.LENGTH_SHORT).show();
//                        Intent intentNotification = new Intent(context, Notification.class);
//                        startActivity(intentNotification);
//                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
//                        break;
                    case R.id.option_aboutUs:
                        Toast.makeText(AppDrwer.this, "About Us", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.option_privacy_policy:
                        Intent intentDisclaimer = new Intent(context, Disclaimer.class);
                        startActivity(intentDisclaimer);
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.option_search:
                        Intent intentSearch = new Intent(context, SearchByMobile.class);
                        intentSearch.putExtra("common_search", "common_search");
                        startActivity(intentSearch);
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;


                    case R.id.option_Contact_us:
                        Intent intentContactUs = new Intent(context, ContactUs.class);
                        startActivity(intentContactUs);
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                    case R.id.option_logout:
                        if (isInternetOn()) {
                            callLogoutApi();

                        } else {
                            Toast.makeText(context, "Please Check Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                        overridePendingTransition(R.anim.open_next, R.anim.close_next);
                        break;
                }
                return true;
            }

        });
    }

    public void callLogoutApi() {
        progressDialog = ProgressDialog.show(context, "Logout", "Please Wait...", true);
        CountInput countInput = new CountInput();
        countInput.setUser_id(sharedPrefHelper.getString("user_id", ""));

        Gson mGson = new Gson();
        String data = mGson.toJson(countInput);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        ProgressDialog finalProgressDialog = progressDialog;
        APIClient.getClient().

                create(TELEMEDICINE_API.class).callLogoutApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    finalProgressDialog.dismiss();
                    Log.e("jbfd", "nsh " + jsonObject.toString());
                    if (jsonObject.optString("success").equalsIgnoreCase("1")) {
                        sharedPrefHelper.setString("is_login", "");
                        Intent i = new Intent(AppDrwer.this, LoginAcivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        Toast.makeText(context, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    finalProgressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                finalProgressDialog.dismiss();
            }
        });


}

    private void logoutDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Logout")
                .setMessage("")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sharedPrefHelper.setString("is_login", "");
                        Intent i = new Intent(AppDrwer.this, LoginAcivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public boolean isInternetOn() {
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            return true;

        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }
}
