package org.example;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }


        // мутим потоки
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        List<Future<Integer>> tasks = new ArrayList<>();
        long startTsMulti = System.currentTimeMillis(); // start time

        for (String text : texts) {
            Callable<Integer> searchLogic = () -> ineffectiveAlgoResult(text);
            Future<Integer> task = threadPool.submit(searchLogic);
            tasks.add(task);

        }
        int finalResult = 0;
        for (Future<Integer> task : tasks) {
            Integer resultOfTask = task.get();
            if (resultOfTask > finalResult) {
                finalResult = resultOfTask;
            }

        }
        long endTsMulti = System.currentTimeMillis(); // end time
        System.out.println("Time: " + (endTsMulti - startTsMulti) + "ms");
        System.out.println("Max interval is " + finalResult);
        threadPool.shutdown();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static Integer ineffectiveAlgoResult(String text) {
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
        return maxSize;
    }
}