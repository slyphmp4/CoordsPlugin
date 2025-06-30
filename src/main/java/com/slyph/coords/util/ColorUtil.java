package com.slyph.coords.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ColorUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("&([A-Fa-f0-9]{6})");

    private ColorUtil() {}

    public static String translateHexColors(String input) {
        Matcher matcher = HEX_PATTERN.matcher(input);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(buffer, "&#" + hex);
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
