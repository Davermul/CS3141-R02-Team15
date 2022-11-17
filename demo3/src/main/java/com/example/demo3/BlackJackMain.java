package com.example.demo3;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.Node;

import java.util.ArrayList;

import static com.example.demo3.CardFaceCreator.createFrontFace;

public class BlackJackMain extends Application {

    private Stage stage;
    private int playerCount = 1;
    private ArrayList<Player> players = new ArrayList<>();

    private ArrayList<Scene> playerScreens = new ArrayList<>();

    private Deck deck = new Deck();


    private int screensShown = 0;
    ObservableList<Node> dealerHand = new HBox().getChildren();
    Player overallDealer = new Player(dealerHand);

    Rectangle2D sceneBounds;

    //private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    private Scene resultsScreencreator(){
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        HBox centerstructure =new HBox();
        centerstructure.setAlignment(Pos.CENTER);

        VBox displayCase = new VBox(10);
        displayCase.setAlignment(Pos.CENTER);

        while(overallDealer.getHandValue().intValue()<17){
            overallDealer.takeCard(deck.drawCard());
        }
        int dealerHandValue = overallDealer.getHandValue().intValue();
        //System.out.println(overallDealer.getHand().size() + ": SIZE......VALUE :"+ overallDealer.getHandValue());
        HBox dealercards = new HBox(10);
        Text dealerlabel = new Text("Dealer:");
        dealercards.getChildren().add(dealerlabel);
        int j = overallDealer.getHand().size();
        for (int i=j-1; i>=0; i--)
        {
            //System.out.println("LOOP\n");
            //overallDealer.getHand().get(i);
            dealercards.getChildren().add(overallDealer.getHand().get(i));
        }
        Text dealhandvalue = new Text("Dealer Hand Value: "+dealerHandValue);
        dealercards.getChildren().add(dealhandvalue);
        displayCase.getChildren().add(dealercards);

        int d =1;
        for (Player player: players) {
            int playerHandValue = player.getHandValue().intValue();
            HBox playerCards = new HBox(10);
            Text playerLabel = new Text("Player "+d+":");
            playerCards.getChildren().add(playerLabel);
            Text result;
            if (dealerHandValue>21 && playerHandValue<=21){result = new Text("Dealer Bust. Winner! Hand Value: "+playerHandValue);}
            else if(playerHandValue>dealerHandValue && playerHandValue <=21){result = new Text("Winner! Hand Value: "+playerHandValue );}
            else if(playerHandValue>21) {result = new Text("Bust. Hand Value: " +playerHandValue);}
            else if (playerHandValue == dealerHandValue) {result =new Text("Tie. Hand Value: " +playerHandValue);}
            else {result=new Text("Loser. Hand Value: "+playerHandValue);}

            int s = player.getHand().size();
            for (int i=s-1; i>=0; i--)
            {
                //System.out.println("LOOP\n");
                //overallDealer.getHand().get(i);
                playerCards.getChildren().add(player.getHand().get(i));
            }
            playerCards.getChildren().addAll(result);
            displayCase.getChildren().addAll(playerCards);
            d++;
        }

        Button returnToMenu = new Button("Return to Main Menu");
        returnToMenu.setOnAction(event -> {
            playerCount =1;
            players.clear();
            playerScreens.clear();
            deck.stack();
            screensShown=0;
            dealerHand.clear();
            overallDealer.reset();
            switchScenes(createMainScreen());

        });
        displayCase.getChildren().add(returnToMenu);
        centerstructure.getChildren().addAll(displayCase);
        root.getChildren().add(centerstructure);
        RadialGradient scheme= new RadialGradient(0,
                0.3,
                sceneBounds.getWidth()/2,
                sceneBounds.getHeight()/2,
                750,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.LIMEGREEN),
                new Stop(1, Color.DARKGREEN));

        root.setBackground(Background.EMPTY);
        scene.setFill(scheme);
        return scene;
    }

    private Scene playerTransitionScene(Boolean bust, Boolean blackJack, Boolean morePlayers,HBox playercards){
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        HBox outerBox = new HBox(15);
        VBox innerBox = new VBox(15);

        outerBox.setAlignment(Pos.CENTER);
        innerBox.setAlignment(Pos.CENTER);
        RadialGradient scheme= new RadialGradient(0,
                0.3,
                sceneBounds.getWidth()/2,
                sceneBounds.getHeight()/2,
                750,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.LIMEGREEN),
                new Stop(1, Color.DARKGREEN));

        /*
            This creates the scene if the player busts and there is still another player left
         */
        if (bust && morePlayers){
            Text bustedMessage = new Text("You Bust, Pass to Next Player Please");
            Button nextPlayer = new Button("Next Player");

            innerBox.getChildren().addAll(playercards,bustedMessage,nextPlayer);
            outerBox.getChildren().addAll(innerBox);

            nextPlayer.setOnAction(event -> {
                startNewGame(players.get(screensShown));
                screensShown++;
                switchScenes(playerScreens.get(screensShown-1));
            });

            scheme= new RadialGradient(0,
                    0.3,
                    sceneBounds.getWidth()/2,
                    sceneBounds.getHeight()/2,
                    750,
                    false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.RED),
                    new Stop(1, Color.DARKRED));
        }
        else if (blackJack && morePlayers){
            Text blackJackMessage = new Text("BLACKJACK!!! \n Please Pass to Next Player");
            Button nextPlayer = new Button("Next Player");

            innerBox.getChildren().addAll(playercards,blackJackMessage,nextPlayer);
            outerBox.getChildren().addAll(innerBox);

            nextPlayer.setOnAction(event -> {
                startNewGame(players.get(screensShown));
                screensShown++;
                switchScenes(playerScreens.get(screensShown-1));
            });

            scheme= new RadialGradient(0,
                    0.3,
                    sceneBounds.getWidth()/2,
                    sceneBounds.getHeight()/2,
                    750,
                    false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.LIMEGREEN),
                    new Stop(1, Color.GREEN));
        }
        else if (bust && !morePlayers) {
            Text bustMeseeage = new Text("You Bust, Click to see Results");
            Button results = new Button("Results");

            innerBox.getChildren().addAll(playercards,bustMeseeage,results);
            outerBox.getChildren().addAll(innerBox);

            results.setOnAction(event -> {
                switchScenes(resultsScreencreator());
            });

            scheme= new RadialGradient(0,
                    0.3,
                    sceneBounds.getWidth()/2,
                    sceneBounds.getHeight()/2,
                    750,
                    false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.RED),
                    new Stop(1, Color.DARKRED));
        }
        else if (blackJack  && !morePlayers) {
            Text blackJackmessage = new Text("BLACKJACK!!!!\nClick to see Results");
            Button results = new Button("Results");

            innerBox.getChildren().addAll(playercards,blackJackmessage,results);
            outerBox.getChildren().addAll(innerBox);

            results.setOnAction(event -> {
                switchScenes(resultsScreencreator());
            });

            scheme= new RadialGradient(0,
                    0.3,
                    sceneBounds.getWidth()/2,
                    sceneBounds.getHeight()/2,
                    750,
                    false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.LIMEGREEN),
                    new Stop(1, Color.GREEN));
        }
        else if (!bust && !blackJack && morePlayers) {
            Text blackJackMessage = new Text("Wait Until The End To See If You Won!!! \n Please Pass to Next Player");
            Button nextPlayer = new Button("Next Player");

            innerBox.getChildren().addAll(playercards,blackJackMessage,nextPlayer);
            outerBox.getChildren().addAll(innerBox);

            nextPlayer.setOnAction(event -> {
                startNewGame(players.get(screensShown));
                screensShown++;
                switchScenes(playerScreens.get(screensShown-1));
            });

            scheme= new RadialGradient(0,
                    0.3,
                    sceneBounds.getWidth()/2,
                    sceneBounds.getHeight()/2,
                    750,
                    false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.LIMEGREEN),
                    new Stop(1, Color.GREEN));
        }
        else if (!bust && !blackJack && !morePlayers) {
            Text blackJackmessage = new Text("Click to see Results");
            Button results = new Button("Results");

            innerBox.getChildren().addAll(playercards,blackJackmessage,results);
            outerBox.getChildren().addAll(innerBox);

            results.setOnAction(event -> {
                switchScenes(resultsScreencreator());
            });

            scheme= new RadialGradient(0,
                    0.3,
                    sceneBounds.getWidth()/2,
                    sceneBounds.getHeight()/2,
                    750,
                    false,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.LIMEGREEN),
                    new Stop(1, Color.GREEN));
        }




        root.getChildren().addAll(outerBox);
        root.setBackground(Background.EMPTY);
        scene.setFill(scheme);

        return scene;
    }
    private void intializePlayers(){
        System.out.println(playerCount);
        System.out.println("\nPlayers size: "+players.size());
        System.out.println("Screens Shown"+screensShown);
        for (int i=0;i<playerCount; i++){
            Player player = new Player(null);
            Player dealer = new Player(null);

            players.add(player);
        }
        System.out.println("\nPlayers size: "+players.size());
        deck.stack();
    }

    private Scene createMainScreen(){

        StackPane root = new StackPane();
        Scene scene = new Scene(root);

        HBox outerBox = new HBox();
        VBox innerBox = new VBox(20);

        Button playButton = new Button();
        Button settings = new Button("Settings");
        playButton.setBackground(Background.EMPTY);

        Image play = new Image("playbutton.png");
        ImageView playV = new ImageView(play);
        playButton.setGraphic(playV);
        playV.setFitHeight(75);
        playV.setFitWidth(175);

        playButton.setOnAction(event-> {
            menuToPlayers();
        });





        Image blkjk = new Image("blackjacktitle.png");
        ImageView view = new ImageView(blkjk);
        view.setFitHeight(200);
        view.setFitWidth(250);

        settings.setStyle("-fx-background-color: #ffed00");


        ChoiceBox<Integer> numPlayers = new ChoiceBox<Integer>();

        numPlayers.getItems().addAll(1,2,3,4);

        numPlayers.setStyle("-fx-background-color: #ffed00");

        numPlayers.setOnAction(event ->{
            playerCount= numPlayers.getValue();
        });

        innerBox.getChildren().addAll(view,playButton,settings,numPlayers);

        outerBox.getChildren().add(innerBox);

        innerBox.setAlignment(Pos.CENTER);
        outerBox.setAlignment(Pos.CENTER);

        numPlayers.setValue(1);
        root.getChildren().addAll(outerBox);

        RadialGradient gradient1 = new RadialGradient(0,
                0.3,
                sceneBounds.getWidth()/2,
                sceneBounds.getHeight()/2,
                750,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.LIMEGREEN),
                new Stop(1, Color.DARKGREEN));
        root.setBackground(Background.EMPTY);
        scene.setFill(gradient1);
        return scene;
    }
    private Parent createStage(Player player,Card dealercard,Node dealerCardFace){
        HBox playerCards = new HBox(30);
        playerCards.setAlignment(Pos.CENTER);
        HBox dealerCards = new HBox(30);
        dealerCards.setAlignment(Pos.CENTER);
        player.setHand(playerCards.getChildren());
        dealerCards.getChildren().add(dealerCardFace);

        Pane root = new Pane();
        root.setPrefSize(sceneBounds.getWidth(),sceneBounds.getHeight());
        Region background = new Region();
        background.setPrefSize(sceneBounds.getWidth(),sceneBounds.getHeight());
        background.setStyle("-fx-background-color: rgba(0,0,0,1)");

        HBox rootLayout = new HBox(5);
        rootLayout.setPadding(new Insets(5,5,5,5));

        Rectangle left = new Rectangle(2*(sceneBounds.getWidth()/3)-10,sceneBounds.getHeight()-45);
        left.setArcHeight(50);
        left.setArcWidth(50);

        RadialGradient gradient1 = new RadialGradient(0,
                0.1,
                (2*(sceneBounds.getWidth()/3)-10)/2,
                (sceneBounds.getHeight()-45)/2,
                1200,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.LIMEGREEN),
                new Stop(1, Color.DARKGREEN));

        left.setFill(gradient1);

        Rectangle right = new Rectangle(sceneBounds.getWidth()/3-10,sceneBounds.getHeight()-45);
        right.setArcHeight(50);
        right.setArcWidth(50);

        RadialGradient gradient2 = new RadialGradient(0,
                0.1,
                (sceneBounds.getWidth()/3-10)/2,
                (sceneBounds.getHeight()-45)/2,
                750,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.ORANGE),
                new Stop(1, Color.ORANGERED));

        right.setFill(gradient2);

        //Left
        StackPane leftStack = new StackPane();


        VBox leftVBox = new VBox(50);
        leftVBox.setAlignment(Pos.CENTER);

        Image blkjk = new Image("blackjacktitle.png");
        ImageView view = new ImageView(blkjk);
        view.setFitHeight(250);
        view.setFitWidth(325);

        Text scoreDealer = new Text("'Dealer: ");
        Text scorePlayer = new Text("'Player: ");

        leftVBox.getChildren().addAll(scoreDealer,dealerCards,view, playerCards ,scorePlayer);
        leftStack.getChildren().addAll(left, leftVBox);

        //Right

        /*
        Outlines for Debugging Right VBoxes

        String cssLayout = "-fx-border-color: red;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: dashed;\n";

        String cssLayout1 = "-fx-border-color: blue;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: dashed;\n";

         */

        StackPane rightStack = new StackPane();
        rightStack.setPrefSize(sceneBounds.getWidth()/3-10,sceneBounds.getHeight()-45);
        VBox rightVbox = new VBox(90);  //125 for presentation
        rightVbox.setAlignment(Pos.CENTER);
        //rightVbox.setStyle(cssLayout1);
        Button btnBet = new Button("Bet");
        //Button btnPlay = new Button("Play");
        Button returnToHome = new Button("Return To Main Screen");

        HBox topDeco = new HBox(sceneBounds.getWidth()/3-270);
        topDeco.setAlignment(Pos.TOP_CENTER);
        //topDeco.setStyle(cssLayout1);

        HBox bottomDeco = new HBox(sceneBounds.getWidth()/3-270);
        bottomDeco.setAlignment(Pos.BOTTOM_CENTER);
        //bottomDeco.setStyle(cssLayout);


        VBox tright = new VBox();
        VBox tleft = new VBox();
        VBox bright = new VBox();
        VBox bleft = new VBox();

        tright.setAlignment(Pos.TOP_RIGHT);
        tleft.setAlignment(Pos.TOP_LEFT);
        bright.setAlignment(Pos.BOTTOM_RIGHT);
        bleft.setAlignment(Pos.BOTTOM_LEFT);

        tleft.getChildren().add(createFrontFace(Suit.SPADES,Rank.JACK,120,180));
        bright.getChildren().add(createFrontFace(Suit.SPADES,Rank.JACK,120,180));
        tright.getChildren().add(createFrontFace(Suit.CLUBS,Rank.JACK,120,180));
        bleft.getChildren().add(createFrontFace(Suit.CLUBS,Rank.JACK,120,180));

        topDeco.getChildren().addAll(tleft,tright);
        bottomDeco.getChildren().addAll(bleft,bright);


        Button btnHit = new Button("Hit");
        Button btnStand = new Button("Stand");

        HBox hitStandBox = new HBox(15);
        hitStandBox.setAlignment(Pos.CENTER);
        hitStandBox.getChildren().addAll(btnHit,btnStand);



        rightVbox.getChildren().addAll(topDeco,btnBet,hitStandBox,returnToHome,bottomDeco);
        rightStack.getChildren().addAll(right,rightVbox);

        rootLayout.getChildren().addAll(leftStack,rightStack);
        root.getChildren().addAll(background,rootLayout);

        //btnPlay.disableProperty().bind(playable);
        //btnHit.disableProperty().bind(playable.not());
        // btnStand.disableProperty().bind(playable.not());

        scorePlayer.textProperty().bind(new SimpleStringProperty("Player: ").concat(player.getHandValue()));
        scoreDealer.textProperty().bind(new SimpleStringProperty("Dealer: ").concat(dealercard.value));

        player.getHandValue().addListener((obs, old, newValue)-> {

            if (newValue.intValue()>21 && screensShown < playerCount){
                switchScenes(playerTransitionScene(true,false,true,playerCards));
            }

            else if (newValue.intValue()==21 && screensShown < playerCount)
            {
                switchScenes(playerTransitionScene(false,true,true,playerCards));
            }
            else if (newValue.intValue()>21 && screensShown == playerCount)
            {
                switchScenes(playerTransitionScene(true,false,false,playerCards));
            }
            else if (newValue.intValue()==21 && screensShown == playerCount)
            {
                switchScenes(playerTransitionScene(false,true,false,playerCards));
            }
        });

        btnHit.setOnAction(event-> {
            player.takeCard(deck.drawCard());
        });

        btnStand.setOnAction(event-> {
            if (screensShown<playerCount){
                switchScenes(playerTransitionScene(false,false,true,playerCards));
            }
            else if (screensShown==playerCount) {
                switchScenes(playerTransitionScene(false,false,false,playerCards));
            }
        });
        return root;
    }

    public void switchScenes(Scene scene) {
        stage.setTitle("Player: "+screensShown);
        stage.setScene(scene);
    }
    private void startNewGame(Player player) {
        player.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
    }

    private void menuToPlayers(){
        intializePlayers();

        overallDealer.takeCard(deck.drawCard());
        Card dealerStartCard = (Card)overallDealer.getHand().get(0);

        for(Player player : players ){
            Node cardface = createFrontFace(dealerStartCard.suit,dealerStartCard.rank,120,180);
            Scene scene = new Scene(createStage(player,dealerStartCard,cardface));
            playerScreens.add(scene);
        }
        startNewGame(players.get(0));
        screensShown++;
        switchScenes(playerScreens.get(0));

    }
    public void start(Stage primaryStage) {
        playerCount = 0;
        stage=primaryStage;
        Screen screen = Screen.getPrimary();
        sceneBounds = screen.getVisualBounds();
        //System.out.println(sceneBounds.getWidth() + "H: "+sceneBounds.getHeight() );

        Scene mainMenu = createMainScreen();

        stage.setScene(mainMenu);
        stage.setX(sceneBounds.getMinX());
        stage.setY(sceneBounds.getMinY());
        stage.setWidth(sceneBounds.getWidth());
        stage.setHeight(sceneBounds.getHeight());
        stage.setResizable(false);
        stage.setTitle("Main Menu");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
