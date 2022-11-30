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
    private Stage stage;  //This is the stage that switches between the scenes of the game
    private int playerCount = 1; //set to default of 1 and is changed depending on how many players are selected on the Main Menu Screen
    private ArrayList<Player> players = new ArrayList<>(); //Arraylist of the game players
    private ArrayList<Scene> playerScreens = new ArrayList<>(); //Holds each player's game scene
    private Deck deck = new Deck(); //The card deck that the game is played off of
    private int screensShown = 0; //A counter for how many of the player screens has been shown
    ObservableList<Node> dealerHand = new HBox().getChildren(); //Empty hand of cards for dealer to be created
    Player overallDealer = new Player(dealerHand); //Dealer for the players to compete against
    Rectangle2D sceneBounds; // Automatically set to the max bounds of computer screen, Sets size of game screen

    /**
     * This Method creates the end result scene to show the end hands and which players won
     * @return scene
     */
    private Scene resultsScreenCreator(){
        /*
            Scene Structure
            StackPane root -> Hbox centerStructure -> Vbox displayCase -> up to 5 HBoxs, 1 for each player's hand and the dealer
         */

        StackPane root = new StackPane();  //The outermost layer of the scene
        Scene scene = new Scene(root);     //Creates Scene to be returned
        HBox centerStructure =new HBox();  //Hbox is purely for allignment and contains the Vbox that has all of the player's hands
        centerStructure.setAlignment(Pos.CENTER);

        VBox displayCase = new VBox(10); //This Vbox contains all of the player's hands as well as if they won
        displayCase.setAlignment(Pos.CENTER);

        while(overallDealer.getHandValue().intValue()<17){overallDealer.takeCard(deck.drawCard());} //Up until this point the dealer has only been delt the one card to begin with, now that all the players have gone the dealer will draw untill his hand value is 17 or greater
        int dealerHandValue = overallDealer.getHandValue().intValue();

        HBox dealercards = new HBox(10);  //This is a Hbox to display the dealers final hand
        Text dealerlabel = new Text("Dealer:");
        dealercards.getChildren().add(dealerlabel);
        int j = overallDealer.getHand().size();

        for (int i=j-1; i>=0; i--) {
            dealercards.getChildren().add(overallDealer.getHand().get(i));
        } //This for loop adds all of the dealers cards to the dealercards Hbox
        Text dealhandvalue = new Text("Dealer Hand Value: "+dealerHandValue);
        dealercards.getChildren().add(dealhandvalue); // Adds the final dealer cards value to be displayed

        displayCase.getChildren().add(dealercards); //Adds Dealercards to the outer VBox displayCase to be displayed

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
                playerCards.getChildren().add(player.getHand().get(i));
            }
            playerCards.getChildren().addAll(result);
            displayCase.getChildren().addAll(playerCards);
            d++;
        } //This loop creates an hBox for each player's hand and adds the players cards to it, then it adds the Hbox to the Vbox display case.

        Button returnToMenu = new Button("Return to Main Menu"); //Button to start game over and return to Main Menu scene
        returnToMenu.setOnAction(event -> {  //This resets all the game counters and returns to main menu
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


        centerStructure.getChildren().addAll(displayCase); //adds displayCase to centerStructure
        root.getChildren().add(centerStructure); //adds centerStructure to root stackPane

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
        return scene;  //Returns the result scene to be displayed
    }

    /**
     * This method creates the scene that is displayed inbetween players, it displays the players final hand and the value of their hand.
     * @param bust a boolean, true means player was over 21, false player was under 21
     * @param blackJack a boolean, true means player scored a perfect 21
     * @param morePlayers a boolean, true means there are still more players to go before the result screen
     * @param playercards the hBox that contains the players final hand to be displayed
     * @return Scene to be displayed between players
     */
    private Scene playerTransitionScene(Boolean bust, Boolean blackJack, Boolean morePlayers,HBox playercards){
        /*
            Scene Structure
            StackPane root -> Hbox outerbox -> VBox innerBox -> Hbox playerDards
         */
        StackPane root = new StackPane();
        Scene scene = new Scene(root);   //Scene to be returned
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
        /*
            This creates the scene if the player has a blackjack and there are more players left
         */
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
        /*
            This creates the scene if the player bust and there are no more players left
         */
        else if (bust && !morePlayers) {
            Text bustMeseeage = new Text("You Bust, Click to see Results");
            Button results = new Button("Results");

            innerBox.getChildren().addAll(playercards,bustMeseeage,results);
            outerBox.getChildren().addAll(innerBox);

            results.setOnAction(event -> {
                switchScenes(resultsScreenCreator());
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
        /*
            This creates the scene if the player has a blackjack and there are no more players left
         */
        else if (blackJack  && !morePlayers) {
            Text blackJackmessage = new Text("BLACKJACK!!!!\nClick to see Results");
            Button results = new Button("Results");

            innerBox.getChildren().addAll(playercards,blackJackmessage,results);
            outerBox.getChildren().addAll(innerBox);

            results.setOnAction(event -> {
                switchScenes(resultsScreenCreator());
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
        /*
            This creates the scene if the player has a score below 21 and there are more players left
         */
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
        /*
            This creates the scene if the player has a score below 21 and there are no more players left
         */
        else if (!bust && !blackJack && !morePlayers) {
            Text blackJackmessage = new Text("Click to see Results");
            Button results = new Button("Results");

            innerBox.getChildren().addAll(playercards,blackJackmessage,results);
            outerBox.getChildren().addAll(innerBox);

            results.setOnAction(event -> {
                switchScenes(resultsScreenCreator());
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




        root.getChildren().addAll(outerBox); //adds outerBox to root
        root.setBackground(Background.EMPTY);
        scene.setFill(scheme);

        return scene; //returns scene
    }

    /**
     * This method is called with no parameters and a void return and is only called upon clicking play on the main menu screen. It creates all of the player objects depending on how many are playing and adds
     * them to the player arraylist
     */
    private void initializesPlayers(){
        for (int i=0;i<playerCount; i++){
            Player player = new Player(null);
            players.add(player);
        }
        deck.stack(); //Also initializes the deck
    }

    /**
     * This method creates the MainMenu Scene
     * @return Scene
     */
    private Scene createMainScreen(){
        /*
            Scene Structure
            StackPane root -> Hbox outerBox -> VBox innerBox -> BlackJack image, PlayButton, player count selector, settings
         */

        StackPane root = new StackPane();
        Scene scene = new Scene(root);

        HBox outerBox = new HBox();
        VBox innerBox = new VBox(20);

        Button playButton = new Button(); //PlayButton and formatting
        Button settings = new Button("Settings");
        playButton.setBackground(Background.EMPTY);

        Image play = new Image("playbutton.png");
        ImageView playV = new ImageView(play);
        playButton.setGraphic(playV);
        playV.setFitHeight(75);
        playV.setFitWidth(175);

        //Upon pressing play the menuToPlayers method is called, essentially staring the game
        playButton.setOnAction(event-> {
            menuToPlayers();
        });


        Image blkjk = new Image("blackjacktitle.png");  //BlackJack image and formating
        ImageView view = new ImageView(blkjk);
        view.setFitHeight(200);
        view.setFitWidth(250);

        settings.setStyle("-fx-background-color: #ffed00"); //Settings Button and formatting


        ChoiceBox<Integer> numPlayers = new ChoiceBox<Integer>(); //Number of Players and formatting
        numPlayers.getItems().addAll(1,2,3,4);
        numPlayers.setStyle("-fx-background-color: #ffed00");
        numPlayers.setValue(1); //default number of players is 1

        numPlayers.setOnAction(event ->{
            playerCount= numPlayers.getValue();
        }); //This sets the numOfPlayers variable to the amount selected on the MainMenu

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

        innerBox.getChildren().addAll(view,playButton,settings,numPlayers);
        outerBox.getChildren().add(innerBox);
        innerBox.setAlignment(Pos.CENTER);
        outerBox.setAlignment(Pos.CENTER);
        root.getChildren().addAll(outerBox);
        scene.setFill(gradient1);
        return scene; // Return the scene to be displayed
    }

    /**
     * This method creates a scene for an individual player that shows the dealer's starting card and the players cards. This is also the scene where each player chooses to hit or stand
     * @param player The player the scene is for
     * @param dealercard The starting Dealer's card
     * @param dealerCardFace a visual node of the dealers staring card to be displayed
     * @return Parent root
     */
    private Parent createPlayerStage(Player player, Card dealercard, Node dealerCardFace){
        /*
            Scene Structure
            Pane root -> Hbox rootLayout -> rectangleLeft -> StackPane leftStack -> Vbox leftVbox -> scoreDealer,dealerCards,view, playerCards ,scorePlayer
                                         -> rectangleRight -> StackPane rightStack -> VBox rightVbox -> topDeco,btnBet,hitStandBox,returnToHome,bottomDeco
         */
        HBox playerCards = new HBox(30);  //Hbox to display players cards
        playerCards.setAlignment(Pos.CENTER);

        HBox dealerCards = new HBox(30); //Hbox to display dealers starting card
        dealerCards.setAlignment(Pos.CENTER);

        player.setHand(playerCards.getChildren()); //This makes the Hbox update everytime player draws card
        dealerCards.getChildren().add(dealerCardFace); //Adds dealers staring card to Hbox

        Pane root = new Pane();  //root pane and formatting
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
        VBox leftVBox = new VBox(30);
        leftVBox.setAlignment(Pos.CENTER);

        Image blkjk = new Image("blackjacktitle.png");
        ImageView view = new ImageView(blkjk);
        view.setFitHeight(250);
        view.setFitWidth(325);

        Text scoreDealer = new Text("'Dealer: ");
        Text scorePlayer = new Text("'Player: ");

        leftVBox.getChildren().addAll(scoreDealer,dealerCards,view, playerCards ,scorePlayer);
        leftStack.getChildren().addAll(left, leftVBox);

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

        //right  (Commented out lines are for debugging )
        StackPane rightStack = new StackPane();
        rightStack.setPrefSize(sceneBounds.getWidth()/3-10,sceneBounds.getHeight()-45);
        VBox rightVbox = new VBox(90);  //125 for presentation
        rightVbox.setAlignment(Pos.CENTER);
        //rightVbox.setStyle(cssLayout1);


        Button btnBet = new Button("Bet"); //Bet Button

        Button returnToHome = new Button("Return To Main Screen"); //Return to Home button

        HBox topDeco = new HBox(sceneBounds.getWidth()/3-270); //Top decoration cards
        topDeco.setAlignment(Pos.TOP_CENTER);
        //topDeco.setStyle(cssLayout1);

        HBox bottomDeco = new HBox(sceneBounds.getWidth()/3-270); //Bottom decoration cards
        bottomDeco.setAlignment(Pos.BOTTOM_CENTER);
        //bottomDeco.setStyle(cssLayout);

        //Start of card decorations on right-hand side
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
        //End of card decorations on right-hand side


        Button btnHit = new Button("Hit"); //Hit Button
        Button btnStand = new Button("Stand");//Stand Button

        HBox hitStandBox = new HBox(15);
        hitStandBox.setAlignment(Pos.CENTER);
        hitStandBox.getChildren().addAll(btnHit,btnStand);


        rightVbox.getChildren().addAll(topDeco,btnBet,hitStandBox,returnToHome,bottomDeco);
        rightStack.getChildren().addAll(right,rightVbox);

        rootLayout.getChildren().addAll(leftStack,rightStack); //add all of the structure to the pane
        root.getChildren().addAll(background,rootLayout);



        scorePlayer.textProperty().bind(new SimpleStringProperty("Player: ").concat(player.getHandValue())); //Formats the display of the players hand value
        scoreDealer.textProperty().bind(new SimpleStringProperty("Dealer: ").concat(dealercard.value));      //Formats the display of the Dealers hand value

        //This listener switches to the transition scene when ever the players hand reaches 21 or above
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

        //Draws a card for the player
        btnHit.setOnAction(event-> {
            player.takeCard(deck.drawCard());
        });

        //Ends the players turn and switches to the transition scene when pressed
        btnStand.setOnAction(event-> {
            if (screensShown<playerCount){
                switchScenes(playerTransitionScene(false,false,true,playerCards));
            }
            else if (screensShown==playerCount) {
                switchScenes(playerTransitionScene(false,false,false,playerCards));
            }
        });
        return root; //returns the scene to be displayed
    }

    /**
     * This Method switches the displayed scene to the scene that is passed in
     * @param scene the scene to be displayed
     */
    public void switchScenes(Scene scene) {
        if (screensShown==0 ){stage.setTitle("Main Menu");}
        if (screensShown==playerCount){stage.setTitle("Results");}
        else {stage.setTitle("Player: "+screensShown);}
        stage.setScene(scene);
    }

    /**
     * This method draws 2 cards to start the players turn
     * @param player player that needs to draw two cards
     */
    private void startNewGame(Player player) {
        player.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
    }

    /**
     * This Method starts the game off after play is pushed on the main Menu
     */
    private void menuToPlayers(){
        initializesPlayers();   //creates the desired amount of player objects
        overallDealer.takeCard(deck.drawCard());    //starts the dealer off with one card
        Card dealerStartCard = (Card)overallDealer.getHand().get(0);


        for(Player player : players ){
            Node cardface = createFrontFace(dealerStartCard.suit,dealerStartCard.rank,120,180);
            Scene scene = new Scene(createPlayerStage(player,dealerStartCard,cardface));
            playerScreens.add(scene);
        } //Creates a sceen for each player and adds it to the PlayerScreens arraylist

        startNewGame(players.get(0));
        screensShown++;
        switchScenes(playerScreens.get(0));
    }

    /**
     * Starts off program by displaying Main Menu
     * @param primaryStage
     */
    public void start(Stage primaryStage) {
        //Sets variables to staring ammount
        playerCount = 0;
        stage=primaryStage;

        //Gets screen size data
        Screen screen = Screen.getPrimary();
        sceneBounds = screen.getVisualBounds();


        Scene mainMenu = createMainScreen(); //Creates Main Menu Scene and formats it to screen size
        stage.setScene(mainMenu);
        stage.setX(sceneBounds.getMinX());
        stage.setY(sceneBounds.getMinY());
        stage.setWidth(sceneBounds.getWidth());
        stage.setHeight(sceneBounds.getHeight());
        stage.setResizable(false);

        stage.setTitle("Main Menu");
        stage.show();   //Displays Main Menu
    }

    public static void main(String[] args) {
        launch(args);
    }

}
