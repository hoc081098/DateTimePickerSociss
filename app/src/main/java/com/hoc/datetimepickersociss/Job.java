package com.hoc.datetimepickersociss;

import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;

public final class Job {
    private final long id;
    @NonNull
    private final String title;
    @NonNull
    private final String description;
    @NonNull
    private final Date finishTime;

    public Job(@NonNull String title, @NonNull String description, @NonNull Date finishTime) {
        this.id = System.currentTimeMillis();
        this.title = title;
        this.description = description;
        this.finishTime = finishTime;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public Date getFinishTime() {
        return finishTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(id, job.id) &&
                Objects.equals(title, job.title) &&
                Objects.equals(description, job.description) &&
                Objects.equals(finishTime, job.finishTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, finishTime);
    }
}
