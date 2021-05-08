package com.indev.jubicare_assistants;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.adapter.BookingListAdapter;
import com.indev.jubicare_assistants.adapter.PatientListAdapter;
import com.indev.jubicare_assistants.model.PharmacyPatientModel;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientProfileList extends AppCompatActivity {
    @BindView(R.id.rv_profile)
    RecyclerView rv_profile;
    @BindView(R.id.etSearchBar)
    EditText etSearchBar;
    @BindView(R.id.btnSearch)
    ImageView btnSearch;
    @BindView(R.id.tv_list_count)
    TextView tv_list_count;
    @BindView(R.id.tvNoDataFound)
    TextView tvNoDataFound;
    @BindView(R.id.btn_go_home)
    TextView btn_go_home;
    String contect_no;
    /*normal widgets*/
    private Context context = this;
    SharedPrefHelper sharedPrefHelper;
    PatientListAdapter patientListAdapter;
    PharmacyPatientModel pharmacyPatientModel = new PharmacyPatientModel();
    ArrayList<ContentValues> patientContentValue = new ArrayList<ContentValues>();
    private ProgressDialog mProgressDialog;
    String searchInput;
    PatientModel patientModel = new PatientModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
        setTitle("Patient Profile List");
        initViews();


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            contect_no = bundle.getString("et_contect", "");

        }
        if (contect_no!=null) {
            callPatientListApi1();
        }else {
            callPatientListApi();

        }


    }

    @OnClick({R.id.btnSearch, R.id.btn_go_home})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                searchInput = etSearchBar.getText().toString().trim();
                /*Intent intent1 = new Intent(context, PatientListReplicaforSearch.class);
                intent1.putExtra("searchInput", searchInput);
                startActivity(intent1);*/
                callPatientListApi();
                //finish();
                break;
            case R.id.btn_go_home:
                Intent intentPatient = new Intent(context, HomeActivity.class);
                intentPatient.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentPatient);
                finish();
                break;
        }
    }


    private void callPatientListApi1() {
        mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
        pharmacyPatientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
        pharmacyPatientModel.setContact_no(contect_no);
        pharmacyPatientModel.setRole_id(sharedPrefHelper.getString("role_id", ""));
       // pharmacyPatientModel.setMobile(searchInput);
        Gson mGson = new Gson();
        String data = mGson.toJson(pharmacyPatientModel);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        APIClient.getClient().create(TELEMEDICINE_API.class).searchData(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        mProgressDialog.dismiss();
                        patientContentValue.clear();
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {

                        }
                        JsonObject singledataP = response.body();
                        JsonArray data = singledataP.getAsJsonArray("tableData");
                        if (data.size() > 0) {
                            for (int i = 0; i < data.size(); i++) {
                                JSONObject singledata = new JSONObject(data.get(i).toString());
                                Log.e("bcjhdbjcb", "onResponse: " + singledata.toString());

                                Iterator keys = singledata.keys();
                                ContentValues contentValues = new ContentValues();
                                while (keys.hasNext()) {
                                    String currentDynamicKey = (String) keys.next();
                                    contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                }
                                patientContentValue.add(contentValues);
                                tv_list_count.setText("Total - " + patientContentValue.size());

                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                                patientListAdapter = new PatientListAdapter(context, patientContentValue);
                                rv_profile.setLayoutManager(mLayoutManager);
                                rv_profile.setAdapter(patientListAdapter);
                                patientListAdapter.onItemClick(new PatientListAdapter.ClickListener() {
                                    @Override
                                    public void onItemClick(int position) {

                                    }

                                    @Override
                                    public void onListItemClick(int position) {
                                        Intent intent = new Intent(context, CommonProfile.class);
                                        intent.putExtra("profile_tab", "profile_tab");
                                        intent.putExtra("profile_patient_id", patientContentValue.get(position).get("profile_patient_id").toString());
                                        startActivity(intent);
                                    }
                                });
                            }
                        } else {
                            tv_list_count.setVisibility(View.GONE);
                            rv_profile.setVisibility(View.GONE);
                            tvNoDataFound.setVisibility(View.VISIBLE);
                            btn_go_home.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    private void callPatientListApi() {
        mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
        pharmacyPatientModel.setUser_id(sharedPrefHelper.getString("user_id", ""));
        pharmacyPatientModel.setRole_id(sharedPrefHelper.getString("role_id", ""));
        pharmacyPatientModel.setMobile(searchInput);
        Gson mGson = new Gson();
        String data = mGson.toJson(pharmacyPatientModel);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        APIClient.getClient().create(TELEMEDICINE_API.class).patientListingApi(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        mProgressDialog.dismiss();
                        patientContentValue.clear();
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {

                        }
                        JsonObject singledataP = response.body();
                        JsonArray data = singledataP.getAsJsonArray("tableData");
                        if (data.size() > 0) {
                            for (int i = 0; i < data.size(); i++) {
                                JSONObject singledata = new JSONObject(data.get(i).toString());
                                Log.e("bcjhdbjcb", "onResponse: " + singledata.toString());

                                Iterator keys = singledata.keys();
                                ContentValues contentValues = new ContentValues();
                                while (keys.hasNext()) {
                                    String currentDynamicKey = (String) keys.next();
                                    contentValues.put(currentDynamicKey, singledata.get(currentDynamicKey).toString());
                                }
                                patientContentValue.add(contentValues);
                                tv_list_count.setText("Total - " + patientContentValue.size());

                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                                patientListAdapter = new PatientListAdapter(context, patientContentValue);
                                rv_profile.setLayoutManager(mLayoutManager);
                                rv_profile.setAdapter(patientListAdapter);
                                patientListAdapter.onItemClick(new PatientListAdapter.ClickListener() {
                                    @Override
                                    public void onItemClick(int position) {

                                    }

                                    @Override
                                    public void onListItemClick(int position) {
                                        Intent intent = new Intent(context, CommonProfile.class);
                                        intent.putExtra("profile_tab", "profile_tab");
                                        intent.putExtra("profile_patient_id", patientContentValue.get(position).get("profile_patient_id").toString());
                                        startActivity(intent);
                                    }
                                });
                            }
                        } else {
                            tv_list_count.setVisibility(View.GONE);
                            rv_profile.setVisibility(View.GONE);
                            tvNoDataFound.setVisibility(View.VISIBLE);
                            btn_go_home.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    private void initViews() {
        sharedPrefHelper = new SharedPrefHelper(this);
        mProgressDialog = new ProgressDialog(context);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(PatientProfileList.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PatientProfileList.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
