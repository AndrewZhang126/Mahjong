/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;


import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.input.MouseEvent;
import socketfx.Constants;
import socketfx.FxSocketServer;
import socketfx.SocketListener;

/**
 * FXML Controller class
 *
 * @author jtconnor
 */

//ASK WHY IMAGES AREN'T SHOWING UP


public class ServerGUIController implements Initializable {
    @FXML
    private Button sendButton;
    @FXML
    private TextField sendTextField;
    @FXML
    private Button connectButton, btnStart;
    @FXML
    private TextField portTextField;
    @FXML
    private Label lblMessages, lblText;
    @FXML
    private Label lblName2;
    @FXML
    private AnchorPane sAnchor;
    @FXML
    private ImageView imgC0,imgC1,imgC2,imgC3,imgC4,imgC5,imgC6,imgC7,imgC8,imgC9, imgC10, imgC11, imgC12,imgC13,
                      imgS0,imgS1,imgS2,imgS3,imgS4, imgS5,imgS6,imgS7,imgS8,imgS9, imgS10, imgS11, imgS12, imgS13, imgDraw, imgDiscard, imgBack;


    

//    private String n1, n2;
//    private boolean cClicked,sClicked;


    private final static Logger LOGGER =
            Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private boolean isConnected, turn = true, serverUNO = false, clientUNO = false;
    private int counter = 0;
    private String color;

    public enum ConnectionDisplayState {
           
        DISCONNECTED, WAITING, CONNECTED, AUTOCONNECTED, AUTOWAITING
    }

    private FxSocketServer socket;

    private void connect() {
        socket = new FxSocketServer(new FxSocketListener(),
                Integer.valueOf(portTextField.getText()),
                Constants.instance().DEBUG_NONE);
        socket.connect();
    }

    private void displayState(ConnectionDisplayState state) {
//        switch (state) {
//            case DISCONNECTED:
//                connectButton.setDisable(false);
//                sendButton.setDisable(true);
//                sendTextField.setDisable(true);
//                break;
//            case WAITING:
//            case AUTOWAITING:
//                connectButton.setDisable(true);
//                sendButton.setDisable(true);
//                sendTextField.setDisable(true);
//                break;
//            case CONNECTED:
//                connectButton.setDisable(true);
//                sendButton.setDisable(false);
//                sendTextField.setDisable(false);
//                break;
//            case AUTOCONNECTED:
//                connectButton.setDisable(true);
//                sendButton.setDisable(false);
//                sendTextField.setDisable(false);
//                break;
//        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        isConnected = false;
        displayState(ConnectionDisplayState.DISCONNECTED);
        imgBack.setImage(new Image("resources/background.jpg"));
        

        Runtime.getRuntime().addShutdownHook(new ShutDownThread());

        /*
         * Uncomment to have autoConnect enabled at startup
         */
//        autoConnectCheckBox.setSelected(true);
//        displayState(ConnectionDisplayState.WAITING);
//        connect();
    }

    class ShutDownThread extends Thread {

        @Override
        public void run() {
            if (socket != null) {
                if (socket.debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
                    LOGGER.info("ShutdownHook: Shutting down Server Socket");    
                }
                socket.shutdown();
            }
        }
    }

    class FxSocketListener implements SocketListener {

        @Override
        public void onMessage(String line) {
            if (line.substring(0,3).equals("rdy")){
            }
            else if (line.substring(0, 3).equals("msg")) {//server has received a message from the client
                lblMessages.setText(line.substring(3));
            }
            else if(line.substring(0,3).equals("nm2")){//this is the client's name
                name2 = line.substring(3);
                lblCName.setText(name2);
                start2=true;
                checkDeal();//checks if both players are ready
            }
            else if(line.equals("draw")) {//client draws a card from deck
                cCards.get(0).setImage(new Image("resources/tile3.jpg"));//their hand has added a card
                if (shuffleDeck.size() > 0) {//client can draw a card from deck if the deck still has cards
                    socket.sendMessage("card" + shuffleDeck.get(0).getCardPath());//sends the card to be added to the client's hand
                    cHand.add(shuffleDeck.get(0));//adds the card to the client's hand
                    shuffleDeck.remove(0);//removes the card from the deck
                }
                else {
                    socket.sendMessage("no cards left");//there are no more cards in the deck
                }
            }
            else if (line.equals("draw2")) {//client draws a card from discard pile
                cCards.get(0).setImage(new Image("resources/tile3.jpg"));//client has a card added to their hand
                if (discardDeck.size() > 0) {//client can draw a card from the discard pile if there is cards in the discard pile
                    socket.sendMessage("card" + discardDeck.get(discardDeck.size() - 1).getCardPath());//sends the card that will be added to the client's hand
                    cHand.add(discardDeck.get(discardDeck.size() - 1));//adds the card to the client's hand
                    discardDeck.remove(discardDeck.size() - 1);//removes the card from the discard pile
                }
            }
            else if (line.equals("win")) {//check if client has won
                if (g1.checkWin(cHand)) {//if client won, set the text to say they have won
                    lblText.setText(name2 + " Wins");
                    socket.sendMessage("cWin");
                    btnStart.setDisable(false);//allows server to play again
                }
            }
            else if (line.substring(0, 5).equals("sTurn")) {//client's turn is over
                cCards.get(0).setImage(null);//client has discarded a card to signal their turn has ended
                lblText.setText("");//clears text
                gPane.setDisable(false);
                imgDiscard.setImage(new Image(line.substring(5)));//displays the card that the client discarded
                discardDeck.add(new Card(line.substring(15, 17)));//adds that card to the array of discarded cards
            }
        }
        @Override
        public void onClosedStatus(boolean isClosed) {
        }
    }

    @FXML
    private void handleSendMessageButton(ActionEvent event) {
        if (!sendTextField.getText().equals("")) {//server has sent a message
            socket.sendMessage("msg" + sendTextField.getText());
        }
    }
    @FXML
    private void handleConnectButton(ActionEvent event) {
        connectButton.setDisable(true);
        displayState(ConnectionDisplayState.WAITING);
        connect();
    }
    @FXML
    private void handleStart(){
        name1 = txtName.getText();//sets server's name
        lblSName.setText(name1);
        socket.sendMessage("nm1" + name1);//sends name to client
        start1 = true;
        checkDeal();//checks if both players are ready
    }
    private void checkDeal(){
        if (start1 && start2 ){//if both players are empty, the server's turn begins
            socket.sendMessage("sTurn");
            lblText.setText("");
            deal();//deals the cards
            start1 = false;
            start2 = false;
            btnStart.setDisable(true);
        }
        else{
            lblText.setText("Waiting For Players To Be Ready");
        }
    }

    private boolean start1 = false;
    private boolean start2 = false;
    @FXML
    public TextField txtName;
    private String name1;
    private String name2;
    @FXML
    public Label lblCName,lblSName;
    @FXML
    private GridPane gPane;
    private ArrayList<Card> deck = new ArrayList<>();
    private ArrayList<Card> shuffleDeck = new ArrayList<>();
    private ArrayList<Card> discardDeck = new ArrayList<>();
    private ArrayList<Card> sHand = new ArrayList<>(14);
    private ArrayList<Card> cHand = new ArrayList<>(14);
    private ArrayList<ImageView> sCards = new ArrayList<>();
    private ArrayList<ImageView> cCards = new ArrayList<>();
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    int imgClicked;
    int countClick = 0;
    int click1 = -1;
    int click2 = -1;
    Card cardSelect;
    int c = 0;
    boolean drag = false;
    Gameplay g1 = new Gameplay();
    private void deal() {//deals the cards to both server and client
        lblText.setText("");
        gPane.setDisable(false);
        sCards.add(imgS0);//adds all imageviews to the arrays of imageviews
        sCards.add(imgS1);
        sCards.add(imgS2);
        sCards.add(imgS3);
        sCards.add(imgS4);
        sCards.add(imgS5);
        sCards.add(imgS6);
        sCards.add(imgS7);
        sCards.add(imgS8);
        sCards.add(imgS9);
        sCards.add(imgS10);
        sCards.add(imgS11);
        sCards.add(imgS12);
        sCards.add(imgS13);
        cCards.add(imgC0);
        cCards.add(imgC1);
        cCards.add(imgC2);
        cCards.add(imgC3);
        cCards.add(imgC4);
        cCards.add(imgC5);
        cCards.add(imgC6);
        cCards.add(imgC7);
        cCards.add(imgC8);
        cCards.add(imgC9);
        cCards.add(imgC10);
        cCards.add(imgC11);
        cCards.add(imgC12);
        cCards.add(imgC13);
        for (int h = 0; h < 4; h ++) {//there are 4 of each type of tile
            for (int i = 1; i < 10; i ++) {
                deck.add(new Card("S" + i));//there are 3 categories of tiles, each wtih digits from 1 to 9
                deck.add(new Card("D" + i));
                deck.add(new Card("N" + i));
            }
        }
        for (int i = deck.size() - 1; i > -1; i --) { //starts at the end of the array
            int random = ((int)(Math.random() * i + 1)) - 1; //finds random number that is within the length of the array. As the array gets smaller, the range of numbers for the random number gets smaller too
            shuffleDeck.add(deck.get(random)); //adds a random number from the array list with numbers from the array into the newly made array
            deck.remove(random); //removes the random number from the array list with array numbers to get rid of repeats
        }
        for (int j = 0; j < 14; j ++) {
            sHand.add(shuffleDeck.get(0));//adds a card from the shuffled deck 14 times
            sCards.get(j).setImage(new Image(sHand.get(j).getCardPath()));//sets the imageviews to display the cards in the server's hand
            shuffleDeck.remove(0);//removes the cards from the shuffled deck
        }
        for (int k = 0; k < 13; k ++) {//adds a card from the shuffled deck 13 times as the client starts off with one less card than server
            cHand.add(shuffleDeck.get(0));
            shuffleDeck.remove(0);//removes the card from the shuffled deck
            socket.sendMessage("C" + k + " " + cHand.get(k).getCardPath());//sends the client the cards they will have
        }
        for (int l = 1; l < 14; l ++) {
            cCards.get(l).setImage(new Image("resources/tile3.jpg"));//displays the back of the cards the client has
        }
        socket.sendMessage("start");//starts the game
        imgDraw.setImage(new Image("resources/tile3.jpg"));//deck of cards shows only the back of the cards
    }
    @FXML
    private void handleDraw(MouseEvent event) {
        socket.sendMessage("sDraw");//server has drawn a card
        gPane.setDisable(false);
        if (sHand.size()==14) {//server already has all the cards they can have
            lblText.setText("You Must Discard First");
        }
        else {
            if (shuffleDeck.size() > 0) {//server can draw if the deck still has cards
                sCards.get(sHand.size()).setImage(new Image(shuffleDeck.get(0).getCardPath()));//displays the card the server drew, which is the first card in the deck
                sHand.add(shuffleDeck.remove(0));//adds the card the server drew
                if (g1.checkWin(sHand)) {//checks if server won
                   lblText.setText(name1 + " Wins");//if server won, display it
                    btnStart.setDisable(false);//allows server to play again
                    socket.sendMessage("sWin");//notifies client that server has won
                }
            }
            else {//if there are no more cards left then reset the game
                lblText.setText("No More Cards");
                socket.sendMessage("reset");
                imgDiscard.setImage(null);
                reset();
            }
        }

    }
    @FXML
    private void handleDiscardDraw() {
        socket.sendMessage("sDraw");//server has drawn a card
        gPane.setDisable(false);
        if (sHand.size()==14) {//server has all the cards they can have
            lblText.setText("You Must Discard First");
        }
        else {
            if (discardDeck.size() > 0) {//server can draw if the deck still has cards
                sHand.add(discardDeck.get(discardDeck.size() - 1));//adds the card the server drew
                imgS13.setImage(new Image(discardDeck.get(discardDeck.size() - 1).getCardPath()));//displays the card the server drew, which is the last card in the array of discarded cards
                if (g1.checkWin(sHand)) {//checks if server won
                    lblText.setText(name1 + " Wins");//if server won, display it
                    socket.sendMessage("sWin");//notifies client that server has won
                }
            }
            else {//if there are no more cards left then reset the game
                lblText.setText("No More Cards");
                socket.sendMessage("reset");
                imgDiscard.setImage(null);
                reset();
            }
        }

    }
    @FXML
    private void handleMouseClick(MouseEvent t) {
        countClick++;//counts total clicks
        if (countClick % 2 != 0) {//user has only clicked one card they want to switch
            click1 = GridPane.getColumnIndex((ImageView) t.getSource());//gets the column of the card they want to switch
        }
        else {//user has selected the two cards they want to switch
            click2 = GridPane.getColumnIndex((ImageView) t.getSource());
            shiftHand(click1, click2);//swaps the two cards
        }
    }
    @FXML
    private void handleMousePress(MouseEvent t) {//code from Khush Makadia and http://java-buddy.blogspot.com/2013/07/javafx-drag-and-move-something.html
        lblText.setText("");
        orgSceneX = t.getSceneX();//the original coordinates of the image
        orgSceneY = t.getSceneY();
        orgTranslateX = ((ImageView)(t.getSource())).getTranslateX();//how much the image has moved
        orgTranslateY = ((ImageView)(t.getSource())).getTranslateY();
        imgClicked =GridPane.getColumnIndex((ImageView) t.getSource());//the column clicked
        cardSelect = sHand.get(imgClicked);//gets the card that is going to be discarded
    }
    @FXML
    private void handleMouseDrag(MouseEvent t) {
        drag = true;
        double offsetX = t.getSceneX() - orgSceneX;//the new current location of the image
        double offsetY = t.getSceneY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;//new position is how much the image has moved and new location
        double newTranslateY = orgTranslateY + offsetY;
        ((ImageView)(t.getSource())).setTranslateX(newTranslateX);
        ((ImageView)(t.getSource())).setTranslateY(newTranslateY);
    }
    private void shiftHand(int x, int y) {
        String c = sHand.get(x).getCardPath();//sets temporary variable for one element to be swapped
        sHand.get(x).changeCard(sHand.get(y).getCardPath());//swaps one element
        sHand.get(y).changeCard(c);//swaps the other
        for (int j = 0; j < 14; j ++) {
            sCards.get(j).setImage(new Image(sHand.get(j).getCardPath()));//redraws the hand
        }
    }
    @FXML
    private void handleMouseRelease(MouseEvent t) {
        double x = t.getSceneX();
        double y = t.getSceneY();//current location of the mouse
        if (x < 520.0 && x > 400.0 && y < 430 && y > 340) {//if mouse is within the bounds for the discard pile, then server can discard
            if (sHand.size() != 14) {//cannot discard if server's hand is not full
                lblText.setText("You Must Draw First");
            }
            else {
                imgDiscard.setImage(new Image(cardSelect.getCardPath()));//sets discard pile image to be of the image that is getting discarded
                sHand.remove(cardSelect);//removes the card to be discarded from server's hand
                discardDeck.add(cardSelect);//adds the discarded card to discard array
                for (int j = 0; j < 14; j ++) {
                    sCards.get(j).setImage(null);//resets server;s hand display
                }
                for (int j = 0; j < 13; j ++) {
                    sCards.get(j).setImage(new Image(sHand.get(j).getCardPath()));//redraws server's hand display
                }
                socket.sendMessage("cTurn" + cardSelect.getCardPath());//notifies client it is their turn and sends the card that was discarded
                lblText.setText(name2 + "'s Turn");
                socket.sendMessage("sDiscard");
                gPane.setDisable(true);
            }
        }
        ((ImageView)(t.getSource())).setTranslateX(orgTranslateX);//returns the image that was dragged back to original position
        ((ImageView)(t.getSource())).setTranslateY(0.0);
        drag  = false;
    }
    public void reset() {//resets the game
        sHand.clear();
        cHand.clear();
        sCards.clear();
        cCards.clear();
        countClick = 0;
        deal();
    }
}
