package com.example.demo3;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class BlackJackMain extends Application {

    private int playerCount;
    private Deck deck = new Deck();
    private Dealer dealer = new Dealer();
    private Player player = new Player();

    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    private HBox dealerCards = new HBox(30);
    private HBox playerCards = new HBox(30);

    public static void main(String[] args) {
        launch(args);
    }

    private Parent createStage(){
        deck.shuffle(deck,52);
        dealer.printDeck(deck);
        dealer.drawCard(dealer,deck);
        dealer.drawCard(dealer,deck);
        player.drawCard(player,deck);
        player.drawCard(player,deck);
        dealer.printHand(player.hand);
        dealer.printHand(dealer.hand);
        System.out.println(dealer.handValue(player.hand));

        Pane root = new Pane();
        root.setPrefSize(800,600);

        Region background = new Region();
        background.setPrefSize(800,600);
        background.setStyle("-fx-background-color: rgba(0,0,0,1)");

        HBox rootLayout = new HBox(5);
        rootLayout.setPadding(new Insets(5,5,5,5));

        Rectangle left = new Rectangle(550,560);
        left.setArcHeight(50);
        left.setArcWidth(50);
        left.setFill(Color.GREEN);

        Rectangle right = new Rectangle(230,560);
        right.setArcHeight(50);
        right.setArcWidth(50);
        right.setFill(Color.ORANGE);

        //Left
        StackPane leftStack = new StackPane();

        VBox leftVBox = new VBox(50);
        leftVBox.setAlignment(Pos.TOP_CENTER);

        Text scoreDealer = new Text("'Dealer: ");
        Text scorePlayer = new Text("'Player: ");

        leftVBox.getChildren().addAll(scoreDealer, dealerCards, playerCards, scorePlayer);
        leftStack.getChildren().addAll(left, leftVBox);

        //Right
        StackPane rightStack = new StackPane();
        VBox rightVbox = new VBox(50);
        rightVbox.setAlignment(Pos.CENTER);
        Button btnBet = new Button("Bet");
        Button btnPlay = new Button("Play");

        Button btnHit = new Button("Hit");
        Button btnStand = new Button("Stand");

        HBox hitStandBox = new HBox(15);
        hitStandBox.setAlignment(Pos.CENTER);
        hitStandBox.getChildren().addAll(btnHit,btnStand);


        rightVbox.getChildren().addAll(btnBet,btnPlay,hitStandBox);
        rightStack.getChildren().addAll(right,rightVbox);

        rootLayout.getChildren().addAll(leftStack,rightStack);
        root.getChildren().addAll(background,rootLayout);

        btnPlay.disableProperty().bind(playable);
        btnHit.disableProperty().bind(playable.not());
        btnStand.disableProperty().bind(playable.not());

        scorePlayer.textProperty().bind(new SimpleStringProperty("Player: ").concat(player.handValue(player.hand)));
        scoreDealer.textProperty().bind(new SimpleStringProperty("Dealer: ").concat(dealer.handValue(dealer.hand)));

        
        return root;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(createStage()));
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.setTitle("BlackJack");
        primaryStage.show();
    }


}
