package com.hoc.datetimepickersociss;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class JobAdapter extends ListAdapter<JobAdapter.JobWrapper, JobAdapter.ViewHolder> {
    private final static DiffUtil.ItemCallback<JobWrapper> DIFF_CALLBACK = new DiffUtil.ItemCallback<JobWrapper>() {
        @Override
        public boolean areItemsTheSame(@NonNull JobWrapper oldItem, @NonNull JobWrapper newItem) {
            return oldItem.job.getId() == newItem.job.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull JobWrapper oldItem, @NonNull JobWrapper newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    private final RecyclerOnClickListener onClickListener;
    @NonNull
    private final RecyclerOnLongClickListener onLongClickListener;
    @NonNull
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy - hh:mm aa", Locale.getDefault());
    @NonNull
    private final Set<Job> expandedItems = new HashSet<>();
    @NonNull
    private List<Job> currentJobs = new ArrayList<>();

    public JobAdapter(@NonNull RecyclerOnClickListener onClickListener, @NonNull RecyclerOnLongClickListener onLongClickListener) {
        super(DIFF_CALLBACK);
        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    public void submitListJobs(@Nullable final List<Job> jobs) {
        currentJobs = jobs != null ? jobs : Collections.emptyList();
        submitList(mapToListWrappers(currentJobs));
    }

    @NonNull
    private List<JobWrapper> mapToListWrappers(@NonNull List<Job> jobs) {
        List<JobWrapper> wrappers = new ArrayList<>(jobs.size());
        for (Job job : jobs) {
            wrappers.add(new JobWrapper(job, expandedItems.contains(job)));
        }
        return wrappers;
    }

    @FunctionalInterface
    interface RecyclerOnClickListener {
        void onClick(@NonNull Job item);
    }

    @FunctionalInterface
    interface RecyclerOnLongClickListener {
        void onLongClick(@NonNull Job item);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private final TextView textView;
        private final TextView textDescription;
        private final ImageView imageExpandCollapse;
        private final View divider;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_job);
            textDescription = itemView.findViewById(R.id.text_description);
            imageExpandCollapse = itemView.findViewById(R.id.image_expand_collapse);
            divider = itemView.findViewById(R.id.divider);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            imageExpandCollapse.setOnClickListener(this);
        }

        void bindTo(JobWrapper wrapper) {
            final String time = simpleDateFormat.format(wrapper.job.getFinishTime());
            final String s = String.format(Locale.getDefault(), "%s - %s", wrapper.job.getTitle(), time);
            textView.setText(s);

            textDescription.setText(wrapper.job.getDescription());
            final int visibility = wrapper.expanded ? View.VISIBLE : View.GONE;
            if (textDescription.getVisibility() != visibility) {
                textDescription.setVisibility(visibility);
                divider.setVisibility(visibility);
            }
            imageExpandCollapse.setImageResource(
                    wrapper.expanded
                            ? R.drawable.ic_expand_less_black_24dp
                            : R.drawable.ic_expand_more_black_24dp
            );
        }

        @Override
        public boolean onLongClick(View v) {
            final int adapterPosition = getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onLongClickListener.onLongClick(getItem(adapterPosition).job);
                return true;
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            final int adapterPosition = getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return;
            }
            final JobWrapper jobWrapper = getItem(adapterPosition);

            if (v.getId() == R.id.image_expand_collapse) {
                if (jobWrapper.expanded) {
                    expandedItems.remove(jobWrapper.job);
                } else {
                    expandedItems.add(jobWrapper.job);
                }
                submitListJobs(currentJobs);
            } else {
                onClickListener.onClick(jobWrapper.job);
            }
        }
    }

    class JobWrapper {
        @NonNull
        final Job job;
        final boolean expanded;


        private JobWrapper(@NonNull Job job, boolean expanded) {
            this.job = job;
            this.expanded = expanded;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            JobWrapper that = (JobWrapper) o;
            return expanded == that.expanded && Objects.equals(job, that.job);
        }

        @Override
        public int hashCode() {
            return Objects.hash(job, expanded);
        }
    }
}
