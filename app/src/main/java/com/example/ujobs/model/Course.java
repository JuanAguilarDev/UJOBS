package com.example.ujobs.model;

public class Course {
    private String uid;
    private String topic;
    private String author;
    private String image;
    private String description;
    private int month;
    private int year;

    public void setDate(int month, int year){
        this.month = month;
        this.year = year;
    }

    public int [] getDate(){
        int [] date = {month, year};
        return date;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getAuthor(){
        return author;
    }

    public void setTopic(String topic){
        this.topic = topic;
    }

    public String getTopic(){
        return topic;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getImage(){
        return image;
    }

    public String getUid(){
        return uid;
    }

    public void setUid(String uid){
        this.uid = uid;
    }

}
