package com.example.thelibrary.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;

public class Book {
    private int id;
    private Bitmap bitmapImage;
    private byte[] image;
    private String name;
    private String description;
    private String rack;
    private String step;
    private String column;
    private String position;

    public Book() {
    }

    public Book(int id, byte[] image, String name, String description, String rack, String step, String column, String position) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.description = description;
        this.rack = rack;
        this.step = step;
        this.column = column;
        this.position = position;

        initBitMap();
    }

    private void initBitMap() {
        if(image != null) this.bitmapImage = BitmapFactory.decodeByteArray(this.image,0,image.length);
    }

    public Book(byte[] image, String name, String description, String rack, String step, String column, String position) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.rack = rack;
        this.step = step;
        this.column = column;
        this.position = position;
        initBitMap();
    }

    public Book(Bitmap bitmapImage, String name, String description, String rack, String step, String column, String position) {
        this.bitmapImage = bitmapImage;
        this.name = name;
        this.description = description;
        this.rack = rack;
        this.step = step;
        this.column = column;
        this.position = position;
        initByteArray();
    }

    private void initByteArray() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.image = stream.toByteArray();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public Bitmap getBitmapImage(){ return this.bitmapImage;}

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rack='" + rack + '\'' +
                ", step='" + step + '\'' +
                ", column='" + column + '\'' +
                ", position='" + position + '\'' +
                '}';
    }

    public boolean isFront(){
        String front = this.position.toUpperCase().trim();
        return front.equals("FRONT");
    }

    public boolean isBack(){
        String back = this.position.toUpperCase();
        return back.equals("BACK");
    }

}
