package com.dylansalim.qrmenuapp.models.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RoleDao implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RoleDao createFromParcel(Parcel in) {
            return new RoleDao(in);
        }

        public RoleDao[] newArray(int size) {
            return new RoleDao[size];
        }
    };

    protected RoleDao(Parcel in){
        id = in.readInt();
        name = in.readString();
    }

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    public RoleDao(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
