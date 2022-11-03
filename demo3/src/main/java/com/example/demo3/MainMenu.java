package com.example.demo3;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    Button playButton;
    Button settings;
    Stage window;
    Scene scene;


    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;
        window.setTitle("Main Menu");
        playButton = new Button("Play BlackJack!");
        settings = new Button("Settings");

        ChoiceBox<Integer> players = new ChoiceBox<Integer>();

        players.getItems().addAll(1,2,3,4);

        StackPane layout = new StackPane();
        Scene scene = new Scene(layout, 800,600);

        Text introText = new Text("BLACKJACK");
        Text playersText = new Text("Players");

        VBox vertical = new VBox();

        vertical.setAlignment(Pos.CENTER);

        vertical.setSpacing(20);
        vertical.getChildren().addAll(introText, playButton, playersText, players, settings);

        layout.getChildren().add(vertical);

        players.setValue(1);

        layout.setBackground(Background.EMPTY);
        scene.setFill(Color.DARKGREEN);
        window.setScene(scene);
        window.show();

        //on action -> switch scenes for settings + play buttons
        //get method for # of players



    }
}
