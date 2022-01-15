package me.hugo.thankmaslobby.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static final String LINEBREAK = "\n";
    static SecureRandom rnd = new SecureRandom();

    public static String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static List<String> wrapLine(String line, int lineLength) {
        if (line.length() == 0) return null;
        if (line.length() <= lineLength) return null;
        String[] words = line.split(" ");
        StringBuilder allLines = new StringBuilder();
        List<String> stringList = new ArrayList<>();
        StringBuilder trimmedLine = new StringBuilder();
        for (String word : words) {
            if (trimmedLine.length() + 1 + word.length() > lineLength) {
                stringList.add(trimmedLine.toString());
                allLines.append(trimmedLine).append(LINEBREAK);
                trimmedLine = new StringBuilder();
            }
            trimmedLine.append(word).append(" ");
        }
        if (trimmedLine.length() > 0) {
            stringList.add(trimmedLine.toString());
            allLines.append(trimmedLine);
        }
        allLines.append(LINEBREAK);
        return stringList;
    }
}
