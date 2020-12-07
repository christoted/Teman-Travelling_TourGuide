package com.example.temantravellingtourguide.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class History implements Parcelable{
    private String userUID, city, language, dateAndTime, timeType, paymentMethod, status;
    private int duration;
    private long price;
    private boolean needVehicle;

    public History(String userUID, String city, String language, String dateAndTime, int duration, String timeType, boolean needVehicle, String paymentMethod, long price, String status) {
        this.userUID = userUID;
        this.city = city;
        this.language = language;
        this.dateAndTime = dateAndTime;
        this.timeType = timeType;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.duration = duration;
        this.price = price;
        this.needVehicle = needVehicle;
    }

    protected History(Parcel in) {
        userUID = in.readString();
        city = in.readString();
        language = in.readString();
        dateAndTime = in.readString();
        timeType = in.readString();
        paymentMethod = in.readString();
        status = in.readString();
        duration = in.readInt();
        price = in.readLong();
        needVehicle = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userUID);
        dest.writeString(city);
        dest.writeString(language);
        dest.writeString(dateAndTime);
        dest.writeString(timeType);
        dest.writeString(paymentMethod);
        dest.writeString(status);
        dest.writeInt(duration);
        dest.writeLong(price);
        dest.writeByte((byte) (needVehicle ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    public String getPartnerUID() {
        return userUID;
    }

    public void setPartnerUID(String userUID) {
        this.userUID = userUID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public boolean isNeedVehicle() {
        return needVehicle;
    }

    public void setNeedVehicle(boolean needVehicle) {
        this.needVehicle = needVehicle;
    }
}
