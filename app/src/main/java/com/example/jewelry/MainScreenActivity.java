package com.example.jewelry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jewelry.databinding.ActivityMainScreenBinding;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainScreenActivity extends AppCompatActivity {

    private static final String TAG = "MainScreenActivity";
    private ActivityMainScreenBinding binding;
    private MainScreenViewModel viewModel;
    private List<TextView> categories;
    private DishesAdapter adapter;
    private List<Dish> dishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        categories = Arrays.asList(
                binding.textViewFoods,
                binding.textViewDrinks,
                binding.textViewSnacks,
                binding.textViewSauce
        );

        setupListeners();
        adapter = new DishesAdapter(this);
        binding.recyclerViewDishes.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(MainScreenViewModel.class);
        observeViewModel();
        viewModel.loadDishes();
    }

    private void setupListeners() {
        for (int i = 0; i < categories.size(); i++) {
            setupOnChangeCategoryListener(i);
        }
        binding.buttonSearch.setOnClickListener(view -> {
            binding.textViewAddress.setVisibility(View.INVISIBLE);
            binding.buttonCloseSearch.setVisibility(View.VISIBLE);
            binding.buttonAddress.setVisibility(View.GONE);
            binding.editTextSearch.setVisibility(View.VISIBLE);
        });
        binding.buttonCloseSearch.setOnClickListener(view -> {
            binding.textViewAddress.setVisibility(View.VISIBLE);
            binding.buttonCloseSearch.setVisibility(View.GONE);
            binding.buttonAddress.setVisibility(View.VISIBLE);
            binding.editTextSearch.setVisibility(View.GONE);
            binding.editTextSearch.setText("");
        });
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, err -> {
            Log.d(TAG, err);
        });
        viewModel.getDishes().observe(this, dishesList -> {
            adapter.setDishes(dishesList);
            Log.d(TAG, String.valueOf(dishesList.size()));
            //dishes = dishesList;
            /*adapter.setDishes(dishesList
                    .stream()
                    .filter(dish -> dish.getCategory()
                            .equals(categories.get(0).getText().toString()))
                    .collect(Collectors.toList())
            );*/
        });
    }

    private void setupOnChangeCategoryListener(int activeCategoryIndex) {
        TextView activeCategory = categories.get(activeCategoryIndex);
        activeCategory.setOnClickListener(view -> {
            for (TextView category : categories) {
                category.setPaintFlags(category.getPaintFlags()
                        & (~ Paint.UNDERLINE_TEXT_FLAG));
                int color = ContextCompat.getColor(this, R.color.gray);
                category.setTextColor(color);
            }
            activeCategory
                    .setPaintFlags(activeCategory.getPaintFlags()
                            | Paint.UNDERLINE_TEXT_FLAG);
            int activeColor = ContextCompat.getColor(this, R.color.orange);
            activeCategory.setTextColor(activeColor);
            /*adapter.setDishes(dishes
                    .stream()
                    .filter(dish -> dish.getCategory()
                            .equals(categories.get(activeCategoryIndex).getText().toString()))
                    .collect(Collectors.toList())
            );*/
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView foods = binding.textViewFoods;
        foods.setPaintFlags(foods.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MainScreenActivity.class);
    }
}