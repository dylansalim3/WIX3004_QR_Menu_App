package com.dylansalim.qrmenuapp.models.dao;

import android.os.Parcel;
import android.os.Parcelable;


import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Time;

public class StoreDao implements Parcelable {
    private int id;

    private String name;

    private String address;

    @SerializedName("postal_code")
    private int postalCode;

    private String city;

    private String country;

    @Nullable
    private Double latitude;

    @Nullable
    private Double longitude;

    @SerializedName("phone_num")
    private String phoneNum;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("open_hour")
    private String openHour;

    @SerializedName("closing_hour")
    private String closingHour;

    @SerializedName("special_opening_note")
    private String specialOpeningNote;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public StoreDao createFromParcel(Parcel in) {
            return new StoreDao(in);
        }

        public StoreDao[] newArray(int size) {
            return new StoreDao[size];
        }
    };

    protected StoreDao(Parcel in){
        id = in.readInt();
        name = in.readString();
        address = in.readString();
        postalCode = in.readInt();
        city = in.readString();
        country = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        phoneNum = in.readString();
        userId = in.readInt();
        openHour = in.readString();
        closingHour = in.readString();
        specialOpeningNote = in.readString();
    }

    public StoreDao(int id, String name, String address, int postalCode, String city, String country, @Nullable Double latitude, @Nullable Double longitude, String phoneNum, int userId, String openHour, String closingHour, String specialOpeningNote) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNum = phoneNum;
        this.userId = userId;
        this.openHour = openHour;
        this.closingHour = closingHour;
        this.specialOpeningNote = specialOpeningNote;
    }

    public StoreDao(String name, String address, int postalCode, String city, String country, @Nullable Double latitude, @Nullable Double longitude, String phoneNum, int userId, String openHour, String closingHour, String specialOpeningNote) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phoneNum = phoneNum;
        this.userId = userId;
        this.openHour = openHour;
        this.closingHour = closingHour;
        this.specialOpeningNote = specialOpeningNote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Nullable
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@Nullable Double latitude) {
        this.latitude = latitude;
    }

    @Nullable
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(@Nullable Double longitude) {
        this.longitude = longitude;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOpenHour() {
        return openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public String getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(String closingHour) {
        this.closingHour = closingHour;
    }

    public String getSpecialOpeningNote() {
        return specialOpeningNote;
    }

    public void setSpecialOpeningNote(String specialOpeningNote) {
        this.specialOpeningNote = specialOpeningNote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.address);
        parcel.writeInt(this.postalCode);
        parcel.writeString(this.city);
        parcel.writeString(this.country);
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);
        parcel.writeString(this.phoneNum);
        parcel.writeInt(this.userId);
        parcel.writeString(this.openHour);
        parcel.writeString(this.closingHour);
        parcel.writeString(this.specialOpeningNote);
    }

}
