package com.example.universalyoga.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyoga.Model.YogaCourse;
import com.example.universalyoga.R;

import java.util.List;

public class YogaCourseAdapter extends RecyclerView.Adapter<YogaCourseAdapter.CourseViewHolder> {

    private List<YogaCourse> courseList;
    private OnCourseClickListener courseClickListener;

    // Constructor
    public YogaCourseAdapter(List<YogaCourse> courseList, OnCourseClickListener courseClickListener) {
        this.courseList = courseList;
        this.courseClickListener = courseClickListener;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        YogaCourse course = courseList.get(position);
        holder.courseName.setText(course.typeOfClass);
        holder.courseDetails.setText(course.dayOfWeek + " - " + course.timeOfCourse);

        // Item click listener
        holder.itemView.setOnClickListener(v -> {
            if (courseClickListener != null) {
                courseClickListener.onCourseClick(course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList != null ? courseList.size() : 0;
    }

    // Update data dynamically
    public void updateCourses(List<YogaCourse> updatedCourses) {
        this.courseList = updatedCourses;
        notifyDataSetChanged();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, courseDetails;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseName);
            courseDetails = itemView.findViewById(R.id.courseDetails);
        }
    }

    // Listener interface for click actions
    public interface OnCourseClickListener {
        void onCourseClick(YogaCourse course);
//        void onEditCourse(YogaCourse course);
//        void onDeleteCourse(YogaCourse course);
    }
}
