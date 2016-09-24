package au.edu.uts.mad.id11258732.wreckaphraseproject.helper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Boozy on 4/06/2016.
 */
public class RhymeParserHelper {

    public RhymeParserHelper(){

    }

    public ArrayList<String> parse(InputStream inputStream) {

        XmlPullParserFactory factory;
        XmlPullParser parser;
        ArrayList<String> words = new ArrayList<>();
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            String tagName;
            String currentTag = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTag = tagName;
                        break;
                    case XmlPullParser.TEXT:
                        if (currentTag.equals("rhymes")){
                            words = splitRhymeWords(parser.getText());
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    private ArrayList<String> splitRhymeWords(String rhymeWords){
        String[] wordsArray = rhymeWords.split(",");
        ArrayList<String> wordsList = new ArrayList<>();
        for (String s: wordsArray) {
            wordsList.add(s.trim());
        }
        return wordsList;
    }

}
