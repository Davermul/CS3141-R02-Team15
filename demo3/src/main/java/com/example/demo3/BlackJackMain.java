package com.example.demo3;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private int screensShown = -1; //A counter for how many of the player screens has been shown
    ObservableList<Node> dealerHand = new HBox().getChildren(); //Empty hand of cards for dealer to be created
    Player overallDealer = new Player(dealerHand); //Dealer for the players to compete against
    Rectangle2D sceneBounds; // Automatically set to the max bounds of computer screen, Sets size of game screen

    RadialGradient scheme;
    Color color1 = Color.LIMEGREEN;
    Color color2 = Color.DARKGREEN;

    ArrayList<Integer> outPlayers = new ArrayList<Integer>();



    private Scene createSettingsScreen(){
        StackPane root = new StackPane();
        Scene scene = new Scene(root);

        HBox outerBox = new HBox();
        VBox innerBox = new VBox(20);

        MenuButton modelButton = new MenuButton("Color Scheme");
        CheckMenuItem checkMenuItem = new CheckMenuItem("Dark");
        CheckMenuItem checkMenuItem1 = new CheckMenuItem("Standard");
        checkMenuItem1.setSelected(true);
        checkMenuItem.setSelected(false);
        modelButton.getItems().addAll(checkMenuItem,checkMenuItem1);

        checkMenuItem1.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                checkMenuItem.setSelected(false);
                scheme = new RadialGradient(0,
                        0.3,
                        sceneBounds.getWidth()/2,
                        sceneBounds.getHeight()/2,
                        750,
                        false,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.LIMEGREEN),
                        new Stop(1, Color.DARKGREEN));
                scene.setFill(scheme);
                color1 = Color.LIMEGREEN;
                color2 = Color.DARKGREEN;
            }
        });

        checkMenuItem.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                    if (isSelected) {
                        checkMenuItem1.setSelected(false);
                        scheme = new RadialGradient(0,
                                0.3,
                                sceneBounds.getWidth()/2,
                                sceneBounds.getHeight()/2,
                                750,
                                false,
                                CycleMethod.NO_CYCLE,
                                new Stop(0, Color.GREY),
                                new Stop(1, Color.BLACK));

                    }
                    scene.setFill(scheme);
                    color1 = Color.GREY;
                    color2 = Color.BLACK;
        });


        ToggleButton musicButton = new ToggleButton("Music On/Off");

        Button returnBtn = new Button("Return");
        returnBtn.setOnAction(actionEvent -> {switchScenes(createMainScreen());});

        Image blkjk = new Image("blackjacktitle.png");  //BlackJack image and formating
        ImageView view = new ImageView(blkjk);
        view.setFitHeight(200);
        view.setFitWidth(250);



        root.setBackground(Background.EMPTY);

        innerBox.getChildren().addAll(view,modelButton,musicButton,returnBtn);
        outerBox.getChildren().add(innerBox);
        innerBox.setAlignment(Pos.CENTER);
        outerBox.setAlignment(Pos.CENTER);
        root.getChildren().addAll(outerBox);
        scene.setFill(scheme);
        return scene; // Return the scene to be displayed
    }

    private Scene createBetScene(){

        StackPane root = new StackPane();
        root.setPrefSize(sceneBounds.getWidth(),sceneBounds.getHeight());
        Scene scene = new Scene(root);
        Rectangle main = new Rectangle(sceneBounds.getWidth(),sceneBounds.getHeight());
        main.setArcHeight(50);
        main.setArcWidth(50);
        main.setFill(scheme);

        HBox outerBox = new HBox(10);
        VBox innerBox = new VBox(15);

        outerBox.setAlignment(Pos.CENTER);
        innerBox.setAlignment(Pos.CENTER);

        Button play = new Button("Play");
        play.setDisable(true);
        play.setOnAction(event -> {
            menuToPlayerScreen();});

        //int i = 1;
        int[] d = {0};
        for (Player player: players) {
            //if (!outPlayers.contains(i)) {
                HBox playerDetails = new HBox(30);
                playerDetails.setAlignment(Pos.CENTER);
                Text text = new Text("Player: " + player.getPlayerNumber() + "\n Balance: " + player.getBalance());
                Spinner slider = new Spinner(0, player.getBalance(), 50, 50);

                Button btnBet = new Button("Confirm Bet");

                btnBet.setOnAction(event -> {
                    slider.setVisible(false);
                    btnBet.setDisable(true);
                    player.setBetAmount((int) slider.getValue());
                    d[0]++;
                    if (d[0] == playerCount-outPlayers.size()) {
                        play.setDisable(false);
                    }
                });

                playerDetails.getChildren().addAll(text, slider, btnBet);
                innerBox.getChildren().add(playerDetails);

            }
            //i++;
       // }
        innerBox.getChildren().add(play);
        outerBox.getChildren().add(innerBox);
        root.getChildren().addAll(main,outerBox);
        return scene;
    }

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

        while(overallDealer.getHandValue().intValue()<17){overallDealer.takeCard(deck.drawCard());} //Up until this point the dealer has only been dealt the one card to begin with, now that all the players have gone the dealer will draw untill his hand value is 17 or greater
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


        for (Player player: players) {
            int playerHandValue = player.getHandValue().intValue();
            HBox playerCards = new HBox(10);
            Text playerLabel = new Text("Player "+player.getPlayerNumber()+":");
            playerCards.getChildren().add(playerLabel);
            Text result;
            if (dealerHandValue>21 && playerHandValue<=21){
                player.setBalance(player.getBalance()+player.getBetAmount());
                result = new Text("Dealer Bust. Winner! Hand Value: "+playerHandValue +"\n Bet Amount: " +player.getBetAmount() + "\nNew Balance: " +player.getBalance());
            }
            else if(playerHandValue>dealerHandValue && playerHandValue <=21){
                player.setBalance(player.getBalance()+player.getBetAmount());
                result = new Text("Winner! Hand Value: "+playerHandValue  +"\n Bet Amount: " +player.getBetAmount() + "\nNew Balance: " +player.getBalance());
            }
            else if(playerHandValue>21) {
                player.setBalance(player.getBalance()-player.getBetAmount());
                result = new Text("Bust. Hand Value: " +playerHandValue +"\n Bet Amount: " +player.getBetAmount() + "\nNew Balance: " +player.getBalance());
            }
            else if (playerHandValue == dealerHandValue) {
                result =new Text("Tie. Hand Value: " +playerHandValue +"\n Bet Amount: " +player.getBetAmount() + "\nNew Balance: " +player.getBalance());
            }
            else {
                player.setBalance(player.getBalance()-player.getBetAmount());
                result=new Text("Loser. Hand Value: "+playerHandValue +"\n Bet Amount: " +player.getBetAmount() + "\nNew Balance: " +player.getBalance());}

            int s = player.getHand().size();
            for (int i=s-1; i>=0; i--)
            {
                playerCards.getChildren().add(player.getHand().get(i));
            }
            playerCards.getChildren().addAll(result);
            displayCase.getChildren().addAll(playerCards);

        } //This loop creates an hBox for each player's hand and adds the players cards to it, then it adds the Hbox to the Vbox display case.

        Button returnToMenu = new Button("Return to Main Menu"); //Button to start game over and return to Main Menu scene
        returnToMenu.setOnAction(event -> {  //This resets all the game counters and returns to main menu
            playerCount =1;
            players.clear();
            playerScreens.clear();
            deck.stack();
            screensShown=-1;
            dealerHand.clear();
            overallDealer.reset();
            switchScenes(createMainScreen());

        });

        Button nextHand = new Button("Next Hand");
        nextHand.setOnAction(event -> {
            /*
            for (Player player : players){
                if (player.getBalance()<=0){
                    player.setBetAmount(0);
                    for (int i=0; i<playerCount; i++){
                        if (players.get(i).equals(player)){

                            outPlayers.add(i+1);
                            System.out.println("\nPlayer Out: "+i);
                        }
                    }
                }
            }

             */

            for (Player player : players)
            {if (player.getBalance()<=0){players.remove(player); playerCount--;}}
            //SimpleIntegerProperty reset = new SimpleIntegerProperty(0);
            for (Player player : players){
                player.reset();
            }
            playerCount = players.size();
            playerScreens.clear();
            screensShown = 0;
            dealerHand.clear();
            overallDealer.reset();
            if(playerCount>=1){switchScenes(createBetScene());}
            else if(playerCount==0){
                playerCount =1;
                players.clear();
                playerScreens.clear();
                deck.stack();
                screensShown=-1;
                dealerHand.clear();
                overallDealer.reset();
                switchScenes(createMainScreen());
            }

        });


        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(returnToMenu,nextHand);
        displayCase.getChildren().add(buttonBox);


        centerStructure.getChildren().addAll(displayCase); //adds displayCase to centerStructure
        root.getChildren().add(centerStructure); //adds centerStructure to root stackPane

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

        RadialGradient colorScheme= new RadialGradient(0,
                0.3,
                sceneBounds.getWidth()/2,
                sceneBounds.getHeight()/2,
                750,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, color1),
                new Stop(1, color2));

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
                screensShown++;
                switchScenes(resultsScreenCreator());
            });
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
                screensShown++;
                switchScenes(resultsScreenCreator());
            });

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
                screensShown++;
                switchScenes(resultsScreenCreator());
            });
        }

        root.getChildren().addAll(outerBox); //adds outerBox to root
        root.setBackground(Background.EMPTY);
        scene.setFill(colorScheme);

        return scene; //returns scene
    }

    /**
     * This method is called with no parameters and a void return and is only called upon clicking play on the main menu screen. It creates all of the player objects depending on how many are playing and adds
     * them to the player arraylist
     */
    private void initializesPlayers(){
        for (int i=0;i<playerCount; i++){
            Player player = new Player(null);
            player.setPlayerNumber(i+1);
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
            menuToBetScreen();
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

        settings.setOnAction(event -> {switchScenes(createSettingsScreen());});

        RadialGradient gradient1 = new RadialGradient(0,
                0.3,
                sceneBounds.getWidth()/2,
                sceneBounds.getHeight()/2,
                750,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, color1),
                new Stop(1, color2));
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
                new Stop(0, color1),
                new Stop(1, color2));

        left.setFill(gradient1);

        Rectangle right = new Rectangle(sceneBounds.getWidth()/3-10,sceneBounds.getHeight()-45);
        right.setArcHeight(50);
        right.setArcWidth(50);

        Color colorOne;
        Color colorTwo;

        if(color1 == Color.LIMEGREEN)
        {colorOne = Color.ORANGE; colorTwo = Color.ORANGERED;}
        else {colorOne = Color.GREY; colorTwo = Color.BLACK;}

        RadialGradient gradient2 = new RadialGradient(0,
                0.1,
                (sceneBounds.getWidth()/3-10)/2,
                (sceneBounds.getHeight()-45)/2,
                750,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, colorOne),
                new Stop(1,  colorTwo));

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


        Text betInfo = new Text("Balance: " + player.getBalance() + "\nBet Amount: "+player.getBetAmount());

        Button returnToHome = new Button("Return To Main Screen"); //Return to Home button

        returnToHome.setOnAction(event -> {
            playerCount =1;
            players.clear();
            playerScreens.clear();
            deck.stack();
            screensShown=-1;
            dealerHand.clear();
            overallDealer.reset();
            switchScenes(createMainScreen());});

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


        rightVbox.getChildren().addAll(topDeco,betInfo,hitStandBox,returnToHome,bottomDeco);
        rightStack.getChildren().addAll(right,rightVbox);

        rootLayout.getChildren().addAll(leftStack,rightStack); //add all of the structure to the pane
        root.getChildren().addAll(background,rootLayout);



        scorePlayer.textProperty().bind(new SimpleStringProperty("Player: ").concat(player.getHandValue())); //Formats the display of the players hand value
        scoreDealer.textProperty().bind(new SimpleStringProperty("Dealer: ").concat(dealercard.value));      //Formats the display of the Dealers hand value

        //This listener switches to the transition scene when ever the players hand reaches 21 or above
        player.getHandValue().addListener((obs, old, newValue)-> {

            if (newValue.intValue()>=21)
            {btnHit.setDisable(true);}
        });

        //Draws a card for the player
        btnHit.setOnAction(event-> {
            player.takeCard(deck.drawCard());
        });

        //Ends the players turn and switches to the transition scene when pressed
        btnStand.setOnAction(event-> {

            if (player.getHandValue().intValue()>21 && screensShown < playerCount){
                switchScenes(playerTransitionScene(true,false,true,playerCards));
            }

            else if (player.getHandValue().intValue()==21 && screensShown < playerCount)
            {
                switchScenes(playerTransitionScene(false,true,true,playerCards));
            }
            else if (player.getHandValue().intValue()>21 && screensShown == playerCount)
            {
                switchScenes(playerTransitionScene(true,false,false,playerCards));
            }
            else if (player.getHandValue().intValue()==21 && screensShown == playerCount)
            {
                switchScenes(playerTransitionScene(false,true,false,playerCards));
            }
            else if (screensShown<playerCount){
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
        //System.out.println(screensShown+"\n");
        if (screensShown==-1 ){stage.setTitle("Main Menu");}
        else if(screensShown == 0) {stage.setTitle("Betting Phase");}
        else if (screensShown==playerCount+1){stage.setTitle("Results");}
        else {stage.setTitle("Player: "+players.get(screensShown-1).getPlayerNumber());}
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
    private void menuToBetScreen(){
        initializesPlayers();   //creates the desired amount of player objects
        screensShown++;
        switchScenes(createBetScene());
    }

    private void menuToPlayerScreen(){
        overallDealer.takeCard(deck.drawCard());    //starts the dealer off with one card
        Card dealerStartCard = (Card)overallDealer.getHand().get(0);


        for(Player player : players ){
            Node cardface = createFrontFace(dealerStartCard.suit,dealerStartCard.rank,120,180);
            Scene scene = new Scene(createPlayerStage(player,dealerStartCard,cardface));
            playerScreens.add(scene);
        }

        startNewGame(players.get(0));
        screensShown++;
        switchScenes(playerScreens.get(0));
    }

    /**
     * Starts off program by displaying Main Menu
     * @param primaryStage
     */
    public void start(Stage primaryStage) {
        //Sets variables to staring amount
        playerCount = 1;
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

        scheme = new RadialGradient(0,
                0.3,
                sceneBounds.getWidth()/2,
                sceneBounds.getHeight()/2,
                750,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, color1),
                new Stop(1, color2));

        stage.setTitle("Main Menu");
        stage.show();   //Displays Main Menu
    }

    public static void main(String[] args) {
        launch(args);
    }

}
