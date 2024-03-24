package com.fxa.image.client.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomChineseCharacterGenerator {
    private static final int TOTAL_CHARACTERS = 5000; // 总共需要生成的汉字数量
    private static final int UNICODE_START = 0x4E00; // 汉字的Unicode起始值
    private static final int UNICODE_RANGE = 0x9FFF - 0x4E00 + 1; // 汉字的Unicode范围

    private static Set<Character> characters = new HashSet<>(TOTAL_CHARACTERS);
    public static Set<Character> generateRandomChineseCharacters() {
        if(!characters.isEmpty()){
            return characters;
        }
        Random random = new Random();

        while (characters.size() < TOTAL_CHARACTERS) {
            int randomCodePoint = UNICODE_START + random.nextInt(UNICODE_RANGE);
            char randomCharacter = (char) randomCodePoint;
            characters.add(randomCharacter);
        }

        return characters;
    }

    public static void main(String[] args) {
        Set<Character> randomCharacters = generateRandomChineseCharacters();
        StringBuilder sb = new StringBuilder();

        for (char character : randomCharacters) {
            sb.append(character);
        }

        System.out.println(sb.toString());
        System.out.println("Total unique characters: " + randomCharacters.size());
    }
}