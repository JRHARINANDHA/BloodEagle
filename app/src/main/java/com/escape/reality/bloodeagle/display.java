package com.escape.reality.bloodeagle;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class display extends AppCompatActivity {
    private static String temp;
    private String[] nameArray,numberArray;
    List<String> dynNameList = new ArrayList<String>();
    List<String> dynNumberList = new ArrayList<String>();
    ListView lv;
    Boolean loader=true;
    SharedPreferences reg;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String name, blood1;
    private ProgressBar spinner;
   // public static int[] prgmImages = {R.drawable.ic_menu_camera, R.drawable.ic_menu_camera, R.drawable.ic_menu_camera};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        reg = getSharedPreferences("district", MODE_PRIVATE);
        name = reg.getString("locate", "district");
        blood1 = reg.getString("blood", "blood");




}}


















