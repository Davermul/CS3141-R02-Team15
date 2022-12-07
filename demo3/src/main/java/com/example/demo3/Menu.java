package com.example.demo3;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;


public class Menu extends Application {

    Scene mainscene;
    MediaPlayer menuPlayer;
    public Menu(Scene mainscene, MediaPlayer menuPlayer){
        this.mainscene = mainscene;
        this.menuPlayer = menuPlayer;
    }
    @Override

    public void start (Stage primaryStage) throws FileNotFoundException {

    AtomicBoolean sound_fx = new AtomicBoolean(false);




    // Make the window a set size...
        String imag_location = "C:\\Users\\94744";
        Image image = new Image(new FileInputStream(imag_location+"\\Documents\\GitHub\\CS3141-R02-Team15\\wp21802299.jpg"));
        //Setting the image view
        ImageView imageView = new ImageView(image);
        ImageView imageView1 = new ImageView(image);


        //creating ImageView for adding image
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);


        VBox menuVBox = new VBox();
        menuVBox.setAlignment(Pos.CENTER);

        //scroll pane for scrolling the images if we more width and height
        ScrollPane scrollPane=new ScrollPane();
        scrollPane.setContent(menuVBox);
        //setting the fit height and width of the image view
        menuVBox.setMinWidth(1600);
        menuVBox.setMinHeight(760);
//
//        menuVBox.autosize();
//        menuVBox.autosize();



    // music and sound_fx location

    //sound_fx file came from
    /**
        String location1 = "C:/Users/94744";
        Media card = new Media(new File(location1+"/Downloads/sound_dealingcard.wav").toURI().toString());
    */

    // music setting



    // sound_fx setting
    /**
        MediaPlayer menuCard = new MediaPlayer(card);
        menuCard.setCycleCount(999999999);
        menuCard.setVolume(0.5);
        menuCard.setAutoPlay(true);
    */



    // Create music toggle button
        ToggleButton musicButton = new ToggleButton("Music On/Off");

            musicButton.setOnAction(event -> {
                if (musicButton.isSelected()) {
                    menuPlayer.pause();
                }else {
                    menuPlayer.play();
                }
            });

     // New scene, place pane in it
        Scene scene = new Scene(scrollPane, 800, 630);
        imageView.fitWidthProperty().bind(scene.widthProperty());

        scene.setFill(new ImagePattern(image));

    //create button dark model
        MenuButton modelButton = new MenuButton("model");
        CheckMenuItem checkMenuItem = new CheckMenuItem("Dark");

        checkMenuItem.setSelected(false);

        //Instantiating the ColorAdjust class
        ColorAdjust colorAdjust = new ColorAdjust();


        checkMenuItem.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                //Setting the brightness value
                colorAdjust.setBrightness(0.3);
                scene.getRoot().setStyle("-fx-base:black");
            } else {
                scene.getRoot().setStyle("");
                colorAdjust.setBrightness(1.0);
            }

            imageView.setEffect(colorAdjust);

        });

        modelButton.getItems().add(checkMenuItem);



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

        //back button
        Button Back = new Button("Back");
        Back.setOnMouseClicked((e)-> {

            try {
                primaryStage.setScene(mainscene);
//                primaryStage.show();
            } catch (Exception exception)
            {
            }
        });



        //set background

        menuVBox.setBackground(new Background(new BackgroundImage(image,null,null,null,new BackgroundSize(800, 630, false, false,false,false))));

        //menuVBox.getChildren().add(imageView);
        // Add all nodes to the vbox pane and center it all
        // Must be in order from top to bottom

        //add button
        menuVBox.getChildren().add(musicButton);
        musicButton.setTranslateX(10);
        musicButton.setTranslateY(80);

        menuVBox.getChildren().add(modelButton);
        modelButton.setTranslateX(10);
        modelButton.setTranslateY(120);

        menuVBox.getChildren().add(soundfx);
        soundfx.setTranslateX(10);
        soundfx.setTranslateY(160);

        menuVBox.getChildren().add(Back);
        Back.setTranslateX(10);
        Back.setTranslateY(180);


    // Place scene in stage
        primaryStage.setTitle("-Menu-");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
    public static void main(String[] args) {

        launch(args);
    }



}

