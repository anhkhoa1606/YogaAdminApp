package com.example.universalyoga;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.universalyoga.Dao.YogaClassDao;
import com.example.universalyoga.Dao.YogaCourseDao;
import com.example.universalyoga.Database.AppDatabase;
import com.example.universalyoga.Model.YogaClass;
import com.example.universalyoga.Model.YogaCourse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataTransferHelper {

    private static final String TAG = "DataTransferHelper";
    private final DatabaseReference dbRef;
    private final YogaCourseDao yogaCourseDao;
    private final YogaClassDao yogaClassDao;
    private final ExecutorService executorService;

    public DataTransferHelper(AppDatabase db) {
        this.dbRef = FirebaseDatabase.getInstance("https://universal-yoga-6a677-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference();
        this.yogaCourseDao = db.yogaCourseDao();
        this.yogaClassDao = db.yogaClassDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Transfer all local Room data to Firebase.
     */
    public void transferDataToFirebase() {
        // Xóa toàn bộ dữ liệu trên Firebase trước khi đẩy dữ liệu mới
        dbRef.child("YogaCourses").removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        executorService.execute(() -> {
                            try {
                                List<YogaCourse> courseList = yogaCourseDao.getAllYogaCourses();
                                List<YogaClass> classList = yogaClassDao.getAllYogaClasses();

                                for (YogaCourse course : courseList) {
                                    transferCourseToFirebase(course, classList);
                                }

                                Log.d(TAG, "Data successfully pushed to Firebase");
                            } catch (Exception e) {
                                Log.e(TAG, "Error during data transfer to Firebase", e);
                            }
                        });
                    } else {
                        Log.e(TAG, "Failed to clear Firebase data before pushing.");
                    }
                });
    }


    private void transferCourseToFirebase(YogaCourse course, List<YogaClass> classList) {
        DatabaseReference courseRef = dbRef.child("YogaCourses").child(String.valueOf(course.getCourseId()));

        // Push course details
        courseRef.setValue(course.toMap())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Course uploaded: " + course.getCourseId()))
                .addOnFailureListener(e -> Log.e(TAG, "Error uploading course ID " + course.getCourseId(), e));

        // Push related classes
        for (YogaClass yogaClass : classList) {
            if (yogaClass.getCourseId() == course.getCourseId()) {
                courseRef.child("classes").child(String.valueOf(yogaClass.getClassId()))
                        .setValue(yogaClass.toMap())
                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Class uploaded: " + yogaClass.getClassId()))
                        .addOnFailureListener(e -> Log.e(TAG, "Error uploading class ID " + yogaClass.getClassId(), e));
            }
        }
    }

    /**
     * Delete a specific class from Firebase under a course.
     */
    public void deleteClassFromFirebase(int classId, int courseId) {
        dbRef.child("YogaCourses")
                .child(String.valueOf(courseId))
                .child("classes")
                .child(String.valueOf(classId))
                .removeValue()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Class deleted from Firebase: " + classId))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to delete class from Firebase", e));
    }

    /**
     * Delete a course and all its related classes from Firebase.
     */
    public void deleteCourseFromFirebase(int courseId) {
        DatabaseReference courseRef = dbRef.child("Courses").child(String.valueOf(courseId));

        // Delete related classes
        courseRef.child("classes").removeValue()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Classes under course deleted: " + courseId))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to delete classes under course: " + courseId, e));

        // Delete the course
        courseRef.removeValue()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Course deleted from Firebase: " + courseId))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to delete course from Firebase", e));
    }
}
