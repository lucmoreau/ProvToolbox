package org.openprovenance.prov.rules;

public interface Ansi {
    String RED = "\u001B[31m";
    String GREEN = "\u001B[32m";
    String YELLOW = "\u001B[33m";
    String BLUE = "\u001B[34m";
    String PURPLE = "\u001B[35m";
    String CYAN = "\u001B[36m";
    String WHITE = "\u001B[37m";
    String ORANGE = "\u001B[38;5;208m";
    String RESET = "\u001B[0m";

    default String red(String s) {
        return RED + s + RESET;
    }
    default String green(String s) {
        return GREEN + s + RESET;
    }
    default String yellow(String s) {
        return YELLOW + s + RESET;
    }
    default String blue(String s) {
        return BLUE + s + RESET;
    }
    default String purple(String s) {
        return PURPLE + s + RESET;
    }
    default String cyan(String s) {
        return CYAN + s + RESET;
    }
    default String white(String s) {
        return WHITE + s + RESET;
    }
    default String orange(String s) {
        return ORANGE + s + RESET;
    }
}
