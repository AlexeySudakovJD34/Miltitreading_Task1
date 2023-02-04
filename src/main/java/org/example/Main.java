package org.example;

import java.util.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis(); // start time
        for (String text : texts) {
            ineffectiveAlgo(text);
        }
        long endTs = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (endTs - startTs) + "ms");

        // мутим потоки
        List<Thread> threads = new ArrayList<>();
        long startTsMulti = System.currentTimeMillis(); // start time

        for (String text : texts) {
            Runnable searchLogic = () -> ineffectiveAlgo(text);
            Thread thread = new Thread(searchLogic);
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join(); // зависаем, ждём когда поток объект которого лежит в thread завершится
        }
        long endTsMulti = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (endTsMulti - startTsMulti) + "ms");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void ineffectiveAlgo(String text) {
        int maxSize = 0;
        int iStart = 0; // добавлено
        for (int i = 0; i < text.length(); i++) {
            for (int j = 0; j < text.length(); j++) {
                if (i >= j) {
                    continue;
                }
                boolean bFound = false;
                for (int k = i; k < j; k++) {
                    if (text.charAt(k) == 'b') {
                        bFound = true;
                        break;
                    }
                }
                if (!bFound && maxSize < j - i) {
                    maxSize = j - i;
                    iStart = i; // добавлено
                }
            }
        }
        System.out.println(text.substring(0, 100) + "... -> " + maxSize + " начиная с " + iStart); // дополнено
    }
}