package com.example.przemeksokolowski.dietingcontroller;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

public class NewExerciseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String mSelectedExercise;
    private ProgressBar mLoadingIndicator;
    private ConstraintLayout mConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mConstraintLayout = findViewById(R.id.exercise_constraint);
        mLoadingIndicator = findViewById(R.id.pb_loading_exercises_indicator);
        showLoading();

        // TODO(8) spinner i podłączenie do niego danych z API (przykład) -> potrzeba danych o activity z API
        // TODO(9) aktualizacja napisu (sprawdzanie wpisanych cyfr?)
        // TODO(10) Zapis do API
/*
        Spinner spinner = (Spinner) findViewById(R.id.exercise_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sport_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
        */
    }

    private void showLoading() {
        mConstraintLayout.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
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

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        mSelectedExercise = (String) parent.getItemAtPosition(pos);
        Log.v("NewExerciseActivity", mSelectedExercise);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        mSelectedExercise = "Bieganie";
    }
}
