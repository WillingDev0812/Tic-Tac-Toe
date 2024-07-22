package com.iti.tictactoe.models;

public class PlayerNames {
    private final String playerOne;
    private final String playerTwo;

    public PlayerNames(String playerOne, String playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public String getPlayerOne() {
        return playerOne;
    }
}
