package com.dylansalim.qrmenuapp.ui.onboarding;

public class ScreenItem {
    int Image;
    String Description;

    public ScreenItem(String description, int image){
        Description = description;
        Image = image;
    }

    public void setTitle(String title) {
        Description = title;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getTitle() {
        return Description;
    }

    public int getImage() {
        return Image;
    }
}

