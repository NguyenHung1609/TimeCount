package com.example.teamproject;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE_PICK = 101;
    private static final int REQUEST_CODE_PERMISSION = 100;

    private FrameLayout imageContainer;
    private ImageView imageView;
    private Uri selectedImageUri = null;

    private EditText etTitle, etDate, etDateType;
    private Switch switchCountdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_event);

        imageContainer = findViewById(R.id.imageContainer);
        imageView = new ImageView(this);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageContainer.addView(imageView);

        imageContainer.setOnClickListener(v -> {
            if (hasImagePermission()) openGallery();
            else requestImagePermission();
        });

        etTitle = findViewById(R.id.etTitle);
        etDate = findViewById(R.id.etDate);
        etDateType = findViewById(R.id.etDateType);
        switchCountdown = findViewById(R.id.switchCountdown);

        etDateType.setText("Bình thường");
        etDateType.setFocusable(false);
        etDateType.setOnClickListener(v -> showDateTypeDialog(etDateType));

        etDate.setOnClickListener(v -> showDatePickerDialog());

        Button btnAdd = findViewById(R.id.btnAddEvent);
        btnAdd.setOnClickListener(v -> saveEvent());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            etDate.setText(selectedDate);

            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year, month, dayOfMonth);
            Calendar today = Calendar.getInstance();
            switchCountdown.setChecked(selectedCalendar.after(today));

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveEvent() {
        String title = etTitle.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String dateType = etDateType.getText().toString().trim();
        String imageUriStr = (selectedImageUri != null) ? selectedImageUri.toString() : "";

        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        long millis = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date dateObj = sdf.parse(date);
            if (dateObj != null) millis = dateObj.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Event event = new Event(title, date, dateType, imageUriStr, millis);
        EventDatabaseHelper dbHelper = new EventDatabaseHelper(this);
        dbHelper.insertEvent(event);
        Toast.makeText(this, "Đã lưu sự kiện!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showDateTypeDialog(EditText etDateType) {
        final String[] options = {"Bình thường", "Hẹn hò"};
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Chọn kiểu ngày")
                .setItems(options, (dialog, which) -> {
                    etDateType.setText(options[which]);
                    if (which == 1) {
                        startActivity(new Intent(this, AddEventSecondModeActivity.class));
                    }
                })
                .show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
    }

    private boolean hasImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestImagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) imageView.setImageURI(selectedImageUri);
        }
    }
}
