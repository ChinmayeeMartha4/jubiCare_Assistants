package com.indev.jubicare_assistants.model;

public class AppointmentMedicinePrescribedModel {

    private  int id;
    private  int patient_appointment_id;
    private  int medicine_id;
    private  int quantity_by_doctor;
    private  int quantity_by_chemist;
    private String doctor_updated_on;
    private String chemist_updated_on;
    private  int created_by;


    public static final String TABLE_NAME = "appointment_medicine_prescribed";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PATIENT_APPOINTMENT_ID  = "patient_appointment_id";
    public static final String COLUMN_MEDICINE_ID = "medicine_id";
    public static final String COLUMN_QUANTITY_BY_DOCTOR = "quantity_by_doctor";
    public static final String COLUMN_QUANTITY_BY_CHEMIST = "quantity_by_chemist";
    public static final String COLUMN_DOCTOR_UPDATED_ON = "doctor_updated_on";
    public static final String COLUMN_CHEMIST_UPDATED_ON = "chemist_updated_on";
    public static final String COLUMN_CREATED_BY = "created_by";
    public static final String COLUMN_FLAG = "flag";


    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PATIENT_APPOINTMENT_ID + " TEXT ,"
                    + COLUMN_MEDICINE_ID  + " TEXT ,"
                    + COLUMN_QUANTITY_BY_DOCTOR + " TEXT ,"
                    + COLUMN_QUANTITY_BY_CHEMIST + " TEXT ,"
                    + COLUMN_DOCTOR_UPDATED_ON + " TEXT ,"
                    + COLUMN_CHEMIST_UPDATED_ON + " TEXT ,"
                    + COLUMN_CREATED_BY + " TEXT ,"
                    + COLUMN_FLAG + "  INTEGER   "
                    + ")";


}
