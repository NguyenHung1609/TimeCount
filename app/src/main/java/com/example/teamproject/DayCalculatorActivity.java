package com.example.teamproject;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;


import java.text.SimpleDateFormat;
import java.util.*;

public class DayCalculatorActivity extends AppCompatActivity {
    private TextView startDateDiffText, endDateText, totalDaysText;
    private TextView startDateAddText, beforeDateText, afterDateText;
    private CheckBox includeStartDateCheckBox;
    private EditText numberInput;
    private Spinner unitSpinner;
    private Button resetButton;

    private Calendar startDateDiff, endDate, startDateAdd;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_calculator);

        startDateDiffText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
        totalDaysText = findViewById(R.id.totalDaysText);
        includeStartDateCheckBox = findViewById(R.id.includeStartDate);
        startDateAddText = findViewById(R.id.startDateText2); // dùng chung ID
        beforeDateText = findViewById(R.id.beforeDate);
        afterDateText = findViewById(R.id.afterDate);
        numberInput = findViewById(R.id.numberInput);
        unitSpinner = findViewById(R.id.unitSpinner);
        resetButton = findViewById(R.id.calculateButton);

        startDateDiff = Calendar.getInstance();
        endDate = Calendar.getInstance();
        startDateAdd = Calendar.getInstance();

        // Đơn vị
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"Ngày", "Tháng", "Năm"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        // Chọn ngày bắt đầu
        startDateDiffText.setOnClickListener(v -> showDatePicker(startDateDiff, startDateDiffText));
        startDateAddText.setOnClickListener(v -> showDatePicker(startDateAdd, startDateAddText));

        // Chọn ngày kết thúc
        endDateText.setOnClickListener(v -> showDatePicker(endDate, endDateText));

        // Khi chọn xong ngày thì tự động cập nhật tính toán
        includeStartDateCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> updateDifference());

        numberInput.setOnKeyListener((v, keyCode, event) -> {
            updateAddition();
            return false;
        });

        // Xử lý chọn ngày/tháng/năm
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateAddition();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Nút reset
        resetButton.setOnClickListener(v -> resetAll());

        // Khởi tạo ban đầu
        updateDifference();
        updateAddition();

        // Nút back
        findViewById(R.id.backButton).setOnClickListener(v -> onBackPressed());
    }

    private void showDatePicker(Calendar calendar, TextView targetView) {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            targetView.setText(sdf.format(calendar.getTime()));
            updateDifference();
            updateAddition();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDifference() {
        long diffInMillis = endDate.getTimeInMillis() - startDateDiff.getTimeInMillis();
        long diffDays = diffInMillis / (1000 * 60 * 60 * 24);

        if (includeStartDateCheckBox.isChecked()) {
            diffDays += 1;
        }

        totalDaysText.setText("Tổng chênh lệch là: " + diffDays + " ngày");
    }

    private void updateAddition() {
        String inputStr = numberInput.getText().toString();
        if (inputStr.isEmpty()) {
            beforeDateText.setText("Ngày đã nhập: ");
            afterDateText.setText("Ngày sau khi tính toán: ");
            return;
        }

        int value = Integer.parseInt(inputStr);
        Calendar resultCal = (Calendar) startDateAdd.clone();
        beforeDateText.setText("Ngày đã nhập: " + sdf.format(startDateAdd.getTime()));

        switch (unitSpinner.getSelectedItemPosition()) {
            case 0: resultCal.add(Calendar.DAY_OF_MONTH, value); break;
            case 1: resultCal.add(Calendar.MONTH, value); break;
            case 2: resultCal.add(Calendar.YEAR, value); break;
        }

        afterDateText.setText("Ngày sau khi tính toán: " + sdf.format(resultCal.getTime()));
    }

    // Reset all
    private void resetAll() {
        startDateDiff = Calendar.getInstance();
        endDate = Calendar.getInstance();
        startDateAdd = Calendar.getInstance();

        startDateDiffText.setText("Chọn ngày bắt đầu");
        endDateText.setText("Chọn ngày kết thúc");
        startDateAddText.setText("Chọn ngày bắt đầu");

        totalDaysText.setText("Tổng chênh lệch là: 0 ngày");
        includeStartDateCheckBox.setChecked(false);

        numberInput.setText("");
        unitSpinner.setSelection(0);
        beforeDateText.setText("Ngày đã nhập: ");
        afterDateText.setText("Ngày sau khi tính toán: ");

        updateDifference();
        updateAddition();
    }
}
