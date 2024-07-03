package com.jafari.kotletapp.ui.home;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jafari.kotletapp.DatabaseHelper;
import com.jafari.kotletapp.R;
import com.jafari.kotletapp.databinding.FragmentHomeBinding;

import java.util.EventListener;

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
        View root = binding.getRoot();


        final Button addBtn = binding.addBtn;
        GridLayout ingredients = binding.ingredients;
        autoCompleteTextView = binding.autoComplete;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, items);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ingredient = autoCompleteTextView.getText().toString();
                makeCard(ingredients , ingredient);
                autoCompleteTextView.setText("");
            }
        });
        binding.admBtn.setOnClickListener(v -> {
            databaseHelper.addNewIngredient(autoCompleteTextView.getText().toString(), 10);
            items = databaseHelper.allIngredients();
            ArrayAdapter<String> new_adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, items);
            autoCompleteTextView.setAdapter(new_adapter);
            autoCompleteTextView.setThreshold(1);
        });
        return root;
    }

    public void makeCard(GridLayout plain , String ingredient){
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

        // Add LinearLayout to CardView
        cardView.addView(textView);
        plain.addView(cardView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}