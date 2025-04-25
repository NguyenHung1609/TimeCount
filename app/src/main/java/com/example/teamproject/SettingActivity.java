package com.example.teamproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    ImageButton closeIcon;
    TextView dateCalculator;
    TextView languageText;
    TextView languageValue;
    TextView editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.setting);

        // Nút close, close về Home
        closeIcon = findViewById(R.id.closeIcon);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Nút Ngôn ngữ
        languageText = findViewById(R.id.languageText);
        languageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, LanguageSettingActivity.class);
                startActivity(intent);
            }
        });
        languageValue = findViewById(R.id.languageValue);
        languageValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, LanguageSettingActivity.class);
                startActivity(intent);
            }
        });

        // Nút Máy tính ngày
        dateCalculator = findViewById(R.id.dateCalculator);
        dateCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, DayCalculatorActivity.class);
                startActivity(intent);
            }
        });

        // Nút Chỉnh sửa
//        editText = findViewById(R.id.editText);
//        editText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SettingActivity.this, CustomSettingActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}