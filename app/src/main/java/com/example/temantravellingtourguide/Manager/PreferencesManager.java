package com.example.temantravellingtourguide.Manager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String PREF_NAME = "userPref";
    private static final String KEY_PROGRESS_ID = "progress_id";
    private static final String KEY_NAME = "nama";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHOTO_URL = "photo_url";
    private static final String KEY_NO_WA = "no_wa";
    private static final String KEY_KODE_NEGARA = "kode_negara";
    private static final String KEY_KOTA = "kota";
    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    public PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void setProgressId(String progressId){
        mPref.edit()
                .putString(KEY_PROGRESS_ID, progressId)
                .apply();
    }

    public String getProgressId(){
        return mPref.getString(KEY_PROGRESS_ID, "");
    }

    public void setName(String name){
        mPref.edit()
                .putString(KEY_NAME, name)
                .apply();
    }

    public String getName(){
        return mPref.getString(KEY_NAME, "");
    }

    public void setEmail(String email){
        mPref.edit()
                .putString(KEY_EMAIL, email)
                .apply();
    }

    public String getEmail(){
        return mPref.getString(KEY_EMAIL, "");
    }

    public void setPhotoUrl(String photoUrl){
        mPref.edit()
                .putString(KEY_PHOTO_URL, photoUrl)
                .apply();
    }

    public String getPhotoUrl(){
        return mPref.getString(KEY_PHOTO_URL, "");
    }

    public void setNoWhatsapp(String noWhatsapp){
        mPref.edit()
                .putString(KEY_NO_WA, noWhatsapp)
                .apply();
    }

    public String getNoWhatsapp(){
        return mPref.getString(KEY_NO_WA, "");
    }

    public void setKodeNegara(String kodeNegara){
        mPref.edit()
                .putString(KEY_KODE_NEGARA, kodeNegara)
                .apply();
    }

    public String getKodeNegara(){
        return mPref.getString(KEY_KODE_NEGARA, "");
    }

    public void setKota(String kota){
        mPref.edit()
                .putString(KEY_KOTA, kota)
                .apply();
    }

    public String getKota(){
        return mPref.getString(KEY_KOTA, "");
    }

    public void resetAllUserData(){
        mPref.edit()
                .putString(KEY_PROGRESS_ID,"")
                .putString(KEY_NAME,"")
                .putString(KEY_EMAIL,"")
                .putString(KEY_PHOTO_URL,"")
                .putString(KEY_NO_WA,"")
                .putString(KEY_KOTA,"")
                .apply();
    }
}
