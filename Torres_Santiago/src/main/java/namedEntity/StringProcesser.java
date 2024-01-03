package namedEntity;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StringProcesser implements Serializable {

    private String str;

    public List<String> filterCandidates(String input) {
        List<String> fullNames = new ArrayList<>();

        // Titulos Dr. Prof. Engr. Eng. Ms. Mr. Esq.i
        String titleRegex = "\\b([A-Z][a-z]{1,3}\\.)\\s+([A-Z][a-zA-Z]+\\s*){1,4}\\b";
        Pattern titlePattern = Pattern.compile(titleRegex);
        Matcher titleMatcher = titlePattern.matcher(input);

        while (titleMatcher.find()) {
            String fullName = titleMatcher.group();
            String[] oneNames = fullName.split(" ");
            for (String word : oneNames) {
                word = word.trim();
                fullNames.add(word);
            }

        }

        input = titleMatcher.replaceAll("");

        // Calles
        String streetRegex = "\\b\\d+\\s[A-Z][a-zA-Z]+\\s+[A-Z][a-zA-Z]+\\b";
        Pattern streetPattern = Pattern.compile(streetRegex);
        Matcher streetMatcher = streetPattern.matcher(input);

        while (streetMatcher.find()) {
            String fullName = streetMatcher.group();
            String[] oneNames = fullName.split(" ");
            for (String word : oneNames) {
                word = word.trim();
                fullNames.add(word);
            }
        }

        input = streetMatcher.replaceAll("");

        // Fecha
        String dateRegex = "\\b([A-Z][a-z]{2}\\s+){2}\\d{2}\\b";
        Pattern datePattern = Pattern.compile(dateRegex);
        Matcher dateMatcher = datePattern.matcher(input);

        while (dateMatcher.find()) {
            String fullName = dateMatcher.group();
            String[] oneNames = fullName.split(" ");
            for (String word : oneNames) {
                word = word.trim();
                fullNames.add(word);
            }
        }

        input = dateMatcher.replaceAll("");

        // Name Fullname
        String nameRegex = "\\b([A-Z][a-z]{2,}+[^'’-]){1,2}\\b";
        Pattern namePattern = Pattern.compile(nameRegex);
        Matcher nameMatcher = namePattern.matcher(input);

        while (nameMatcher.find()) {
            String fullName = nameMatcher.group();
            String[] oneNames = fullName.split(" ");
            for (String word : oneNames) {
                word = word.trim();
                fullNames.add(word);
            }
        }

        input = nameMatcher.replaceAll("");

        String placeRegex = "\\b[A-Z][a-z]+,\\s[A-Z][a-z]+\\b";
        Pattern placePattern = Pattern.compile(placeRegex);
        Matcher placeMatcher = placePattern.matcher(input);

        while (placeMatcher.find()) {
            String fullName = placeMatcher.group();
            String[] oneNames = fullName.split(" ");
            for (String word : oneNames) {
                word = word.trim();
                fullNames.add(word);
            }
        }

        input = placeMatcher.replaceAll("");

        return fullNames;
    }

    private void removeLinks() {
        // Expresion regular pattern para matchear URLs
        String regex = ", link=(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regex);

        // Borrar URLs del string
        Matcher matcher = pattern.matcher(this.str);
        this.str = matcher.replaceAll("");
    }

    private void removeTags() {
        // Expresion regular pattern para matchear Tags
        String regex = "[-a-zA-Z]*[-a-zA-Z]=";
        Pattern pattern = Pattern.compile(regex);

        // Borrar Tags del string
        Matcher matcher = pattern.matcher(this.str);
        this.str = matcher.replaceAll("");
    }

    private void removeTrash() {
        // Expresion regular pattern para matchear Tags
        String regex = "(Feed  |, Article|  Article)";
        Pattern pattern = Pattern.compile(regex);

        // Borrar Tags del string
        Matcher matcher = pattern.matcher(this.str);
        this.str = matcher.replaceAll("");
    }

    public StringProcesser(String str) {
        this.str = str;
    }

    public void setString(String str) {
        this.str = str;
    }

    public String getString() {
        return this.str;
    }

    public void processString() {

        this.str = this.str.replace('[', ' ');
        this.str = this.str.replace(']', ' ');

        this.removeLinks();
        this.removeTags();
        this.removeTrash();

        String charsToRemove = ".,;:()'!?\n";
        for (char c : charsToRemove.toCharArray()) {
            this.str = this.str.replace(String.valueOf(c), "");
        }
    }
}
