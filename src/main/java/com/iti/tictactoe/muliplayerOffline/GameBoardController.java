package com.iti.tictactoe.muliplayerOffline;

import com.iti.tictactoe.muliplayerOffline.models.AlertUtils;
import com.iti.tictactoe.muliplayerOffline.models.PlayerNames;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

import java.util.Optional;

public class GameBoardController {
    @FXML
    private ImageView gameBackGround;
    @FXML
    private ImageView gameBoard;

    @FXML
    private Label drawScore;
    private int drawCounter = 0;

    @FXML
    private Label playerOne;
    @FXML
    private ImageView cross; // for X player
    @FXML
    private Label playerOneScore;
    private int playerOneScoreCount = 0;

    @FXML
    private Label playerTwo;
    @FXML
    private ImageView circle; // for O player
    @FXML
    private Label playerTwoScore;
    private int playerTwoScoreCount = 0;

    @FXML
    private Button button00;
    @FXML
    private Button button01;
    @FXML
    private Button button02;
    @FXML
    private Button button10;
    @FXML
    private Button button11;
    @FXML
    private Button button12;
    @FXML
    private Button button20;
    @FXML
    private Button button21;
    @FXML
    private Button button22;
    @FXML
    private Button exit_btn;
    @FXML
    private Button restart_btn;

    private final int[][] board = new int[3][3];   // board game
    private PlayerNames playerNames;
    private boolean isPlayerOneTurn = true;

    private AudioClip clickXSound;
    private AudioClip clickOSound;
    private AudioClip winnerSound;
    private AudioClip buttonSound;

    public void initialize(PlayerNames playerNames) {
        this.playerNames = playerNames;
        clickOSound = new AudioClip(getClass().getResource("/com/iti/tictactoe/Sounds/OTone.mp3").toExternalForm());
        clickXSound = new AudioClip(getClass().getResource("/com/iti/tictactoe/Sounds/xTone.mp3").toExternalForm());
        winnerSound = new AudioClip(getClass().getResource("/com/iti/tictactoe/Sounds/win.mp3").toExternalForm());
        buttonSound = new AudioClip(getClass().getResource("/com/iti/tictactoe/Sounds/buttonSoundEffect.wav").toExternalForm());

        try {
            Image backgroundImage = new Image(getClass().getResource("/com/iti/tictactoe/assets/gameBackground.png").toExternalForm());
            gameBackGround.setImage(backgroundImage);

            Image boardImage = new Image(getClass().getResource("/com/iti/tictactoe/assets/gameBoard.png").toExternalForm());
            gameBoard.setImage(boardImage);

            Image xPhoto = new Image(getClass().getResource("/com/iti/tictactoe/assets/Cross.png").toExternalForm());
            cross.setImage(xPhoto);

            Image oPlayer = new Image(getClass().getResource("/com/iti/tictactoe/assets/Circle.png").toExternalForm());
            circle.setImage(oPlayer);

        } catch (Exception e) {
            e.printStackTrace();
        }

        setPlayerOneName();
        setPlayerTwoName();
    }

    public void updateDrawCount() {
        drawCounter++;
        drawScore.setText(String.valueOf(drawCounter));
    }

    // Method to set player name
    public void setPlayerOneName() {
        playerOne.setText(playerNames.getPlayerOne());
    }

    // Method to update player one score
    public void updatePlayerOneScore() {
        playerOneScoreCount++;
        playerOneScore.setText(String.valueOf(playerOneScoreCount));
    }

    //Method to set the second player name
    public void setPlayerTwoName() {
        playerTwo.setText(playerNames.getPlayerTwo());
    }

    //Method to update player two score
    public void updatePlayerTwoScore() {
        playerTwoScoreCount++;
        playerTwoScore.setText(String.valueOf(playerTwoScoreCount));
    }

    @FXML
    private void handleButton00Action(ActionEvent event) {
        handleButtonAction(button00, 0, 0);
    }

    public void handleButton01Action(ActionEvent actionEvent) {
        handleButtonAction(button01, 0, 1);
    }

    public void handleButton02Action(ActionEvent actionEvent) {
        handleButtonAction(button02, 0, 2);
    }

    public void handleButton10Action(ActionEvent actionEvent) {
        handleButtonAction(button10, 1, 0);
    }

    public void handleButton11Action(ActionEvent actionEvent) {
        handleButtonAction(button11, 1, 1);
    }

    public void handleButton12Action(ActionEvent actionEvent) {
        handleButtonAction(button12, 1, 2);
    }

    public void handleButton20Action(ActionEvent actionEvent) {
        handleButtonAction(button20, 2, 0);
    }

    public void handleButton21Action(ActionEvent actionEvent) {
        handleButtonAction(button21, 2, 1);
    }

    public void handleButton22Action(ActionEvent actionEvent) {
        handleButtonAction(button22, 2, 2);
    }

    @FXML
    private void handleExitButton(ActionEvent event) {
        System.out.println("Quit button clicked");
    }

    private void handleButtonAction(Button button, int row, int col) {
        if (button.getGraphic() == null && board[row][col] == 0) {
            updateButtonGraphic(button);
            playClickSound();
            updateBoardStateForPlayers(row, col);
            int[][] coloredButtons = checkWinner();  // retrieve the winning array of buttons
            if (coloredButtons != null) {
                handleWinnerState(coloredButtons);
            } else if (isBoardFull()) {
                handleDrawState();
            } else {
                isPlayerOneTurn = !isPlayerOneTurn;
            }
        }
    }

    private void playClickSound() {
        if (isPlayerOneTurn) {
            clickXSound.play();
        } else {
            clickOSound.play();
        }
    }

    private void updateButtonGraphic(Button button) {
        ImageView image = new ImageView((isPlayerOneTurn ? cross : circle).getImage());
        image.setFitWidth(110);
        image.setFitHeight(110);
        button.setGraphic(image);
    }

    private void updateBoardStateForPlayers(int row, int col) {
        if (isPlayerOneTurn) {
            board[row][col] = 1;    // Player X
        } else {
            board[row][col] = 2;    // Player O
        }

       // System.out.println("Player " + (isPlayerOneTurn ? "One (X)" : "Two (O)") + " made a move.");
    }

    private int[][] checkWinner() {
        //checking rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return new int[][]{{i, 0}, {i, 1}, {i, 2}};
            }
        }
        // checking columns
        for (int i = 0; i < 3; i++) {
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return new int[][]{{0, i}, {1, i}, {2, i}};
            }
        }
        //checking first diagonal
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return new int[][]{{0, 0}, {1, 1}, {2, 2}};
        }
        // checking last diagonal
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return new int[][]{{0, 2}, {1, 1}, {2, 0}};
        }
        return null;    //return null if draw
    }

    private void handleWinnerState(int[][] coloredButtons) {
        checkHighlightWinningButtons(coloredButtons);
      //  System.out.println("feee 7ad kesb " + (isPlayerOneTurn ? "Player One (X)" : "Player Two (O)"));
        updateScore();
        winnerSound.play();
        showResultAlert(isPlayerOneTurn ? playerNames.getPlayerOne() + " wins" : playerNames.getPlayerTwo() + " wins");
    }

    private void handleDrawState() {
        System.out.println("el mfrood kda draw");
        updateDrawCount(); // Increment draw counter
        showResultAlert("It's a draw!"); // Display draw message
    }

  /*  private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            // Check rows
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
            // Check columns
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return true;
            }
        }
        // Check diagonals
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            updateScore();
            return true;
        }
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            updateScore();
            return true;
        }
        return false;
    }*/

    private void checkHighlightWinningButtons(int[][] winningButtons) {
        for (int i = 0; i < winningButtons.length; i++) {
            int row = winningButtons[i][0];
            int col = winningButtons[i][1];
            // getting row ,col indices of the button to get colored later
            Button button = getButtonAt(row, col);
            if (isPlayerOneTurn) {
                button.getStyleClass().add("winning-button-x");
            } else {
                button.getStyleClass().add("winning-button-o");
            }
        }
    }

    private Button getButtonAt(int row, int col) {
        if (row == 0 & col == 0) return button00;
        if (row == 0 & col == 1) return button01;
        if (row == 0 & col == 2) return button02;
        if (row == 1 & col == 0) return button10;
        if (row == 1 & col == 1) return button11;
        if (row == 1 & col == 2) return button12;
        if (row == 2 & col == 0) return button20;
        if (row == 2 & col == 1) return button21;
        if (row == 2 & col == 2) return button22;
        return null;
    }

    private void updateScore() {
        if (isPlayerOneTurn) {
            updatePlayerOneScore();
        } else {
            updatePlayerTwoScore();
        }
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showResultAlert(String message) {
        AlertUtils.showInformationAlert("Game Over", message, null);
        resetGame(); // bat'kd en el game cleared b3d el confirmation
    }

    private void resetGame() {
        Button[] buttons = {button00, button01, button02,
                button10, button11, button12,
                button20, button21, button22};
        for (int i = 0; i < buttons.length; i++) {
            resetButtonToItsOriginalState(buttons[i]);
        }
        isPlayerOneTurn = true;  // Reset to player one's turn
        // Reset the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }
        winnerSound.stop();

    }

    private void resetButtonToItsOriginalState(Button button) {
        button.setGraphic(null);
        button.getStyleClass().removeAll("winning-button-x", "winning-button-o");
    }

    public void handleRestartButton(ActionEvent actionEvent) {
        buttonSound.play();
        Optional<ButtonType> result = AlertUtils.showConfirmationAlert("Restart Game", "Are you sure you want to restart the game?", "This will clear the current game state.");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            resetGame();
        }
    }
}
