package com.iti.tictactoe;

import com.iti.tictactoe.models.PlayerNames;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

import java.util.List;
import java.util.Objects;

public class RecordingViewController {
    private NavigationController navController;
    @FXML
    private ImageView gameBackGround;
    @FXML
    private ImageView gameBoard;
    @FXML
    private ImageView circle; // for O player
    @FXML
    private ImageView cross; // for X player
    private final int[][] board = new int[3][3];   // board game
    private AudioClip clickXSound;
    private AudioClip clickOSound;
    private AudioClip winnerSound;
    private PlayerNames playerNames;
    @FXML
    private Label playerOne;
    @FXML
    private Label drawLabel;
    @FXML
    private Label playerTwo;
    private boolean isPlayerOneTurn = true;

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

    private Timeline timeline; // Reference to the Timeline

    boolean isRecording = false;

    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }

    public void initialize(PlayerNames playerNames, List<int[]> moves) {
        isRecording = true;
        this.playerNames = playerNames;
        clickOSound = new AudioClip(getClass().getResource("/com/iti/tictactoe/Sounds/OTone.mp3").toExternalForm());
        clickXSound = new AudioClip(getClass().getResource("/com/iti/tictactoe/Sounds/xTone.mp3").toExternalForm());
        winnerSound = new AudioClip(getClass().getResource("/com/iti/tictactoe/Sounds/win.mp3").toExternalForm());

        try {
            Image backgroundImage = new Image(getClass().getResource("/com/iti/tictactoe/assets/gameBackground.png").toExternalForm());
            gameBackGround.setImage(backgroundImage);

            Image boardImage = new Image(getClass().getResource("/com/iti/tictactoe/assets/gameBoard.png").toExternalForm());
            gameBoard.setImage(boardImage);

            Image xPhoto = new Image(getClass().getResource("/com/iti/tictactoe/assets/Cross.png").toExternalForm());
            cross.setImage(xPhoto);

            Image oPlayer = new Image(Objects.requireNonNull(getClass().getResource("/com/iti/tictactoe/assets/Circle.png")).toExternalForm());
            circle.setImage(oPlayer);

        } catch (Exception e) {
            e.printStackTrace();
        }

        setPlayerOneName();
        setPlayerTwoName();
        playMoves(moves);
    }

    public void setPlayerOneName() {
        playerOne.setText(playerNames.getPlayerOne());
    }

    public void setPlayerTwoName() {
        playerTwo.setText(playerNames.getPlayerTwo());
    }

    public void exitButton(ActionEvent actionEvent) {
        UiUtils.playSoundEffect();
        if (navController != null) {
            UiUtils.playSoundEffect();
            isRecording = false;
            if (timeline != null) {
                timeline.stop();
            }
            navController.popScene();
        }
    }

//    private void stopTimeline() {
//        if (timeline != null) {
//            timeline.stop(); // Stop the timeline
//        }
//    }

//    private void stopAudioClips() {
//        if (clickXSound != null) {
//            clickXSound.stop();
//        }
//        if (clickOSound != null) {
//            clickOSound.stop();
//        }
//        if (winnerSound != null) {
//            winnerSound.stop();
//        }
//    }

    private void updateButtonGraphic(Button button) {
        ImageView image = new ImageView((isPlayerOneTurn ? cross : circle).getImage());
        image.setFitWidth(110);
        image.setFitHeight(110);
        button.setGraphic(image);
    }

    private void viewRecordMoves(Button button, int row, int col) {
        if (button.getGraphic() == null && board[row][col] == 0) {
            updateButtonGraphic(button);
            playClickSound();
            updateBoardStateForPlayers(row, col);
            int[][] coloredButtons = checkWinner();  // retrieve the winning array of buttons
            if (coloredButtons != null) {
                handleWinnerState(coloredButtons);
            } else if (isBoardFull()) {
                handleDrawState();
                System.out.println("Draw");
            } else {
                isPlayerOneTurn = !isPlayerOneTurn;
            }
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

    private void handleDrawState() {
        drawLabel.setOpacity(1.0);
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

    private void updateBoardStateForPlayers(int row, int col) {
        if (isPlayerOneTurn) {
            board[row][col] = 1;    // Player X
        } else {
            board[row][col] = 2;    // Player O
        }
        System.out.println("Player " + (isPlayerOneTurn ? "One (X)" : "Two (O)") + " made a move.");
    }

    private void playClickSound() {
        if (isPlayerOneTurn) {
            clickXSound.play();
        } else {
            clickOSound.play();
        }
    }

    private void handleWinnerState(int[][] coloredButtons) {
        checkHighlightWinningButtons(coloredButtons);
        winnerSound.play();
    }

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

    private void playMoves(List<int[]> moves) {
        timeline = new Timeline(); // Initialize the timeline
        for (int i = 0; i < moves.size(); i++) {
            int[] move = moves.get(i); // moves[0] = {1,1} moves[1] = {2,2} >>>>....
            int row = move[0]; //first number in array
            int col = move[1]; //second number in array
            Button button = getButtonAt(row, col);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i), e -> viewRecordMoves(button, row, col));
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();
    }
}
