/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;
import socketfx.Constants;
import socketfx.FxSocketClient;
import socketfx.SocketListener;

/**
 * FXML Controller class
 *
 * @author jtconnor
 */
public class ClientGUIController implements Initializable {


    @FXML
    private Button sendButton, btnStart;
    @FXML
    private TextField sendTextField;
    @FXML
    private Button connectButton;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField hostTextField;
    @FXML
    private Label lblCName, lblSName, lblMessages;
    @FXML
    private AnchorPane cAnchor;
    @FXML
    private ImageView imgS0,imgS1,imgS2,imgS3,imgS4,imgS5,imgS6,imgS7,imgS8,imgS9,imgS10,imgS11,imgS12,imgS13,
            
                      imgC0,imgC1,imgC2,imgC3,imgC4, imgC5,imgC6,imgC7,imgC8, imgC9 ,imgC10 ,imgC11,imgC12, imgC13, imgDraw, imgDiscard,imgBack;
    @FXML
    private GridPane gPane;

    
    private final static Logger LOGGER =
            Logger.getLogger(MethodHandles.lookup().lookupClass().getName());



    private boolean isConnected, turn, serverUNO = false, clientUNO = false;

    public enum ConnectionDisplayState {

        DISCONNECTED, WAITING, CONNECTED, AUTOCONNECTED, AUTOWAITING
    }

    private FxSocketClient socket;
    private void connect() {
        socket = new FxSocketClient(new FxSocketListener(),
                hostTextField.getText(),
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
            if(line.substring(0,3).equals("nm1")){//this is the server's name
                name1 = line.substring(3);
                lblSName.setText(line.substring(3));
            }
            else if (line.substring(0, 3).equals("msg")) {//server has sent a message
                lblMessages.setText(line.substring(3));
            }
            else if (line.substring(0, 2).equals("C0")) {//this is the client's first card
                imgC0.setImage(new Image(line.substring(3)));//displays the client's first card
                cHand.add(new Card(line.substring(13, 15)));//adds the card to client's hand
            }
            else if (line.substring(0, 3).equals("C1 ")) {//this is the client's second card
                imgC1.setImage(new Image(line.substring(3)));//displays the client's second card
                cHand.add(new Card(line.substring(13, 15)));//adds the card to client's hand
            }
            else if (line.substring(0, 2).equals("C2")) {//this is the client's third card
                imgC2.setImage(new Image(line.substring(3)));//displays the client's third card
                cHand.add(new Card(line.substring(13, 15)));//adds the card to client's hand
            }
            else if (line.substring(0, 2).equals("C3")) {//this is the client's fourth card
                imgC3.setImage(new Image(line.substring(3)));//displays the client's fourth card
                cHand.add(new Card(line.substring(13, 15)));//adds the card to client's hand
            }
            else if (line.substring(0, 2).equals("C4")) {//this is the client's fifth card
                imgC4.setImage(new Image(line.substring(3)));//displays the client's fifth card
                cHand.add(new Card(line.substring(13, 15)));//adds the card to client's hand
            }
            else if (line.substring(0, 2).equals("C5")) {//this is the client's sixth card
                imgC5.setImage(new Image(line.substring(3)));//displays the client's sixth card
                cHand.add(new Card(line.substring(13, 15)));//adds the card to client's hand
            }
            else if (line.substring(0, 2).equals("C6")) {//this is the client's seventh card
                imgC6.setImage(new Image(line.substring(3)));//displays the client's seventh card
                cHand.add(new Card(line.substring(13, 15)));//adds the card to client's hand
            }
            else if (line.substring(0, 2).equals("C7")) {//this is the client's eighth card
                imgC7.setImage(new Image(line.substring(3)));//displays the client's eighth card
                cHand.add(new Card(line.substring(13, 15)));//adds the card to client's hand
            }
            else if (line.substring(0, 2).equals("C8")) {//this is the client's ninth card
                imgC8.setImage(new Image(line.substring(3)));//displays the client's ninth card
                cHand.add(new Card(line.substring(13, 15)));//adds the card to client's hand
            }
            else if (line.substring(0, 2).equals("C9")) {//this is the client's tenth card
                imgC9.setImage(new Image(line.substring(3)));//displays the client's tenth card
                cHand.add(new Card(line.substring(13, 15)));//adds the card to client's hand
            }
            else if (line.substring(0, 3).equals("C10")) {//this is the client's eleventh card
                imgC10.setImage(new Image(line.substring(4)));//displays the client's eleventh card
                cHand.add(new Card(line.substring(14, 16)));//adds the card to client's hand
            }
            else if (line.substring(0, 3).equals("C11")) {//this is the client's twelfth card
                imgC11.setImage(new Image(line.substring(4)));//displays the client's twelfth card
                cHand.add(new Card(line.substring(14, 16)));//adds the card to client's hand
            }
            else if (line.substring(0, 3).equals("C12")) {//this is the client's thirteenth card
                imgC12.setImage(new Image(line.substring(4)));//displays the client's thirteenth card
                cHand.add(new Card(line.substring(14, 16)));//adds the card to client's hand
            }
            else if (line.substring(0, 4).equals("card")) {//client has drawn a card
                imgC13.setImage(new Image(line.substring(4)));//displays the card the client drew
                cHand.add(new Card(line.substring(14, 16)));//adds the card to client's hand
            }
            else if (line.equals("no cards left")) {//there are no cards left in deck
                lblText.setText("No More Cards");
            }
            else if (line.equals("start")) {//game has started
                handleStart();
            }
            else if (line.equals("sTurn")) {//server's turn
                gPane.setDisable(true);
            }
            else if (line.equals("sDraw")) {//server has drawn
                sCards.get(0).setImage(new Image("resources/tile.jpg"));//adds card to server's hand
            }
            else if (line.equals("sDiscard")) {//server has discarded
                sCards.get(0).setImage(null);//removes card from server's hand
            }
            else if (line.equals("sWin")) {//reset game
                lblText.setText(name1 + " Wins");
                btnStart.setDisable(false);
            }
            else if (line.equals("cWin")) {//client has won
                lblText.setText(name2 + " Wins");//displays client has won
                btnStart.setDisable(false);
            }
            else if (line.equals("reset")) {//reset game
                reset();
            }
            else if (line.substring(0, 5).equals("cTurn")) {//client's turn
                sCards.get(0).setImage(new Image("resources/tile.jpg"));//se
                lblText.setText("");
                gPane.setDisable(false);
                imgDiscard.setImage(new Image(line.substring(5)));//discard pile now has the card the server discarded
                discardDeck.add(new Card(line.substring(15, 17)));//adds discarded card to discard array
            }
        }
        @Override
        public void onClosedStatus(boolean isClosed) {
        }
    }
    @FXML
    private void handleSendMessageButton(ActionEvent event) {
        if (!sendTextField.getText().equals("")) {//client has sent message
            String x = sendTextField.getText();
            socket.sendMessage("msg" + x);
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
        btnStart.setDisable(true);
        lblText.setText("");
        name2 = txtName.getText();//sets name for server and client
        lblCName.setText(name2);
        socket.sendMessage("nm2" + name2);
        sCards.add(imgS0);//adds imageviews to arrays
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
        imgDraw.setImage(new Image("resources/tile.jpg"));
        for (int l = 0; l < 14; l ++) {
            sCards.get(l).setImage(new Image("resources/tile.jpg"));//displays server's hand
        }
        gPane.setDisable(true);
    }
    @FXML
    public TextField txtName;
    @FXML
    public Label lblName1, lblName2, lblText;
    private String name1;
    private String name2;
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
    @FXML
    private void handleDraw(MouseEvent event) {
        gPane.setDisable(false);
        if (cHand.size()==14) {//client has all cards they can have
            lblText.setText("You Must Discard First");
        }
        else {
            socket.sendMessage("draw");//notfies server that client has drawn from deck and server will add drawn card to client's hand
            for (int i = 0; i < cHand.size(); i ++) {
                cCards.get(i).setImage(new Image(cHand.get(i).getCardPath()));//redisplays client's hand
            }
            socket.sendMessage("win");//checks if client has won
        }
    }
    @FXML
    private void handleDiscardDraw() {
        gPane.setDisable(false);
        if (cHand.size()==14) {//client has all cards they can have
            lblText.setText("You Must Discard First");
        }
        else {
            socket.sendMessage("draw2");//notfies server that client has drawn from discard pile and server will add drawn card to client's hand
            for (int i = 0; i < cHand.size(); i ++) {
                cCards.get(i).setImage(new Image(cHand.get(i).getCardPath()));//redisplays client's hand
            }
            socket.sendMessage("win");//checks if client has won
        }
    }
    @FXML
    private void handleMouseClick(MouseEvent t) {
        countClick++;//counts total clicks
        if (countClick % 2 != 0) {//user has only selected one card they want to swap
            click1 = GridPane.getColumnIndex((ImageView) t.getSource());//gets column of card client wants to swap
        }
        else {//client has selected two cards they want to swap
            click2 = GridPane.getColumnIndex((ImageView) t.getSource());
            shiftHand(click1, click2);//swaps two cards
        }
    }
    @FXML
    private void handleMousePress(MouseEvent t) {
        lblText.setText("");
        orgSceneX = t.getSceneX();//original coordinates of image
        orgSceneY = t.getSceneY();
        orgTranslateX = ((ImageView)(t.getSource())).getTranslateX();//how much image has moved
        orgTranslateY = ((ImageView)(t.getSource())).getTranslateY();
        imgClicked =GridPane.getColumnIndex((ImageView) t.getSource());//gets column of card selected
        cardSelect = cHand.get(imgClicked);//gets card to be discarded
    }
    @FXML
    private void handleMouseDrag(MouseEvent t) {
        drag = true;
        double offsetX = t.getSceneX() - orgSceneX;//how much image has moved from original position
        double offsetY = t.getSceneY() - orgSceneY;
        double newTranslateX = orgTranslateX + offsetX;//new position of image
        double newTranslateY = orgTranslateY + offsetY;
        ((ImageView)(t.getSource())).setTranslateX(newTranslateX);//draws image as it moves
        ((ImageView)(t.getSource())).setTranslateY(newTranslateY);
    }
    private void shiftHand(int x, int y) {
        String c = cHand.get(x).getCardPath();//sets temporary variable from a card to be swapped
        cHand.get(x).changeCard(cHand.get(y).getCardPath());//swaps one card
        cHand.get(y).changeCard(c);//swaps other card
        for (int j = 0; j < 14; j ++) {
            cCards.get(j).setImage(new Image(cHand.get(j).getCardPath()));//redisplays hand
        }
    }
    @FXML
    private void handleMouseRelease(MouseEvent t) {
        double x = t.getSceneX();
        double y = t.getSceneY();//current location of mouse
        if (x < 520.0 && x > 400.0 && y < 430 && y > 340) {//if mouse is within bounds for discard pile then client can discard
            if (cHand.size() != 14) {//cannot discard if hand is not full
                lblText.setText("You Must Draw First");
            }
            else {
                imgDiscard.setImage(new Image(cardSelect.getCardPath()));//sets discard pile to be card that is getting discarded
                cHand.remove(cardSelect);//removes card that is being discarded from client's hand
                discardDeck.add(cardSelect);//adds card to be discarded to discard array
                for (int j = 0; j < 14; j ++) {
                    cCards.get(j).setImage(null);//clears client's hand display
                }
                for (int j = 0; j < 13; j ++) {
                    cCards.get(j).setImage(new Image(cHand.get(j).getCardPath()));//redraws client's hand
                }
                socket.sendMessage("sTurn" + cardSelect.getCardPath());//notifies server it is their turn with card that got discarded
                lblText.setText(name1 + "'s Turn");
                gPane.setDisable(true);
            }
        }
        ((ImageView)(t.getSource())).setTranslateX(orgTranslateX);//resets image that was dragged back to original position
        ((ImageView)(t.getSource())).setTranslateY(0.0);
        drag  = false;
    }
    public void reset() {//resets game
        sHand.clear();
        cHand.clear();
        sCards.clear();
        cCards.clear();
        countClick = 0;
    }
}
