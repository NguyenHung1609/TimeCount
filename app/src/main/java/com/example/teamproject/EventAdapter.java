package com.example.teamproject;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_LOVE = 1;

    private Context context;
    private List<Event> eventList;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public int getItemViewType(int position) {
        if ("Hẹn hò".equalsIgnoreCase(eventList.get(position).getDateType())) {
            return TYPE_LOVE;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOVE) {
            View view = LayoutInflater.from(context).inflate(R.layout.date_card, parent, false);
            return new LoveEventViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
            return new EventViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Event event = eventList.get(position);

        if (holder instanceof LoveEventViewHolder) {
            LoveEventViewHolder loveHolder = (LoveEventViewHolder) holder;
            long daysTogether = (System.currentTimeMillis() - event.getEventTimeMillis()) / (1000 * 60 * 60 * 24);
            loveHolder.tvDays.setText(String.valueOf(daysTogether));

            String[] parts = event.getTitle().split(" ❤ ");
            loveHolder.tvNameLeft.setText(parts.length > 0 ? parts[0] : "H");
            loveHolder.tvNameRight.setText(parts.length > 1 ? parts[1] : "N");

            loveHolder.itemView.setOnLongClickListener(v -> {
                showDeleteDialog(event, position);
                return true;
            });

        } else if (holder instanceof EventViewHolder) {
            EventViewHolder normalHolder = (EventViewHolder) holder;
            normalHolder.tvTitle.setText(event.getTitle());
            normalHolder.tvCountdown.setText(event.getCountdownText());
            if (event.getImageUri() != null) {
                normalHolder.eventImage.setImageURI(Uri.parse(event.getImageUri()));
            }

            normalHolder.itemView.setOnLongClickListener(v -> {
                showDeleteDialog(event, position);
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateEventList(List<Event> newList) {
        eventList.clear();
        eventList.addAll(newList);
        notifyDataSetChanged();
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCountdown;
        ImageView eventImage;

        public EventViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.eventTitle);
            tvCountdown = itemView.findViewById(R.id.eventCountdown);
            eventImage = itemView.findViewById(R.id.eventImage);
        }
    }

    //Show SK
    public static class LoveEventViewHolder extends RecyclerView.ViewHolder {
        TextView tvDays, tvNameLeft, tvNameRight;

        public LoveEventViewHolder(View itemView) {
            super(itemView);
            tvDays = itemView.findViewById(R.id.tvDays);
            tvNameLeft = itemView.findViewById(R.id.letterH);
            tvNameRight = itemView.findViewById(R.id.letterN);
        }
    }

    // Xóa SK
    private void showDeleteDialog(Event event, int position) {
        new android.app.AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sự kiện này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    EventDatabaseHelper dbHelper = new EventDatabaseHelper(context);
                    dbHelper.deleteEventById(event.getId());

                    eventList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, eventList.size());
                    Toast.makeText(context, "Đã xóa sự kiện", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

}
