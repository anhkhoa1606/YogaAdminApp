package com.example.universalyoga;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.universalyoga.R;

import java.util.Calendar;
import java.util.List;

public class SearchFragment extends Fragment {

    private Spinner dayOfWeekSpinner;
    private EditText timeOfClass, teacherName;
    private Button searchButton;
    private RecyclerView recyclerView;
    private YogaClassAdapter adapter;
    private List<YogaClass> searchResults;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        db = AppDatabase.getDatabase(getContext());

        // Khởi tạo các trường tìm kiếm
        teacherName = view.findViewById(R.id.teacherName);
        searchButton = view.findViewById(R.id.searchButton);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Thiết lập sự kiện tìm kiếm
        searchButton.setOnClickListener(v -> performSearch());

        return view;
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
    private void performSearch() {
        String teacher = teacherName.getText().toString();

        // Truy vấn database với các tiêu chí tìm kiếm
        searchResults = db.yogaClassDao().searchYogaClasses(teacher);

        if (searchResults.isEmpty()) {
            Toast.makeText(getContext(), "No results found", Toast.LENGTH_SHORT).show();
        }

        // Cập nhật RecyclerView với kết quả tìm kiếm
        adapter = new YogaClassAdapter(searchResults, new YogaClassAdapter.OnYogaClassActionListener() {
            @Override
            public void onEditYogaClass(YogaClass yogaClass) {
                // Implement edit functionality if needed
            }

            @Override
            public void onDeleteYogaClass(YogaClass yogaClass) {
                // Implement delete functionality if needed
            }

            @Override
            public void onYogaClassSelected(YogaClass yogaClass) {
                // Chuyển đến ClassDetailFragment
                navigateToClassDetail(yogaClass.getClassId());
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
