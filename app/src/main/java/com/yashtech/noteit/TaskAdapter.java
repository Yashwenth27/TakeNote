package com.yashtech.noteit;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<TaskModel> taskList;

    public TaskAdapter(List<TaskModel> taskList) {
        this.taskList = taskList;
        updateSerialNumbers();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskModel taskModel = taskList.get(position);
        holder.bind(taskModel);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    private void updateSerialNumbers() {
        for (int i = 0; i < taskList.size(); i++) {
            TaskModel taskModel = taskList.get(i);
            taskModel.setSlNo(String.valueOf(i + 1));
        }
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView slNoTextView;
        private TextView taskTextView;
        private CheckBox checkBox;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            slNoTextView = itemView.findViewById(R.id.slno);
            taskTextView = itemView.findViewById(R.id.task);
            checkBox = itemView.findViewById(R.id.checkBox);

            checkBox.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    TaskModel taskModel = taskList.get(position);
                    taskModel.setChecked(checkBox.isChecked());

                    if (checkBox.isChecked()) {
                        // If the CheckBox is checked, remove the item from the list and notify the adapter
                        taskList.remove(position);
                        notifyDataSetChanged();
                        updateSerialNumbers(); // Update serial numbers after deletion
                        notifyDataSetChanged();
                    }
                }
            });
        }

        public void bind(TaskModel taskModel) {
            slNoTextView.setText(taskModel.getSlNo());
            taskTextView.setText(taskModel.getTask());
            checkBox.setChecked(taskModel.isChecked());
        }
    }
}
