package com.example.universalyoga.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.universalyoga.Model.YogaClass;

import java.util.List;

@Dao
public interface YogaClassDao {
    @Insert
    long insertYogaClass(YogaClass yogaClass);

    @Update
    int updateYogaClass(YogaClass yogaClass);

    @Delete
    int deleteYogaClass(YogaClass yogaClass);

    @Query("SELECT * FROM yoga_class WHERE classId = :classId")
    YogaClass getYogaClassById(int classId);

    @Query("SELECT * FROM yoga_class WHERE courseId = :courseId")
    List<YogaClass> getYogaClassesByCourseId(int courseId);

    @Query("SELECT * FROM yoga_class")
    List<YogaClass> getAllYogaClasses();

    @Query("SELECT * FROM yoga_class WHERE " +
            "(:teacher IS NULL OR teacher LIKE '%' || :teacher || '%')")
    List<YogaClass> searchYogaClasses(String teacher);
}
