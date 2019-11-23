package com.loftschool.ozaharenko.loftcoin19.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.loftschool.ozaharenko.loftcoin19.R;
import com.loftschool.ozaharenko.loftcoin19.prefs.Settings;
import com.loftschool.ozaharenko.loftcoin19.ui.main.MainActivity;
import com.loftschool.ozaharenko.loftcoin19.ui.splash.SplashActivity;

public class WelcomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SnapHelper snapHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        recyclerView = findViewById(R.id.pager);
        recyclerView.setNestedScrollingEnabled(false); //for dots
        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true); //for dots

        recyclerView.swapAdapter(new WelcomePageAdapter(), false);

        final int radius = getResources().getDimensionPixelSize(R.dimen.radius); //for dots
        final int dotsHeight = getResources().getDimensionPixelSize(R.dimen.dots_height); //for dots
        final int color = ContextCompat.getColor(this, R.color.primaryTextColor);//for dots
        final int colorIn = ContextCompat.getColor(this, R.color.secondaryLightColor);//for dots

        recyclerView.addItemDecoration(new DotsIndicatorDecoration(radius, radius * 4, dotsHeight, colorIn, color)); //for dots

        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        final Settings settings = new Settings(getApplicationContext());
        findViewById(R.id.btn_start).setOnClickListener((view) -> {
            startActivity(new Intent(this, MainActivity.class));
            settings.doNotShowWelcomeScreenNextTime();
            finish();
        });
    }

    @Override
    public void onDestroy() {
        recyclerView.swapAdapter(null, false);
        snapHelper.attachToRecyclerView(null);
        super.onDestroy();
    }
}


