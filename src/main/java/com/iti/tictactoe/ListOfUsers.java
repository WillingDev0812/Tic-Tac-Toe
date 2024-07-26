package com.iti.tictactoe;

import com.iti.tictactoe.auth.SignUp;
import com.iti.tictactoe.models.UiUtils;
import com.iti.tictactoe.navigation.NavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOfUsers {
    private NavigationController navController;

    @FXML
    private Label backLabel;

    @FXML
    private Button inviteBtn;

    @FXML
    private ListView<String> PlayerListView;

    @FXML
    private Label invitePlayerWarning;

    @FXML
    private Text playerName;
   static DataOutputStream dos;
    List<String> playerList = new ArrayList<>();

    public static String currentUserEmail;
    public String username;
    List allFilesNames;
    public static void setCurrentUserEmail(String email) {
        currentUserEmail = email;
    }
    public void initialize(){
        try (Socket socket = new Socket("localhost", 12345);
             DataInputStream dis = new DataInputStream(socket.getInputStream())) {
            dos = new DataOutputStream(socket.getOutputStream());

             dos.writeUTF("showUsers");
             dos.writeUTF(currentUserEmail);
             username = dis.readUTF();
             playerName.setText("Hello " + username);
             int size = dis.readInt();
             for (int i = 0; i < size; i++) {
                playerList.add(dis.readUTF());
             }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //PlayerList.getItems().addAll(allFilesNames);
        for (String playerName : playerList) {
            System.out.println(playerName);
        }

        PlayerListView.getItems().setAll(playerList);

        //System.out.println(PlayerList.getSelectionModel().getSelectedItem());
        UiUtils.addHoverAnimation(inviteBtn);

    }

    public  void logout() {
        if(currentUserEmail==null)
            return;
        try {
            Socket socket = new Socket("localhost", 12345);
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF("offline");
            dos.writeUTF(currentUserEmail);
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        try {
//            this.dos.writeUTF("offline");
//            this.dos.writeUTF(currentUserEmail);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }


    public void onBackImageClick(MouseEvent mouseEvent) {
        if (navController == null) {
            System.out.println("error in navController");
        } else {
            UiUtils.playSoundEffect();
            navController.popScene();
        }
    }

    public void inviteBtn(ActionEvent actionEvent) {

    }
    public void setNavController(NavigationController navController) {
            this.navController = navController;
    }

}
