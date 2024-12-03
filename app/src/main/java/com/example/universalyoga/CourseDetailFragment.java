package com.example.universalyoga;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyoga.Adapter.YogaClassAdapter;
import com.example.universalyoga.Database.AppDatabase;
import com.example.universalyoga.Model.YogaClass;
import com.example.universalyoga.Model.YogaCourse;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CourseDetailFragment extends Fragment {

    private TextView courseName, courseDetails;
    private RecyclerView recyclerView;
    private MaterialButton btnUpdateCourse, btnDeleteCourse, btnAddClass;
    private YogaClassAdapter adapter;
    private List<YogaClass> yogaClasses;
    private YogaCourse currentCourse;
    private int courseId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_detail, container, false);

        // Bind views
        courseName = view.findViewById(R.id.courseName);
        courseDetails = view.findViewById(R.id.courseDetails);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnUpdateCourse = view.findViewById(R.id.btnUpdateCourse);
        btnDeleteCourse = view.findViewById(R.id.btnDeleteCourse);
        btnAddClass = view.findViewById(R.id.btnAddNewClass);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get courseId from arguments
        if (getArguments() != null) {
            courseId = getArguments().getInt("courseId");
            loadCourseDetails(courseId);
            loadYogaClasses(courseId);
        }

        // Update button click
        btnUpdateCourse.setOnClickListener(v -> openUpdateCourse());

        // Delete button click
        btnDeleteCourse.setOnClickListener(v -> deleteCourse());

        // Add class button click
        btnAddClass.setOnClickListener(v -> openAddClass());

        return view;
    }

    private void loadCourseDetails(int courseId) {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        currentCourse = db.yogaCourseDao().getYogaCourseById(courseId);

        if (currentCourse != null) {
            courseName.setText(currentCourse.getTypeOfClass());
            courseDetails.setText(currentCourse.getDayOfWeek() + " - " + currentCourse.getTimeOfCourse() + "\n" +
                    "Capacity: " + currentCourse.getCapacity() + "\n" +
                    "Duration: " + currentCourse.getDuration() + " mins\n" +
                    "Price: $" + currentCourse.getPrice() + "\n" +
                    "Description: " + currentCourse.getDescription());
        }
    }

    private void loadYogaClasses(int courseId) {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        yogaClasses = db.yogaClassDao().getYogaClassesByCourseId(courseId);

        adapter = new YogaClassAdapter(yogaClasses, new YogaClassAdapter.OnYogaClassActionListener() {
            @Override
            public void onEditYogaClass(YogaClass yogaClass) {
                // Navigate to ClassDetailFragment with the selected class ID
                navigateToClassDetail(yogaClass.getClassId());
            }

            @Override
            public void onDeleteYogaClass(YogaClass yogaClass) {
                deleteYogaClass(yogaClass);
            }
            @Override
            public void onYogaClassSelected(YogaClass yogaClass) {
                // Chuyển đến ClassDetailFragment
                navigateToClassDetail(yogaClass.getClassId());
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void navigateToClassDetail(int classId) {
        Bundle bundle = new Bundle();
        bundle.putInt("classId", classId);

        ClassDetailFragment fragment = new ClassDetailFragment();
        fragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void deleteYogaClass(YogaClass yogaClass) {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        db.yogaClassDao().deleteYogaClass(yogaClass);

        yogaClasses.remove(yogaClass);
        adapter.notifyDataSetChanged();

        Toast.makeText(getContext(), "Class deleted", Toast.LENGTH_SHORT).show();
    }

    private void openUpdateCourse() {
        Bundle bundle = new Bundle();
        bundle.putInt("courseId", courseId);

        AddCourseFragment fragment = new AddCourseFragment();
        fragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void openAddClass() {
        Bundle bundle = new Bundle();
        bundle.putInt("courseId", courseId);

        AddClassFragment fragment = new AddClassFragment();
        fragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void deleteCourse() {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        db.yogaCourseDao().deleteYogaCourse(currentCourse);

        DataTransferHelper helper = new DataTransferHelper(db);
        helper.deleteCourseFromFirebase(courseId);

        Toast.makeText(getContext(), "Course deleted", Toast.LENGTH_SHORT).show();

        // Navigate back to HomeFragment
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }
}