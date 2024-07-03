package com.jafari.kotletapp.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jafari.kotletapp.DatabaseHelper;
import com.jafari.kotletapp.databinding.FragmentGalleryBinding;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private DatabaseHelper databaseHelper;
    private EditText multiLineEditText;
    private String[] items;
    private AutoCompleteTextView autoCompleteTextView;
    private LinearLayout buttonContainer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);

        databaseHelper = new DatabaseHelper(requireActivity());

        multiLineEditText = binding.editTextMultiLine;
        items = databaseHelper.allIngredients();

        buttonContainer = binding.buttonContainer;

        autoCompleteTextView = binding.autoComplete;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, items);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);

        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        final Button addIngredient = binding.addIngredient;
        addIngredient.setOnClickListener(v -> addButton(autoCompleteTextView.getText().toString()));
        final Button addRecipeBtn = binding.addRecipe;
        addRecipeBtn.setOnClickListener(v -> addRecipe());
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void addButton(String text) {
        boolean isValid = false;
        for (String item : items)
            if (item.equals(text)) {
                isValid = true;
                break;
            }
        if (! isValid)
            return;

        for (int i = 0; i < buttonContainer.getChildCount(); i++) {
            View child = buttonContainer.getChildAt(i);
            if (child instanceof Button) {
                Button existingButton = (Button) child;
                if (existingButton.getText().toString().equals(text))
                    return;
            }
        }

        final Button newButton = new Button(requireActivity());
        newButton.setText(text);
        newButton.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        newButton.setOnClickListener(v -> buttonContainer.removeView(newButton));

        buttonContainer.addView(newButton);
    }

    private void addRecipe() {
        String description = multiLineEditText.getText().toString();
        String[] ingredients = items.clone();
        databaseHelper.addNewRecipe("name", ingredients, description, "creator"); // TODO name and creator
    }
}