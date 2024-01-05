package com.yashtech.noteit;

public class TaskModel {
    private String slNo;
    private String task;
    private boolean isChecked;

    public TaskModel(String slNo, String task, boolean isChecked) {
        this.slNo = slNo;
        this.task = task;
        this.isChecked = isChecked;
    }

    public String getSlNo() {
        return slNo;
    }

    public void setSlNo(String slNo) {
        this.slNo = slNo;
    }

    public String getTask() {
        return task;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
