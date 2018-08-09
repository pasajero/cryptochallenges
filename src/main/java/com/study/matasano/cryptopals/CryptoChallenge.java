package com.study.matasano.cryptopals;


import com.sun.org.apache.bcel.internal.classfile.Code;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;

import java.util.*;

public class CryptoChallenge {

    // Problem 1
    public static String hexToBase64(String hexString) {
        byte[] raw = Hex.decode(hexString);
        return Base64.encodeToString(raw);
    }

    public static String base64ToHex(String base64) {
        byte[] raw = Base64.decode(base64);
        return Hex.encodeToString(raw);
    }

    public static String xOr(String hex1, String hex2) {
        byte[] raw1 = Hex.decode(hex1);
        byte[] raw2 = Hex.decode(hex2);
        byte[] xored = xOr(raw1, raw2);
        return Hex.encodeToString(xored);
    }

    private static byte[] xOr(byte[] raw1, byte[] raw2) {
        byte[] longest = raw1;
        byte[] shortest = raw2;
        if (shortest.length > longest.length) {
            shortest = raw1;
            longest = raw2;
        }
        byte[] res = new byte[longest.length];
        for (int i = 0; i < longest.length; i++) {
            res[i] = (byte) (longest[i] ^ shortest[i % shortest.length]);
        }
        return res;
    }

    private static Map.Entry xOrWithRepeatedeOneCharKeyTopScoredEntry(String input) {
        byte[] rawByteInput = Hex.decode(input);
        Map<Character, Integer> scores = new HashMap<>();
        for (char i = 0; i < 255; i++) {
            byte[] keyAsArray = new byte[]{(byte) i};
            byte[] guessHex = xOr(rawByteInput, keyAsArray);
            int score = qualifyGuess(guessHex);
            scores.putIfAbsent(i, score);
        }
        return getTopResult(scores);
    }

    public static String xOrWithRepeatedeOneCharKey(String input) {
        byte[] rawByteInput = Hex.decode(input);
        Map.Entry mostPromisingKey = xOrWithRepeatedeOneCharKeyTopScoredEntry(input);
        byte[] mostPromisingDecode = xOr(rawByteInput, new byte[]{(byte) (char) mostPromisingKey.getKey()});
        return CodecSupport.toString(mostPromisingDecode);
    }

    private static Map.Entry getTopResult(Map<Character, Integer> scoresMap) {
        Map.Entry<Character, Integer> maxScoreEntry = scoresMap.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue)).get();
//        System.out.printf("Key: %c, it the highest with score: %d\n", maxScoreEntry.getKey(), maxScoreEntry.getValue());
        return maxScoreEntry;
    }

    private static int qualifyGuess(byte[] guessHex) {
        int validEnglishChars = 0;
        for (byte b : guessHex) {
            if (isValidEnglishChar(b)) validEnglishChars++;
        }
        return validEnglishChars;
    }

    private static boolean isValidEnglishChar(byte ch) {
        return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
                || ch == ' ' || ch == '-' || ch == '\'' || ch == '\n' || ch == '/' || ch == ','
                || ch == '.' || ch == '?' || ch == '!';
    }

    public static String getTopResultOfManyString(String[] encodedStrings) {
        List<Map.Entry<Character, Integer>> topResults = new ArrayList<>(encodedStrings.length);
        for (String s : encodedStrings) {
            topResults.add(xOrWithRepeatedeOneCharKeyTopScoredEntry(s));
        }
        Map.Entry<Character, Integer> topScored = getTopScoredEntryFromMany(topResults);
        byte[] keyAsArray = new byte[]{(byte) ((char) topScored.getKey())};
        byte[] bytesRes = xOr(Hex.decode(encodedStrings[topScored.getValue()]), keyAsArray);
        return CodecSupport.toString(bytesRes);
    }

    private static Map.Entry<Character, Integer> getTopScoredEntryFromMany(
            List<Map.Entry<Character, Integer>> topResults) {
        Map.Entry<Character, Integer> maxScoreEntry = topResults.stream()
                .max(Comparator.comparingInt(Map.Entry::getValue)).get();
        int index = topResults.indexOf(maxScoreEntry);
        maxScoreEntry.setValue(index);
        return maxScoreEntry;
    }

    public static String repetingKeyXor(String cipher, String key) {
        byte[] cipherraw = CodecSupport.toBytes(cipher);
        byte[] keyraw = CodecSupport.toBytes(key);
        byte[] xored = xOr(cipherraw, keyraw);
        return Hex.encodeToString(xored);
    }

    public static int computeHammingDistance(String input1, String input2) {
        byte[] rawWord1 = CodecSupport.toBytes(input1);
        byte[] rawWord2 = CodecSupport.toBytes(input2);
        return getHammingDistance(rawWord1, rawWord2);
    }

    private static int getHammingDistance(byte[] rawWord1, byte[] rawWord2) {
        int minLength = Math.min(rawWord1.length, rawWord2.length);
        int maxLength = Math.max(rawWord1.length, rawWord2.length);

        int distance = 0;
        for (int i = 0; i < minLength; i++) {
            int xor = rawWord1[i] ^ rawWord2[i];
            distance += Integer.bitCount(xor);
        }

        distance += 8 * (maxLength - minLength);

        return distance;
    }

    public double normalizedDistance(byte[] first, byte[] second) {
        int distance = getHammingDistance(first, second);
        int maxLength = Math.max(first.length, second.length);

        return (double)distance/(double)maxLength;
    }

    public static void main(String[] args) {

    }
}
