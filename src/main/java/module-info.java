module com.iti.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires javafx.media;

    opens com.iti.tictactoe.AIGame to javafx.fxml;

    opens com.iti.tictactoe to javafx.fxml;
    exports com.iti.tictactoe;
    exports com.iti.tictactoe.splash;
    opens com.iti.tictactoe.splash to javafx.fxml;


    opens com.iti.tictactoe.muliplayerOffline to javafx.fxml;
    exports com.iti.tictactoe.muliplayerOffline;

    opens com.iti.tictactoe.Single to javafx.fxml;
    exports com.iti.tictactoe.Single;
    exports com.iti.tictactoe.muliplayerOffline.models;
    opens com.iti.tictactoe.muliplayerOffline.models to javafx.fxml;

}