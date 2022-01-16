package com.noalecohen1.moveonotes.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.noalecohen1.moveonotes.R;
import com.noalecohen1.moveonotes.models.Note;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private final Context context;
    private final List<Note> data;
    private OnItemClickListener listener;
    Calendar calendar;
    LocalDate localDate;

    public NoteAdapter(List<Note> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.list_row, null));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = data.get(position);
        holder.bindData(note, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }




    //ViewHolder
    @RequiresApi(api = Build.VERSION_CODES.O)
    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView body;
        TextView date;
        int position;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.listrow_title);
            body = itemView.findViewById(R.id.listrow_body);
            date = itemView.findViewById(R.id.listrow_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }
        public void bindData(Note note, int position){
            this.title.setText(note.getTitle());
            this.body.setText(note.getBody());
            calendar = note.getCalender();
            localDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
            this.date.setText(formatter.format(localDate));
            this.position = position;
        }

    }

}


