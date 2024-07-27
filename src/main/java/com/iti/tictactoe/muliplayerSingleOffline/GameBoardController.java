package com.iti.tictactoe.muliplayerSingleOffline;

import com.iti.tictactoe.models.AlertUtils;
import com.iti.tictactoe.models.PlayerNames;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

import static com.iti.tictactoe.AIGame.SinglePlayerMenuController.flag;


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
    Button record_btn;
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

    String movesRecorded ="";
    boolean isRecording = false;

    private NavigationController navController;

    private boolean isSinglePlayer = false;


    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }



    private final int[][] board = new int[3][3];   // board game
    private PlayerNames playerName;
    private boolean isPlayerOneTurn = true;

    private AudioClip clickXSound;
    private AudioClip clickOSound;
    private AudioClip winnerSound;

    public void initialize(PlayerNames playerName, boolean isSinglePlayer, int difficultyFlag) {
        this.playerName = playerName;
        this.isSinglePlayer = isSinglePlayer;

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

            Image oPlayer = new Image(getClass().getResource("/com/iti/tictactoe/assets/Circle.png").toExternalForm());
            circle.setImage(oPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setPlayerNames();
    }

    private void setPlayerNames() {
        playerOne.setText(playerName.getPlayerOne());
        playerTwo.setText(playerName.getPlayerTwo());
    }

    public void updateDrawCount() {
        drawCounter++;
        drawScore.setText(String.valueOf(drawCounter));
    }

    // Method to update player one score
    public void updatePlayerOneScore() {
        playerOneScoreCount++;
        playerOneScore.setText(String.valueOf(playerOneScoreCount));
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
    private void ExitButton(ActionEvent event) {
        UiUtils.playSoundEffect();
        Optional<ButtonType> result = AlertUtils.showConfirmationAlert("Leave Game", "Are you sure you want to quit playing and leave the game?", "This will moves you to the lobby");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (navController != null) {
                UiUtils.playSoundEffect();
                navController.popScene();
                navController.popScene();
                record_btn.setDisable(false);
                winnerSound.stop();    // to stop sound when quit
            }
        }
    }
    private void handleButtonAction(Button button, int row, int col) {
        if (button.getGraphic() == null && board[row][col] == 0) {
            record_btn.setDisable(true);
        if(isRecording)
            movesRecorded +=row+","+col +"\n";
            updateButtonGraphic(button);
            playClickSound();
            updateBoardStateForPlayers(row, col);
            int[][] coloredButtons = checkWinner();
            if (coloredButtons != null) {
                handleWinnerState(coloredButtons);
            } else if (isBoardFull()) {
                handleDrawState();
            } else {
                isPlayerOneTurn = !isPlayerOneTurn;
                if (!isPlayerOneTurn && isSinglePlayer) {
                    setButtonDisabledToPreventUserAtComputerTurns(true);
                    delayComputerMove();
                } else {
                    setButtonDisabledToPreventUserAtComputerTurns(false);
                }
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
     //   System.out.println("Player " + (isPlayerOneTurn ? "One (X)" : "Two (O)") + " made a move.");
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
        updateScore();
        winnerSound.play();
        // these line to prevent the aler to be popped when someone wins to see the highlighted buttons
        //  showResultAlert(isPlayerOneTurn ? playerName.getPlayerOne() + " wins" : playerName.getPlayerTwo() + " wins");
        String videoPath;
        if(isSinglePlayer && isPlayerOneTurn) {
            videoPath = "/com/iti/tictactoe/Videos/video2.mp4";
        }
        else
            videoPath = "/com/iti/tictactoe/Videos/video4.mp4";

        PauseTransition pause = new PauseTransition(Duration.seconds(0.3));
        pause.setOnFinished(event -> {
            showVideo(videoPath);
        });
        pause.play();
    }

    public void showVideo(String videoPath) {
        Media media = new Media(getClass().getResource(videoPath).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        StackPane root = new StackPane();
        root.getChildren().add(mediaView);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setFullScreen(true); // Make the stage full screen
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disable ESC to exit full-screen
        stage.setScene(scene);
        stage.show();

        mediaPlayer.play();

        // Hide the video after 3 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            mediaPlayer.stop();
            stage.close();
        });
        delay.play();
    }

    private void handleDrawState() {
        System.out.println("el mfrood kda draw");
        updateDrawCount(); // Increment draw counter
        showResultAlert("It's a draw!"); // Display draw message
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
        resetGame();// bat'kd en el game cleared b3d el confirmation
        writingRecordedMoves();
       record_btn.setDisable(false);
        isRecording = false;
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
        UiUtils.playSoundEffect();
        Optional<ButtonType> result = AlertUtils.showConfirmationAlert("Restart Game", "Are you sure you want to restart the game?", "This will clear the current game state.");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            resetGame();
            setButtonDisabledToPreventUserAtComputerTurns(false);
            record_btn.setDisable(false);
            writingRecordedMoves();
            isRecording = false;
        }
    }

    private void setButtonDisabledToPreventUserAtComputerTurns(boolean disabled) {
        button00.setDisable(disabled);
        button01.setDisable(disabled);
        button02.setDisable(disabled);
        button10.setDisable(disabled);
        button11.setDisable(disabled);
        button12.setDisable(disabled);
        button20.setDisable(disabled);
        button21.setDisable(disabled);
        button22.setDisable(disabled);
    }

    private void delayComputerMove() {
        Duration duration = switch (flag) {
            case 1 -> Duration.seconds(0.5);
            case 2 -> Duration.seconds(0.7);
            case 3 -> Duration.seconds(1);
            default -> null;
        };

        PauseTransition delay = new PauseTransition(duration);
        delay.setOnFinished(event -> {
            if (flag == 1) {
                easyMove();
            } else if (flag == 2) {
                medMove();
            } else if (flag == 3) {
                hardMove();
            }
        });
        delay.play();
    }

    private void easyMove() {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (board[row][col] != 0);
        Button button = getButtonAt(row, col);
        assert button != null;
        handleButtonAction(button, row, col);
    }

    private void medMove() {
        Random random = new Random();
        if(random.nextInt(100)<70) //Medium Level : 70% random 30% minimax algorithm
        {
            easyMove();
        }
        else
        {
            minimaxMove();
        }
    }

    private void hardMove()
    {
        minimaxMove();
    }

    private void minimaxMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestCol = -1;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == 0) {
                    board[row][col] = 2; // Computer's move
                    int score = minimax(board, 0, false);
                    board[row][col] = 0; // Undo move
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = row;
                        bestCol = col;
                    }
                }
            }
        }
        if (bestRow != -1 && bestCol != -1) {
            Button button = getButtonAt(bestRow, bestCol);
            assert button != null;
            handleButtonAction(button, bestRow, bestCol);
        }
    }

    private int minimax(int[][] board, int depth, boolean isMaximizing) {
        int[][] winningCombo = checkWinner();
        if (winningCombo != null) {
            if (isMaximizing) {
                return -10 + depth; // If maximizing, minimize the score
            } else {
                return 10 - depth; // If minimizing, maximize the score
            }
        }

        if (isBoardFull()) {
            return 0; // Draw
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board[row][col] == 0) {
                        board[row][col] = 2; // Computer's move
                        int score = minimax(board, depth + 1, false);
                        board[row][col] = 0; // Undo move
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board[row][col] == 0) {
                        board[row][col] = 1; // Player's move
                        int score = minimax(board, depth + 1, true);
                        board[row][col] = 0; // Undo move
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }


    //**************************************************************************************************************/
    // implement functionality ya ahmmed ya gamallllll
    public void handleRecordButton(ActionEvent actionEvent) {
        UiUtils.playSoundEffect();
        record_btn.setDisable(true);
        //remove this 2 lines if u want to change style
       // record_btn.setStyle("-fx-background-color: #ff0000");
       // record_btn.setText("Recording");
        record_btn.getStyleClass().add("record-button-recording");
        isRecording = true;

    }
    public void writingRecordedMoves() {
        if (isRecording) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy' @'HH','mm");
            String timeStamp = dtf.format(LocalDateTime.now());
            try {
                BufferedWriter rec = new BufferedWriter(new FileWriter("src/main/resources/com/iti/tictactoe/Recordings/" + timeStamp + ".txt")); //this will cause a problem
                rec.write(playerName.getPlayerOne() + "," + playerName.getPlayerTwo() + "\n");
                rec.write(movesRecorded);
                rec.close();
                movesRecorded = "";
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            record_btn.getStyleClass().add("record-button");

            //record_btn.setStyle(" -fx-background-color: #0012AF");
            //record_btn.setText("REC");
        }
    }
}
