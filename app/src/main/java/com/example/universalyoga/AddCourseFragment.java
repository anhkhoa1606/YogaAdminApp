package com.example.universalyoga;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.universalyoga.Database.AppDatabase;
import com.example.universalyoga.Model.YogaCourse;
import com.example.universalyoga.R;

import java.util.Calendar;

public class AddCourseFragment extends Fragment {

    private YogaCourse course;
    private Spinner dayOfWeekSpinner;
    private EditText timeOfCourse, capacity, duration, pricePerClass, description;
    private RadioGroup typeOfClassGroup;
    private Button addButton;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_course, container, false);
        db = AppDatabase.getDatabase(getContext());

        // Khởi tạo các trường
        dayOfWeekSpinner = view.findViewById(R.id.dayOfWeekSpinner);
        timeOfCourse = view.findViewById(R.id.timeOfCourse);
        capacity = view.findViewById(R.id.capacity);
        duration = view.findViewById(R.id.duration);
        pricePerClass = view.findViewById(R.id.pricePerClass);
        description = view.findViewById(R.id.description);
        typeOfClassGroup = view.findViewById(R.id.typeOfClass);
        addButton = view.findViewById(R.id.addButton);

        // Cài đặt adapter cho dayOfWeekSpinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.days_of_week, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayOfWeekSpinner.setAdapter(adapter);

        // Thiết lập sự kiện cho trường timeOfCourse
        timeOfCourse.setOnClickListener(v -> showTimePicker());

        // Kiểm tra nếu có courseId để cập nhật
        if (getArguments() != null && getArguments().containsKey("courseId")) {
            int courseId = getArguments().getInt("courseId");
            course = db.yogaCourseDao().getYogaCourseById(courseId);

            if (course != null) {
                populateFields(course);
            }

            addButton.setText("Update");
        } else {
            course = new YogaCourse();
        }

        addButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveOrUpdateCourse();
            }
        });

        return view;
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, hourOfDay, minuteOfDay) -> {
                    // Định dạng giờ theo HH:mm và hiển thị trong trường timeOfCourse
                    timeOfCourse.setText(String.format("%02d:%02d", hourOfDay, minuteOfDay));
                }, hour, minute, true); // true để sử dụng định dạng 24 giờ
        timePickerDialog.show();
    }

    private void populateFields(YogaCourse course) {
        // Điền dữ liệu vào các trường
        timeOfCourse.setText(course.timeOfCourse);
        capacity.setText(String.valueOf(course.capacity));
        duration.setText(String.valueOf(course.duration));
        pricePerClass.setText(String.valueOf(course.price));
        description.setText(course.description);

        // Chọn ngày trong tuần trong Spinner
        int dayPosition = getDayOfWeekPosition(course.dayOfWeek);
        if (dayPosition >= 0) {
            dayOfWeekSpinner.setSelection(dayPosition);
        }

        // Chọn loại lớp học
        for (int i = 0; i < typeOfClassGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) typeOfClassGroup.getChildAt(i);
            if (button.getText().toString().equals(course.typeOfClass)) {
                button.setChecked(true);
                break;
            }
        }
    }

    // Phương thức để lấy vị trí của ngày trong Spinner dựa trên tên ngày
    private int getDayOfWeekPosition(String day) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) dayOfWeekSpinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(day)) {
                return i;
            }
        }
        return -1; // Không tìm thấy
    }

    private void saveOrUpdateCourse() {
        course.dayOfWeek = dayOfWeekSpinner.getSelectedItem().toString();
        course.timeOfCourse = timeOfCourse.getText().toString();
        course.capacity = Integer.parseInt(capacity.getText().toString());
        course.duration = Integer.parseInt(duration.getText().toString());
        course.price = Double.parseDouble(pricePerClass.getText().toString());
        course.description = description.getText().toString();
        course.typeOfClass = ((RadioButton) getView().findViewById(typeOfClassGroup.getCheckedRadioButtonId())).getText().toString();

        if (course.getCourseId() > 0) {
            db.yogaCourseDao().updateYogaCourse(course);
            Toast.makeText(getContext(), "Course updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            db.yogaCourseDao().insertYogaCourse(course);
            clearInputs();
            Toast.makeText(getContext(), "Course added successfully", Toast.LENGTH_SHORT).show();
        }

        // Quay lại HomeFragment sau khi lưu
        getParentFragmentManager().popBackStack();
    }
    //khi add xong thi xoa toan bo cac input
    private void clearInputs() {
        dayOfWeekSpinner.setSelection(0);
        timeOfCourse.setText("");
        capacity.setText("");
        duration.setText("");
        pricePerClass.setText("");
        description.setText("");
        typeOfClassGroup.clearCheck();
    }
    private boolean validateInputs() {
        if (dayOfWeekSpinner.getSelectedItem() == null ||
                timeOfCourse.getText().toString().isEmpty() ||
                capacity.getText().toString().isEmpty() ||
                duration.getText().toString().isEmpty() ||
                pricePerClass.getText().toString().isEmpty() ||
                typeOfClassGroup.getCheckedRadioButtonId() == -1) {

            Toast.makeText(getContext(), "Please fill out all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}