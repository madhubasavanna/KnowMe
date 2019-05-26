package com.madhubasavanna.knowme.wikipediadata;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class WikiUserData {
    String name;
    String imageUrl;
    int pageId;
    Bitmap thumbnail;

    public WikiUserData(){}

    public WikiUserData(String name, String imageUrl, int pageId, Bitmap thumbnail) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.pageId = pageId;
        this.thumbnail = thumbnail;
    }

    public WikiUserData(String name, int pageId){
        this.pageId = pageId;
        this.name = name;
        this.imageUrl = null;
    }

    public WikiUserData(String name, String url){
        this.name = name;
        this.imageUrl = url;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public byte[] convertTobyte(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = null;
        this.thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }
}
