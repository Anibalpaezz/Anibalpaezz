package es.uned.lsi.eped.pract2024_2025;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TestGenerator {

    private static final String[] OPERATIONS = {"add", "delete", "move", "execute", "discard"};
    private static final int START_DATE = 20240101;
    private static final int END_DATE = 20251231;
    private static final Random rand = new Random();

    public static void main(String[] args) throws IOException {
        generateTest("test_10k.txt", 10_000);
        generateTest("test_100k.txt", 100_000);
        generateTest("test_500k.txt", 500_000);
        generateTest("test_1M.txt", 1_000_000);
    }

    private static void generateTest(String fileName, int operationsCount) throws IOException {
        Set<Integer> usedDates = new HashSet<>();
        List<Integer> existingDates = new ArrayList<>();
        try (FileWriter writer = new FileWriter(fileName)) {
            for (int i = 0; i < operationsCount; i++) {
                String op = OPERATIONS[rand.nextInt(OPERATIONS.length)];

                switch (op) {
                    case "add": {
                        int date;
                        do {
                            date = randomDate();
                        } while (usedDates.contains(date));
                        usedDates.add(date);
                        existingDates.add(date);
                        writer.write("add Tarea_" + i + " " + date + "\n");
                        break;
                    }
                    case "delete": {
                        if (existingDates.isEmpty()) continue;
                        int date = randomFromList(existingDates);
                        usedDates.remove(date);
                        existingDates.remove((Integer) date);
                        writer.write("delete " + date + "\n");
                        break;
                    }
                    case "move": {
                        if (existingDates.size() < 1) continue;
                        int origDate = randomFromList(existingDates);
                        int newDate;
                        do {
                            newDate = randomDate();
                        } while (usedDates.contains(newDate));
                        usedDates.remove(origDate);
                        existingDates.remove((Integer) origDate);
                        usedDates.add(newDate);
                        existingDates.add(newDate);
                        writer.write("move " + origDate + " " + newDate + "\n");
                        break;
                    }
                    case "execute":
                    case "discard": {
                        writer.write(op + "\n");
                        break;
                    }
                }
            }
        }
        System.out.println("Fichero generado: " + fileName);
    }

    private static int randomDate() {
        int year = 2024 + rand.nextInt(2); // 2024–2025
        int month = 1 + rand.nextInt(12);
        int day = 1 + rand.nextInt(28); // Evitamos problemas con días inválidos
        return year * 10000 + month * 100 + day;
    }

    private static int randomFromList(List<Integer> list) {
        return list.get(rand.nextInt(list.size()));
    }
}
