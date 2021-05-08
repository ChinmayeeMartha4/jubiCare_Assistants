package com.indev.jubicare_assistants;

public class AppointmentInput {
    private String profile_patient_id;
    private String prescribed_medicine;
    private String user_id;
    private String prescribed_medicine_date;
    private String is_emergency;
    private String height;
    private String weight;
    private String appointment_file;
    private String blood_group_id;
    private String remarks;
    private String bp_high;
    private String bp_low;
    private String bp_lower;
    private String bp_upper;


    private String sugar;
    private String temperature;
    private String blood_oxygen_level;
    private String pulse;

    public String getBp_high() {
        return bp_high;
    }

    public void setBp_high(String bp_high) {
        this.bp_high = bp_high;
    }

    public String getBp_low() {
        return bp_low;
    }

    public void setBp_low(String bp_low) {
        this.bp_low = bp_low;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

//    private String sugar;
public String getBp_lower() {
    return bp_lower;
}

    public void setBp_lower(String bp_lower) {
        this.bp_lower = bp_lower;
    }

    public String getBp_upper() {
        return bp_upper;
    }

    public void setBp_upper(String bp_upper) {
        this.bp_upper = bp_upper;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getBlood_oxygen_level() {
        return blood_oxygen_level;
    }

    public void setBlood_oxygen_level(String blood_oxygen_level) {
        this.blood_oxygen_level = blood_oxygen_level;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }


    public String getAppointment_age() {
        return appointment_age;
    }

    public void setAppointment_age(String appointment_age) {
        this.appointment_age = appointment_age;
    }

    private String appointment_age;

    public String getCounsellor_remarks() {
        return counsellor_remarks;
    }

    public void setCounsellor_remarks(String counsellor_remarks) {
        this.counsellor_remarks = counsellor_remarks;
    }

    private String counsellor_remarks;
    private String appointment_type;
    private String disease_id;
    private String symptom_id;
    private String app_version;
    private String patient_appointment_id;
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPatient_appointment_id() {
        return patient_appointment_id;
    }

    public void setPatient_appointment_id(String patient_appointment_id) {
        this.patient_appointment_id = patient_appointment_id;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getProfile_patient_id() {
        return profile_patient_id;
    }

    public void setProfile_patient_id(String profile_patient_id) {
        this.profile_patient_id = profile_patient_id;
    }

    public String getAppointment_file() {
        return appointment_file;
    }

    public void setAppointment_file(String appointment_file) {
        this.appointment_file = appointment_file;
    }

    public String getBlood_group_id() {
        return blood_group_id;
    }

    public void setBlood_group_id(String blood_group_id) {
        this.blood_group_id = blood_group_id;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAppointment_type() {
        return appointment_type;
    }

    public void setAppointment_type(String appointment_type) {
        this.appointment_type = appointment_type;
    }

    public String getPrescribed_medicine() {
        return prescribed_medicine;
    }

    public void setPrescribed_medicine(String prescribed_medicine) {
        this.prescribed_medicine = prescribed_medicine;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPrescribed_medicine_date() {
        return prescribed_medicine_date;
    }

    public void setPrescribed_medicine_date(String prescribed_medicine_date) {
        this.prescribed_medicine_date = prescribed_medicine_date;
    }

    public String getIs_emergency() {
        return is_emergency;
    }

    public void setIs_emergency(String is_emergency) {
        this.is_emergency = is_emergency;
    }

    public String getSymptom_id() {
        return symptom_id;
    }

    public void setSymptom_id(String symptom_id) {
        this.symptom_id = symptom_id;
    }

    public String getDisease_id() {
        return disease_id;
    }

    public void setDisease_id(String disease_id) {
        this.disease_id = disease_id;
    }
}
