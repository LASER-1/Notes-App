package com.example.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.model.DatabaseManager;
import com.example.notes.model.Note;
import com.example.notes.model.NoteDbHelper;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder>{
    ArrayList<Note> notes;
    Context context;
    public NotesAdapter(Context context,ArrayList<Note> notesList) {
        this.context = context;
        this.notes  = notesList;
    }
     class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView = itemView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        ImageView updateButton = itemView.findViewById(R.id.editNote);
        ImageView deleteNote = itemView.findViewById(R.id.deleteNote);

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_item,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.descriptionTextView.setText(note.getDescription());
        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int noteId = note.getId();
                Bundle bundle = new Bundle();
                bundle.putInt("noteId",noteId);
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_updateNoteFragment,bundle);
            }
        });
        holder.deleteNote.setOnClickListener(view -> {
            NoteDbHelper dbHelper = DatabaseManager.getInstance(context).getNoteDbHelper();
            dbHelper.deleteNote(note.getId());
            notes.remove(note);
            notifyItemRemoved(position);
            Toast.makeText(context,"Note Deleted",Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

}
