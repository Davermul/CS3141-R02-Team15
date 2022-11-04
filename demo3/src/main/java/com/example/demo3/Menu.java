package com.example.demo3;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class Menu extends Application {

    @Override

    public void start (Stage primaryStage) {

    AtomicBoolean sound_fx = new AtomicBoolean(false);
    // Make the window a set size...
    primaryStage.setResizable(false);

        VBox menuVBox = new VBox(50);
        menuVBox.setAlignment(Pos.CENTER);


    Media menuMusic = new Media(new File("C:/Users/94744/Downloads/Spring-Flowers.mp3").toURI().toString());

    MediaPlayer menuPlayer = new MediaPlayer(menuMusic);

    menuPlayer.setCycleCount(999999999);
    menuPlayer.setVolume(0.5);
    menuPlayer.setAutoPlay(true);



    // Create music toggle button
    ToggleButton musicButton = new ToggleButton("Music On/Off");

        musicButton.setOnAction(event -> {
            if (musicButton.isSelected()) {
                menuPlayer.pause();
            }else {
                menuPlayer.play();
            }
        });






    // Add all nodes to the vbox pane and center it all
    // Must be in order from top to bottom


    // New scene, place pane in it
    Scene scene = new Scene(menuVBox, 630, 730);

    //create button dark model
    MenuButton menuButton = new MenuButton("model");
    CheckMenuItem checkMenuItem = new CheckMenuItem("Dark");

    checkMenuItem.setSelected(false);

    checkMenuItem.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
        if (isSelected) {
            scene.getRoot().setStyle("-fx-base:black");
        } else {
            scene.getRoot().setStyle("");
        }
    });
        menuButton.getItems().add(checkMenuItem);



        //sound_fx file came from
        //Media card = new Media(new File("C:/Users/94744/Downloads/sound_dealingcard.wav").toURI().toString());

        //MediaPlayer menuCard = new MediaPlayer(card);

        //menuCard.setCycleCount(999999999);
        //menuCard.setVolume(0.5);
       // menuCard.setAutoPlay(true);


        //create button sound_fx button

        ToggleButton soundfx = new ToggleButton("soundfx On/Off");

        soundfx.setOnAction(event -> {
            if (soundfx.isSelected()&& sound_fx.get() ==false) {
                sound_fx.set(true);
            }else if(soundfx.isSelected()&&sound_fx.get()==true)
            {
                sound_fx.set(false);
            }


        });




        //add button
        menuVBox.getChildren().add(musicButton);
        musicButton.setAlignment(Pos.CENTER);

        menuVBox.getChildren().add(menuButton);
        menuButton.setAlignment(Pos.TOP_RIGHT);

        menuVBox.getChildren().add(soundfx);
        soundfx.setAlignment(Pos.TOP_LEFT);



    // Place scene in stage
        primaryStage.setTitle("-Menu-");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {

        launch(args);
    }



}

