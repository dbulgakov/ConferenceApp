package guru.myconf.conferenceapp.utils;

import android.app.ProgressDialog;
import android.content.Context;

import guru.myconf.conferenceapp.R;

public class ProgressBarUtility {
    private static ProgressDialog PROGRESS_BAR_DIALOG;


    public static void showProgressBar(Context activity, String message) {
        if (PROGRESS_BAR_DIALOG == null)
        {
            PROGRESS_BAR_DIALOG = new ProgressDialog(activity, R.style.AppTheme_Dark_Dialog);
            PROGRESS_BAR_DIALOG.setIndeterminate(true);
            PROGRESS_BAR_DIALOG.setMessage(message);
            PROGRESS_BAR_DIALOG.show();
        }
        else {
            throw new IllegalStateException();
        }
    }

    public static void dismissProgressBar()
    {
        if (PROGRESS_BAR_DIALOG != null) {
            PROGRESS_BAR_DIALOG.dismiss();
        }
        PROGRESS_BAR_DIALOG = null;
    }
}
