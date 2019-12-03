package util;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    //dd-mm-YYYY or dd/mm/YYYY or the contrary
    private static String dateRegex = "([0-2][0-9]|3(1|0))(\\/)([0-9]{2}|(1[0-2]))(\\/)([0-9]{4})";

    //This will match 0-3 words next to each other followed by a space and tab or new line
    //Problem: if their is more then 3 words, it will take the last ones.
    //(.([a-z]|[A-Z]){0,7}.){1,3}(\t+|\n)
    private static String songNameRegex = "(.([a-z]|[A-Z]){0,9}){1,4}(\\t+|\\n)";
    private static String artistName = "(presented by|show by|sing by)\\s*(\\w+)";
    private static String location = "(location|event|concert).(:|at)\\s*\\\"(.*)\\\"";


    public static String findDate(String result) {
        Pattern patt = Pattern.compile(dateRegex);
        Matcher matcher = patt.matcher(result);

        if (matcher.find()) {
            Log.d("matcher", matcher.group());
            Log.d("date", matcher.group(0));
            return matcher.group(0);
        }

        return "";
    }

    public static List<String> findSongName(String result) {
        Pattern patt = Pattern.compile(songNameRegex);
        Matcher matcher = patt.matcher(result);
        List<String> list = new ArrayList<>();

        int i = 0;

        while (matcher.find()) {
            Log.d("group", matcher.group(0));
            list.add(matcher.group(0));
            i++;
        }

        Log.d("list size", list.size() + "");
        return list;
    }

    public static String findArtist(String result) {

        Pattern patt = Pattern.compile(artistName);
        Matcher matcher = patt.matcher(result);


        if (matcher.find()) {
            Log.d("Artist name", matcher.group(2));
            return matcher.group(2);
        }
        return "";
    }

    public static String findLocation(String result) {
        Pattern patt = Pattern.compile(location);
        Matcher matcher = patt.matcher(result);


        if (matcher.find()) {
            Log.d("Loc", matcher.group(3));
            return matcher.group(3);
        }

        return "";
    }


}
