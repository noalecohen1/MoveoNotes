package com.noalecohen1.moveonotes.models;


import java.util.Calendar;

public class Note implements Comparable<Note> {
    String id;
    String title;
    String body;
    Calendar calendar;
    double latitude;
    double longitude;

    public Note(){
        calendar = Calendar.getInstance();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Calendar getCalender() {
        return calendar;
    }

    public void setCalender(Calendar calendar) {
        this.calendar = calendar;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int compareTo(Note note) {
        long thisMillis = this.getCalender().getTimeInMillis();
        long noteMillis = note.getCalender().getTimeInMillis();
        if(thisMillis > noteMillis){return 1;}
        if(thisMillis < noteMillis){return -1;}
        return 0;
    }
}
