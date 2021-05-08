package com.indev.jubicare_assistants.sqllite_db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import com.indev.jubicare_assistants.DiseaseInput;
import com.indev.jubicare_assistants.SharedPrefHelper;
import com.indev.jubicare_assistants.State;
import com.indev.jubicare_assistants.model.AppointmentMedicinePrescribedModel;
import com.indev.jubicare_assistants.model.AppointmentTestModel;
import com.indev.jubicare_assistants.model.Block;
import com.indev.jubicare_assistants.model.BloodGroupModel;
import com.indev.jubicare_assistants.model.Caste;
import com.indev.jubicare_assistants.model.DiseaseModel;
import com.indev.jubicare_assistants.model.District;
import com.indev.jubicare_assistants.model.IvrCallingMasking;
import com.indev.jubicare_assistants.model.MedicineListModel;
import com.indev.jubicare_assistants.model.Postoffice;
import com.indev.jubicare_assistants.model.PrescriptionDays;
import com.indev.jubicare_assistants.model.PrescriptionEatingSchedule;
import com.indev.jubicare_assistants.model.PrescriptionInterval;
import com.indev.jubicare_assistants.model.SubTestsModel;
import com.indev.jubicare_assistants.model.Symptom;
import com.indev.jubicare_assistants.model.SymptomModel;
import com.indev.jubicare_assistants.model.Test;
import com.indev.jubicare_assistants.model.Village;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SqliteHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "jubicar_db";
    static final int DATABASE_VERSION = 1;
    String DB_PATH_SUFFIX = "/databases/";
    int version;
    Context ctx;
    SharedPrefHelper sharedPrefHelper;

    public SqliteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
        sharedPrefHelper = new SharedPrefHelper(ctx);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(District.CREATE_TABLE);
        db.execSQL(State.CREATE_TABLE);
        db.execSQL(Block.CREATE_TABLE);
        db.execSQL(DiseaseModel.CREATE_TABLE);
        db.execSQL(MedicineListModel.CREATE_TABLE);
        db.execSQL(Village.CREATE_TABLE);
        db.execSQL(SymptomModel.CREATE_TABLE);
        db.execSQL(SubTestsModel.CREATE_TABLE);
        db.execSQL(AppointmentMedicinePrescribedModel.CREATE_TABLE);
        db.execSQL(AppointmentTestModel.CREATE_TABLE);
        db.execSQL(Test.CREATE_TABLE);
        db.execSQL(PrescriptionDays.CREATE_TABLE);
        db.execSQL(PrescriptionEatingSchedule.CREATE_TABLE);
        db.execSQL(PrescriptionInterval.CREATE_TABLE);
        db.execSQL(Postoffice.CREATE_TABLE);
        db.execSQL(BloodGroupModel.CREATE_TABLE);
        db.execSQL(IvrCallingMasking.CREATE_TABLE);
        db.execSQL(Caste.CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public SQLiteDatabase openDataBase() throws SQLException {

        Log.e("version", "outside" + version);

        File dbFile = ctx.getDatabasePath(DATABASE_NAME);
        //  checkDbVersion(dbFile);
        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }


    public void saveMasterTable(ContentValues contentValues, String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        long idsds = db.insert(tablename, null, contentValues);
        Log.d("LOG", idsds + " id");
        db.close();
    }

    public void dropTable(String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM'" + tablename + "'");
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }


    public Cursor getMasterDataFromLocal(String type, String where) {
        int n = where.length();
        where = where.substring(0, n - 1);
        String query = "Select * from  " + type + " where category_id  in (" + where + ")";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor curs = db.rawQuery(query, null);
        return curs;
    }

    public Cursor getMasterDataFromLocal(String tablename) {
        String query = "Select * from  " + tablename;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor curs = db.rawQuery(query, null);
        return curs;
    }

    public long updateFlagInTable(String tableName, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {

                ContentValues value = new ContentValues();
                value.put("status", flag); // Name
                inserted_id = db.update(tableName, value, null, null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }


    public ArrayList<String> getspnidDistrictData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select name from district where id=state_id";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
//                        PhcPojo phcPojo = new PhcPojo();
                        String name = cursor.getString(cursor.getColumnIndex("name"));

                        cursor.moveToNext();
                        arrayList.add(name);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }
    /*multi select spinner for village*/
    public HashMap<String, Integer> getSymptoms() {
        HashMap<String, Integer> symptomsHM = new HashMap<>();
        Symptom symptom;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from symptom";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        symptom = new Symptom();
                        symptom.setSymptom_id(cursor.getInt(cursor.getColumnIndex("id")));
                        symptom.setSymptom(cursor.getString(cursor.getColumnIndex("symptom")));
                        symptom.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
                        cursor.moveToNext();
                        symptomsHM.put(symptom.getSymptom(), symptom.getSymptom_id());
                    }
                }
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return symptomsHM;
    }

    public ArrayList<String> getspnidStateData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select name from state ";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
//                        PhcPojo phcPojo = new PhcPojo();
                        String name = cursor.getString(cursor.getColumnIndex("name"));

                        cursor.moveToNext();
                        arrayList.add(name);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public HashMap<String, Integer> getDisease() {
        HashMap<String, Integer> diseaseHM = new HashMap<>();
        DiseaseInput opdDisease;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select * from disease";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        opdDisease = new DiseaseInput();
                        opdDisease.setId(cursor.getString(cursor.getColumnIndex("id")));
                        opdDisease.setDisease_name(cursor.getString(cursor.getColumnIndex("disease_name")));
                        cursor.moveToNext();
                        diseaseHM.put(opdDisease.getDisease_name(), Integer.valueOf(opdDisease.getId()));
                    }
                }
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }

        return diseaseHM;
    }
    public ArrayList<String> getspnidVillageData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select name from state";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
//                        PhcPojo phcPojo = new PhcPojo();
                        String name = cursor.getString(cursor.getColumnIndex("name"));

                        cursor.moveToNext();
                        arrayList.add(name);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<String> getspnBloodGroupData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select name from blood_group ";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));

                        cursor.moveToNext();
                        arrayList.add(name);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {


            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public ArrayList<String> getspnCasteData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select name from caste ";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));

                        cursor.moveToNext();
                        arrayList.add(name);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {


            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }


    public ArrayList<String> getspnidBlockData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select name from state";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
//                        PhcPojo phcPojo = new PhcPojo();
                        String name = cursor.getString(cursor.getColumnIndex("name"));

                        cursor.moveToNext();
                        arrayList.add(name);
                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return arrayList;
    }

    public HashMap<String, Integer> getAllState() {
        HashMap<String, Integer> state = new HashMap<>();
        State state1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from state";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        state1 = new State();
                        state1.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        state1.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        state.put(state1.getName(), state1.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return state;
    }

    public HashMap<String, Integer> getAllDistrict(int state_id) {
        HashMap<String, Integer> district = new HashMap<>();
        District district1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from district where state_id=" + state_id;
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        district1 = new District();
                        district1.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        district1.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        district.put(district1.getName().trim(), district1.getId());
                    }
                }
            }
            sqLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return district;
    }

    public void saveVideoData(IvrCallingMasking user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("caller_id", user.getCaller_id());
                values.put("reciver_id", user.getReciver_id());
                values.put("patient_appointment_id", user.getPatient_appointment_id());
                values.put("role_id", user.getRole_id());
                values.put("calling_type", user.getCalling_type());
                values.put("calling_screen", user.getCalling_screen());
                values.put("flag", 0);
                // Inserting Row
                db.insert("ivr_calling_masking", null, values);

                db.close(); // Closing database connection
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }

    public void updateVideoData(IvrCallingMasking user, String masking_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("patient_name", user.getPatient_name());
                values.put("uid", user.getUid());
                values.put("call_status", user.getCall_status());
                values.put("start_time", user.getStart_time());
                values.put("end_time", user.getEnd_time());
                values.put("time_duration", user.getTime_duration());
                values.put("flag", 0);
                // Inserting Row

                db.update("ivr_calling_masking", values, "ivr_calling_masking_id" + " = " + masking_Id + "", null);


                db.close(); // Closing database connection
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
    }


    public ArrayList<IvrCallingMasking> getVideoDataForSync() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<IvrCallingMasking> callingMaskings = new ArrayList<>();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select patient_name, uid, call_status, start_time, end_time, time_duration, id,ivr_calling_masking_id from ivr_calling_masking where flag = 0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        IvrCallingMasking callingMasking = new IvrCallingMasking();
                        callingMasking.setIvr_call_masking_id(cursor.getString(cursor.getColumnIndex("ivr_calling_masking_id")));
                        callingMasking.setPatient_name(cursor.getString(cursor.getColumnIndex("patient_name")));
                        callingMasking.setId(cursor.getString(cursor.getColumnIndex("id")));
                        callingMasking.setVideo_file(cursor.getString(cursor.getColumnIndex("uid")));
                        callingMasking.setCall_status(cursor.getString(cursor.getColumnIndex("call_status")));
                        callingMasking.setStart_time(cursor.getString(cursor.getColumnIndex("start_time")));
                        callingMasking.setEnd_time(cursor.getString(cursor.getColumnIndex("end_time")));
                        callingMasking.setTime_duration(cursor.getString(cursor.getColumnIndex("time_duration")));

                        cursor.moveToNext();
                        callingMaskings.add(callingMasking);

                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return callingMaskings;
    }


    public IvrCallingMasking getVideoData() {
        SQLiteDatabase db = this.getReadableDatabase();
        IvrCallingMasking callingMasking = null;
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select caller_id, reciver_id, patient_appointment_id, role_id, calling_type, calling_screen, id from ivr_calling_masking where flag = 0";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    while (!cursor.isAfterLast()) {
                        callingMasking = new IvrCallingMasking();
                        callingMasking.setUser_id(cursor.getString(cursor.getColumnIndex("caller_id")));
                        callingMasking.setId(cursor.getString(cursor.getColumnIndex("id")));
                        callingMasking.setProfile_patient_id(cursor.getString(cursor.getColumnIndex("reciver_id")));
                        callingMasking.setPatient_appointment_id(cursor.getString(cursor.getColumnIndex("patient_appointment_id")));
                        callingMasking.setRole_id(cursor.getString(cursor.getColumnIndex("role_id")));
                        callingMasking.setCalling_type(cursor.getString(cursor.getColumnIndex("calling_type")));
                        callingMasking.setCalling_screen(cursor.getString(cursor.getColumnIndex("calling_screen")));

                        cursor.moveToNext();

                    }
                    db.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return callingMasking;
    }


    public long updateFlagInTable(String table, String whr, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("flag", flag);
                inserted_id = db.update(table, values, whr + " = " + local_id + "", null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }

    public long updateServerIdTable(String table, String whr, int local_id, int flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("ivr_calling_masking_id", flag);
                inserted_id = db.update(table, values, whr + " = " + local_id + "", null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }


    public long updateVideoPathInTable(String table, String whr, int local_id, String flag) {
        long inserted_id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                ContentValues values = new ContentValues();
                values.put("uid", flag);
                inserted_id = db.update(table, values, whr + " = " + local_id + "", null);
                db.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return inserted_id;
    }


    public HashMap<String, Integer> getAllBlock(int district_id) {
        HashMap<String, Integer> block1 = new HashMap<>();
        Block block;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "";
                //if (sharedPrefHelper.getString("role_id","").equals("5")){
                query = "Select id, name from block where district_id = " + district_id;
                /*}else {
                    query  = "Select id, name from block where district_id = " +district_id + " and is_visual_patient = '1'";
                }*/

                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        /*if (!sharedPrefHelper.getString("role_id", "").equals("7")
                                && !cursor.getString(cursor.getColumnIndex("name")).equals("Other")) {*/
                        block = new Block();
                        block.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        block.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        block1.put(block.getName().trim(), block.getId());
                        //}
                    }
                    /*block = new Block();
                    block.setId(000);
                    block.setName("Other");
                    block1.put(block.getName().trim(), block.getId());*/
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return block1;
    }

    public HashMap<String, Integer> getAllCaste() {
        HashMap<String, Integer> caste = new HashMap<>();
        Caste state1;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "Select id, name from caste";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        state1 = new Caste();
                        state1.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        state1.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        caste.put(state1.getName(), state1.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }
        return caste;
    }


    public HashMap<String, Integer> getAllPostOffice(int block_id) {
        HashMap<String, Integer> postoffice1 = new HashMap<>();
        Postoffice postoffice;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "";
                //if (sharedPrefHelper.getString("role_id","").equals("5")){
                query = "Select id, name from post_office where block_id = " + block_id;
                /*}else {
                    query = "Select id, name from post_office where block_id = " +block_id+ " and is_visual_patient = '1' ";
                }*/

                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        postoffice = new Postoffice();
                        postoffice.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        postoffice.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        postoffice1.put(postoffice.getName(), postoffice.getId());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }

        return postoffice1;
    }

    public HashMap<String, Integer> getAllVillage(int post_office_id) {
        HashMap<String, Integer> village1 = new HashMap<>();
        Village village;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
                String query = "";
                //if (sharedPrefHelper.getString("role_id","").equals("5")){
                query = "Select id, name from village where post_office_id = " + post_office_id;
                /*}else {
                    query = "Select id, name from village where post_office_id = " + post_office_id +" and is_visual_patient = '1'  ";
                }*/
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        /*if (!sharedPrefHelper.getString("role_id", "").equals("7")
                                && !cursor.getString(cursor.getColumnIndex("name")).equals("Other")) {*/
                        village = new Village();
                        village.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        village.setName(cursor.getString(cursor.getColumnIndex("name")));
                        cursor.moveToNext();
                        village1.put(village.getName(), village.getId());
                        //}
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqLiteDatabase.close();
        }

        return village1;
    }

//    public HashMap<String, Integer> getDisease() {
//        HashMap<String, Integer> diseaseHM = new HashMap<>();
//        DiseaseInput opdDisease;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select * from disease";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        opdDisease = new DiseaseInput();
//                        opdDisease.setId(cursor.getString(cursor.getColumnIndex("id")));
//                        opdDisease.setDisease_name(cursor.getString(cursor.getColumnIndex("disease_name")));
//                        cursor.moveToNext();
//                        diseaseHM.put(opdDisease.getDisease_name(), Integer.valueOf(opdDisease.getId()));
//                    }
//                }
//                db.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//
//        return diseaseHM;
//    }


    public String getdisease_idFromDisease(String data) {

        String id = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "select id from disease where disease_name = '" + data + "'";
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        id = cursor.getString(cursor.getColumnIndex("id"));
                        cursor.moveToNext();
                    }
                }

                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return id;
    }

    public ArrayList<String> getSymptomDataList() {
        ArrayList<String> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (db != null && db.isOpen() && !db.isReadOnly()) {
                String query = "SELECT symptom FROM symptom";
                @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        dataList.add(cursor.getString(cursor.getColumnIndex("symptom")));
                        cursor.moveToNext();
                    }
                }
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        }
        return dataList;
    }

    /*multi select spinner for village*/
//    public HashMap<String, Integer> getSymptoms() {
//        HashMap<String, Integer> symptomsHM = new HashMap<>();
//        Symptom symptom;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select * from symptom";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        symptom = new Symptom();
//                        symptom.setSymptom_id(cursor.getInt(cursor.getColumnIndex("id")));
//                        symptom.setSymptom(cursor.getString(cursor.getColumnIndex("symptom")));
//                        symptom.setUser_id(cursor.getInt(cursor.getColumnIndex("user_id")));
//                        cursor.moveToNext();
//                        symptomsHM.put(symptom.getSymptom(), symptom.getSymptom_id());
//                    }
//                }
//                db.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//        return symptomsHM;
//    }
//
//    public HashMap<String, Integer> getPrescriptionDays() {
//        HashMap<String, Integer> symptomsHM = new HashMap<>();
//        PrescriptionDays symptom;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select * from prescription_days";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        symptom = new PrescriptionDays();
//                        symptom.setId(cursor.getString(cursor.getColumnIndex("id")));
//                        symptom.setName(cursor.getString(cursor.getColumnIndex("name")));
//                        cursor.moveToNext();
//                        symptomsHM.put(symptom.getName(), Integer.valueOf(symptom.getId()));
//                    }
//                }
//                db.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//        return symptomsHM;
//    }
//    public HashMap<String, Integer> getPrescriptionInterval() {
//        HashMap<String, Integer> symptomsHM = new HashMap<>();
//        PrescriptionDays symptom;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select * from prescription_interval";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        symptom = new PrescriptionDays();
//                        symptom.setId(cursor.getString(cursor.getColumnIndex("id")));
//                        symptom.setName(cursor.getString(cursor.getColumnIndex("name")));
//                        cursor.moveToNext();
//                        symptomsHM.put(symptom.getName(), Integer.valueOf(symptom.getId()));
//                    }
//                }
//                db.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//        return symptomsHM;
//    }
//    public HashMap<String, Integer> getPrescriptionEatingSchedule() {
//        HashMap<String, Integer> symptomsHM = new HashMap<>();
//        PrescriptionDays symptom;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select * from prescription_eating_schedule";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        symptom = new PrescriptionDays();
//                        symptom.setId(cursor.getString(cursor.getColumnIndex("id")));
//                        symptom.setName(cursor.getString(cursor.getColumnIndex("name")));
//                        cursor.moveToNext();
//                        symptomsHM.put(symptom.getName(), Integer.valueOf(symptom.getId()));
//                    }
//                }
//                db.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//        return symptomsHM;
//    }
//
//    public HashMap<String, Integer> getAllTest() {
//        HashMap<String, Integer> testHM = new HashMap<>();
//        Test testModel;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select id, test_name  from test where  del_action = 'N' ";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//
//
//                    while (!cursor.isAfterLast()) {
//                        testModel = new Test();
//                        testModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
//                        testModel.setTest_name(cursor.getString(cursor.getColumnIndex("test_name")));
//                        cursor.moveToNext();
//                        testHM.put(testModel.getTest_name().trim(), testModel.getId());
//                    }
//
//                }
//                db.close();
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//        return testHM;
//    }
//
//    public HashMap<String, Integer> getAllSubTest() {
//        HashMap<String, Integer> subTestHashMap = new HashMap<>();
//        SubTestsModel subTestsModel;
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        try {
//            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
//                String query = "Select id, test_name from sub_tests";
//                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        subTestsModel = new SubTestsModel();
//                        subTestsModel.setTest_id(cursor.getString(cursor.getColumnIndex("id")));
//                        subTestsModel.setTest_name(cursor.getString(cursor.getColumnIndex("test_name")));
//                        cursor.moveToNext();
//                        subTestHashMap.put(subTestsModel.getTest_name().trim(), Integer.valueOf(subTestsModel.getTest_id()));
//                    }
//                }
//            }
//            sqLiteDatabase.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return subTestHashMap;
//    }

    public int getTestCategoryId(String name) {
        int id = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select id from test where test_name = '" + name + "'  ";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        id = cur.getInt(cur.getColumnIndex("id"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return id;

    }

//    public HashMap<String, Integer> getAllSubTestByTestId(int id) {
//        HashMap<String, Integer> hashMap = new HashMap<>();
//        SubTestsModel testsModel;
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        try {
//            if (sqLiteDatabase != null && sqLiteDatabase.isOpen() && !sqLiteDatabase.isReadOnly()) {
//                String query = "Select id, test_name from sub_tests where test_id=" + id;
//                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        testsModel = new SubTestsModel();
//                        testsModel.setId(cursor.getInt(cursor.getColumnIndex("id")));
//                        testsModel.setTest_name(cursor.getString(cursor.getColumnIndex("test_name")));
//                        cursor.moveToNext();
//                        hashMap.put(testsModel.getTest_name().trim(), testsModel.getId());
//                    }
//                }
//            }
//            sqLiteDatabase.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return hashMap;
//    }
//
//    /*multi select spinner for village*/
//    public HashMap<String, Integer> getVillage() {
//        HashMap<String, Integer> villagesHM = new HashMap<>();
//        Village village;
//        SQLiteDatabase db = this.getReadableDatabase();
//        try {
//            if (db != null && db.isOpen() && !db.isReadOnly()) {
//                String query = "select * from village";
//                Cursor cursor = db.rawQuery(query, null);
//                if (cursor != null && cursor.getCount() > 0) {
//                    cursor.moveToFirst();
//                    while (!cursor.isAfterLast()) {
//                        village = new Village();
//                        village.setId(cursor.getInt(cursor.getColumnIndex("id")));
//                        village.setName(cursor.getString(cursor.getColumnIndex("name")));
//                        //village.setBlock_id(cursor.getString(cursor.getColumnIndex("block_id")));
//                        cursor.moveToNext();
//                        villagesHM.put(village.getName(), village.getId());
//                    }
//                }
//                db.close();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            db.close();
//        }
//        return villagesHM;
//    }

    public String getSelectedItemId(String table, String bloodGroup) {
        String id = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select id from '" + table + "' where name = '" + bloodGroup + "'";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        id = cur.getString(cur.getColumnIndex("id"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return id;
    }

    public String getUpdatedDate(String table_name) {
        String date = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select created_at from '" + table_name + "' order by id desc limit 0,1 ";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        date = cur.getString(cur.getColumnIndex("created_at"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return date;

    }

    public String getUpdatedOn(String table_name) {
        String date = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select created_on from '" + table_name + "' order by id desc limit 0,1 ";
        if (db != null && db.isOpen() && !db.isReadOnly()) {
            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.getCount() > 0) {
                cur.move(0);
                while (cur.moveToNext()) {
                    try {
                        date = cur.getString(cur.getColumnIndex("created_on"));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return date;

    }
}
