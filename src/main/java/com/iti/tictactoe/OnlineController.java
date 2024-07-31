package com.iti.tictactoe;

import com.google.gson.JsonObject;
import com.iti.tictactoe.models.AlertUtils;
import com.iti.tictactoe.models.PlayerNames;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.iti.tictactoe.ListOfUsers.currentUserEmail;
import static com.iti.tictactoe.ServerListener.message;

public class OnlineController {
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
    private Button record_btn;
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

    String movesRecorded = "";
    boolean isRecording = false;
    private NavigationController navController;
    private boolean isSinglePlayer = false;
    SocketManager socketManager = SocketManager.getInstance();
    public void setNavController(NavigationController navController) {
        this.navController = navController;
    }
    private final int[][] board = new int[3][3];   // board game
    private PlayerNames playerName;
    public static boolean isPlayerOneTurn ;
    private AudioClip clickXSound;
    private AudioClip clickOSound;
    private AudioClip winnerSound;

    public void initialize(PlayerNames playerName, int score1 ,int score2) {
if(!isPlayerOneTurn)
    setButtonDisabledToPreventUserAtComputerTurns(true);

        this.playerName = playerName;
        playerOneScore.setText(String.valueOf(score1));
        playerTwoScore.setText(String.valueOf(score2));
        playerOneScoreCount = score1;
        playerTwoScoreCount = score2;
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
        Thread refreshThread = new Thread(() -> {
            while (true) { // Keep the thread running
                try {
                    // Check if a message indicating a player move has been received
                    if (message != null && message.startsWith("PlayerMoved")) {
                        String[] parts = message.split("\\s+");
                        int row = Integer.parseInt(parts[1].substring(0, 1));
                        int col = Integer.parseInt(parts[1].substring(1));
                        Platform.runLater(() -> {
                            try {
                                Button button = getButtonAt(row, col);
                                if (button != null) {
                                    handleButtonAction(button, row, col);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        message=null;
                        setButtonDisabledToPreventUserAtComputerTurns(false);
                    }
                    Thread.sleep(1000); // Adjust sleep time as needed
                } catch (InterruptedException e) {
                    // Handle interruption
                    System.out.println("Thread interrupted: " + e.getMessage());
                }
            }
        });
        refreshThread.setDaemon(true); // Allow thread to be terminated with the application
        refreshThread.start();
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
        try {
            handleButtonAction(button00, 0, 0);
            JsonObject move = new JsonObject();
            move.addProperty("action", "PlayerMove");
            move.addProperty("player", playerTwo.getText());
            move.addProperty("move",  0+""+0);
            System.out.println("Sending moveeeeeeeee    " + move);
            socketManager.sendJson(move);
            setButtonDisabledToPreventUserAtComputerTurns(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleButton01Action(ActionEvent actionEvent){
        try {
            handleButtonAction(button01, 0, 1);
            JsonObject move = new JsonObject();
            move.addProperty("action", "PlayerMove");
            move.addProperty("player", playerTwo.getText());
            move.addProperty("move",  0+""+1);
            System.out.println("Sending moveeeeeeeee    " + move);
            socketManager.sendJson(move);
            setButtonDisabledToPreventUserAtComputerTurns(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleButton02Action(ActionEvent actionEvent) {
        try {
            handleButtonAction(button02, 0, 2);
            JsonObject move = new JsonObject();
            move.addProperty("action", "PlayerMove");
            move.addProperty("player", playerTwo.getText());
            move.addProperty("move",  0+""+2);
            System.out.println("Sending moveeeeeeeee    " + move);
            setButtonDisabledToPreventUserAtComputerTurns(true);
            socketManager.sendJson(move);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleButton10Action(ActionEvent actionEvent) throws IOException {
        handleButtonAction(button10, 1, 0);
        JsonObject move = new JsonObject();
        move.addProperty("action", "PlayerMove");
        move.addProperty("player", playerTwo.getText());
        move.addProperty("move",  1+""+0);
        System.out.println("Sending moveeeeeeeee    " + move);
        socketManager.sendJson(move);
        setButtonDisabledToPreventUserAtComputerTurns(true);
    }

    public void handleButton11Action(ActionEvent actionEvent) throws IOException {
        handleButtonAction(button11, 1, 1);
        JsonObject move = new JsonObject();
        move.addProperty("action", "PlayerMove");
        move.addProperty("player", playerTwo.getText());
        move.addProperty("move",  1+""+1);
        System.out.println("Sending moveeeeeeeee    " + move);
        socketManager.sendJson(move);
        setButtonDisabledToPreventUserAtComputerTurns(true);
    }

    public void handleButton12Action(ActionEvent actionEvent) throws IOException {
        handleButtonAction(button12, 1, 2);
        JsonObject move = new JsonObject();
        move.addProperty("action", "PlayerMove");
        move.addProperty("player", playerTwo.getText());
        move.addProperty("move",  1+""+2);
        System.out.println("Sending moveeeeeeeee    " + move);
        socketManager.sendJson(move);
        setButtonDisabledToPreventUserAtComputerTurns(true);
    }

    public void handleButton20Action(ActionEvent actionEvent) throws IOException {
        handleButtonAction(button20, 2, 0);
        JsonObject move = new JsonObject();
        move.addProperty("action", "PlayerMove");
        move.addProperty("player", playerTwo.getText());
        move.addProperty("move",  2+""+0);
        System.out.println("Sending moveeeeeeeee    " + move);
        socketManager.sendJson(move);
        setButtonDisabledToPreventUserAtComputerTurns(true);
    }

    public void handleButton21Action(ActionEvent actionEvent) throws IOException {
        handleButtonAction(button21, 2, 1);
        JsonObject move = new JsonObject();
        move.addProperty("action", "PlayerMove");
        move.addProperty("player", playerTwo.getText());
        move.addProperty("move",  2+""+1);
        System.out.println("Sending moveeeeeeeee    " + move);
        socketManager.sendJson(move);
        setButtonDisabledToPreventUserAtComputerTurns(true);
    }

    public void handleButton22Action(ActionEvent actionEvent) throws IOException {
        handleButtonAction(button22, 2, 2);
        JsonObject move = new JsonObject();
        move.addProperty("action", "PlayerMove");
        move.addProperty("player", playerTwo.getText());
        move.addProperty("move",  2+""+2);
        System.out.println("Sending moveeeeeeeee    " + move);
        socketManager.sendJson(move);
        setButtonDisabledToPreventUserAtComputerTurns(true);
    }

    @FXML
    private void ExitButton(ActionEvent event) {
        UiUtils.playSoundEffect();
        Optional<ButtonType> result = AlertUtils.showConfirmationAlert("Leave Game",
                "Are you sure you want to quit playing and leave the game?",
                "This will move you to lose");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            sendExitGameRequest();

            // Handle UI updates and other actions

            handleExitAction();
        }
    }

    private void handleExitAction() {
        winnerSound.stop();

        // Re-enable the record button
        record_btn.setDisable(false);

        // Write recorded moves if recording is active
        writingRecordedMoves();

        // Set isRecording to false
        isRecording = false;

        // Navigate back to the previous scene
        if (navController != null) {
            UiUtils.playSoundEffect(); // Optional: play sound effect on navigation
            navController.popScene();
        }
    }

    private void sendExitGameRequest() {
        // Construct the exit game JSON request
        String userEmail = getUserEmail(); // Ensure this method retrieves the user's email correctly
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("action", "exitgame");
        jsonRequest.addProperty("email", userEmail);

        // Send the JSON request to the server
        SocketManager socketManager = SocketManager.getInstance();
        socketManager.connectCheck(); // Ensure connection is checked
        PrintWriter out = socketManager.getPrintWriter();

        if (out != null) {
            out.println(jsonRequest.toString());
            out.flush();
        } else {
            System.err.println("PrintWriter is not initialized.");
        }
    }

    private String getUserEmail() {
        return currentUserEmail;
    }

    private void handleButtonAction(Button button, int row, int col) throws IOException {
        if (button.getGraphic() == null && board[row][col] == 0) {
            record_btn.setDisable(true);
            if (isRecording)
                movesRecorded += row + "," + col + "\n";
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

    private void handleWinnerState(int[][] coloredButtons) throws IOException {
        checkHighlightWinningButtons(coloredButtons);
        updateScore();
        winnerSound.play();
        // these line to prevent the aler to be popped when someone wins to see the highlighted buttons
        //  showResultAlert(isPlayerOneTurn ? playerName.getPlayerOne() + " wins" : playerName.getPlayerTwo() + " wins");
        String videoPath;
        if(  isPlayerOneTurn) {
            videoPath = "/com/iti/tictactoe/Videos/video2.mp4"; //win
            System.out.println(playerOneScore.getText());
            //add
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("action", "incrementScore");
            jsonRequest.addProperty("username", playerName.getPlayerOne());
            jsonRequest.addProperty("score", Integer.parseInt(playerOneScore.getText()));
            System.out.println(jsonRequest + " the sent json request");
            // Send JSON request
            socketManager.sendJson(jsonRequest);
        } else
            videoPath = "/com/iti/tictactoe/Videos/video4.mp4"; //lose
        PauseTransition pause = new PauseTransition(Duration.seconds(0.3));
        pause.setOnFinished(event -> {
            showVideo(videoPath);
        });
        pause.play();
    }

    public void showVideo(String videoPath) {
        try {
            Media media = new Media(getClass().getResource(videoPath).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            StackPane root = new StackPane();
            root.getChildren().add(mediaView);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setScene(scene);

            Platform.runLater(() -> {
                stage.show();
                mediaPlayer.play();
            });
            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(event -> {
                mediaPlayer.stop();
                stage.close();
            });
            delay.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
  
    /*public void handleRestartButton(ActionEvent actionEvent) throws IOException, InterruptedException {
        UiUtils.playSoundEffect();
//        Optional<ButtonType> result = AlertUtils.showConfirmationAlert("Restart Game", "Are you sure you want to restart the game?", "This will clear the current game state.");
//        if (result.isPresent() && result.get() == ButtonType.OK) {
//            resetGame();
        JsonObject jsonRequest = new JsonObject();
        jsonRequest.addProperty("action", "invite");
        jsonRequest.addProperty("player1", playerName.getPlayerOne());
        jsonRequest.addProperty("player", playerName.getPlayerTwo());
        System.out.println(jsonRequest + " the sent json request");
        // Send JSON request
        socketManager.sendJson(jsonRequest);
        Thread.sleep(300);
        if (message != null) {
            Gson gson = new Gson();
            JsonObject reponse = gson.fromJson(message, JsonObject.class);
            String invitationResponse = reponse.get("message").getAsString();
            System.out.println("Invitation response: " + invitationResponse);
            Platform.runLater(() -> {
                if ("online".equals(invitationResponse)) {
                    AlertUtils.showInformationAlert("Invitation Status", "Invitation Sent", "The invitation has been successfully sent.");
                } else if ("offline".equals(invitationResponse)) {
                    AlertUtils.showInformationAlert("Invitation Status", "Invitation Not Sent", "The invited player is currently offline.");
                } else {
                    AlertUtils.showInformationAlert("Invitation Status", "Invitation Error", "The invited player is current in game");
                }
            });
        }
           setButtonDisabledToPreventUserAtComputerTurns(false);
            record_btn.setDisable(false);
            writingRecordedMoves();
            isRecording = false;
        }*/


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
    // implement functionality ya ahmmed ya gamallllll
    public void handleRecordButton(ActionEvent actionEvent) {
        UiUtils.playSoundEffect();
        record_btn.setDisable(true);
        record_btn.setStyle(" -fx-background-color: white;\n" +
                "    -fx-background-radius: 50%;\n" +
                "    -fx-border-radius: 50%;\n" +
                "    -fx-border-color: transparent;\n" +
                "    -fx-font-family: \"Comic Sans MS\";\n" +
                "    -fx-text-fill: RED;\n" +
                "    -fx-font-size: 30px;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-min-width: 100px; /* Ensures the button is a circle */\n" +
                "    -fx-min-height: 100px;\n" +
                "    -fx-max-width: 100px;\n" +
                "    -fx-max-height: 100px;\n" +
                "    -fx-alignment: center;");
       // record_btn.getStyleClass().add("record-button-recording");
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
                isRecording = false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            record_btn.getStyleClass().add("record-button");
        }
    }
}