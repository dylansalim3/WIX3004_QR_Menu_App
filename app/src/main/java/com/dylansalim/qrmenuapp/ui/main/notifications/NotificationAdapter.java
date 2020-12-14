package com.dylansalim.qrmenuapp.ui.main.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dylansalim.qrmenuapp.R;
import com.dylansalim.qrmenuapp.models.dao.NotificationDao;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final NotificationClickListener clickListener;
    private final List<NotificationDao> notificationDaos;

    public NotificationAdapter(List<NotificationDao> daos, NotificationClickListener clickListener) {
        this.notificationDaos = daos;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationDao dao = notificationDaos.get(position);

        holder.getTitle().setText(dao.getTitle());
        holder.getBody().setText(dao.getBody());
        if (!dao.isRead()) {
            holder.getView().setBackgroundResource(R.drawable.orange_grey_rounded);
        }
    }

    @Override
    public int getItemCount() {
        return notificationDaos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View view;
        private final TextView title;
        private final TextView body;

        public ViewHolder(View view, NotificationClickListener clickListener) {
            super(view);

            this.view = view;
            title = view.findViewById(R.id.notification_item_title);
            body = view.findViewById(R.id.notification_item_body);

            view.setOnClickListener(v -> {
                clickListener.onNotificationClick(view, getAdapterPosition());
            });
        }

        public View getView() {
            return view;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getBody() {
            return body;
        }
    }

    public interface NotificationClickListener {
        void onNotificationClick(View view, int position);
    }
}
