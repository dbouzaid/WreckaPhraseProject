package au.edu.uts.mad.id11258732.wreckaphraseproject.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Constants;
import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.RhymeParserHelper;
import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.SynonymParserHelper;

/**
 * WordsIntentService Class
 * An IntentService which handles consuming an API depending on which word change the user wants
 */
public class WordsIntentService extends IntentService {

    // URL combination for retrieving Synonyms. The word to be referenced will be placed between
    private final String SYNONYM_URL_START =
            "http://words.bighugelabs.com/api/2/112a406af713a58c7327d2f8b1e8bcdb/";
    private final String SYNONYM_URL_END = "/xml";

    // URL for retrieving Rhymes. The word to be referenced will be placed at the end
    private final String RHYME_URL_START =
            "http://www.stands4.com/services/v2/rhymes.php?uid=5154&tokenid=FVOOdGJACZ9KK13V&term=";

    // Word to reference in the URL to get either a Synonym or Rhyme of this word
    private String mWordReference;

    // Type of Change represented by an int. Either Synonym or Rhyme change.
    private int mTypeOfChange;

    // Used to stores the Strings which hold the words
    private ArrayList<String> mWords;

    // Used to hold each word of a Phrase.
    private String mWord;

    public WordsIntentService(){
        super("WordsIntentService");
    }

    /**
     * Handles the incoming intent and determines based on the intent's extras what type of change
     * to do and call their respective methods.
     * @param intent intent containing what the service should do
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(Constants.TAG, "Intent Service started");

        // Initiate the fields with Extras from the intent
        mWords = new ArrayList<>();
        mWordReference = intent.getStringExtra(Constants.WORD_TO_CHANGE);
        // Set default to -1
        mTypeOfChange = intent.getIntExtra(Constants.TYPE_OF_CHANGE, -1);

        switch(mTypeOfChange){
            case Constants.GET_SYNONYM :
                // Intent has specified for a synonym
                getSynonym();
                Log.i(Constants.TAG, "Getting Synonym");
                break;
            case Constants.GET_RHYME:
                // intent has specified for a rhyme
                getRhyme();
                Log.i(Constants.TAG, "Getting Rhyme");
                break;
            default:
                break;
        }


    }

    /**
     * Cosumes the Rhyme API and then sends the received XML File to be parsed to the
     * RhymeParserHelper class
     */
    private void getRhyme() {
        // Concatenate the URL to be used to retrieve the Synonyms XML
        String url = RHYME_URL_START + mWordReference;
        Log.i(Constants.TAG, url);

        try {
            // Open up a HTTP connection and retrieve the XML file containing the Rhymes
            HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            // Create a RhymeParserHelper and populate the mWords ArrayList of Strings
            RhymeParserHelper rhymeParserHelper = new RhymeParserHelper();
            mWords = rhymeParserHelper.parse(inputStream);

            // If the Parse was successful, it won't be empty and retrieve a random word
            if (!mWords.isEmpty()) {
                mWord = mWords.get(new Random().nextInt(mWords.size()));
            }

            // Create an Intent to send back the new random word
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(Constants.ACTION_RESP);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(Constants.NEW_WORD_RECEIVED, mWord);
            broadcastIntent.putExtra(Constants.TYPE_OF_CHANGE, Constants.GET_RHYME);
            sendBroadcast(broadcastIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSynonym() {
        String url = SYNONYM_URL_START + mWordReference + SYNONYM_URL_END;
        Log.i(Constants.TAG, url);

        try {
            // Open up a HTTP connection and retrieve the XML file containing the Synonyms
            HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            // Create a RhymeParserHelper and populate the mWords ArrayList of Strings
            SynonymParserHelper synonymParserHelper = new SynonymParserHelper();
            mWords = synonymParserHelper.parse(inputStream);

            // Get a Random rhyme to send back
            mWord = mWords.get(new Random().nextInt(mWords.size()));

            // Create an Intent to send back the new word
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(Constants.ACTION_RESP);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(Constants.NEW_WORD_RECEIVED, mWord);
            sendBroadcast(broadcastIntent);
        } catch (FileNotFoundException e){
            // Synonym API sends back no file if no word found, so send back a null word
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(Constants.ACTION_RESP);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(Constants.NEW_WORD_RECEIVED, mWord);
            broadcastIntent.putExtra(Constants.TYPE_OF_CHANGE, Constants.GET_SYNONYM);
            sendBroadcast(broadcastIntent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
