package com.study.matasano.cryptopals;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Unit test for simple CryptoChallenge.
 */
public class CryptoChallengeTest {

    @Test
    public void problem1set1Test() {
        String input = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
        String actualOutput = CryptoChallenge.hexToBase64(input);
        String expectedOutput = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";
        assertThat(actualOutput, is(equalTo(expectedOutput)));
    }

    @Test
    public void problem2set1Test() {
        String input1 = "1c0111001f010100061a024b53535009181c";
        String input2 = "686974207468652062756c6c277320657965";
        String expectedOutput = "746865206b696420646f6e277420706c6179";
        String actualOutput = CryptoChallenge.xOr(input1, input2);
        assertThat(actualOutput, is(equalTo(expectedOutput)));
    }

    @Test
    public void problem3set1Test() {
        String input = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";
        String expectedOutput = "Cooking MC's like a pound of bacon";
        String actualOutput = CryptoChallenge.xOrWithRepeatedeOneCharKey(input);
        assertThat(actualOutput, is(equalTo(expectedOutput)));
    }

    @Test
    public void problem4set1Test() {
        Path inputFile = Paths.get(".", "4.txt");
        String actualOutput = null;
        try {
            List<String> encodedInputStringList = Files.lines(inputFile)
                    .collect(Collectors.toList());
            String[] encodedInputStrings = encodedInputStringList.toArray(new String[encodedInputStringList.size()]);
            actualOutput = CryptoChallenge.getTopResultOfManyString(encodedInputStrings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String expectedOutput = "Now that the party is jumping\n";
        assertThat(actualOutput, is(equalTo(expectedOutput)));
    }

    @Test
    public void problem5set1Test() {
        String cipher = "Burning 'em, if you ain't quick and nimble\n" +
                "I go crazy when I hear a cymbal";
        String key = "ICE";
        String expectedOutput = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272" +
                "a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f";
        String actualOutput = CryptoChallenge.repetingKeyXor(cipher, key);
        assertThat(actualOutput, is(equalTo(expectedOutput)));
    }

    @Test
    public void problem6set1HammingDistanceTest() {
        String input1 = "this is a test";
        String input2 = "wokka wokka!!!";
        int expectedResult = 37;
        int actualResult = CryptoChallenge.computeHammingDistance(input1, input2);
        assertThat(expectedResult, is(equalTo(actualResult)));
    }
}
