package com.example.notes.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.notes.NotesAdapter;
import com.example.notes.R;
import com.example.notes.SharedPreferencesManager;
import com.example.notes.databinding.FragmentHomeBinding;
import com.example.notes.model.DatabaseManager;
import com.example.notes.model.NoteDbHelper;


public class HomeFragment extends Fragment {
    AuthViewModel viewModel;
    FragmentHomeBinding binding;
    DatabaseManager dbManager;
    NoteDbHelper dbHelper;
    NotesAdapter notesAdapter;
    SharedPreferencesManager sharedPreferencesManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        dbManager = DatabaseManager.getInstance(requireActivity());
        dbHelper = dbManager.getNoteDbHelper();
        notesAdapter = new NotesAdapter(requireActivity(),dbHelper.getAllNotes(requireActivity()));
        sharedPreferencesManager = SharedPreferencesManager.getInstance(requireActivity());

        binding.userNameTextView.setText(sharedPreferencesManager.getString("name",null));
        binding.userEmailTextView.setText(sharedPreferencesManager.getString("email",null));

        binding.navigateToAddNote.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addNoteFragment));

        binding.notesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.notesRecyclerView.setAdapter(notesAdapter);

        binding.logoutButton.setOnClickListener(view -> {
            sharedPreferencesManager.clearSharedPreference();
            viewModel.getOneTapClient(requireActivity()).signOut().addOnCompleteListener(requireActivity(),task ->
                    Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment));
        });

        return binding.getRoot();
    }
}