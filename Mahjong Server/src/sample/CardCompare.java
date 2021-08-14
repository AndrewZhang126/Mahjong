package sample;

import java.util.Comparator;

public class CardCompare implements Comparator<Card> {//code from https://www.geeksforgeeks.org/collections-sort-java-examples/
    @Override
    public int compare(Card o1, Card o2) {
        return o1.getCardNumber() - o2.getCardNumber();
    }
}
