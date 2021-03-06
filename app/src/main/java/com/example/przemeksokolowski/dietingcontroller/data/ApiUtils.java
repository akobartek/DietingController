package com.example.przemeksokolowski.dietingcontroller.data;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.example.przemeksokolowski.dietingcontroller.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    private ApiUtils() {
    }

    private static Retrofit retrofit = null;

    public static WebAPI getWebApi() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(WebAPI.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        Log.d("RETROFIT", retrofit.toString());

        return retrofit.create(WebAPI.class);
    }

    public static void noApiConnectionDialog(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Błąd połączenia");

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Cofnij", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static Intent createIntentWithLoggedUserId(Context context, Class clas, int id) {
        Intent intent = new Intent(context, clas);
        intent.putExtra("logged_user_id", 5);
        return intent;
    }

    public static int getUserIdFromIntent(Intent intent) {
//        return intent.getExtras().getInt("logged_user_id");
        return 5;
    }
}
