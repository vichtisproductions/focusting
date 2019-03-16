package org.vichtisproductions.focusting.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.vichtisproductions.focusting.MainApplication;
import org.vichtisproductions.focusting.R;
import org.vichtisproductions.focusting.utils.DataUtils;

import javax.inject.Inject;

public class UsernameDialog extends AppCompatDialogFragment {

    @Inject
    DataUtils dataUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.from(getContext()).getGraph().inject(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.username_dialog, null);

        EditText usernameInput = view.findViewById(R.id.username_dialog_input);

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.username_dialog_text)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(R.string.username_dialog_action, (dialog, which) -> {
                    dataUtils.setUsername(usernameInput.getText().toString());
                })
                .create();
    }
}
