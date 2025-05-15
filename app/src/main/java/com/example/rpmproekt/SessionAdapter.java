package com.example.rpmproekt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tdsclub.models.SessionResponse;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private List<SessionResponse> sessions;
    private OnCancelListener onCancelListener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public SessionAdapter(List<SessionResponse> sessions, OnCancelListener onCancelListener) {
        this.sessions = sessions;
        this.onCancelListener = onCancelListener;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        SessionResponse session = sessions.get(position);
        holder.bind(session);
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public class SessionViewHolder extends RecyclerView.ViewHolder {
        private TextView computerText;
        private TextView timeText;
        private Button cancelButton;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            computerText = itemView.findViewById(R.id.computerText);
            timeText = itemView.findViewById(R.id.timeText);
            cancelButton = itemView.findViewById(R.id.cancelButton); // Используем кнопку из XML
            cancelButton.setOnClickListener(v -> {
                if (onCancelListener != null) {
                    onCancelListener.onCancel(sessions.get(getAdapterPosition()).getId());
                }
            });
        }

        public void bind(SessionResponse session) {
            computerText.setText("Компьютер: " + session.getComputerNumber());
            try {
                String startTime = timeFormat.format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(session.getStartTime()));
                String endTime = timeFormat.format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(session.getEndTime()));
                String dateStr = dateFormat.format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(session.getStartTime()));
                timeText.setText(dateStr + ", " + startTime + " - " + endTime);
            } catch (Exception e) {
                timeText.setText("Ошибка формата времени");
            }
        }


    }

    public interface OnCancelListener {
        void onCancel(int sessionId);
    }
}