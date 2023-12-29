package me.candiesjar.fallbackserveraddon.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ChatUtil {

    public String color(String message) {
        return convertHexColors(message).replace('&', '§');
    }

    private String convertHexColors(String message) {

        if (!containsHexColor(message)) {
            return message;
        }

        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');
            message = message.replace(hexCode, convertHexToColorCode(replaceSharp));
            matcher = pattern.matcher(message);
        }

        return message;
    }

    private String convertHexToColorCode(String hexCode) {
        char[] ch = hexCode.toCharArray();
        StringBuilder builder = new StringBuilder();

        for (char c : ch) {
            builder.append("&").append(c);
        }

        return builder.toString();
    }

    private boolean containsHexColor(String message) {
        String hexColorPattern = "(?i)&#[a-f0-9]{6}";
        return message.matches(".*" + hexColorPattern + ".*");
    }
}