package me.hugo.thankmaslobby.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringUtils {

    private static final String LINEBREAK = "\n";

    public static String wrap(String string, int lineLength) {
        StringBuilder b = new StringBuilder();
        for (String line : string.split(Pattern.quote(LINEBREAK))) {
            b.append(wrapLine(line, lineLength));
        }
        return b.toString();
    }

    public static List<String> wrapLine(String line, int lineLength) {
        if (line.length() == 0) return null;
        if (line.length() <= lineLength) return null;
        String[] words = line.split(" ");
        StringBuilder allLines = new StringBuilder();
        List<String> stringList = new ArrayList<>();
        StringBuilder trimmedLine = new StringBuilder();
        for (String word : words) {
            if (trimmedLine.length() + 1 + word.length() <= lineLength) {
                trimmedLine.append(word).append(" ");
            } else {
                stringList.add(trimmedLine.toString());
                allLines.append(trimmedLine).append(LINEBREAK);
                trimmedLine = new StringBuilder();
                trimmedLine.append(word).append(" ");
            }
        }
        if (trimmedLine.length() > 0) {
            stringList.add(trimmedLine.toString());
            allLines.append(trimmedLine);
        }
        allLines.append(LINEBREAK);
        return stringList;
    }

}
