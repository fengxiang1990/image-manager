package com.fxa.image.client.utils;

import java.util.Random;
import java.util.Set;

public class RandomChineseNicknameGenerator {
    private static final int MIN_LENGTH = 2; // 昵称最小长度
    private static final int MAX_LENGTH = 6; // 昵称最大长度

    public static String generateRandomChineseNickname(Set<Character> chineseCharacters) {
        Random random = new Random();
        int length = random.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH;

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chineseCharacters.size());
            char randomChar = (char) (chineseCharacters.toArray()[randomIndex]);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        Set<Character> chineseCharacters = RandomChineseCharacterGenerator.generateRandomChineseCharacters();

        for (int i = 0; i < 10; i++) {
            String randomNickname = generateRandomChineseNickname(chineseCharacters);
            System.out.println("Random Chinese Nickname: " + randomNickname);
        }
    }
}