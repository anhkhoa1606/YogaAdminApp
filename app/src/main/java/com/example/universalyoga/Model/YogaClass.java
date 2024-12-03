package com.example.universalyoga.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity(
        tableName = "yoga_class",
        foreignKeys = @ForeignKey(
                entity = YogaCourse.class,
                parentColumns = "courseId",
                childColumns = "courseId",
                onDelete = ForeignKey.CASCADE
        )
)
public class YogaClass {
    @PrimaryKey(autoGenerate = true)
    public int classId;

    public String date;
    public String teacher;
    public String comment;

    public int courseId; // Foreign key to YogaCourse

    // Constructors, getters, and setters can be added here


    public YogaClass() {
    }

    public YogaClass(int classId, String date, String teacher, String comment, int courseId) {
        this.classId = classId;
        this.date = date;
        this.teacher = teacher;
        this.comment = comment;
        this.courseId = courseId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("classId", classId);
        result.put("Date", date);
        result.put("Teacher", teacher);
        result.put("Comment", comment);
        return result;
    }
}
