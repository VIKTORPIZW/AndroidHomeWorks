package com.homework.hw4_1;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.homework.hw4_1_rework.R;

import java.util.Objects;

public class Contact {
    private String name;
    private String info;
    private InfoType infoType;
    private int image;

    public Contact(String name, String info, InfoType infoType) {
        this.name = name;
        this.info = info;
        this.infoType = infoType;
        this.image = setImage(infoType);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfoType(InfoType infoType) {
        this.infoType = infoType;
        this.image = setImage(infoType);
    }

    public InfoType getInfoType() {
        return this.infoType;
    }

    private int setImage(InfoType infoType) {
        if (infoType == InfoType.PHONE_NUMBER) {
            image = R.drawable.ic_baseline_contact_phone_24;
        } else if (infoType == InfoType.EMAIL) {
            image = R.drawable.ic_baseline_contact_mail_24;
        }
        return image;
    }

    public int getImage() {
        return this.image;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (null == object || getClass() != object.getClass())
            return false;
        Contact contact = (Contact) object;
        if (infoType != contact.infoType)
            return false;
        return Objects.equals(name, contact.name) & Objects.equals(info, contact.info);
    }

    @Override
    public int hashCode() {
        return (((null == infoType) ? 0 : infoType.hashCode()) +
                ((null == name) ? 0 : name.hashCode()) +
                ((null == info) ? 0 : info.hashCode()));
    }
}

enum InfoType {PHONE_NUMBER, EMAIL}
