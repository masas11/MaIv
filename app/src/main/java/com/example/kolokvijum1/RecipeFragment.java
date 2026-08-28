package com.example.kolokvijum1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment {

    public static final String ACTION_RECIPE_ADDED = "com.example.kolokvijum1.RECIPE_ADDED";
    public static final String EXTRA_PREP_TIME = "extra_prep_time";

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private final List<Recipe> recipeList = new ArrayList<>();
    private ImageView ivRecipeImage;

    private RecipeBroadcastReceiver recipeReceiver;
    private android.content.BroadcastReceiver cameraAllowedReceiver;

    private ActivityResultLauncher<String> cameraPermissionLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // launcher za trazenje dozvole mora biti registrovan pre nego sto se fragment pokrene
        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    // ne radimo nista posebno ovde, servis ce sam detektovati da je dozvola data
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        recyclerView = view.findViewById(R.id.rvRecipes);
        ivRecipeImage = view.findViewById(R.id.ivRecipeImage);
        View btnDodaj = view.findViewById(R.id.btnDodaj);

        adapter = new RecipeAdapter(recipeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        btnDodaj.setOnClickListener(v -> {
            AddRecipeDialogFragment dialog = new AddRecipeDialogFragment();
            dialog.setListener(recipe -> {
                adapter.addRecipe(recipe);
                recyclerView.scrollToPosition(recipeList.size() - 1);

                Intent intent = new Intent(ACTION_RECIPE_ADDED);
                intent.putExtra(EXTRA_PREP_TIME, recipe.getPrepTimeMinutes());
                requireContext().sendBroadcast(intent);
            });
            dialog.show(getParentFragmentManager(), "AddRecipeDialog");
        });

        // trazimo dozvolu za kameru ako je jos nemamo
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

        // pokrecemo servis koji svake minute proverava dozvolu
        requireContext().startService(new Intent(requireContext(), CameraCheckService.class));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        recipeReceiver = new RecipeBroadcastReceiver();
        requireContext().registerReceiver(recipeReceiver, new IntentFilter(ACTION_RECIPE_ADDED));

        cameraAllowedReceiver = new android.content.BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ivRecipeImage.setVisibility(View.VISIBLE);
            }
        };
        requireContext().registerReceiver(
                cameraAllowedReceiver,
                new IntentFilter(CameraCheckService.ACTION_CAMERA_ALLOWED)
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        if (recipeReceiver != null) {
            requireContext().unregisterReceiver(recipeReceiver);
        }
        if (cameraAllowedReceiver != null) {
            requireContext().unregisterReceiver(cameraAllowedReceiver);
        }
    }
}
