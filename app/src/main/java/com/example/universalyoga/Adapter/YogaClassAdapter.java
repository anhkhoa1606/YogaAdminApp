package com.example.universalyoga.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.universalyoga.Model.YogaClass;
import com.example.universalyoga.R;

import java.util.List;

public class YogaClassAdapter extends RecyclerView.Adapter<YogaClassAdapter.ClassViewHolder> {

    private List<YogaClass> classList;
    private OnYogaClassActionListener listener;

    public YogaClassAdapter(List<YogaClass> classList, OnYogaClassActionListener listener) {
        this.classList = classList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        YogaClass yogaClass = classList.get(position);
        holder.className.setText(yogaClass.teacher); // Hiển thị tên lớp học

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onYogaClassSelected(yogaClass);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView className;
        Button btnEdit, btnDelete;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.className);
        }
    }

    public interface OnYogaClassActionListener {
        void onEditYogaClass(YogaClass yogaClass);
        void onDeleteYogaClass(YogaClass yogaClass);
        void onYogaClassSelected(YogaClass yogaClass); // Mới thêm
    }
}