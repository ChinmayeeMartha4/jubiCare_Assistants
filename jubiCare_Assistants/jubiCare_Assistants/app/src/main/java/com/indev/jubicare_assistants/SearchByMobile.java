package com.indev.jubicare_assistants;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.indev.jubicare_assistants.adapter.BookingListAdapter;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.indev.jubicare_assistants.rest_api.TELEMEDICINE_API;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchByMobile extends AppCompatActivity {
    @BindView(R.id.etSearchBar)
    EditText etSearchBar;
    @BindView(R.id.btnSearch)
    ImageView btnSearch;
    @BindView(R.id.rl_skip)
    RelativeLayout rl_skip;
    @BindView(R.id.tv_search_by_mobile)
    TextView tv_search_by_mobile;
    @BindView(R.id.btn_get_number)
    Button btn_get_number;
  @BindView(R.id.btn_search_by_name)
  CheckBox btn_search_by_name;

    /*normal widgets*/
    private Context context=this;
    String searchInput;
    String common_search="";
    String search_by_name="false";
    SharedPrefHelper sharedPrefHelper;
    ProgressDialog mProgressDialog;
    PatientModel patientModel = new PatientModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sir_batayenge);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
        setTitle("Search Patient ");

        sharedPrefHelper = new SharedPrefHelper(context);
        mProgressDialog = new ProgressDialog(context);

        /*get intent data here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            common_search = bundle.getString("common_search", "");
        }

        if (common_search.equalsIgnoreCase("common_search")) {
            tv_search_by_mobile.setVisibility(View.GONE);
            rl_skip.setVisibility(View.GONE);
        }

        btn_get_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(context, PatientProfileList.class);
                intent1.putExtra("et_contect", etSearchBar.getText().toString());
                startActivity(intent1);
                finish();
            }



//                if (btn_search_by_name.isChecked()){
//                    etSearchBar.setText("");
//                    etSearchBar.setHint("Search By Name");
//                    etSearchBar.setInputType(InputType.TYPE_CLASS_TEXT);
//                    search_by_name="true";
//                }else {
//                    etSearchBar.setText("");
//                    etSearchBar.setHint("Search By Mobile");
//                    etSearchBar.setInputType(InputType.TYPE_CLASS_NUMBER);
//                    search_by_name="false";



          });
    }

//    @OnClick({R.id.btnSearch, R.id.rl_skip, R.id.btn_get_number})
//    void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btnSearch:
//                searchInput = etSearchBar.getText().toString().trim();
//                if (containsDigit(searchInput) && search_by_name.equals("true")){
//                    Toast.makeText(context, "Please enter name of Patient", Toast.LENGTH_SHORT).show();
//                    return;
//                }
////                Intent intent1 = new Intent(context, PatientListReplicaforSearch.class);
////                intent1.putExtra("searchInput", searchInput);
////                intent1.putExtra("common_search", common_search);
////                intent1.putExtra("search_by_name",search_by_name);
////                intent1.putExtra("common_search_counsellor", "common_search_counsellor");
////                startActivity(intent1);
//                //finish();
//                break;
//            case R.id.rl_skip:
//                Intent intent=new Intent(context, PatientFillAppointment.class);
//                intent.putExtra("fromCounselor","fromCounselor");
//                startActivity(intent);
//                break;
//            case R.id.btn_get_number:
//                PatientFilledDataModel appointmentInput = new PatientFilledDataModel();
//                appointmentInput.setUser_id(sharedPrefHelper.getString("user_id", ""));
//                appointmentInput.setRole_id(sharedPrefHelper.getString("role_id", ""));
//
//                Gson gson1 = new Gson();
//                String data1 = gson1.toJson(appointmentInput);
//                MediaType JSON1 = MediaType.parse("application/json; charset=utf-8");
//                RequestBody body1 = RequestBody.create(JSON1, data1);
//
//                mProgressDialog = ProgressDialog.show(context, "", "Please Wait...", true);
//                callFormCounsellorSearchMobile(body1);
//                break;
//
//
//        }
//    }
//    public final boolean containsDigit(String s) {
//        boolean containsDigit = false;
//
//        if (s != null && !s.isEmpty()) {
//            for (char c : s.toCharArray()) {
//                if (containsDigit = Character.isDigit(c)) {
//                    containsDigit=true;
//                    break;
//                }
//            }
//        }
//
//        return containsDigit;
//    }
//
//
//    private void callFormCounsellorSearchMobile(RequestBody body1) {
//        APIClient.getClient().create(TELEMEDICINE_API.class).callFromCounsellorFillFormApi(body1).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body().toString());
//                    mProgressDialog.dismiss();
//                    Log.e("hdsstt", "sbhddyy " + jsonObject.toString());
//                    String success = jsonObject.optString("success");
//                    String message = jsonObject.optString("message");
//                    String caller_no = jsonObject.optString("caller_no");
//
//                    if (success.equalsIgnoreCase("1")) {
//                        etSearchBar.setText(caller_no);
//                        Toast.makeText(context, ""+message, Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(context, "Mobile number not found. Please enter mobile number.", Toast.LENGTH_LONG).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    mProgressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                if (mProgressDialog.isShowing()) {
//                    mProgressDialog.dismiss();
//                }
//            }
//        });
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
