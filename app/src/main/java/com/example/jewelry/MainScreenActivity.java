package com.example.jewelry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jewelry.databinding.ActivityMainScreenBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

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
    private List<Dish> currentDishes;

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

        adapter = new DishesAdapter(this);
        binding.recyclerViewDishes.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(MainScreenViewModel.class);
        setupListeners();
        observeViewModel();
        viewModel.loadDishes();

        binding.linearLayoutAdds.loadAd(new AdRequest.Builder().build());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupListeners() {
        for (int i = 0; i < categories.size(); i++) {
            setupOnChangeCategoryListener(i);
        }
        binding.buttonSearch.setOnClickListener(view -> {
            binding.textViewAddress.setVisibility(View.INVISIBLE);
            binding.buttonCloseSearch.setVisibility(View.VISIBLE);
            binding.buttonAddress.setVisibility(View.GONE);
            binding.editTextSearch.setVisibility(View.VISIBLE);

            binding.textViewFoods.setVisibility(View.GONE);
            binding.textViewDrinks.setVisibility(View.GONE);
            binding.textViewSnacks.setVisibility(View.GONE);
            binding.textViewSauce.setVisibility(View.GONE);

            binding.textViewResults.setVisibility(View.VISIBLE);
        });
        binding.buttonCloseSearch.setOnClickListener(view -> {
            binding.textViewAddress.setVisibility(View.VISIBLE);
            binding.buttonCloseSearch.setVisibility(View.GONE);
            binding.buttonAddress.setVisibility(View.VISIBLE);
            binding.editTextSearch.setVisibility(View.GONE);

            binding.textViewFoods.setVisibility(View.VISIBLE);
            binding.textViewDrinks.setVisibility(View.VISIBLE);
            binding.textViewSnacks.setVisibility(View.VISIBLE);
            binding.textViewSauce.setVisibility(View.VISIBLE);

            binding.textViewResults.setVisibility(View.INVISIBLE);

            binding.editTextSearch.setText("");
            adapter.setDishes(currentDishes);
        });
        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.setDishes(currentDishes
                        .stream()
                        .filter(dish -> dish.getName().toLowerCase()
                                .contains(s))
                        .collect(Collectors.toList()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.recyclerViewDishes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                binding.linearLayoutAdds.setVisibility(View.GONE);
            }
        });
        adapter.setOnItemClickListener(dish -> {
            binding.recyclerViewDishes.setVisibility(View.GONE);
            binding.linearLayoutItem.setVisibility(View.VISIBLE);
            Glide.with(this).load(dish.getIcon()).into(binding.imageViewItemIcon);
            binding.textViewItemPrice.setText("N" + dish.getPrice());
            binding.textViewItemCount.setText("0");
            binding.linearLayoutPlus.setOnClickListener(view -> {
                int count = Integer.parseInt(binding.textViewItemCount.getText().toString());
                binding.textViewItemCount.setText(String.valueOf(count + 1));
            });
            binding.linearLayoutMinus.setOnClickListener(view -> {
                int count = Integer.parseInt(binding.textViewItemCount.getText().toString());
                if (count >= 1) {
                    binding.textViewItemCount.setText(String.valueOf(count - 1));
                }
            });
            binding.linearLayoutAddToCart.setOnClickListener(view -> {
                binding.linearLayoutOrder.setVisibility(View.GONE);
                binding.linearLayoutAddToCart.setVisibility(View.GONE);
                binding.linearLayoutCart.setVisibility(View.VISIBLE);
            });
            binding.buttonContinue.setOnClickListener(view -> {
                binding.linearLayoutItem.setVisibility(View.GONE);
                binding.linearLayoutOrder.setVisibility(View.VISIBLE);
                binding.linearLayoutAddToCart.setVisibility(View.VISIBLE);
                binding.recyclerViewDishes.setVisibility(View.VISIBLE);
            });
            binding.textViewMore.setOnClickListener(view -> {
                startActivity(ItemScreenActivity.newIntent(this, dish));
            });
            binding.buttonGoToCart.setOnClickListener(view -> {

                binding.linearLayoutItem.setVisibility(View.GONE);
                binding.linearLayoutOrder.setVisibility(View.VISIBLE);
                binding.linearLayoutAddToCart.setVisibility(View.VISIBLE);
                binding.recyclerViewDishes.setVisibility(View.VISIBLE);
            });
            binding.imageViewBack.setOnClickListener(view -> {
                binding.linearLayoutItem.setVisibility(View.GONE);
                binding.linearLayoutOrder.setVisibility(View.VISIBLE);
                binding.linearLayoutAddToCart.setVisibility(View.VISIBLE);
                binding.recyclerViewDishes.setVisibility(View.VISIBLE);
            });
        });
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, err -> {
            Log.d(TAG, err);
        });
        viewModel.getDishes().observe(this, dishesList -> {
            dishes = dishesList;
            currentDishes = dishesList
                    .stream()
                    .filter(dish -> dish.getCategory()
                            .equals(categories.get(0).getText().toString()))
                    .collect(Collectors.toList());
            adapter.setDishes(currentDishes);
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
            currentDishes = dishes
                    .stream()
                    .filter(dish -> dish.getCategory()
                            .equals(categories.get(activeCategoryIndex).getText().toString())
                    )
                    .collect(Collectors.toList());
            adapter.setDishes(currentDishes.stream().filter(dish ->
                    dish.getName().toLowerCase()
                            .contains(binding.editTextSearch
                                    .getText().toString().toLowerCase().trim())
                    )
                    .collect(Collectors.toList()));
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