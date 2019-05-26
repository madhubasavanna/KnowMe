package com.madhubasavanna.knowme.userdata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;

@Entity
public class UserPreferences {
    @PrimaryKey
    private int id;
    private String title;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] thumbnail;
    private String thumbnailUrl;

    public UserPreferences(int id, String title, byte[] thumbnail, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Bitmap convertByteToBitmap(){
        Bitmap bitmap = BitmapFactory.decodeByteArray(this.thumbnail, 0, this.thumbnail.length);
        return bitmap;
    }

    public static byte[] convertBitmapTobyte(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = null;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }
}
