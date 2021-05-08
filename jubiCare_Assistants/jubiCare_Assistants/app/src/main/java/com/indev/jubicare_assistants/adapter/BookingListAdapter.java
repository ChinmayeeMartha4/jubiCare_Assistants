package com.indev.jubicare_assistants.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.indev.jubicare_assistants.CommonProfile;
import com.indev.jubicare_assistants.PatientFillAppointment;
import com.indev.jubicare_assistants.R;
import com.indev.jubicare_assistants.SharedPrefHelper;
import com.indev.jubicare_assistants.interfaces.ClickListener;
import com.indev.jubicare_assistants.rest_api.APIClient;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.ViewHolder> {

    private Context context;
    private List<ContentValues> listModels;

    ClickListener clickListener;
    private long different;
    private long ellapsedmonth;
    private long ellapsedDays;
    private long ellapsedHours;
    private long ellapsedMinutes;
    private long ellapsedSeconds;
    SharedPrefHelper sharedPrefHelper;

    public BookingListAdapter(Context context, List<ContentValues> listModels) {
        this.context = context;
        this.listModels = listModels;
        sharedPrefHelper = new SharedPrefHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_patient_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_patient_name.setText(new StringBuilder().append(listModels.get(position).get("full_name").toString()).append(", ").toString());
        holder.tv_age.setText(new StringBuilder().append(listModels.get(position).get("age").toString()).append(" Years").toString());
        holder.tv_contact_no.setText(listModels.get(position).get("contact_no").toString());
        holder.tv_location.setText(listModels.get(position).get("village_name").toString() + ", " +
                listModels.get(position).get("block_name").toString() + ", " +
                listModels.get(position).get("district_name").toString() + ", " +
                listModels.get(position).get("state_name").toString());
        holder.tv_appointment_count.setText("Appointment Count - " + listModels.get(position).get("appointment_counting").toString());




        /*for date in sec, mint, hours and days*/
        String upCommeingDate="";
        if (sharedPrefHelper.getString("role_id", "").equalsIgnoreCase("")) {
            upCommeingDate = listModels.get(position).get("patient_appointments_created_at").toString();
        } else {
            upCommeingDate = listModels.get(position).get("profile_patients_created_at").toString();
        }
        if (upCommeingDate != null) {
            calculateTimeDifference(upCommeingDate, holder.tv_time_ago);
        }

        /*get image path*/
        String url = APIClient.IMAGE_URL + listModels.get(position).get("profile_pic").toString();
        if (listModels.get(position).get("profile_pic").toString().equalsIgnoreCase("")) {
            if (listModels.get(position).get("gender").toString().equalsIgnoreCase("M")) {
                holder.iv_patient_image.setImageResource(R.drawable.male_icon);
            } else {
                holder.iv_patient_image.setImageResource(R.drawable.female_icon);
            }
        } else {
            if (listModels.get(position).get("gender").toString().equalsIgnoreCase("M")) {
                Picasso.with(context)
                        .load(url)
                        .placeholder(R.drawable.male_icon)
                        .into(holder.iv_patient_image);
            } else {
                Picasso.with(context)
                        .load(url)
                        .placeholder(R.drawable.female_icon)
                        .into(holder.iv_patient_image);
            }
        }

        holder.cv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onListItemClick(position);
            }
        });

    }

    //convert time into days, month and years
    // to get the difference between two dates
    public void calculateTimeDifference(String strEndDate, TextView textView) {
        //SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            /*todo get current date time and change its format*/
            Calendar calendar = Calendar.getInstance();
            Date startDate = calendar.getTime();

            String mySdate = outputDateFormat.format(startDate);
            Date dateStart = outputDateFormat.parse(mySdate);

            /*todo change date time format of end date*/
            Date sDate = inputDateFormat.parse(strEndDate);

            //inputDateFormat.setTimeZone(TimeZone.getDefault());
            String myFormattedDate = outputDateFormat.format(sDate);

            Date dateEnd = outputDateFormat.parse(myFormattedDate);

            getTimeDifference(dateEnd, dateStart, textView);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTimeDifference(Date startDate, Date endDate, TextView textView) {
        //milliseconds
        different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long month = daysInMilli * 30;


        ellapsedmonth = different / month;
        different = different % month;

        ellapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        ellapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        ellapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        ellapsedSeconds = different / secondsInMilli;


        if (ellapsedmonth > 0) {

            textView.setText(" " + String.valueOf(ellapsedmonth) + " month" + "-" + ellapsedDays + " days");

        } else if (ellapsedDays > 0) {
            if (ellapsedDays > 1) {
                textView.setText(" " + String.valueOf(ellapsedDays) + " days ago");

            } else {
                textView.setText(" " + String.valueOf(ellapsedDays) + " day ago");
            }
        } else if (ellapsedHours > 0) {
            if (ellapsedHours > 1) {
                textView.setText(" " + String.valueOf(ellapsedHours) + " hours ago");
            } else {
                textView.setText(" " + String.valueOf(ellapsedHours) + " hour ago");
            }

        } else if (ellapsedMinutes > 0) {
            if (ellapsedMinutes > 1) {
                textView.setText(" " + String.valueOf(ellapsedMinutes) + " min ago");
            } else {
                textView.setText(" " + String.valueOf(ellapsedMinutes) + " min ago");
            }

        } else if (ellapsedSeconds > 0) {
            if (ellapsedSeconds > 1) {
                textView.setText(" " + String.valueOf(ellapsedSeconds) + " sec ago");
            } else {
                textView.setText(" " + String.valueOf(ellapsedSeconds) + " sec ago");
            }
        } else {
            textView.setText("0");
        }
    }

    public String getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return ("" + (int) dayCount + " day(s) ago");
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_contact_no)
        TextView tv_contact_no;
        @BindView(R.id.tv_patient_name)
        TextView tv_patient_name;
        @BindView(R.id.iv_patient_image)
        ImageView iv_patient_image;
        @BindView(R.id.cv_main)
        CardView cv_main;
        @BindView(R.id.tv_location)
        TextView tv_location;
        /*@BindView(R.id.tv_state)
        TextView tv_state;
        @BindView(R.id.tv_district)
        TextView tv_district;
        @BindView(R.id.tv_block)
        TextView tv_block;
        @BindView(R.id.tv_village)
        TextView tv_village;*/
        @BindView(R.id.tv_age)
        TextView tv_age;
        @BindView(R.id.tv_time_ago)
        TextView tv_time_ago;
        @BindView(R.id.tv_appointment_count)
        TextView tv_appointment_count;
        // @BindView(R.id.ll_addapppointment)
        //  TextView ll_addapppointment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ClickListener {
        void onItemClick(int position);

        void onListItemClick(int position);
    }

    public void onItemClick(ClickListener listener) {
        this.clickListener = listener;
    }
}


