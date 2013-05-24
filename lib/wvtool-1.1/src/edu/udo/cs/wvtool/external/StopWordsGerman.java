package edu.udo.cs.wvtool.external;

import java.util.Hashtable;

/**
 * Stopwords for the German language
 * 
 * @version $Id$
 *
 */
public class StopWordsGerman {

    /** The hashtable containing the list of stopwords */
    private static Hashtable m_Stopwords = null;

    private static String[] stopWords = new String[] { "ab", "aber", "Aber", "alle", "allein", "allem", "allen",
            "aller", "als", "Als", "also", "alt", "am", "Am", "an", "andere", "anderen", "arbeiten", "auch", "Auch",
            "auf", "Auf", "Aufgabe", "aus", "außer", "bald", "beginnen", "bei", "Bei", "beide", "beiden", "beim",
            "bekannt", "bekennen", "bereits", "berichten", "bestehen", "betonen", "betonte", "bin", "bis", "bißchen",
            "bisschen", "Bisschen", "bist", "bleiben", "bringen", "da", "dabei", "dadurch", "dafür", "dagegen",
            "dahinter", "damit", "danach", "daneben", "dann", "daran", "darauf", "daraus", "darin", "darüber", "darum",
            "darunter", "das", "Das", "daß", "dass", "Dass", "dasselbe", "davon", "davor", "dazu", "dazwischen",
            "dein", "deine", "deinem", "deinen", "deiner", "deines", "dem", "demselben", "den", "denen", "denn", "der",
            "Der", "deren", "derselben", "des", "desselben", "dessen", "deutlich", "dich", "die", "Die", "dies",
            "Dies", "diese", "Diese", "dieselbe", "dieselben", "diesem", "diesen", "dieser", "dieses", "dir", "doch",
            "Doch", "dort", "drei", "du", "durch", "dürfen", "ebenso", "eigen", "eigenen", "ein", "Ein", "eine",
            "Eine", "einem", "einen", "einer", "eines", "einig", "einige", "einigen", "einmal", "entlang",
            "entscheiden", "entsprechen", "EPD", "er", "Er", "erhalten", "erklären", "erklärte", "erst", "ersten",
            "es", "Es", "etwa", "etwas", "euch", "euer", "eure", "eurem", "euren", "eurer", "eures", "fest", "finden",
            "fordern", "fragen", "frei", "früh", "führen", "fünf", "für", "Für", "fürs", "ganz", "gar", "gebe",
            "geben", "gegen", "gegenüber", "gehen", "gehören", "geht", "gemeinsam", "genau", "gewesen", "gibt",
            "glauben", "gleich", "groß", "großen", "gründen", "gut", "habe", "haben", "handeln", "hat", "hatte",
            "hätte", "hatten", "hätten", "heilig", "heißt", "her", "herein", "herum", "heute", "hin", "hinter",
            "hintern", "hoch", "hören", "ich", "ihm", "ihn", "Ihnen", "ihnen", "ihr", "ihre", "Ihre", "ihrem", "Ihrem",
            "ihren", "Ihren", "Ihrer", "ihrer", "ihres", "Ihres", "im", "Im", "immer", "in", "In", "ins",
            "international", "ist", "ja", "je", "jedesmal", "jedoch", "jene", "jenem", "jenen", "jener", "jenes",
            "jetzt", "jung", "kann", "KAP", "kaum", "kein", "keine", "keinem", "keinen", "keiner", "keines",
            "kirchlich", "klein", "kommen", "könne", "können", "könnten", "kritisieren", "lang", "laß", "lass",
            "lassen", "leben", "letzen", "letzte", "letzten", "machen", "man", "mehr", "mein", "meine", "meinem",
            "meinen", "meiner", "meines", "meist", "mich", "mir", "mit", "Mit", "mitteilen", "möglich", "muß", "muss",
            "müsse", "müssen", "müßten", "müssten", "nach", "Nach", "nachdem", "nah", "nämlich", "national", "neben",
            "nehmen", "nein", "nennen", "neu", "neue", "neuen", "nicht", "nichts", "noch", "nun", "nur", "ob", "ober",
            "obgleich", "oder", "ohne", "paar", "Recht", "recht", "reich", "religiös", "rund", "sagte", "schaffen",
            "schon", "schreiben", "schwer", "sehen", "sehr", "sei", "seien", "sein", "seine", "seinem", "seinen",
            "seiner", "seines", "seit", "seitdem", "selbst", "Selbst", "setzen", "sich", "Sie", "sie", "sind", "so",
            "So", "sogar", "solch", "solche", "solchem", "solchen", "solcher", "solches", "soll", "sollen", "sollte",
            "sollten", "sondern", "sonst", "soviel", "soweit", "sowie", "spät", "sprechen", "stark", "stehen", "steht",
            "stellen", "teilen", "teilte", "über", "um", "und", "Und", "uns", "unser", "unsere", "unserem", "unseren",
            "unserer", "unseres", "unter", "vergangen", "vergangenen", "vergehen", "veröffentlichen", "viel", "viele",
            "vier", "voll", "vom", "von", "Von", "vor", "Vor", "vorsitzen", "währen", "während", "war", "wäre",
            "waren", "wären", "warum", "was", "wegen", "weil", "weit", "weiter", "welche", "welchem", "welchen",
            "welcher", "welches", "wem", "wen", "wenig", "wenige", "wenn", "Wenn", "wer", "werde", "werden", "weshalb",
            "wessen", "wichtig", "wie", "Wie", "wieder", "will", "wir", "Wir", "wird", "wo", "wollen", "womit",
            "worden", "wurde", "wurden", "würden", "zehn", "zeigen", "zentral", "zu", "Zu", "zum", "zur", "zwar",
            "zwei", "zweit", "zwischen", "zwischens" };

    static {
        if (m_Stopwords == null) {
            m_Stopwords = new Hashtable();

            Double dummy = new Double(0);
            for (int i = 0; i < stopWords.length; i++) {
                m_Stopwords.put(stopWords[i], dummy);
            }

        }

    }

    public boolean isStopword(String str) {

        return m_Stopwords.containsKey(str.toLowerCase());
    }

}
