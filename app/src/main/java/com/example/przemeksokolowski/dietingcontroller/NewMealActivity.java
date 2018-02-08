package com.example.przemeksokolowski.dietingcontroller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.przemeksokolowski.dietingcontroller.data.ApiUtils;
import com.example.przemeksokolowski.dietingcontroller.model.ChoosenProducts;
import com.example.przemeksokolowski.dietingcontroller.model.Meal;
import com.example.przemeksokolowski.dietingcontroller.model.Product;
import com.example.przemeksokolowski.dietingcontroller.model.ProductListItem;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMealActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private int mealType = 1, mLoggedUserId;

    private ArrayList<Product> products;
    private ArrayList<ProductListItem> choosenProducts;

    private RecyclerViewAdapter recyclerViewAdapter;
    private Button saveMealBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoggedUserId = ApiUtils.getUserIdFromIntent(getIntent());
        products = new ArrayList<>();
        choosenProducts = new ArrayList<>();

        getProductsFromAPI();

        Spinner spinner = findViewById(R.id.meal_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.meal_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        Button addProductBtn = findViewById(R.id.add_product_button);
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProductDialog();
            }
        });

        saveMealBtn = findViewById(R.id.add_meal_button);
        saveMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMealToAPI();
            }
        });

        recyclerViewAdapter = new RecyclerViewAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.products_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                choosenProducts.remove(viewHolder.getAdapterPosition());
                recyclerViewAdapter.swapChoosenProductsList(choosenProducts);
                if (choosenProducts.size() == 0) {
                    saveMealBtn.setEnabled(false);
                }
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void getProductsFromAPI() {
        ApiUtils.getWebApi().getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    products.addAll(response.body());

                    if (products.size() == 0) {
                        noProductsInApiDialog();
                    }
                } else {
                    Log.d("getProductsFromAPI", "Code: " + response.code() + " Message: " + response.message());
                    ApiUtils.noApiConnectionDialog(NewMealActivity.this, getParent());
                }
            }

            @Override public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                t.printStackTrace();
                ApiUtils.noApiConnectionDialog(NewMealActivity.this, getParent());
            }
        });
    }

    private void noProductsInApiDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Brak zapisanych produktów!");

        alertDialogBuilder
                .setMessage("Chcesz dodać produkt, czy wyjść?")
                .setCancelable(false)
                .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        showNewProductDialog();
                    }
                })
                .setNegativeButton("Wyjdź", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        onBackPressed();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showAddProductDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Dodaj produkt do listy");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_product_to_list, null);

        final ArrayList<String> productNames = new ArrayList<>();
        for (Product product : products)
            productNames.add(product.getName());

        final Spinner spinner = view.findViewById(R.id.select_product_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, productNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        final EditText editText = view.findViewById(R.id.select_product_weight);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (editText.getText().toString().equals("")) {
                            editText.setError("Waga nie może być pusta!");
                            Toast.makeText(NewMealActivity.this, "Waga nie może być pusta!", Toast.LENGTH_SHORT).show();
                        } else {
                            choosenProducts.add(new ProductListItem(
                                    products.get(spinner.getSelectedItemPosition()).getId(),
                                    spinner.getSelectedItem().toString(),
                                    Integer.parseInt(editText.getText().toString())
                            ));
                            recyclerViewAdapter.swapChoosenProductsList(choosenProducts);
                            saveMealBtn.setEnabled(true);
                            Toast.makeText(NewMealActivity.this, "Dodano", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton("Wyjdź", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setView(view);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void postMealToAPI() {
        ApiUtils.getWebApi().createMeal(mLoggedUserId, mealType).enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(@NonNull Call<Meal> call, @NonNull Response<Meal> response) {
                if (response.isSuccessful()) {
                    for (ProductListItem product : choosenProducts) {
                        postChoosenProductToAPI(new ChoosenProducts(product.getId(), mealType, product.getWeight()));
                    }

                    Log.i("postMealToAPI", "POST submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Meal> call, @NonNull Throwable t) {
                Log.e("postMealToAPI", "Unable to submit post to API.");
                ApiUtils.noApiConnectionDialog(NewMealActivity.this, getParent());
            }
        });
    }

    private void postChoosenProductToAPI(ChoosenProducts product) {
        ApiUtils.getWebApi().createChoosenProduct(product).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i("postMealToAPI", "POST submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("postMealToAPI", "Unable to submit post to API.");
                ApiUtils.noApiConnectionDialog(NewMealActivity.this, getParent());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_product, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_history:
                startActivity(ApiUtils.createIntentWithLoggedUserId(
                        NewMealActivity.this, HistoryActivity.class, mLoggedUserId));
                return true;
            case R.id.action_settings:
                startActivity(ApiUtils.createIntentWithLoggedUserId(
                        NewMealActivity.this, SettingsActivity.class, mLoggedUserId));
                return true;
            case R.id.action_new_product:
                showNewProductDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showNewProductDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Dodaj nowy produkt");

        View view = getLayoutInflater().inflate(R.layout.dialog_new_product, null);

        final EditText nameEditText = view.findViewById(R.id.new_product_name);
        final EditText caloriesEditText = view.findViewById(R.id.new_product_calories);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = nameEditText.getText().toString();
                        String caloriesText = caloriesEditText.getText().toString();
                        if (name.equals("")) {
                            nameEditText.setError("Nazwa nie może być pusta!");
                            Toast.makeText(NewMealActivity.this, "Nazwa nie może być pusta!", Toast.LENGTH_SHORT).show();
                        } else if (caloriesText.equals("")) {
                            caloriesEditText.setError("Wartość kaloryczna nie może być pusta!");
                            Toast.makeText(NewMealActivity.this, "Wartość kaloryczna nie może być pusta!", Toast.LENGTH_SHORT).show();
                        } else {
                            postProductToAPI(name, Integer.parseInt(caloriesText));
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton("Wyjdź", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (products.size() == 0) {
                            finish();
                        }
                    }
                })
                .setView(view);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void postProductToAPI(String name, int calories) {
        ApiUtils.getWebApi().createProduct(name, calories).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful()) {
                    products.add(response.body());

                    Toast.makeText(NewMealActivity.this, "Zapisano", Toast.LENGTH_SHORT).show();
                    Log.i("postMealToAPI", "POST submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                Toast.makeText(NewMealActivity.this, "Napotkano błąd", Toast.LENGTH_SHORT).show();
                Log.e("postMealToAPI", "Unable to submit post to API.");
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        mealType = pos + 1;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        mealType = 1;
    }
}
