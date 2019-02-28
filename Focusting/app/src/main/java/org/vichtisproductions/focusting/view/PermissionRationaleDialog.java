package org.vichtisproductions.focusting.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import org.vichtisproductions.focusting.R;

/**
 * PermissionRationaleDialog is a dialog shown before runtime permission is requested from the user.
 * The dialog contains the rationale on why the user should give the app the requested permission.
 */
public class PermissionRationaleDialog extends AppCompatDialogFragment {

    private Callback callback;

    /**
     * Callback gets notified when the use interacts with the dialog
     */
    public interface Callback {
        void onDialogButtonClick(int which);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Callback) {
            this.callback = (Callback) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setMessage(R.string.permission_rationale_message)
                .setPositiveButton(R.string.permission_rationale_action, (dialog, which) -> {
                    if (callback != null) {
                        callback.onDialogButtonClick(which);
                    }
                })
                .create();
    }
}
