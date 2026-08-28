package com.example.kolokvijum1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class RecipeBroadcastReceiver extends BroadcastReceiver {

    private static int totalPrepTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        int prepTime = intent.getIntExtra(RecipeFragment.EXTRA_PREP_TIME, 0);
        totalPrepTime += prepTime;

        if (totalPrepTime > 120) {
            Toast.makeText(context, "Predugo kuvanje!", Toast.LENGTH_LONG).show();
        }
    }
}
