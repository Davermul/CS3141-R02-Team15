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
    private Player dealer;
    private Player player ;

    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    private HBox dealerCards = new HBox(50);
    private HBox playerCards = new HBox(30);


    private Text endMessage= new Text("TEST\n");

    private Parent createStage(){

        dealer = new Player(dealerCards.getChildren());
        player = new Player(playerCards.getChildren());


        Pane root = new Pane();
        root.setPrefSize(1000,800);
        Region background = new Region();
        background.setPrefSize(1000,800);
        background.setStyle("-fx-background-color: rgba(0,0,0,1)");

        HBox rootLayout = new HBox(5);
        rootLayout.setPadding(new Insets(5,5,5,5));

        Rectangle left = new Rectangle(650,760);
        left.setArcHeight(50);
        left.setArcWidth(50);
        left.setFill(Color.GREEN);

        Rectangle right = new Rectangle(330,760);
        right.setArcHeight(50);
        right.setArcWidth(50);
        right.setFill(Color.ORANGE);

        //Left
        StackPane leftStack = new StackPane();

        VBox leftVBox = new VBox(70);
        leftVBox.setAlignment(Pos.TOP_CENTER);

        Text scoreDealer = new Text("'Dealer: ");
        Text scorePlayer = new Text("'Player: ");

        leftVBox.getChildren().addAll(scoreDealer,dealerCards,endMessage, playerCards ,scorePlayer);
        leftStack.getChildren().addAll(left, leftVBox);

        //Right
        StackPane rightStack = new StackPane();
        VBox rightVbox = new VBox(50);
        rightVbox.setAlignment(Pos.CENTER);
        Button btnBet = new Button("Bet");
        Button btnPlay = new Button("Play");
        Button returnToHome = new Button("Return To Main Screen");

        Button btnHit = new Button("Hit");
        Button btnStand = new Button("Stand");

        HBox hitStandBox = new HBox(15);
        hitStandBox.setAlignment(Pos.CENTER);
        hitStandBox.getChildren().addAll(btnHit,btnStand);



        rightVbox.getChildren().addAll(btnBet,btnPlay,hitStandBox,returnToHome);
        rightStack.getChildren().addAll(right,rightVbox);

        rootLayout.getChildren().addAll(leftStack,rightStack);
        root.getChildren().addAll(background,rootLayout);

        btnPlay.disableProperty().bind(playable);
        btnHit.disableProperty().bind(playable.not());
        btnStand.disableProperty().bind(playable.not());

        scorePlayer.textProperty().bind(new SimpleStringProperty("Player: ").concat(player.getHandValue()));
        scoreDealer.textProperty().bind(new SimpleStringProperty("Dealer: ").concat(dealer.getHandValue()));

        player.getHandValue().addListener((obs, old, newValue)-> {
           if (newValue.intValue()>=21){
               endGame();
           }
        });

        dealer.getHandValue().addListener((obs, old, newValue)-> {
            if (newValue.intValue()>=21){
                endGame();
            }
        });

        btnPlay.setOnAction(event-> {
            startNewGame();
        });

        btnHit.setOnAction(event-> {
            player.takeCard(deck.drawCard());
        });

        btnStand.setOnAction(event-> {
            while (dealer.getHandValue().get()<17){
                dealer.takeCard(deck.drawCard());
            }

            endGame();
        });

        return root;
    }

    private void startNewGame(){
        playable.set(true);
        endMessage.setText("TEST \n");
        deck.stack();
        dealer.reset();
        player.reset();

        player.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
        dealer.takeCard(deck.drawCard());

    }

    private void endGame() {
        playable.set(false);
        int dealerValue = dealer.getHandValue().getValue();
        int playerValue = player.getHandValue().getValue();

        String gameResult = "Results of Game: Dealer " + dealerValue + ", Player " + playerValue + "\n";
        String winner;

        //Checking Who won, Order is important
        if (dealerValue == 21 || playerValue>21 || dealerValue==playerValue || (dealerValue<21 && dealerValue > playerValue)){
            winner = "Winner is DEALER!";
        }
        else if (playerValue ==21 || dealerValue>21 || playerValue>dealerValue) {
            winner = "Winner is PLAYER!";
        }
        else {
            winner = "something went wrong";
        }

        endMessage.setText(gameResult + winner);

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(createStage()));
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);
        primaryStage.setResizable(false);
        primaryStage.setTitle("BlackJack");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
