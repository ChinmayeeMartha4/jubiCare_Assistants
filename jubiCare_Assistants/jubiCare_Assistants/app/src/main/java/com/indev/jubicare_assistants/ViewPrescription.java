package com.indev.jubicare_assistants;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewPrescription extends AppCompatActivity {
    @BindView(R.id.webView)
    WebView webView;
    String view_prescription_url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prescription);
        ButterKnife.bind(this);
        setTitle("View Prescription");

        /*get intent here*/
        Bundle bundle = getIntent().getExtras();
        if (bundle!= null) {
            view_prescription_url=bundle.getString("url", "");
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(view_prescription_url);

    }
}
