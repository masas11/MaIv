package com.example.kolokvijum1;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddRecipeDialogFragment extends DialogFragment {

    public interface OnRecipeAddedListener {
        void onRecipeAdded(Recipe recipe);
    }

    private OnRecipeAddedListener listener;

    public void setListener(OnRecipeAddedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_recipe, null);

        EditText etName = view.findViewById(R.id.etRecipeName);
        EditText etTime = view.findViewById(R.id.etRecipeTime);
        CheckBox cbFavorite = view.findViewById(R.id.cbFavorite);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(view);
        dialog.setTitle("Novi recept");

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String timeStr = etTime.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(timeStr)) {
                Toast.makeText(getContext(), "Popunite sva polja", Toast.LENGTH_SHORT).show();
                return;
            }

            int time = Integer.parseInt(timeStr);
            boolean favorite = cbFavorite.isChecked();

            if (listener != null) {
                listener.onRecipeAdded(new Recipe(name, time, favorite));
            }
            dialog.dismiss();
        });

        return dialog;
    }
}
