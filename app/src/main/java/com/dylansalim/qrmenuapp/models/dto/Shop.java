/**
 * Copyright 2020 bejson.com
 */
package com.dylansalim.qrmenuapp.models.dto;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Shop implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    protected Shop(Parcel in) {
        id = in.readInt();
        name = in.readString();
        address = in.readString();
        postal_code = in.readInt();
        city = in.readString();
        country = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        phone_num = in.readString();
        user_id = in.readInt();
        created = new Date(in.readLong());
        average_rating = in.readDouble();
    }


    private int id;
    private String name;
    private String address;
    private int postal_code;
    private String city;
    private String country;
    private double latitude;
    private double longitude;
    private String phone_num;
    private Date created;
    private int user_id;
    private double average_rating;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setPostal_code(int postal_code) {
        this.postal_code = postal_code;
    }

    public int getPostal_code() {
        return postal_code;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getCreated() {
        return created;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public double getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(double average_rating) {
        this.average_rating = average_rating;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", postal_code=" + postal_code +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", phone_num='" + phone_num + '\'' +
                ", created=" + created +
                ", user_id=" + user_id +
                ", average_rating=" + average_rating +
                '}';
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
        parcel.writeInt(this.postal_code);
        parcel.writeString(this.city);
        parcel.writeString(this.country);
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);
        parcel.writeString(this.phone_num);
        parcel.writeInt(this.user_id);
        parcel.writeLong(this.created.getTime());
        parcel.writeDouble(this.average_rating);
    }

}
