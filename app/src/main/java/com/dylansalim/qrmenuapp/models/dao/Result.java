package com.dylansalim.qrmenuapp.models.dao;

import com.google.gson.annotations.Expose;

public class Result<T> {
    @Expose
    private String msg;

    @Expose
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
}
