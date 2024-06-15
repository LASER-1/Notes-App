package com.example.notes.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.databinding.FragmentUpdateNoteBinding;
import com.example.notes.model.DatabaseManager;
import com.example.notes.model.Note;
import com.example.notes.model.NoteDbHelper;

public class UpdateNoteFragment extends Fragment {
    FragmentUpdateNoteBinding binding;
    DatabaseManager dbManager;
    NoteDbHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateNoteBinding.inflate(inflater, container, false);
        int noteId = getArguments().getInt("noteId");
        Log.d("UpdateNoteFragment", "noteId: " + noteId);

        dbManager  = DatabaseManager.getInstance(requireActivity());
        dbHelper = dbManager.getNoteDbHelper();
        Note note = dbHelper.getNoteByID(noteId);

        binding.updateTitle.setText(note.getTitle());
        binding.updateDescription.setText(note.getDescription());
        binding.updateNote.setOnClickListener(view -> {
            String title = binding.updateTitle.getText().toString();
            String description = binding.updateDescription.getText().toString();
            if(!title.isEmpty() && !description.isEmpty()) {
                dbHelper.updateNote(new Note(noteId, title, description));

                Toast.makeText(requireActivity(), "Note Updated", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).popBackStack();
            }
            else{
                Toast.makeText(requireActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();

    }
}