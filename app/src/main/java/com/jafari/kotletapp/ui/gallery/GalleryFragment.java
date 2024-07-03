package com.jafari.kotletapp.ui.gallery;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jafari.kotletapp.DatabaseHelper;
import com.jafari.kotletapp.R;
import com.jafari.kotletapp.databinding.FragmentGalleryBinding;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {
    GridLayout ingredients;
    private FragmentGalleryBinding binding;
    private DatabaseHelper databaseHelper;
    private EditText multiLineEditText;
    private String[] items;
    private AutoCompleteTextView autoCompleteTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);

        databaseHelper = new DatabaseHelper(requireActivity());

        multiLineEditText = binding.editTextMultiLine;
        items = databaseHelper.allIngredients();

        autoCompleteTextView = binding.autoComplete;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, items);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        ingredients = binding.ingredients;
        FloatingActionButton clear = binding.clearBtn;
        View root = binding.getRoot();

        final Button addIngredient = binding.addIngredient;
        addIngredient.setOnClickListener(v -> addCard(autoCompleteTextView.getText().toString()));
        final FloatingActionButton addRecipeBtn = binding.addRecipe;
        addRecipeBtn.setOnClickListener(v -> addRecipe());
        clear.setOnClickListener(v -> clear());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void addCard(String text) {
        if (! databaseHelper.validIngredient(text))
            return;

        for (int i = 0; i < ingredients.getChildCount(); i++) {
            View child = ingredients.getChildAt(i);
            if (child instanceof Button) {
                Button existingButton = (Button) child;
                if (existingButton.getText().toString().equals(text))
                    return;
            }
        }

        makeCardIng(ingredients, text);
        autoCompleteTextView.setText("");
    }

    private void addRecipe() {
        String description = multiLineEditText.getText().toString();
        databaseHelper.addNewRecipe("name", extractIngredients(ingredients), description, "creator"); // TODO name and creator
    }

    public void clear(){
        ingredients.removeAllViews();
        multiLineEditText.setText("");
    }

    public void makeCardIng(GridLayout plain , String ingredient){
        CardView cardView = new CardView(requireActivity());
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                450,
                120  // 40dp in pixels
        );
        cardViewParams.setMargins(12, 12, 12, 12);  // 5dp in pixels
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(90);
        cardView.setCardBackgroundColor(getResources().getColor(R.color.greenishYellow));


        // Create TextView
        TextView textView = new TextView(requireActivity());
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,  // 290dp in pixels
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        textView.setLayoutParams(textViewParams);
        textView.setText(ingredient);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);
        textView.setOnClickListener(v -> {
            cardView.removeView(textView);
            plain.removeView(cardView);
        });

        // Add LinearLayout to CardView
        cardView.addView(textView);
        plain.addView(cardView);
    }

    public List<String> extractIngredients(GridLayout gridLayout) {
        List<String> ingredients = new ArrayList<>();

        // Iterate through all children of the GridLayout
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);

            // Check if the child is a CardView
            if (child instanceof CardView) {
                CardView cardView = (CardView) child;

                // Check if the CardView has exactly one child, which is the TextView
                if (cardView.getChildCount() == 1) {
                    View cardChild = cardView.getChildAt(0);

                    // Check if the child is a TextView
                    if (cardChild instanceof TextView) {
                        TextView textView = (TextView) cardChild;
                        ingredients.add(textView.getText().toString());
                    }
                }
            }
        }

        return ingredients;
    }
}