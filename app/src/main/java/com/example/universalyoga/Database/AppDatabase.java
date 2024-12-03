package com.example.universalyoga.Database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.universalyoga.Dao.YogaClassDao;
import com.example.universalyoga.Dao.YogaCourseDao;
import com.example.universalyoga.Model.YogaClass;
import com.example.universalyoga.Model.YogaCourse;

@Database(entities = {YogaCourse.class, YogaClass.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract YogaCourseDao yogaCourseDao();
    public abstract YogaClassDao yogaClassDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "yoga_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
