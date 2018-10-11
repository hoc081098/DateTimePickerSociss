package com.hoc.datetimepickersociss;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static java.util.Collections.emptyList;

/**
 * Created by Peter Hoc on 10/11/2018.
 */

public class MainViewModel extends ViewModel {
    private MutableLiveData<Date> mDate = new MutableLiveData<>();
    private MutableLiveData<List<Job>> mListJobs = new MutableLiveData<>();
    private SingleLiveEvent<CharSequence> mSnackbarMessage = new SingleLiveEvent<>();

    public MainViewModel() {
        mListJobs.setValue(emptyList());
    }

    void addJob(@NonNull Job job) {
        final List<Job> oldValue = mListJobs.getValue();
        final List<Job> newValue = new ArrayList<>(oldValue != null ? oldValue : emptyList());
        newValue.add(job);
        mListJobs.setValue(Collections.unmodifiableList(newValue));
        mSnackbarMessage.setValue("Thêm thành công");
    }

    LiveData<CharSequence> getSnackbarMessage() {
        return mSnackbarMessage;
    }

    LiveData<List<Job>> getLiveDataJobs() {
        return mListJobs;
    }

    void deleteJob(@NonNull Job job) {
        final List<Job> oldValue = mListJobs.getValue();
        final List<Job> newValue = new ArrayList<>(oldValue != null ? oldValue : emptyList());
        newValue.remove(job);
        mListJobs.setValue(Collections.unmodifiableList(newValue));
        mSnackbarMessage.setValue("Xóa thành công");
    }

    @NonNull
    private Date getDate() {
        Date date = mDate.getValue();
        if (date == null) date = new Date();
        return date;
    }

    void setYearMonthDay(int year, int month, int dayOfMonth) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate());
        calendar.set(year, month, dayOfMonth);
        mDate.setValue(calendar.getTime());
    }

    void setHourMinute(int hourOfDay, int minute) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        mDate.setValue(calendar.getTime());
    }

    LiveData<Date> getDateTime() {
        return mDate;
    }
}
