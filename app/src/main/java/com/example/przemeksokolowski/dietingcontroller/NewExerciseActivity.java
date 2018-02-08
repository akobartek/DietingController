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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.przemeksokolowski.dietingcontroller.data.ApiUtils;
import com.example.przemeksokolowski.dietingcontroller.model.Workout;
import com.example.przemeksokolowski.dietingcontroller.model.WorkoutType;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewExerciseActivity extends AppCompatActivity {

    private int mLoggedUserId;
    private ArrayList<WorkoutType> workoutTypes;

    private ProgressBar mLoadingIndicator;
    private ConstraintLayout mConstraintLayout;
    private Button mAddExerciseBtn;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoggedUserId = ApiUtils.getUserIdFromIntent(getIntent());
        workoutTypes = new ArrayList<>();

        mConstraintLayout = findViewById(R.id.exercise_constraint);
        mLoadingIndicator = findViewById(R.id.pb_loading_exercises_indicator);
        showLoading();

        getActivityTypesFromAPI();

        final EditText timeEditText = findViewById(R.id.limit_kcal_edit_text);

        mAddExerciseBtn = findViewById(R.id.add_exercise_button);
        mAddExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeEditText.getText().toString().trim().equals(""))
                    postActivityToAPI(timeEditText.getText().toString().trim());
                else
                    timeEditText.setError("Pole nie może być puste!");
            }
        });
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
                startActivity(new Intent(NewExerciseActivity.this, HistoryActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(NewExerciseActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getActivityTypesFromAPI() {
        ApiUtils.getWebApi().getAllActivityTypes().enqueue(new Callback<List<WorkoutType>>() {
            @Override
            public void onResponse(@NonNull Call<List<WorkoutType>> call, @NonNull Response<List<WorkoutType>> response) {
                if (response.isSuccessful()) {
                    workoutTypes.addAll(response.body());

                    final ArrayList<String> workoutNames = new ArrayList<>();
                    for (WorkoutType workoutType : workoutTypes)
                        workoutNames.add(workoutType.getName());

                    spinner = findViewById(R.id.exercise_spinner);
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                            NewExerciseActivity.this, android.R.layout.simple_spinner_item, workoutNames);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerAdapter);

                    mAddExerciseBtn.setEnabled(true);
                    showDataView();

                    Log.i("postMealToAPI", "POST submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<WorkoutType>> call, @NonNull Throwable t) {
                Log.e("postMealToAPI", "Unable to submit post to API.");
                ApiUtils.noApiConnectionDialog(NewExerciseActivity.this);
            }
        });
    }

    private void postActivityToAPI(String time) {
        int acitivityTypeId = workoutTypes.get(spinner.getSelectedItemPosition()).getId();

        ApiUtils.getWebApi().createActivity(acitivityTypeId, Integer.parseInt(time), mLoggedUserId)
                .enqueue(new Callback<Workout>() {
            @Override
            public void onResponse(@NonNull Call<Workout> call, @NonNull Response<Workout> response) {
                if (response.isSuccessful()) {
                    Log.i("postActivityToAPI", "POST submitted to API." + response.body().toString());
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Workout> call, @NonNull Throwable t) {
                Log.e("postActivityToAPI", "Unable to submit post to API.");
                ApiUtils.noApiConnectionDialog(NewExerciseActivity.this);
            }
        });
    }
}
