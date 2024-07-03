package com.jafari.kotletapp.ui.home;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jafari.kotletapp.DatabaseHelper;
import com.jafari.kotletapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseHelper databaseHelper;
    private String[] items;
    private AutoCompleteTextView autoCompleteTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        databaseHelper = new DatabaseHelper(requireActivity());
        items = databaseHelper.allIngredients();
        autoCompleteTextView = binding.autoComplete;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, items);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        View root = binding.getRoot();


        final TextView textView = binding.textHome;
        final Button btn = binding.addBtn;

        btn.setOnClickListener(v -> System.out.println("fuck"));
        binding.admBtn.setOnClickListener(v -> {
            databaseHelper.addNewIngredient(autoCompleteTextView.getText().toString(), 10);
            items = databaseHelper.allIngredients();
            ArrayAdapter<String> new_adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, items);
            autoCompleteTextView.setAdapter(new_adapter);
            autoCompleteTextView.setThreshold(1);
        });
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}