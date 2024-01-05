package com.yashtech.noteit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter taskAdapter;
    private List<TaskModel> taskList;
    private int currentSerialNumber = 1;
    private static final String PREF_NAME = "TaskAppPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        // Load tasks from SharedPreferences
        taskList = loadTasks();

        // Set up RecyclerView with the TaskAdapter
        taskAdapter = new TaskAdapter(taskList);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAdd.setOnClickListener(v -> showAddTaskPopup());
    }

    private void showAddTaskPopup() {
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_task_details, null);

        EditText editTextTask = popupView.findViewById(R.id.editTextTask);
        Button btnAddTask = popupView.findViewById(R.id.btnAddTask);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        btnAddTask.setOnClickListener(v -> {
            String taskDetails = editTextTask.getText().toString().trim();
            if (!taskDetails.isEmpty()) {
                addTask(taskDetails);
                popupWindow.dismiss();
            }
        });
    }

    private void addTask(String taskDetails) {
        String slNo = String.valueOf(currentSerialNumber++);
        TaskModel newTask = new TaskModel(slNo, taskDetails, false);
        taskList.add(newTask);
        saveTasks(taskList); // Save tasks to SharedPreferences
        taskAdapter.notifyDataSetChanged();
    }

    // Save tasks to SharedPreferences
    private void saveTasks(List<TaskModel> tasks) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("taskCount", tasks.size());

        for (int i = 0; i < tasks.size(); i++) {
            TaskModel task = tasks.get(i);
            editor.putString("task_slno_" + i, task.getSlNo());
            editor.putString("task_details_" + i, task.getTask());
            editor.putBoolean("task_checked_" + i, task.isChecked());
        }

        editor.apply();
    }

    // Load tasks from SharedPreferences
    private List<TaskModel> loadTasks() {
        List<TaskModel> tasks = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int taskCount = sharedPreferences.getInt("taskCount", 0);

        for (int i = 0; i < taskCount; i++) {
            String slNo = sharedPreferences.getString("task_slno_" + i, "");
            String taskDetails = sharedPreferences.getString("task_details_" + i, "");
            boolean isChecked = sharedPreferences.getBoolean("task_checked_" + i, false);
            tasks.add(new TaskModel(slNo, taskDetails, isChecked));
        }

        return tasks;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveTasks(taskList); // Save tasks when the app is closed
    }
}
