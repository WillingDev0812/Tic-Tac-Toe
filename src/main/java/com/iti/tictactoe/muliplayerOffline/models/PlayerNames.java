package com.iti.tictactoe.muliplayerOffline.models;

public class PlayerNames {
    private String playerOne;
    private String playerTwo;

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
