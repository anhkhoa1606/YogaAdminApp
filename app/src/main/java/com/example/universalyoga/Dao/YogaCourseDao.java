package com.example.universalyoga.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.universalyoga.Model.YogaCourse;

import java.util.List;

@Dao
public interface YogaCourseDao {
    @Insert
    long insertYogaCourse(YogaCourse course);

    @Update
    int updateYogaCourse(YogaCourse course);

    @Delete
    int deleteYogaCourse(YogaCourse course);

    @Query("SELECT * FROM yoga_course WHERE courseId = :courseId")
    YogaCourse getYogaCourseById(int courseId);

    @Query("SELECT * FROM yoga_course")
    List<YogaCourse> getAllYogaCourses();
}
