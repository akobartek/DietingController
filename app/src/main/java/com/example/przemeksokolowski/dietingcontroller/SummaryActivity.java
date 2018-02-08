package com.example.przemeksokolowski.dietingcontroller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.przemeksokolowski.dietingcontroller.data.ApiUtils;
import com.example.przemeksokolowski.dietingcontroller.data.WebAPI;
import com.example.przemeksokolowski.dietingcontroller.model.ChoosenProductsUsedToGetMeals;
import com.example.przemeksokolowski.dietingcontroller.model.MealWithChoosenProducts;
import com.example.przemeksokolowski.dietingcontroller.model.Workout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryActivity extends AppCompatActivity {

    private WebAPI mWebAPI;

    private ProgressBar mLoadingIndicator;
    private ConstraintLayout mConstraintLayout;

    private String mSelectedDate;
    private int mLoggedUserId;

    private int sniadanie;
    private int drugieSniadanie;
    private int obiad;
    private int podwieczorek;
    private int kolacja;
    private int aktywnosc;

    private TextView sniadanieTextView, drugieSniadanieTextView, obiadTextView,
            podwieczorekTextView, kolacjaTextView, aktywnoscTextView, bilansTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            mSelectedDate = getIntent().getExtras().getString("selected_date");
        } else {
            mSelectedDate = (String) savedInstanceState.getSerializable("selected_date");
        }

        mLoggedUserId = ApiUtils.getUserIdFromIntent(getIntent());

        getSupportActionBar().setTitle(mSelectedDate);

        mConstraintLayout = findViewById(R.id.summary_constraint);
        mLoadingIndicator = findViewById(R.id.pb_loading_summary_indicator);
        showLoading();

        sniadanieTextView = findViewById(R.id.sniadanie_text);
        drugieSniadanieTextView = findViewById(R.id.drugie_sniadanie_text);
        obiadTextView = findViewById(R.id.obiad_text);
        podwieczorekTextView = findViewById(R.id.podwieczorek_text);
        kolacjaTextView = findViewById(R.id.kolacja_text);
        aktywnoscTextView = findViewById(R.id.aktywnosc_text);
        bilansTextView = findViewById(R.id.balance_text);

        mWebAPI = ApiUtils.getWebApi();
        mWebAPI.getMealsByUserIdAndTime(mLoggedUserId, mSelectedDate).enqueue(mealsCallback);
    }

    private void showLoading() {
        mConstraintLayout.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mConstraintLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_history:
                startActivity(new Intent(SummaryActivity.this, HistoryActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(SummaryActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Callback<List<MealWithChoosenProducts>> mealsCallback = new Callback<List<MealWithChoosenProducts>>() {
        @Override
        public void onResponse(@NonNull Call<List<MealWithChoosenProducts>> call, @NonNull Response<List<MealWithChoosenProducts>> response) {
            if (response.isSuccessful()) {
                List<MealWithChoosenProducts> meals = response.body();

                for (MealWithChoosenProducts meal : meals) {
                    for (ChoosenProductsUsedToGetMeals product : meal.getChoosenProducts()) {
                        switch (meal.getMealType()) {
                            case 1:
                                sniadanie += (product.getWeight() * product.getProduct().getCalories() / 100);
                                break;
                            case 2:
                                drugieSniadanie += (product.getWeight() * product.getProduct().getCalories() / 100);
                                break;
                            case 3:
                                obiad += (product.getWeight() * product.getProduct().getCalories() / 100);
                                break;
                            case 4:
                                podwieczorek += (product.getWeight() * product.getProduct().getCalories() / 100);
                                break;
                            case 5:
                                kolacja += (product.getWeight() * product.getProduct().getCalories() / 100);
                                break;
                            default:
                                break;
                        }
                    }
                }

                mWebAPI.getActivitiesByUserIdAndTime(mLoggedUserId, mSelectedDate).enqueue(activitiesCallback);

            } else {
                Log.d("MealsCallback", "Code: " + response.code() + " Message: " + response.message());
                ApiUtils.noApiConnectionDialog(getApplicationContext(), getParent());
            }
        }

        @Override
        public void onFailure(@NonNull Call<List<MealWithChoosenProducts>> call, @NonNull Throwable t) {
            t.printStackTrace();
            ApiUtils.noApiConnectionDialog(getApplicationContext(), getParent());
        }
    };

    private Callback<List<Workout>> activitiesCallback = new Callback<List<Workout>>() {
        @Override
        public void onResponse(@NonNull Call<List<Workout>> call, @NonNull Response<List<Workout>> response) {
            if (response.isSuccessful()) {
                List<Workout> workouts = response.body();

                for (Workout workout : workouts) {
                    //TODO(5) w zależności od api - dodanie aktywności
                }

                int bilans = sniadanie + drugieSniadanie + obiad + podwieczorek + kolacja - aktywnosc;

                sniadanieTextView.setText(getString(R.string.bilans_sniadanie, sniadanie));
                drugieSniadanieTextView.setText(getString(R.string.bilans_drugie_sniadanie, drugieSniadanie));
                obiadTextView.setText(getString(R.string.bilans_obiad, obiad));
                podwieczorekTextView.setText(getString(R.string.bilans_podwieczorek, podwieczorek));
                kolacjaTextView.setText(getString(R.string.bilans_kolacja, kolacja));
                aktywnoscTextView.setText(getString(R.string.bilans_aktywnosc, aktywnosc));
                bilansTextView.setText(getString(R.string.bilans_dnia, bilans));

                showDataView();
            } else {
                Log.d("ActivitiesCallback", "Code: " + response.code() + " Message: " + response.message());
                ApiUtils.noApiConnectionDialog(getApplicationContext(), getParent());
            }
        }

        @Override
        public void onFailure(@NonNull Call<List<Workout>> call, @NonNull Throwable t) {
            t.printStackTrace();
            ApiUtils.noApiConnectionDialog(getApplicationContext(), getParent());
        }
    };
}
