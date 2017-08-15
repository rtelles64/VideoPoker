package PJ4;

import java.util.*;


//=================================================================================
/** class PlayingCardException: It is used for errors related to Card and Deck objects
 *  Do not modify this class!
 */
class PlayingCardException extends Exception {

    /* Constructor to create a PlayingCardException object */
    PlayingCardException (){
		super ();
    }

    PlayingCardException ( String reason ){
		super ( reason );
    }
}



//=================================================================================
/** class Card : for creating playing card objects
 *  it is an immutable class.
 *  Rank - valid values are 1 to 13
 *  Suit - valid values are 0 to 4
 *  Do not modify this class!
 */
class Card {

    /* constant suits and ranks */
    static final String[] Suit = {"Joker","Clubs", "Diamonds", "Hearts", "Spades" };
    static final String[] Rank = {"","A","2","3","4","5","6","7","8","9","10","J","Q","K"};

    /* Data field of a card: rank and suit */
    private int cardRank;  /* values: 1-13 (see Rank[] above) */
    private int cardSuit;  /* values: 0-4  (see Suit[] above) */

    /* Constructor to create a card */
    /* throw PlayingCardException if rank or suit is invalid */
    public Card(int suit, int rank) throws PlayingCardException {

        // suit =0 is joker, rank must be 1 or 2
	if (suit==0) {
	    if ((rank <1) || (rank >2))
	       throw new PlayingCardException("Invalid rank for Joker:"+rank);
	    cardRank=rank;
	    cardSuit=0;
        } else {

	    if ((rank < 1) || (rank > 13))
		throw new PlayingCardException("Invalid rank:"+rank);
	    else
        	cardRank = rank;

	    if ((suit < 1) || (suit > 4))
		throw new PlayingCardException("Invalid suit:"+suit);
	    else
        	cardSuit = suit;
   	}
    }

    /* Accessor and toString */
    /* You may impelemnt equals(), but it will not be used */
    public int getRank() { return cardRank; }
    public int getSuit() { return cardSuit; }
    public String toString() {
	if (cardSuit == 0) return Suit[cardSuit]+" #"+cardRank;
        else return Rank[cardRank] + " " + Suit[cardSuit];
    }


    /* Few quick tests here */
    public static void main(String args[])
    {

	try {
	    Card c1 = new Card(4,1);    // A Spades
	    System.out.println(c1);
	    c1 = new Card(1,10);	// 10 Clubs
	    System.out.println(c1);
	    c1 = new Card(0,2);		// Joker #2
	    System.out.println(c1);
	    c1 = new Card(5,10);        // generate exception here
	}
	catch (PlayingCardException e)
	{
	    System.out.println("PlayingCardException: "+e.getMessage());
	}
    }
}



//=================================================================================
/** class Decks represents : n decks of 52 (or 54) playing cards
 *  Use class Card to construct n * 52 (or 54) playing cards!
 *
 *  Do not add new data fields!
 *  Do not modify any methods
 *  You may add private methods
 */

class Decks {

    /* this is used to keep track of original n*52 or n*54 cards */
    private List<Card> originalDecks;

    /* this starts with copying cards from originalDecks */
    /* it is used to play the card game                  */
    /* see reset(): resets gameDecks to originalDecks    */
    private List<Card> gameDecks;

    /* number of decks in this object */
    private int numberDecks;
    private boolean withJokers;
    
    private int getJokerRank() throws PlayingCardException {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a Joker rank (1 or 2): ");
        int rank = reader.nextInt(); // Scans the next token of the input as an int.
        
        if ((rank <1) || (rank >2))
	       throw new PlayingCardException("Invalid rank for Joker:"+rank);
            
        return rank;
    }
    
    private List<Card> initializeDeckWithoutJokers() {
        originalDecks = new ArrayList<>(52);
        for (int suit = 1; suit <= 4; suit++) { //for every suit starting with Suit[1] "Clubs"
            for (int rank = 1; rank <= 13; rank++) { //for every rank, A-K
                try{
                    Card c = new Card(suit,rank); //create a new card for each suit and rank
                    this.originalDecks.add(c); //add new card to original decks
                } catch (PlayingCardException e) { //throw exception if task fails
                    System.out.print("Unable to create cards.");
                }
            }
        }
        
        return originalDecks;
    }
    
    private List<Card> initializeDeckWithJokers(int jokerRank) {
        originalDecks = new ArrayList<>(54);
        
        for (int suit = 0; suit <= 4; suit++) {
            if (suit == 0) {
                try {
                    Card c = new Card (0,jokerRank);
                    while (originalDecks.size() != 2) {           
                    this.originalDecks.add(c);
                    }
                } catch (PlayingCardException e) {
                    System.out.println("Invalid Joker Rank. Unable to create Joker");
                }            
            } else {
                for (int rank = 1; rank <= 13; rank++) { //for every rank, A-K
                    try{
                        Card c = new Card(suit,rank); //create a new card for each suit and rank
                        this.originalDecks.add(c); //add new card to original decks
                    } catch (PlayingCardException e) { //throw exception if task fails
                        System.out.print("Unable to create cards.");
                    }
                }
            }
        }
        
        return originalDecks;
    }
    
    /**
     * Constructor: Creates one deck of 52 or 54  (withJokers = false or true)
     *              playing cards in originalDecks and copy them to gameDecks.
     *              initialize numberDecks=1
     * Note: You need to catch PlayingCardException from Card constructor
     *	     Use ArrayList for both originalDecks & gameDecks
     */
    public Decks(boolean withJokers)
    {
        // implement this method!
        numberDecks = 1;
        this.withJokers = withJokers;
        gameDecks = new ArrayList<>();
        if(withJokers) {
            try {
                int jokerRank = getJokerRank();            
                gameDecks.addAll(initializeDeckWithJokers(jokerRank));
            } catch (PlayingCardException e) {
                System.out.println("Invalid Joker rank. Unable to create Joker");
            }
        } else {
            gameDecks.addAll(initializeDeckWithoutJokers());
        }
        

    }


    /**
     * Constructor: Creates n decks (54 or 52 cards each deck - with or without Jokers)
     *              of playing cards in originalDecks and copy them to gameDecks.
     *              initialize numberDecks=n
     * Note: You need to catch PlayingCardException from Card constructor
     *	     Use ArrayList for both originalDecks & gameDecks
     */
    public Decks(int n, boolean withJokers)
    {
        // implement this method!
        numberDecks = 0;
        this.withJokers = withJokers;
        gameDecks = new ArrayList<>();
        if(withJokers) {
            try {
                int jokerRank = getJokerRank();
                while (numberDecks < n) {
                    gameDecks.addAll(initializeDeckWithJokers(jokerRank));
                    numberDecks++;
                }
            } catch (PlayingCardException e) {
                System.out.println("Iinvalid Joker Rank. Unable to create Joker");
            }
        } else {
            while (numberDecks != n) {
                gameDecks.addAll(initializeDeckWithoutJokers());
                numberDecks++;
            }
        }
    }


    /**
     * Task: Shuffles cards in gameDecks.
     * Hint: Look at java.util.Collections
     */
    public void shuffle()
    {
        // implement this method!
        Collections.shuffle(gameDecks);
    }

    /**
     * Task: Deals cards from the gameDecks.
     *
     * @param numberCards number of cards to deal
     * @return a list containing cards that were dealt
     * @throw PlayingCardException if numberCard > number of remaining cards
     *
     * Note: You need to create ArrayList to stored dealt cards
     *       and should removed dealt cards from gameDecks
     *
     */
    public List<Card> deal(int numberCards) throws PlayingCardException
    {
        // implement this method!
        List cardsDealt = new ArrayList<Card>();
        if (numberCards > gameDecks.size()) {
            throw new PlayingCardException ("Not enough cards to be dealt");
        }
        
        
        int i = ((gameDecks.size()) - numberCards);
        while (gameDecks.size() > i) {
            cardsDealt.add(gameDecks.remove(i));
        }
        
        return cardsDealt;
    }

    /**
     * Task: Resets gameDecks by getting all cards from the originalDecks.
     */
    public void reset()
    {
        // implement this method!
        gameDecks = originalDecks;

    }

    /**
     * Task: Return number of decks.
     */
    public int getNumberDecks()
    {
	return numberDecks;
    }

    /**
     * Task: Return withJokers.
     */
    public boolean getWithJokers()
    {
	return withJokers;
    }

    /**
     * Task: Return number of remaining cards in gameDecks.
     */
    public int remainSize()
    {
	return gameDecks.size();
    }

    /**
     * Task: Returns a string representing cards in the gameDecks
     */
    public String toString()
    {
	return ""+gameDecks;
    }


    /* Quick test                    */
    /*                               */
    /* Do not modify these tests     */
    /* Generate 2 decks of 54 cards  */
    /* Loop 2 times:                 */
    /*   Deal 27 cards for 5 times   */
    /*   Expect exception at 5th time*/
    /*   reset()                     */

    public static void main(String args[]) {
        /*
        //This is a personal Check (with jokers)
        Decks deck = new Decks(2, true);    
        for (int i = 0; i < 2; i++) {
            for(int j = 0; j <= 53; j++){
                System.out.println(deck.gameDecks.get(j));
            }
        }
        */
        /*
        //This is a personal Check (without jokers)
        Decks deck = new Decks(2, false);    
        for (int i = 0; i < 2; i++) {
            for(int j = 0; j <= 51; j++){
                System.out.println(deck.gameDecks.get(j));
            }
        }
        */
        
        System.out.println("*******    Create 2 decks of cards      ********\n");
        Decks decks  = new Decks(2, true);
        System.out.println("getNumberDecks:" + decks.getNumberDecks());
        System.out.println("getWithJokers:" + decks.getWithJokers());

	for (int j=0; j < 2; j++)
	{
        	System.out.println("\n************************************************\n");
        	System.out.println("Loop # " + j + "\n");
		System.out.println("Before shuffle:"+decks.remainSize()+" cards");
		System.out.println("\n\t"+decks);
        	System.out.println("\n==============================================\n");

                int numHands = 5;
                int cardsPerHand = 27;
                
                
        	for (int i=0; i < numHands; i++)
	 	{
                        decks.shuffle();
		        System.out.println("After shuffle:"+decks.remainSize()+" cards");
		        System.out.println("\n\t"+decks);
			try {
            		    System.out.println("\n\nHand "+i+":"+cardsPerHand+" cards");
            		    System.out.println("\n\t"+decks.deal(cardsPerHand));
            		    System.out.println("\n\nRemain:"+decks.remainSize()+" cards");
		            System.out.println("\n\t"+decks);
        	            System.out.println("\n==============================================\n");
			}
			catch (PlayingCardException e)
			{
		 	        System.out.println("*** In catch block:PlayingCardException:Error Msg: "+e.getMessage());
			}
		}


		decks.reset();
	}
    }
  }
