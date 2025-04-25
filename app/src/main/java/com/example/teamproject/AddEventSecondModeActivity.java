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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AddEventSecondModeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE_PICK = 102;
    private static final int REQUEST_CODE_PERMISSION = 103;

    private EditText etTitle, etDateType, etDate, name1, name2;
    private FrameLayout imageContainer;
    private ImageView imageView;
    private Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_event_second_mode);

        etTitle = findViewById(R.id.etTitle);
        etDateType = findViewById(R.id.etDateType);
        etDate = findViewById(R.id.etDate);
        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);

        etDateType.setText("Hẹn hò");
        etDateType.setFocusable(false);
        etDateType.setOnClickListener(v -> showDateTypeDialog());

        etDate.setOnClickListener(v -> showDatePicker());

        imageContainer = findViewById(R.id.imageContainer);
        imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageContainer.addView(imageView);

        imageContainer.setOnClickListener(v -> {
            if (hasImagePermission()) openGallery();
            else requestImagePermission();
        });

        Button btnAdd = findViewById(R.id.btnAddEvent);
        btnAdd.setOnClickListener(v -> saveEvent());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, y, m, d) -> {
            String dateStr = d + "/" + (m + 1) + "/" + y;
            etDate.setText(dateStr);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveEvent() {
        String title = etTitle.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String type = etDateType.getText().toString().trim();
        String person1 = name1.getText().toString().trim();
        String person2 = name2.getText().toString().trim();
        String imageUriStr = (selectedImageUri != null) ? selectedImageUri.toString() : "";

        if (title.isEmpty() || date.isEmpty() || person1.isEmpty() || person2.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        long millis = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date d = sdf.parse(date);
            if (d != null) millis = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Tên sự kiện hiển thị tên người dùng
        String displayTitle = person1 + " ❤ " + person2;
        Event event = new Event(displayTitle, date, type, imageUriStr, millis);

        EventDatabaseHelper dbHelper = new EventDatabaseHelper(this);
        dbHelper.insertEvent(event);

        Toast.makeText(this, "Đã lưu sự kiện!", Toast.LENGTH_SHORT).show();

        // Quay về màn hình chính sau khi thêm
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showDateTypeDialog() {
        final String[] options = {"Bình thường", "Hẹn hò"};
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Chọn kiểu ngày")
                .setItems(options, (dialog, which) -> {
                    etDateType.setText(options[which]);
                    if (which == 0) {
                        startActivity(new Intent(this, AddEventActivity.class));
                        finish();
                    }
                })
                .show();
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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            // giữ permission để dùng lâu dài
            final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(uri, takeFlags);

            selectedImageUri = uri;
            imageView.setImageURI(uri);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            Toast.makeText(this, "Không có quyền truy cập ảnh", Toast.LENGTH_SHORT).show();
        }
    }

}
