package bruno.tennis;
/**
 *  Represents a player
 * @author Bruno
 */
public class Player {
    private String name;
    private int winProbability;
    private String currentScore;
    private int setsWinned;
    private int gamesWinned;

    /**
     *  Constructor
     * @param name
     * @param winProbability
     */
    public Player(String name,int winProbability) {
        this.name = name;
        this.winProbability = winProbability;
        this.currentScore="0";
    }
     
    /**
     *  Increase the count of gamesWinned
     */
    public void addGame() {
        this.gamesWinned++;
    }

    /**
     *  Increase the count of setsWinned
     */
    public void addSet() {
        this.setsWinned++;
    }
    
    /**
     * Set
     * @param score 
     */
    public void setCurrentScore(String score) {
        this.currentScore = score;
    }

    /**
     *  Set
     * @param setsWinned 
     */
    public void setSetsWinned(int setsWinned) {
        this.setsWinned = setsWinned;
    }

    /**
     *  Set
     * @param gamesWinned
     */
    public void setGamesWinned(int gamesWinned) {
        this.gamesWinned = gamesWinned;
    }
    /**
     *  Set
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
     /**
     *  Set
     * @param winProbability
     */
    public void setWinProbability(int winProbability) {
        this.winProbability = winProbability;
    }
    
    /**
     *  Get
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *  Get
     * @return winProbability
     */
    public int getWinProbability() {
        return winProbability;
    }


    /**
     *  Get
     * @return currenScore
     */
    public String getCurrentScore() {
        return currentScore;
    }

    /**
     *  Get
     * @return setsWinned
     */
    public int getSetsWinned() {
        return setsWinned;
    }

    /**
     *  Get
     * @return gamesWinned
     */
    public int getGamesWinned() {
        return gamesWinned;
    }
}

