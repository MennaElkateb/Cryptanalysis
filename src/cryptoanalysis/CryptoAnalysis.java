package cryptoanalysis;

import java.util.*;

public class CryptoAnalysis {
    static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final String FREQUENCY_ORDER = "ETAOINSHRDLCUMWFGYPBVKJXQZ";
    static final Set<String> COMMON_WORDS = new HashSet<>(Arrays.asList(
        "THE", "AND", "TO", "OF", "IN", "THAT", "IT", "IS", "WAS", "FOR", "ON", "WITH", "AS", "I", "HE", "BE", "AT", "ONE", "HAVE"
    ));

    public static Map<Character, Double> countFrequencies(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        int totalLetters = 0;
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
                totalLetters++;
            }
        }
        
        Map<Character, Double> frequencyPercentage = new HashMap<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            frequencyPercentage.put(entry.getKey(), (entry.getValue() * 100.0) / totalLetters);
        }
        return frequencyPercentage;
    }

    public static String decrypt(String cipherText, Map<Character, Character> keyMap) {
        StringBuilder plainText = new StringBuilder();
        for (char c : cipherText.toCharArray()) {
            if (Character.isLetter(c)) {
                char mappedChar = keyMap.getOrDefault(Character.toUpperCase(c), c);
                plainText.append(Character.isUpperCase(c) ? mappedChar : Character.toLowerCase(mappedChar));
            } else {
                plainText.append(c);
            }
        }
        return plainText.toString();
    }

    public static int scoreDecryption(String text) {
        int score = 0;
        String[] words = text.split("\\s+");
        for (String word : words) {
            if (COMMON_WORDS.contains(word.toUpperCase())) {
                score += 2;
            }
        }
        return score;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter encrypted text: ");
        String cipherText = scanner.nextLine().toUpperCase();

        Map<Character, Double> freqMap = countFrequencies(cipherText);
        List<Map.Entry<Character, Double>> sortedFrequencies = new ArrayList<>(freqMap.entrySet());
        sortedFrequencies.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));
        
        Map<Character, Character> keyMap = new HashMap<>();
        for (int i = 0; i < sortedFrequencies.size() && i < FREQUENCY_ORDER.length(); i++) {
            keyMap.put(sortedFrequencies.get(i).getKey(), FREQUENCY_ORDER.charAt(i));
        }

        System.out.println("Key Map: " + keyMap);

        String decryptedText = decrypt(cipherText, keyMap);
        int score = scoreDecryption(decryptedText);
        
        System.out.println("\nMost likely decrypted text: " + decryptedText);
        System.out.println("Confidence score: " + score);
        scanner.close();
    }
}
