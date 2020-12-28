package com.dylansalim.qrmenuapp.models.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Role implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Role createFromParcel(Parcel in) {
            return new Role(in);
        }

        public Role[] newArray(int size) {
            return new Role[size];
        }
    };

    protected Role(Parcel in){
        id = in.readInt();
        name = in.readString();
    }

    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("name")
    private String name;

    public Role(int id, String name) {
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
