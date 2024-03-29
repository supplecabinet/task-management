package com.test.tasks.model;

import java.util.HashMap;
import java.util.Map;

public enum TaskStatus {
    TODO(1,"To Do"),
    IN_PROGRESS(2, "In Progress"),
    DONE(3, "Done");

    private int value;
    private static final Map<Integer, TaskStatus> byName = new HashMap<>();
    private static final Map<String, Integer> byValue = new HashMap<>();
    static {
        for (TaskStatus e : values()) {
            byName.put(e.getValue(), e);
            byValue.put(e.getName(), e.getValue());
        }
    }
    private String name;

    private TaskStatus(int value) {
        this.value = value;
    }
    private TaskStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }
    public String getName(){
        return byName.get(this.value).name;
    }
    public static String getName(int value){
        return byName.get(value).name;
    }
    public int getValue() {
        return this.value;
    }
    public static Integer getValueByName(String name1) {
        return byValue.get(name1);
    }

}
