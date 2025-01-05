package org.example;


public record Jatekos(String name, char symbol, String color) {

    @Override
    public String toString() {
        return "Player{name='" + name + "', symbol='" + symbol + "', color='" + color + "'}";
    }
}