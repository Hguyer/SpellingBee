import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Hunter Guyer
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        generateHelper("", letters);
    }
    public void generateHelper(String current, String remaining){
        // Add the current string to words if it's not empty
        if(!current.isEmpty()){
            words.add(current);
        }
        // generate all possible permutations by recursively adding each letter
        for (int i = 0; i < remaining.length(); i++) {
            // Add the current character to the new string
            String newCurrent = current + remaining.charAt(i);
            // remove the used character and create a new remaining string
            String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
            // Recursively call generateHelper with the updated strings
            generateHelper(newCurrent, newRemaining);
        }

    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        // mergesort function
        mergeSort(words, 0, words.size() - 1);
    }
    private void mergeSort(ArrayList<String> list, int low, int high) {
        // base case for splitting
        if (low >= high) {
            return;
        }

        int mid = low + (high - low) / 2;
        //recursively split the arrayList
        mergeSort(list, low, mid);
        mergeSort(list, mid + 1, high);

        // now merge
        merge(list, low, mid, high);
    }
    private ArrayList<String> merge(ArrayList<String> list, int low, int mid, int high) {
        // add a temp arrayList for comperisons
        ArrayList<String> temp = new ArrayList<>();
        int left = low;
        int right = mid + 1;

        // compare and merge from both halves
        while (left <= mid && right <= high) {
            // if the left is smaller put it first otherwise add the right
            if (list.get(left).compareTo(list.get(right)) <= 0) {
                temp.add(list.get(left++));
            } else {
                temp.add(list.get(right++));
            }
        }
        // add all of the left remaining
        while (left <= mid) {
            temp.add(list.get(left++));
        }
        // add all of the right
        while (right <= high) {
            temp.add(list.get(right++));
        }
        // copy everything back into list but sorted
        for (int i = 0; i < temp.size(); i++) {
            list.set(low + i, temp.get(i));
        }

        return list;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        // use binary search to check the dictionary for words and if it isn't in it remove it
        for (int i = words.size() - 1; i >= 0; i--) {
            if (!binarySearch(words.get(i))) {
                words.remove(i);
            }
        }
    }
    private boolean binarySearch(String word) {
        int left = 0;
        int right = DICTIONARY.length - 1;

        while (left <= right) {
            // get the middle index
            int mid = left + (right - left) / 2;
            // compear the target word to the word at the middle
            int comparison = word.compareTo(DICTIONARY[mid]);
            // if they are the same return true
            if (comparison == 0) {
                return true;
            }
            // otherwise search the left and right halves
            else if (comparison < 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return false;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
