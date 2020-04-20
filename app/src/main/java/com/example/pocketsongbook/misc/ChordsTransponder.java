package com.example.pocketsongbook.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChordsTransponder {
    private static final String[] scale = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private static final Map<String, Integer> scaleIndex = new HashMap<>();

    static {
        String[] normalize = {"Cb", "B", "Db", "C#", "Eb", "D#", "Fb", "E", "Gb", "F#", "Ab", "G#", "Bb", "A#", "E#", "F", "B#", "C"};
        for (int i = 0; i < scale.length; i++)
            scaleIndex.put(scale[i], i);
        for (int i = 0; i < normalize.length; i += 2)
            scaleIndex.put(normalize[i], scaleIndex.get(normalize[i + 1]));
    }

    public static String transposeChord(String chordRegel, int amount) {
        int normalizedAmount = (amount % scale.length + scale.length) % scale.length;
        StringBuffer buf = new StringBuffer();
        Matcher m = Pattern.compile("[CDEFGAB][b#]?").matcher(chordRegel);
        while (m.find())
            m.appendReplacement(buf, scale[(scaleIndex.get(m.group()) + normalizedAmount) % scale.length]);
        return m.appendTail(buf).toString();
    }

}


//regex for chords:
// "(?m)(^| )([A-G](##?|bb?)?((sus|maj|min|aug|dim)\\d?)?(/[A-G](##?|bb?)?)?)( (?!\\w)|$)"