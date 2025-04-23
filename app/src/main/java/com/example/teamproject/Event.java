package com.example.teamproject;

import java.util.Calendar;

public class Event {
    private int id;
    private String title;
    private String date;
    private String dateType;
    private String imageUri;
    private long eventTimeMillis;

    // Constructor có ID (đọc từ DB)
    public Event(int id, String title, String date, String dateType, String imageUri, long eventTimeMillis) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.dateType = dateType;
        this.imageUri = imageUri;
        this.eventTimeMillis = eventTimeMillis;
    }

    // Constructor tạo mới
    public Event(String title, String date, String dateType, String imageUri, long eventTimeMillis) {
        this.title = title;
        this.date = date;
        this.dateType = dateType;
        this.imageUri = imageUri;
        this.eventTimeMillis = eventTimeMillis;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDateType() { return dateType; }
    public void setDateType(String dateType) { this.dateType = dateType; }

    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }

    public long getEventTimeMillis() { return eventTimeMillis; }
    public void setEventTimeMillis(long eventTimeMillis) { this.eventTimeMillis = eventTimeMillis; }


    // Hàm tính ngày SK
    public String getCountdownText() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        long todayMillis = today.getTimeInMillis();
        long diffMillis = eventTimeMillis - todayMillis;
        long days = diffMillis / (1000 * 60 * 60 * 24);

        if (diffMillis > 0 && diffMillis % (1000 * 60 * 60 * 24) != 0) {
            days += 1; // Làm tròn lên nếu chưa đủ 1 ngày nguyên
        }

        if (days > 0) return days + " ngày nữa";
        else if (days == 0) return "Hôm nay";
        else return days*(-1) + " ngày trước";
    }
}
