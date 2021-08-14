package sample;
public class Card {
    
    String cType;
    int cNumber;
    String pathName;
    String cName;
    
    // contructor that starts the formation of the card path
    // the methods of this class allow the main class to obtain certain properties of a 
    // single card.
    public Card(String cName)
    {
        pathName = "resources/" + cName + ".gif";
        this.cName = cName;
        parseCardName(cName);
    }
    // 
    public void parseCardName(String cName)
    {
        cType = cName.substring(0,1); // creates the color of a card
        cNumber = Integer.parseInt(cName.substring(1)); // creates the value of a card       
    }
    public int getCardNumber() // sends the card value when called
    {
        return cNumber;
    }
    public void changeCard(String newName) {
        pathName = newName;
    }
    public String getCardType() // sends the card color when called
    {
        return cType;
    }
    public String getCardPath() // sends card path when called/ loaction in resources
    {
        return pathName;
    }
    public String getCardName()
    {
        return cName;
    }

    
}
