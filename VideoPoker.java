package PJ4;

import java.util.*;

/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.freeslots.com/poker.htm
 *
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker.
 * The dealer uses a 52-card deck, which is played fresh after each playerHand.
 * The player is dealt one five-card poker playerHand.
 * After the first draw, which is automatic, you may hold any of the cards and draw
 * again to replace the cards that you haven't chosen to hold.
 * Your cards are compared to a table of winning combinations.
 * The object is to get the best possible combination so that you earn the highest
 * payout on the bet you placed.
 *
 * Winning Combinations
 *
 * 1. One Pair: one pair of the same card
 * 2. Two Pair: two sets of pairs of the same card denomination.
 * 3. Three of a Kind: three cards of the same denomination.
 * 4. Straight: five consecutive denomination cards of different suit.
 * 5. Flush: five non-consecutive denomination cards of the same suit.
 * 6. Full House: a set of three cards of the same denomination plus
 * 	a set of two cards of the same denomination.
 * 7. Four of a kind: four cards of the same denomination.
 * 8. Straight Flush: five consecutive denomination cards of the same suit.
 * 9. Royal Flush: five consecutive denomination cards of the same suit,
 * 	starting from 10 and ending with an ace
 *
 */
 /* This is the video poker game class.
 * It uses Decks and Card objects to implement video poker game.
 * Please do not modify any data fields or defined methods
 * You may add new data fields and methods
 * Note: You must implement defined methods
 */
public class VideoPoker {

    // default constant values
    private static final int startingBalance = 1000;
    private static final int numberOfCards = 5;

    // default constant payout value and playerHand types
    private static final int[] multipliers = {1, 2, 3, 5, 6, 10, 25, 50, 1000};
    private static final String[] goodHandTypes = {
        "One Pair", "Two Pairs", "Three of a Kind", "Straight", "Flush	",
        "Full House", "Four of a Kind", "Straight Flush", "Royal Flush"};

    // must use only one deck
    private final Decks oneDeck;

    // holding current poker 5-card hand, balance, bet
    private List<Card> playerHand;
    private int playerBalance;
    private int playerBet;
    
    //boolean to displayTable
    boolean showPayoutTable = true;

    /**
     * default constructor, set balance = startingBalance
     */
    public VideoPoker() {
        this(startingBalance);
    }

    /**
     * constructor, set given balance
     */
    public VideoPoker(int balance) {
        this.playerBalance = balance;
        oneDeck = new Decks(1, false);
    }

    /**
     * This display the payout table based on multipliers and goodHandTypes
     * arrays
     */
    private void showPayoutTable() {
        System.out.println("\n\n");
        System.out.println("Payout Table   	      Multiplier   ");
        System.out.println("=======================================");
        int size = multipliers.length;
        for (int i = size - 1; i >= 0; i--) {
            System.out.println(goodHandTypes[i] + "\t|\t" + multipliers[i]);
        }
        System.out.println("\n\n");
    }

    /**
     * Check current playerHand using multipliers and goodHandTypes arrays Must
     * print yourHandType (default is "Sorry, you lost") at the end of function.
     * This can be checked by testCheckHands() and main() method.
     */
    private void checkHands() {
        // implement this method!
        if(royalFlush(playerHand))
        {
            System.out.println("Royal Flush!");
            playerBalance += multipliers[8] * playerBet;
        } 
        else if (straightFlush(playerHand))
        {
            System.out.println("Straight Flush!");
            playerBalance += multipliers[7] * playerBet;
        }
        else if (fourKind(playerHand))
        {
            System.out.println("Four of a Kind!");
            playerBalance += multipliers[6] * playerBet;
        }
        else if (fullHouse(playerHand))
        {
            System.out.println("Full House!");
            playerBalance += multipliers[5] * playerBet;
        }
        else if (flush(playerHand))
        {
            System.out.println("Flush!");
            playerBalance += multipliers[4] * playerBet;
        }
        else if (straight(playerHand))
        {
            System.out.println("Straight!");
            playerBalance += multipliers[3] * playerBet;
        }
        else if (threeKind(playerHand))
        {
            System.out.println("Three of a Kind!");
            playerBalance += multipliers[2] * playerBet;
        }
        else if (twoPair(playerHand))
        {
            System.out.println("Two Pairs!");
            playerBalance += multipliers[1] * playerBet;
        }
        else if (onePair(playerHand))
        {
            System.out.println("One Pair!");
            playerBalance += multipliers[0] * playerBet;
        }
        else
        {
            System.out.println("Sorry, you lost!");
        }
    }

    /**
     * ***********************************************
     * add new private methods here ....
     *
     ************************************************
     */
    /***********************
        Hand Type Methods 
     ***********************/
    //sort by rank
    private static void rankSort(List<Card> playerHand) {
        int i, j, minimum;
        for (i = 0; i < playerHand.size(); i++)
        {
            minimum = i;
            for (j = i + 1; j < playerHand.size(); j++)
            {
                if (playerHand.get(j).getRank() < playerHand.get(minimum).getRank())
                {
                    minimum = j;
                }
            }            
            
            Card temp = playerHand.get(i);
            playerHand.set(i, playerHand.get(minimum));
            playerHand.set(minimum, temp);
        }
    }
    
    //sort by suit
    private static void suitSort(List<Card> playerHand) {
        int i, j, minimum;
        
        for (i = 0; i < playerHand.size(); i++) {
            
            minimum = i;
            
            for (j = i + 1; j < playerHand.size(); j++) {
                if (playerHand.get(j).getSuit() < playerHand.get(minimum).getSuit()) {
                    minimum = j;
                }
            }
            
            Card temp = playerHand.get(i);
            playerHand.set(i, playerHand.get(minimum));
            playerHand.set(minimum, temp);
        }
    }
    
    //check if hand has Ace
    private boolean hasAce (List<Card> playerHand) {
        for (int i = 0; i < playerHand.size(); i++)
        {
            if (playerHand.get(i).getRank() == 1)
            {
                return true;
            }
        }
        return false;
    }
    
    //check if hand is royalFlush
    private boolean royalFlush(List<Card> playerHand) {
        return (straight(playerHand) && flush(playerHand) && hasAce(playerHand));
    }
    
    //check if hand is straightFlush
    private boolean straightFlush(List<Card> playerHand) {
        return (straight(playerHand) && flush(playerHand));
    }
    
    //check if hand is fourKind
    private boolean fourKind (List<Card> playerHand) {
        boolean c1, c2;
        
        if (playerHand.size() != 5) return false;
        
        rankSort(playerHand);
        
        //Check if x x x x b
        c1 = playerHand.get(0).getRank() == playerHand.get(1).getRank() &&
             playerHand.get(1).getRank() == playerHand.get(2).getRank() &&
             playerHand.get(2).getRank() == playerHand.get(3).getRank();
        //Check if a x x x x
        c2 = playerHand.get(1).getRank() == playerHand.get(2).getRank() &&
             playerHand.get(2).getRank() == playerHand.get(3).getRank() &&
             playerHand.get(3).getRank() == playerHand.get(4).getRank();
        
        return (c1 || c2);
    }
    
    //check if hand is fullHouse
    private boolean fullHouse (List<Card> playerHand) {
        boolean c1, c2;
        
        if (playerHand.size() != 5) return false;
        
        rankSort(playerHand);
        
        //Check if x x x y y
        c1 = playerHand.get(0).getRank() == playerHand.get(1).getRank() &&
             playerHand.get(1).getRank() == playerHand.get(2).getRank() &&
             playerHand.get(3).getRank() == playerHand.get(4).getRank();
        //Check if x x y y y
        c2 = playerHand.get(0).getRank() == playerHand.get(1).getRank() &&
             playerHand.get(2).getRank() == playerHand.get(3).getRank() &&
             playerHand.get(3).getRank() == playerHand.get(4).getRank();
        
        return (c1 || c2);
    }
    
    //check if hand is flush
    private boolean flush(List<Card> playerHand) {
        if (playerHand.size() != 5) return false;
        
        suitSort(playerHand);
        
        return (playerHand.get(0).getSuit() == playerHand.get(4).getSuit());
    }
    
    //check if hand is straight
    private boolean straight(List<Card> playerHand) {
        int i, testRank;
        
        if (playerHand.size() != 5) return false;
        
        rankSort(playerHand);
        
        //Check for an Ace
        if (playerHand.get(0).getRank() == 1) {
            /*Check for straight using Ace*/
            //Check for 2, 3, 4, 5
            boolean c1 = playerHand.get(1).getRank() == 2 &&
                         playerHand.get(2).getRank() == 3 &&
                         playerHand.get(3).getRank() == 4 &&
                         playerHand.get(4).getRank() == 5;
            //Check for 10, J, Q, K
            boolean c2 = playerHand.get(1).getRank() == 10 &&
                         playerHand.get(2).getRank() == 11 &&
                         playerHand.get(3).getRank() == 12 &&
                         playerHand.get(4).getRank() == 13;
            
            return (c1 || c2);
        } else { //Check if cards are continuous in rank
            testRank = playerHand.get(0).getRank() + 1;
            
            for (i = 1; i < 5; i++) {
                if (playerHand.get(i).getRank() != testRank) return false; //Not a straight
                testRank++; //move to next card
            }
            return true; //Straight!
        }
    }
    
    //check if three of a Kind in hand
    private boolean threeKind (List<Card> playerHand) {
        boolean c1, c2, c3;
        
        if (playerHand.size() != 5) return false;
        
        if (fourKind(playerHand) || fullHouse(playerHand))
            return false;
        
        rankSort(playerHand);
        
        //Check if x x x c b
        c1 = playerHand.get(0).getRank() == playerHand.get(1).getRank() &&
             playerHand.get(1).getRank() == playerHand.get(2).getRank();
        //Check if c x x x b
        c2 = playerHand.get(1).getRank() == playerHand.get(2).getRank() &&
             playerHand.get(2).getRank() == playerHand.get(3).getRank();
        //Check if c b x x x
        c3 = playerHand.get(2).getRank() == playerHand.get(3).getRank() &&
             playerHand.get(3).getRank() == playerHand.get(4).getRank();
        
        return (c1 || c2 || c3);
    }
    
    //check if hand has twoPair
    private boolean twoPair (List<Card> playerHand) {
        boolean c1, c2, c3;
        
        if (playerHand.size() != 5) return false;
        
        if (fourKind(playerHand) || fullHouse(playerHand) || threeKind(playerHand))
            return false;
        
        rankSort(playerHand);
        
        //Check if c c b b x
        c1 = playerHand.get(0).getRank() == playerHand.get(1).getRank() &&
             playerHand.get(2).getRank() == playerHand.get(3).getRank();
        //Check if c c x b b
        c2 = playerHand.get(0).getRank() == playerHand.get(1).getRank() &&
             playerHand.get(3).getRank() == playerHand.get(4).getRank();
        //Check if x c c b b
        c3 = playerHand.get(1).getRank() == playerHand.get(2).getRank() &&
             playerHand.get(3).getRank() == playerHand.get(4).getRank();
        
        return (c1 || c2 || c3);
    }
    
    //check if hand has onePair
    private boolean onePair (List<Card> playerHand) {
        boolean c1, c2, c3, c4;
        
        if (playerHand.size() != 5) return false;
        
        if (fourKind(playerHand) || fullHouse(playerHand) || threeKind(playerHand) || twoPair(playerHand))
            return false;
        
        rankSort(playerHand);
        
        //Check if c c x y z
        c1 = playerHand.get(0).getRank() == playerHand.get(1).getRank();
        //Check if x c c y z
        c2 = playerHand.get(1).getRank() == playerHand.get(2).getRank();
        //Check if x y c c z
        c3 = playerHand.get(2).getRank() == playerHand.get(3).getRank();
        //Check if x y z c c
        c4 = playerHand.get(3).getRank() == playerHand.get(4).getRank();
        
        return (c1 || c2 || c3 || c4);
    }
    /***************************
        End Hand Type Methods
    ****************************/
    
    /**********************
         GamePlay methods
     **********************/
    private void getPlayerBet() { //ask user for bet
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter bet: ");
        try {
            playerBet = scanner.nextInt();
            
            if(playerBet > playerBalance) {
                System.out.println("\nBet larger than balance! Enter new balance...");
                getPlayerBet();
            }
        }
        catch (Exception e){
            System.out.println("\nPLease input integers only.");
            getPlayerBet();
        }
    }
    
    private void updateBalance() {
        playerBalance -= playerBet;
    }
    
    private void dealHand() {
        try {
            playerHand = oneDeck.deal(numberOfCards);
        }
        catch (PlayingCardException e){
            System.out.println("Could not deal hand.");
        }
    }
    
    private void getHoldCardPositions() {        
        Scanner scanner = new Scanner(System.in);
	System.out.println("\nEnter positions of cards to keep (e.g. 1 4 5 )");
	List<Card> keptCards = new ArrayList<Card>();
	List<Card> dealCard = new ArrayList<Card>();
	String input = scanner.nextLine();
	Scanner scanformat = new Scanner(input);
	scanformat = scanformat.useDelimiter("\\s*");
        
        
        if (input.isEmpty()) return;
        
	while (scanformat.hasNext()) {
            String numstring = scanformat.findInLine("\\d+");
            int a = Integer.parseInt(numstring);
            if (a != 0) {
		keptCards.add(playerHand.get(a - 1));
            }
	}
        
	int count = 5;
	int counter = keptCards.size();
	while (count > counter) {
            try {
		dealCard = oneDeck.deal(1);
            } catch (PlayingCardException e) {
		System.out.println("Unable to deal");
            }
            keptCards.add(dealCard.get(0));
            count--;
	}
	playerHand = keptCards;
	System.out.println(playerHand.toString());
    }
    
    private void askToDisplayTable() {        
        System.out.println("\nWant to see payout table? (y or n)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        
        
        if (input.equals("n")) {
            showPayoutTable = false;
        } else if (input.equals("y")) {
            showPayoutTable = true;
        }
        else {
            System.out.println("Please enter (y or n");
            askToDisplayTable();
        }
        
    }
    
    private void playAgain() {
        System.out.println("\nDo you want to play again? (y or n)");
        Scanner scanner = new Scanner(System.in);
        
        String input = scanner.nextLine();
            
        if(input.equals("y")) {
            askToDisplayTable();    
            play();
        }
        else if (input.equals("n"))
        {
            exitGame();
        }
        else
        {
            System.out.println("Please enter (y or n)");
            playAgain();
        }
    }
    
    private void exitGame() {
        System.out.println("\nBalance: $" + playerBalance + "\nBye!");
        System.exit(0);
    }
    /***********************
      End gamePlay methods
    ************************/
    
    public void play() {
        /**
         * The main algorithm for single player poker game
         *
         * Steps: showPayoutTable() //this is outside the loop!
         *
         * ++	//this symbolizes loop show balance, get bet verify bet value,
         * update balance reset deck, shuffle deck, deal cards and display cards
         * ask for positions of cards to replace get positions in one input line
         * update cards check hands, display proper messages update balance if
         * there is a payout if balance = O: end of program else ask if the
         * player wants to play a new game if the answer is "no" : end of
         * program else : showPayoutTable() if user wants to see it goto ++
         */

        // implement this method!
        if(showPayoutTable) {
            showPayoutTable();
        }
        
        System.out.println("\n\n-----------------------------------");
        
        //Show balance
        System.out.println("\nBalance: $" + playerBalance);
        
        //get Bet
        getPlayerBet();
        
        //update balance
        updateBalance();
        
        //reset, shuffle deck
        oneDeck.reset();
        oneDeck.shuffle();
        
        //deal cards and display
        dealHand();
        System.out.println(playerHand.toString());
        
        //ask for positions of cards to replace
        getHoldCardPositions();
        
        //update cards and check hands
        checkHands();
        
        //show updated Balance
        System.out.println("\nBalance: $" + playerBalance);
        
        if(playerBalance == 0)
        {
            exitGame();
        }
        else {
            playAgain();
        }
    }

    /**
     * ***********************************************
     * Do not modify methods below
     * /*************************************************
     *
     * /** testCheckHands() is used to test checkHands() method checkHands()
     * should print your current hand type
     */
    public void testCheckHands() {
        try {
            playerHand = new ArrayList<Card>();

            // set Royal Flush
            playerHand.add(new Card(3, 1));
            playerHand.add(new Card(3, 10));
            playerHand.add(new Card(3, 12));
            playerHand.add(new Card(3, 11));
            playerHand.add(new Card(3, 13));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Straight Flush
            playerHand.set(0, new Card(3, 9));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Straight
            playerHand.set(4, new Card(1, 8));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Flush
            playerHand.set(4, new Card(3, 5));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	",
            // "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };
            // set Four of a Kind
            playerHand.clear();
            playerHand.add(new Card(4, 8));
            playerHand.add(new Card(1, 8));
            playerHand.add(new Card(4, 12));
            playerHand.add(new Card(2, 8));
            playerHand.add(new Card(3, 8));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Three of a Kind
            playerHand.set(4, new Card(4, 11));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Full House
            playerHand.set(2, new Card(2, 11));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Two Pairs
            playerHand.set(1, new Card(2, 9));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set One Pair
            playerHand.set(0, new Card(2, 3));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set One Pair
            playerHand.set(2, new Card(4, 3));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set no Pair
            playerHand.set(2, new Card(4, 6));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /* Quick testCheckHands() */
    public static void main(String args[]) {
        
        VideoPoker pokergame = new VideoPoker();
        pokergame.testCheckHands();
        
    }

}
