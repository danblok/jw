package com.example.jewelry;

import static com.example.jewelry.Constants.DISH_EXTRA;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.jewelry.databinding.ActivityItemScreenBinding;

public class ItemScreenActivity extends AppCompatActivity {

    private ActivityItemScreenBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Dish dish = getIntent().getSerializableExtra(DISH_EXTRA, Dish.class);
        Glide.with(this).load(dish.getIcon()).into(binding.imageViewDishIcon);
        binding.textViewDishName.setText(dish.getName());
        binding.textViewDishPrice.setText("N" + dish.getPrice());
        binding.buttonAddToCart.setOnClickListener(view -> {
            startActivity(MainScreenActivity.newIntent(this));
        });
        binding.buttonGoBack.setOnClickListener(view -> {
            startActivity(MainScreenActivity.newIntent(this));
        });
    }
    public static Intent newIntent(Context context, Dish dish) {
        Intent intent = new Intent(context, ItemScreenActivity.class);
        intent.putExtra(DISH_EXTRA, dish);
        return intent;
    }
}