package com.jafari.kotletapp.ui.home;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jafari.kotletapp.DatabaseHelper;
import com.jafari.kotletapp.R;
import com.jafari.kotletapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseHelper databaseHelper;
    private String[] items;

    GridLayout ingredients;
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
        final FloatingActionButton findBtn = binding.findBtn;
        final FloatingActionButton clearBtn = binding.clearBtn;
        ingredients = binding.ingredients;
        ingredients.setColumnCount(2);
        autoCompleteTextView = binding.autoComplete;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, items);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);

        clearBtn.setOnClickListener(view -> clear());

        findBtn.setOnClickListener(view -> {
            System.out.println(extractIngredients(ingredients));
            ingredients.removeAllViews();
            ingredients.setColumnCount(1);
            makeCardRecipe(ingredients);
            List<Pair<String, Pair<Integer, Integer>>> availableRecipes =
                    databaseHelper.getTop20Recipes(extractIngredients(ingredients));
        });

        addBtn.setOnClickListener(view -> {
            if(ingredients.getColumnCount() == 1){
                clear();
            }
            String ingredient = autoCompleteTextView.getText().toString();
            if (databaseHelper.validIngredient(ingredient)) {
                makeCardIng(ingredients, ingredient);
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

        cardView.addView(textView);
        plain.addView(cardView);
    }

    void makeCardRecipe(GridLayout gridLayout){
        // Convert dp to pixels
        int cardViewHeight = (int) (70 * getResources().getDisplayMetrics().density);
        int cardViewMarginBottom = (int) (10 * getResources().getDisplayMetrics().density);
        int imageViewSize = (int) (60 * getResources().getDisplayMetrics().density);
        int imageViewMarginHorizontal = (int) (10 * getResources().getDisplayMetrics().density);
        int innerLinearLayoutWidth = (int) (260 * getResources().getDisplayMetrics().density);
        int innerLinearLayoutHeight = (int) (70 * getResources().getDisplayMetrics().density);
        int innerTextViewHeightLarge = (int) (30 * getResources().getDisplayMetrics().density);
        int innerTextViewHeightSmall = (int) (20 * getResources().getDisplayMetrics().density);
        int innerLinearLayoutWidthSmall = (int) (130 * getResources().getDisplayMetrics().density);
        int paddingVertical = (int) (5 * getResources().getDisplayMetrics().density);
        float radius = 20 * getResources().getDisplayMetrics().density;

        // Create CardView
        CardView cardView = new CardView(requireActivity());
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                cardViewHeight
        );
        cardViewParams.setMargins(0, 0, 0, cardViewMarginBottom);
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(radius);
        cardView.setCardBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.lemon));

        // Create outer LinearLayout
        LinearLayout outerLinearLayout = new LinearLayout(requireActivity());
        outerLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        outerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Create ImageView
        ImageView imageView = new ImageView(requireActivity());
        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                imageViewSize,
                imageViewSize
        );
        imageViewParams.setMargins(imageViewMarginHorizontal, 0, imageViewMarginHorizontal, 0);
        imageView.setLayoutParams(imageViewParams);
        imageView.setBackground(ContextCompat.getDrawable(requireActivity(), R.drawable.recipe_book));

        // Create inner LinearLayout
        LinearLayout innerLinearLayout = new LinearLayout(requireActivity());
        LinearLayout.LayoutParams innerLinearLayoutParams = new LinearLayout.LayoutParams(
                innerLinearLayoutWidth,
                innerLinearLayoutHeight
        );
        innerLinearLayout.setLayoutParams(innerLinearLayoutParams);
        innerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        innerLinearLayout.setPadding(0, paddingVertical, 0, paddingVertical);
        innerLinearLayout.setGravity(Gravity.CENTER_VERTICAL);

        // Create first inner vertical LinearLayout
        LinearLayout firstInnerVerticalLayout = new LinearLayout(requireActivity());
        LinearLayout.LayoutParams firstInnerVerticalLayoutParams = new LinearLayout.LayoutParams(
                innerLinearLayoutWidthSmall,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        firstInnerVerticalLayout.setLayoutParams(firstInnerVerticalLayoutParams);
        firstInnerVerticalLayout.setOrientation(LinearLayout.VERTICAL);

        // Create first TextView
        TextView firstTextView = new TextView(requireActivity());
        LinearLayout.LayoutParams firstTextViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                innerTextViewHeightLarge
        );
        firstTextView.setLayoutParams(firstTextViewParams);
        firstTextView.setText("Kotlet");
        firstTextView.setTextSize(20);
        firstTextView.setGravity(Gravity.CENTER);

        // Create second TextView
        TextView secondTextView = new TextView(requireActivity());
        LinearLayout.LayoutParams secondTextViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                innerTextViewHeightSmall
        );
        secondTextView.setLayoutParams(secondTextViewParams);
        secondTextView.setText("3 Likes");
        secondTextView.setTextSize(10);
        secondTextView.setGravity(Gravity.CENTER);

        // Add TextViews to first inner vertical LinearLayout
        firstInnerVerticalLayout.addView(firstTextView);
        firstInnerVerticalLayout.addView(secondTextView);

        // Create second inner vertical LinearLayout
        LinearLayout secondInnerVerticalLayout = new LinearLayout(requireActivity());
        LinearLayout.LayoutParams secondInnerVerticalLayoutParams = new LinearLayout.LayoutParams(
                innerLinearLayoutWidthSmall,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        secondInnerVerticalLayout.setLayoutParams(secondInnerVerticalLayoutParams);
        secondInnerVerticalLayout.setOrientation(LinearLayout.VERTICAL);
        secondInnerVerticalLayout.setGravity(Gravity.CENTER);

        // Create third TextView
        TextView thirdTextView = new TextView(requireActivity());
        LinearLayout.LayoutParams thirdTextViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                innerTextViewHeightSmall
        );
        thirdTextView.setLayoutParams(thirdTextViewParams);
        thirdTextView.setText("3 available ingredients");
        thirdTextView.setTextSize(10);
        thirdTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.green));
        thirdTextView.setGravity(Gravity.CENTER);

        // Create fourth TextView
        TextView fourthTextView = new TextView(requireActivity());
        LinearLayout.LayoutParams fourthTextViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                innerTextViewHeightSmall
        );
        fourthTextView.setLayoutParams(fourthTextViewParams);
        fourthTextView.setText("3 unavailable ingredients");
        fourthTextView.setTextSize(10);
        fourthTextView.setTextColor(ContextCompat.getColor(requireActivity(), R.color.red));
        fourthTextView.setGravity(Gravity.CENTER);

        // Add TextViews to second inner vertical LinearLayout
        secondInnerVerticalLayout.addView(thirdTextView);
        secondInnerVerticalLayout.addView(fourthTextView);

        // Add inner vertical LinearLayouts to inner LinearLayout
        innerLinearLayout.addView(firstInnerVerticalLayout);
        innerLinearLayout.addView(secondInnerVerticalLayout);

        // Add ImageView and inner LinearLayout to outer LinearLayout
        outerLinearLayout.addView(imageView);
        outerLinearLayout.addView(innerLinearLayout);

        // Add outer LinearLayout to CardView
        cardView.addView(outerLinearLayout);
        cardView.setOnClickListener(view -> {
            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.recipe_box);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(Boolean.TRUE);
            dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.tp));
            dialog.show();
        });

        gridLayout.addView(cardView);
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
    public void clear(){
        ingredients.setColumnCount(2);
        ingredients.removeAllViews();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}