package au.edu.uts.mad.id11258732.wreckaphraseproject.helper;

/**
 * All Keys and constants used by the application are kept in this class
 */
public class Constants {

    public static final String TAG = "WAP";

    public static final String PHRASE_REGEX = "[A-Za-z.,?'!\\- ]+";
    public static final String PUNCTUATION_REGEX = "[.,?'!]";
    public static final String EMPTY_REGEX = "[ ]+";

    public static final String WORD_TO_CHANGE = "wordToChange";
    public static final String TYPE_OF_CHANGE = "typeOfChange";
    public static final String NEW_WORD_RECEIVED = "newWordReceived";
    public static final int GET_SYNONYM = 1;
    public static final int GET_RHYME = 2;
    public static final String RHYMES_TAG = "rhymes";

    public static final String PHRASE = "phrase";
    public static final String CREATION_DATE = "creationDate";
    public static final String WORD = "word";
    public static final String POSITION = "position";

    public static final String TABLE_FAVOURITES = "Favourites";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PHRASE = "phrase";
    public static final String COLUMN_CREATION_DATE = "creationDate";

    public static final String[] GET_FAVOURITE_COLUMNS = {
            Constants.COLUMN_ID,
            Constants.COLUMN_PHRASE,
            Constants.COLUMN_CREATION_DATE};

    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String ACTION_RESP = "messageProcessed";

    public static final int UPWARD_TRANSITION = 0;
    public static final int FORWARD_TRANSITION = 1;
    public static final int BACKWARD_TRANSITION = 2;
}
