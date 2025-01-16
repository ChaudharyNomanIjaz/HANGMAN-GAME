import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Hangman {
    private String wordToGuess;
    private StringBuilder guessedWord;
    private int attemptsLeft;
    private String guessedLetters;
    private final int maxAttempts;

    public Hangman(String wordToGuess, int maxAttempts) {
        this.wordToGuess = wordToGuess.toLowerCase();
        this.guessedWord = new StringBuilder("_".repeat(wordToGuess.length()));
        this.maxAttempts = maxAttempts;
        this.attemptsLeft = maxAttempts;
        this.guessedLetters = "";
    }

    public void displayWord() {
        System.out.println("Word: " + guessedWord);
    }

    public boolean guessLetter(char letter) {
        letter = Character.toLowerCase(letter);
        if (guessedLetters.indexOf(letter) != -1) {
            System.out.println("You already guessed the letter '" + letter + "'.");
            return false;
        }

        guessedLetters += letter;

        if (wordToGuess.indexOf(letter) != -1) {
            for (int i = 0; i < wordToGuess.length(); i++) {
                if (wordToGuess.charAt(i) == letter) {
                    guessedWord.setCharAt(i, letter);
                }
            }
            return true;
        } else {
            attemptsLeft--;
            return false;
        }
    }

    public boolean isWordGuessed() {
        return guessedWord.toString().equals(wordToGuess);
    }

    public boolean isGameOver() {
        return attemptsLeft <= 0;
    }

    public void displayAttemptsLeft() {
        System.out.println("Attempts left: " + attemptsLeft);
    }

    public void displayGuessedLetters() {
        System.out.println("Guessed letters: " + guessedLetters);
    }

    public void drawStickman() {
        int mistakes = maxAttempts - attemptsLeft;

        System.out.println("  _______");
        System.out.println("  |     |");
        System.out.println("  |     " + (mistakes > 0 ? "O" : ""));
        System.out.println("  |    " + (mistakes > 2 ? "/" : " ") + (mistakes > 1 ? "|" : "") + (mistakes > 3 ? "\\" : ""));
        System.out.println("  |    " + (mistakes > 4 ? "/" : "") + " " + (mistakes > 5 ? "\\" : ""));
        System.out.println("  |");
        System.out.println("__|__");
    }
}

public class HangmanGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.println("Welcome to Hangman!");

        // Print the current working directory to debug file issues
        System.out.println("Current working directory: " + new File(".").getAbsolutePath());

        // Load words from the file
        List<String> words = loadWordsFromFile("src/words.txt");
        if (words == null || words.isEmpty()) {
            System.out.println("No words found in the file. Exiting.");
            return;
        }

        // Pick a random word
        String wordToGuess = words.get(random.nextInt(words.size()));

        Hangman game = new Hangman(wordToGuess, 6);

        while (!game.isGameOver() && !game.isWordGuessed()) {
            game.drawStickman();
            game.displayWord();
            game.displayAttemptsLeft();
            game.displayGuessedLetters();

            System.out.print("Enter a letter: ");
            char letter = scanner.nextLine().charAt(0);

            if (game.guessLetter(letter)) {
                System.out.println("Correct guess!");
            } else {
                System.out.println("Wrong guess!");
            }
        }

        game.drawStickman();

        if (game.isWordGuessed()) {
            System.out.println("Congratulations! You guessed the word: " + wordToGuess);
        } else {
            System.out.println("Game over! The word was: " + wordToGuess);
        }

        scanner.close();
    }

    // Method to load words from a file
    private static List<String> loadWordsFromFile(String fileName) {
        List<String> words = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    words.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return words;
    }
}