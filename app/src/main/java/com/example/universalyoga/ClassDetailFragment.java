package com.example.universalyoga;

import android.os.Bundle;
import android.util.Log;
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

public class ClassDetailFragment extends Fragment {

    private EditText editTeacherName, editClassDate, editComments;
    private Button btnUpdateClass, btnDeleteClass;
    private YogaClass currentClass;
    private int classId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_detail, container, false);

        // Ánh xạ các view
        editTeacherName = view.findViewById(R.id.editTeacherName);
        editClassDate = view.findViewById(R.id.editClassDate);
        editComments = view.findViewById(R.id.editComments);
        btnUpdateClass = view.findViewById(R.id.btnUpdateClass);
        btnDeleteClass = view.findViewById(R.id.btnDeleteClass);

        // Lấy classId từ Bundle
        if (getArguments() != null) {
            classId = getArguments().getInt("classId", -1); // Nếu không tìm thấy, classId = -1
        }

        // Tải thông tin lớp học
        if (classId != -1) {
            loadClassDetails(classId);
        } else {
            Toast.makeText(getContext(), "Invalid class ID", Toast.LENGTH_SHORT).show();
            Log.e("ClassDetailFragment", "Invalid class ID");
        }

        // Xử lý sự kiện nút Update
        btnUpdateClass.setOnClickListener(v -> {
            if (validateInputs()) {
                updateClassDetails();
            }
        });

        // Xử lý sự kiện nút Delete
        btnDeleteClass.setOnClickListener(v -> deleteClass());

        return view;
    }

    private void loadClassDetails(int classId) {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        currentClass = db.yogaClassDao().getYogaClassById(classId);

        if (currentClass != null) {
            editTeacherName.setText(currentClass.teacher);
            editClassDate.setText(currentClass.date);
            editComments.setText(currentClass.comment);
            Log.d("ClassDetailFragment", "Loaded class: " + currentClass);
        } else {
            Toast.makeText(getContext(), "Class not found", Toast.LENGTH_SHORT).show();
            Log.e("ClassDetailFragment", "Class not found for ID: " + classId);
        }
    }

    private boolean validateInputs() {
        if (editTeacherName.getText().toString().isEmpty() || editClassDate.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateClassDetails() {
        if (currentClass == null) {
            Toast.makeText(getContext(), "Error: Unable to update. Class not found.", Toast.LENGTH_SHORT).show();
            Log.e("ClassDetailFragment", "Attempted to update a null class");
            return;
        }

        AppDatabase db = AppDatabase.getDatabase(getContext());

        // Cập nhật thông tin lớp học
        currentClass.teacher = editTeacherName.getText().toString();
        currentClass.date = editClassDate.getText().toString();
        currentClass.comment = editComments.getText().toString();

        // Lưu vào database
        int rowsAffected = db.yogaClassDao().updateYogaClass(currentClass);
        if (rowsAffected > 0) {
            Toast.makeText(getContext(), "Class updated successfully", Toast.LENGTH_SHORT).show();
            Log.d("ClassDetailFragment", "Class updated: " + currentClass);
            // Quay lại Fragment trước đó
            getParentFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
            Log.e("ClassDetailFragment", "Update failed for class: " + currentClass);
        }
    }
    private void deleteClass() {
        if (currentClass == null) {
            Toast.makeText(getContext(), "Error: Unable to delete. Class not found.", Toast.LENGTH_SHORT).show();
            Log.e("ClassDetailFragment", "Attempted to delete a null class");
            return;
        }

        AppDatabase db = AppDatabase.getDatabase(getContext());

        // Xóa lớp học khỏi database
        db.yogaClassDao().deleteYogaClass(currentClass);

        Toast.makeText(getContext(), "Class deleted successfully", Toast.LENGTH_SHORT).show();
        Log.d("ClassDetailFragment", "Class deleted: " + currentClass);

        // Quay lại Fragment trước đó
        getParentFragmentManager().popBackStack();
    }
}
