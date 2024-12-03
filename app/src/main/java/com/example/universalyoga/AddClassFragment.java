package com.example.universalyoga;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.universalyoga.Database.AppDatabase;
import com.example.universalyoga.Model.YogaClass;

import java.util.Calendar;

public class AddClassFragment extends Fragment {

    private EditText teacherName, date, comments;
    private Button addButton;
    private int courseId; // ID của khóa học mà lớp này thuộc về

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_class, container, false);

        // Ánh xạ các trường
        teacherName = view.findViewById(R.id.teacherName);
        date = view.findViewById(R.id.date);
        comments = view.findViewById(R.id.comments);
        addButton = view.findViewById(R.id.addButton);

        // Nhận courseId từ Bundle
        if (getArguments() != null) {
            courseId = getArguments().getInt("courseId");
        }

        // Mở DatePicker khi nhấn vào trường Date
        date.setOnClickListener(v -> showDatePicker());

        // Xử lý sự kiện khi nhấn nút Add
        addButton.setOnClickListener(v -> {
            if (validateInputs()) {
                addNewClass();
            }
        });

        return view;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    date.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private boolean validateInputs() {
        if (teacherName.getText().toString().isEmpty() || date.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addNewClass() {
        AppDatabase db = AppDatabase.getDatabase(getContext());

        // Tạo đối tượng YogaClass mới
        YogaClass yogaClass = new YogaClass();
        yogaClass.teacher = teacherName.getText().toString();
        yogaClass.date = date.getText().toString();
        yogaClass.comment = comments.getText().toString();
        yogaClass.courseId = courseId; // Gắn ID khóa học

        // Lưu lớp học vào database
        db.yogaClassDao().insertYogaClass(yogaClass);

        Toast.makeText(getContext(), "Class added successfully", Toast.LENGTH_SHORT).show();

        // Quay lại Fragment trước đó
        getParentFragmentManager().popBackStack();
    }
}
