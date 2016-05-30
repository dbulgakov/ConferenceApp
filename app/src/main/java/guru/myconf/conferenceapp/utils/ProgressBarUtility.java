package guru.myconf.conferenceapp.utils;

import android.app.ProgressDialog;
import android.content.Context;

import guru.myconf.conferenceapp.R;

public class ProgressBarUtility {
    private static ProgressDialog _progressDialog;


    public static void showProgressBar(Context activity, String message) {
        if (_progressDialog == null)
        {
            _progressDialog = new ProgressDialog(activity, R.style.AppTheme_Dark_Dialog);
            _progressDialog.setIndeterminate(true);
            _progressDialog.setMessage(message);
            _progressDialog.show();
        }
        else {
            throw new IllegalStateException();
        }
    }

    public static void dismissProgressBar()
    {
        if (_progressDialog != null) {
            _progressDialog.dismiss();
        }
        _progressDialog = null;
    }
}
