package com.example.teamproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageButton btnAdd;
    ImageButton btnSettings;

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private EventDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Nút sự kiện
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                startActivity(intent);
            }
        });

        // Nút cài đặt
        btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo database helper và lấy danh sách sự kiện
        dbHelper = new EventDatabaseHelper(this);
        List<Event> eventList = dbHelper.getAllEvents();

        // Tạo adapter và thiết lập vào RecyclerView
        adapter = new EventAdapter(this, eventList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Event> updatedList = dbHelper.getAllEvents();
        adapter.updateEventList(updatedList);

        Log.d("EventListDebug", "Số lượng sự kiện: " + updatedList.size());
        for (Event event : updatedList) {
            Log.d("EventListDebug", "ID: " + event.getId() + ", Title: " + event.getTitle() + ", Date: " + event.getDate());
        }
    }
}
