package com.example.notes.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.SharedPreferencesManager;
import com.example.notes.databinding.FragmentAddNoteBinding;
import com.example.notes.model.DatabaseManager;
import com.example.notes.model.Note;
import com.example.notes.model.NoteDbHelper;

public class AddNoteFragment extends Fragment {
    FragmentAddNoteBinding binding;
    DatabaseManager dbManager;
    NoteDbHelper dbHelper;
    SharedPreferencesManager sharedPreferencesManager;
    Note note;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddNoteBinding.inflate(inflater,container,false);
        dbManager =  DatabaseManager.getInstance(requireActivity());
        dbHelper = dbManager.getNoteDbHelper();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(requireActivity());

        binding.addNote.setOnClickListener(view -> {
            String title = binding.title.getText().toString();
            String description = binding.description.getText().toString();
            if(!title.isEmpty() && !description.isEmpty()) {
                note = new Note(title, description, sharedPreferencesManager.getString("email", null));
                long id = dbHelper.insertNote(note);
                Log.d("AddNoteFragment", "Note Added" + id);

                Toast.makeText(requireActivity(), "Note Added Successfully", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).popBackStack();
            }
            else{
                Toast.makeText(requireActivity(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();

    }
}