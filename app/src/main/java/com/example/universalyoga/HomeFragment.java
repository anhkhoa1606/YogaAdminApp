package com.example.universalyoga;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyoga.Adapter.YogaCourseAdapter;
import com.example.universalyoga.Database.AppDatabase;
import com.example.universalyoga.Model.YogaCourse;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HomeFragment extends Fragment implements YogaCourseAdapter.OnCourseClickListener {

    private YogaCourseAdapter adapter;
    private List<YogaCourse> yogaCourses;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        Button btnPushToFirebase = view.findViewById(R.id.btnPushToFirebase);

        // Use GridLayoutManager to arrange items in a grid layout with 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        loadData();

        adapter = new YogaCourseAdapter(yogaCourses, this);
        recyclerView.setAdapter(adapter);

        // Set up Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("yoga_courses");

        // Set up click listener for "Push to Firebase" button
        btnPushToFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushDataToFirebase();
            }
        });

        return view;
    }

    private void loadData() {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        yogaCourses = db.yogaCourseDao().getAllYogaCourses();
    }

    private void pushDataToFirebase() {
        DataTransferHelper firebase = new DataTransferHelper(AppDatabase.getDatabase(getContext()));
        firebase.transferDataToFirebase();
        // Display success message
        Toast.makeText(getContext(), "Data pushed to Firebase successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCourseClick(YogaCourse course) {
        // Open CourseDetailFragment
        Bundle bundle = new Bundle();
        bundle.putInt("courseId", course.getCourseId());

        CourseDetailFragment fragment = new CourseDetailFragment();
        fragment.setArguments(bundle);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
