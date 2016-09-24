package au.edu.uts.mad.id11258732.wreckaphraseproject.helper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.app.ShareCompat;
import android.view.inputmethod.InputMethodManager;

import au.edu.uts.mad.id11258732.wreckaphraseproject.R;
import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Phrase;

/**
 * Utilities Class
 * Provides various methods to be used by the whole Wreck a Phrase android application
 */
public class Utilities {

    /**
     * Closes the soft keyboard
     * @param context is the current Context
     * @param windowToken provides the window to close the keyboard on
     */
    public static void closeKeyboard(Context context, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    /**
     * Sends an intent to message a phrase to someone else
     * @param activity current Activity
     * @param phrase to be sent
     */
    public static void sendPhrase(Activity activity, Phrase phrase) {
        // Use ShareCompat to quickly make an Intent to send the Phrase
        ShareCompat.IntentBuilder
                .from(activity)
                // Sets the phrase to be sent
                .setText(phrase.getPhrase())
                // Keeps its this media type as only text is sent, not other medias
                .setType(activity.getString(R.string.media_type_plain_text))
                // Set subject
                .setChooserTitle(R.string.share_phrase)
                // Fire off intent
                .startChooser();
    }

    /**
     * Checks the network connection of the phone
     * @param activity current Activity
     * @return true if there is a connection, false if no connection is available
     */
    public static boolean isNetworkConnected(Activity activity) {
        // Get all the classes necessary to check connection
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            // There are no active networks.
            return false;
        } else {
            // There are active networks
            return true;
        }
    }
}
