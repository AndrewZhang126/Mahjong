package sample;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Gameplay {
    int countWin;
    public boolean checkWin(ArrayList<Card> hand) {
        countWin = 0;
        ArrayList<Card> copyHand = new ArrayList<>();
        copyArray(hand, copyHand);
        ArrayList<Card> sArray = new ArrayList<>();
        ArrayList<Card> nArray = new ArrayList<>();
        ArrayList<Card> dArray = new ArrayList<>();
        for (int i = 0; i < 14; i ++) {//separates the three card types into three separate arrays
            if (hand.get(i).getCardType().equals("S")) {//all cards of stick type
                sArray.add(hand.get(i));
            }
            if (hand.get(i).getCardType().equals("N")) {//all cards of letter type
                nArray.add(hand.get(i));
            }
            if (hand.get(i).getCardType().equals("D")) {//all cards of circle type
                dArray.add(hand.get(i));
            }
        }
        checkArraay(sArray);//checks each array to see if they have 3 of the same number or 3 consecutive numbers
        checkArraay(dArray);
        checkArraay(nArray);
        if (countWin == 4) {//player must have 4 pairs that satisfy win condition in order to win
            if (sArray.size() == 2) {//means that there are only 2 cards left so win conditions have been satisfied
                if (sArray.get(0).getCardName().equals(sArray.get(1).getCardName())) {//if last two cards are the same, then player has won
                    return true;
                }
            }
            if (dArray.size() == 2) {
                if (dArray.get(0).getCardName().equals(dArray.get(1).getCardName())) {//if last two cards are the same, then player has won
                    return true;
                }
            }
            if (nArray.size() == 2) {
                if (nArray.get(0).getCardName().equals(nArray.get(1).getCardName())) {//if last two cards are the same, then player has won
                    return true;
                }
            }
        }
        return false;//player did not win if none of conditions were met
    }
    public void checkArraay(ArrayList<Card> array) {//checks to see if the array satisfies any of the win conditions
        boolean consecutive = false;
        int c = array.size();//this loop is for the win condition for 3 of the same card
        if (array.size() >= 3) {//only checks the array if it is greater than 3 because only a pair of 3 cards can satisfy win conditions
            while (array.size() >= 3 && c >= 0) {//runs the loop while there are still more than 3 cards in the array or until every card has been checked
                for (int i = 0; i < array.size(); i ++){
                    c--;
                    if (checkSame(array, array.get(i))) {//checks if a card appears 3 times
                        countWin++;//one pair has satisfied a win condition
                        for (int j = 0; j < 3; j ++) {//if card does appear three times then 3 of them are removed from the array
                            removeItem(array, array.get(i));
                        }
                    }
                }
            }
            int d = array.size();//this loop is for the win condition for 3 cards in numerical order
            while (array.size() >= 3 && d >= 0) {//runs the loop while there are still more than 3 cards in the array or until every card has been checked
                Collections.sort(array, new CardCompare());//sorts the array in numerical order
                int index = -1;
                int index1 = -1;
                int index2 = -1;
                for (int i = 0; i < array.size(); i ++) {
                    d --;
                    if (checkConsecutive(array, array.get(i), 1) != -1) {//checks to see if there is a card that is 1 greater than another
                        index1 = checkConsecutive(array, array.get(i), 1);//sets index of card that is one greater
                        index = i;//sets index of the card being checked
                        consecutive = true;
                        break;//once one is found, exit the loop
                    }
                }
                if (consecutive && index != -1) {//if a successful sonsecutive pair has been found then check for another consecutive card
                    if (checkConsecutive(array, array.get(index), 2) != -1) {//checks to see if there is a card that is 2 greater than original card being checked
                        index2 = checkConsecutive(array, array.get(index), 2);//if one is found, set index of card that is 2 greater
                    }
                }
                if (index2 != -1) {//if card that is 2 greater is found, then a consecutive pair of 3 cards has been found
                    countWin++;//one pair has satisfied a win condition
                    Card card = array.get(index);
                    Card card1 = array.get(index1);
                    Card card2 = array.get(index2);
                    array.remove(card);
                    array.remove(card1);
                    array.remove(card2);//removes all the cards of the pair from the array
                }
            }
        }
    }
    public int checkConsecutive(ArrayList<Card> h, Card c, int d) {//checks to see if there is a number that is one greater than the one being checked
        for (int i = 0; i < h.size(); i ++) {
            if (h.get(i).getCardNumber() == c.getCardNumber() + d) {
                return i;//returns the index of the card if a consecutive one is found
            }
        }
        return -1;

    }
    public boolean checkSame(ArrayList<Card> h, Card c) {//checks to see if a card appears 3 times in the array
        int count = 0;
        for (int i = 0; i < h.size(); i ++) {
            if (c.getCardName().equals(h.get(i).getCardName())) {//checks if each card is the same as the card that is being checked
                count++;
            }
        }
        if (count >= 3) {//there are at least 3 cards that are the same as the card being checked
            return true;
        }
        else {
            return false;
        }
    }
    public void copyArray(ArrayList<Card> one, ArrayList<Card> two) {//copies the elements of one array to the other
        for (Card c: one) {
            two.add(c);
        }
    }
    public void removeItem(ArrayList<Card> x, Card y) {//removes an item from an array
        x.remove(y);
    }
}
