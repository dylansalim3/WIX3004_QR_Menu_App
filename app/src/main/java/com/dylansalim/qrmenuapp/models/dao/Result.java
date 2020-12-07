package com.dylansalim.qrmenuapp.models.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;

public class Result<T> {
    @Expose
    @NonNull
    private String msg;

    @Expose
    @Nullable
    private T data;

    public Result(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "msg='" + msg + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}
