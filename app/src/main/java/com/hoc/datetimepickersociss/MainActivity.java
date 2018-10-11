package com.hoc.datetimepickersociss;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final JobAdapter adapter = new JobAdapter(this::onClickListener, this::onLongClickListener);

    private MainViewModel mainViewModel;

    private View mRootLayout;
    private EditText mEditTitle;
    private EditText mEditDescription;
    private View mButtonSelectDate;
    private View mButtonSelectTime;
    private TextView mTextDate;
    private TextView mTextTime;
    private View mButtonAdd;
    private RecyclerView mRecyclerJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        setupRecycler();

        mButtonAdd.setOnClickListener(this);
        mButtonSelectDate.setOnClickListener(this);
        mButtonSelectTime.setOnClickListener(this);

        subscribe();
    }

    private void subscribe() {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getLiveDataJobs().observe(this, adapter::submitListJobs);
        mainViewModel.getSnackbarMessage().observe(this, charSequence -> {
            if (charSequence != null) {
                Snackbar.make(mRootLayout, charSequence, Snackbar.LENGTH_SHORT).show();
            }
        });
        mainViewModel.getDateTime().observe(this, date -> {
            if (date != null) {
                mTextTime.setText(new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(date));
                mTextDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date));
            }
        });
    }

    private void setupRecycler() {
        mRecyclerJobs.setHasFixedSize(true);
        mRecyclerJobs.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerJobs.setAdapter(adapter);
    }

    private void onClickListener(@NonNull Job job) {
        Snackbar.make(mRootLayout, job.getTitle() + " - " + job.getDescription(), Snackbar.LENGTH_SHORT).show();
    }

    private void onLongClickListener(@NonNull Job job) {
        mainViewModel.deleteJob(job);
    }

    private void findViews() {
        mRootLayout = findViewById(R.id.main_root_layout);
        mEditTitle = findViewById(R.id.edit_title);
        mEditDescription = findViewById(R.id.edit_description);
        mButtonSelectDate = findViewById(R.id.button_select_date);
        mButtonSelectTime = findViewById(R.id.button_select_time);
        mTextDate = findViewById(R.id.text_date);
        mTextTime = findViewById(R.id.text_time);
        mButtonAdd = findViewById(R.id.button_add);
        mRecyclerJobs = findViewById(R.id.recycler_jobs);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                onButtonAddClicked();
                break;
            case R.id.button_select_date:
                onButtonSelectDateClicked();
                break;
            case R.id.button_select_time:
                onButtonSelectTimeClicked();
                break;
        }
    }

    private void onButtonSelectTimeClicked() {
        final Calendar current = Calendar.getInstance();

        new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> mainViewModel.setHourMinute(hourOfDay, minute),
                current.get(Calendar.HOUR_OF_DAY),
                current.get(Calendar.MINUTE),
                false
        ).show();
    }

    private void onButtonSelectDateClicked() {
        final Calendar current = Calendar.getInstance();

        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> mainViewModel.setYearMonthDay(year, month, dayOfMonth),
                current.get(Calendar.YEAR),
                current.get(Calendar.MONTH),
                current.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void onButtonAddClicked() {
        final String title = mEditTitle.getText().toString();
        final String description = mEditDescription.getText().toString();
        final Date dateTime = mainViewModel.getDateTime().getValue();

        if (title.isEmpty()
                || description.isEmpty()
                || dateTime == null) {
            Snackbar.make(mRootLayout, "Chưa nhập đủ thông tin", Snackbar.LENGTH_SHORT).show();
        } else {
            mainViewModel.addJob(new Job(title, description, dateTime));
        }
    }
}
