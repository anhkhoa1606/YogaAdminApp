package com.example.universalyoga.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "yoga_course")
public class YogaCourse {
    @PrimaryKey(autoGenerate = true)
    public int courseId;

    public String dayOfWeek;
    public String timeOfCourse;
    public int capacity;
    public int duration;
    public double price;
    public String description;
    public String typeOfClass;

    // Constructors, getters, and setters can be added here

    public YogaCourse() {
    }

    public YogaCourse(String dayOfWeek, String timeOfCourse, int capacity, int duration, double price, String description, String typeOfClass) {
        this.dayOfWeek = dayOfWeek;
        this.timeOfCourse = timeOfCourse;
        this.capacity = capacity;
        this.duration = duration;
        this.price = price;
        this.description = description;
        this.typeOfClass = typeOfClass;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeOfCourse() {
        return timeOfCourse;
    }

    public void setTimeOfCourse(String timeOfCourse) {
        this.timeOfCourse = timeOfCourse;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeOfClass() {
        return typeOfClass;
    }

    public void setTypeOfClass(String typeOfClass) {
        this.typeOfClass = typeOfClass;
    }


    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("courseId", courseId);
        result.put("dayOfWeek", dayOfWeek);
        result.put("timeOfCourse", timeOfCourse);
        result.put("capacity", capacity);
        result.put("duration", duration);
        result.put("price", price);
        result.put("description", description);
        result.put("typeOfClass", typeOfClass);
        return result;
    }
}
