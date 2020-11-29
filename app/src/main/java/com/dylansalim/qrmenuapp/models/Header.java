package com.dylansalim.qrmenuapp.models;

public class Header {
    private String header;
    private int id;

    public Header() {
    }

    public Header(String header,int id) {
        this.header = header;
        this.id = id;
    }

    public String getHeader() {
        return header;
    }
    public void setHeader(String header) {
        this.header = header;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
