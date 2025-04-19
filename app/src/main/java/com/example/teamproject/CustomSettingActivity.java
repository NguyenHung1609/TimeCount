package com.example.teamproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CustomSettingActivity extends AppCompatActivity {
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customize_setting);

        // Nút back về Activity trước đó
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());

    }
}
