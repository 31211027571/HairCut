package com.example.haircut.classes;

import java.util.HashMap;

public class Settings {
    public HashMap<String,String> DayOffList;
    public HashMap<String,Day> OperationTime;

    public Settings(){
        DayOffList = new HashMap();
        OperationTime = new HashMap();
    }
}
