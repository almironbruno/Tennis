package bruno.tennis;

/**
 *
 * @author Bruno
 */
public class Match {

    private static final String[] SCORES = {"0", "15", "30", "40", "Win"};
    private static final String DEUCE_POINT = "Ad";
    private final Player[] players;
    private final int totalSets;
    private final String tournamentName;
    private final String[] historial;
    private boolean deuce;
    private final int[] playersScoreIndex;
    
    /**
     * Constructor
     * @param name name of the tournament
     * @param sets numbers of sets to play
     * @param p1   player 1
     * @param p2   player 2
     */
    public Match(String name, int sets, Player p1, Player p2) {
        this.players = new Player[2];
        this.deuce = false;
        this.historial = new String[]{"", ""};
        this.playersScoreIndex = new int[]{0, 0};
        this.tournamentName = name;
        this.totalSets = sets;
        this.players[0] = p1;
        this.players[1] = p2;
    }

    /**
     * Decides who serves
     * @return index for player who won the serve
     */
    private int decideServe() {
        Double serve = Math.random();
        return serve < 0.5 ? 0 : 1;
    }

    /**
     * Generates a new point
     * @return index of the point winner
     */
    private int playPoint() {

        int pointWinner;

        float point = (float) Math.random();

        int bestPlayer = maxWinProbabilityPlayer();

        float maxProbability = players[bestPlayer].getWinProbability() / 100f;

        if (point < maxProbability) {
            pointWinner = bestPlayer;

        } else {
            //XOR to get the other player
            int worstPlayer = bestPlayer ^ 1;
            pointWinner = worstPlayer;
        }

        //Deuce
        if (players[0].getCurrentScore().equals(SCORES[3])
                && players[0].getCurrentScore().equals(players[1].getCurrentScore())) {
            deuce = true;
        }
        return pointWinner;
    }

    /**
     * Adds the point to the winner's score
     */
    private void addPoint(int pointWinner) {
        if (deuce) {
            int otherPlayer = pointWinner ^ 1;
            if (players[otherPlayer].getCurrentScore().equals(DEUCE_POINT)) {
                players[0].setCurrentScore(SCORES[3]);
                players[1].setCurrentScore(SCORES[3]);
                return;
            }
            if (players[pointWinner].getCurrentScore().equals(SCORES[3])) {
                players[pointWinner].setCurrentScore(DEUCE_POINT);
                return;
            }
        }
        players[pointWinner].setCurrentScore(SCORES[++playersScoreIndex[pointWinner]]);
    }

    /**
     * Starts the match
     */
    public void startMatch() {

        System.out.println("- Match Starts -");
        int servePlayer = decideServe();

        for (int i = 0; i < totalSets; i++) {

            boolean setWinner = false;

            while (!setWinner) {

                boolean gameWinner = false;

                showServeResult(players[servePlayer]);
                showInitialScore();
                while (!gameWinner) {

                    int pointPWinner = playPoint();
                    addPoint(pointPWinner);
                    showPointResult(players[pointPWinner].getName());
                    //Deuce
                    if (players[0].getCurrentScore().equals(SCORES[3])
                            && players[1].getCurrentScore().equals(players[0].getCurrentScore())) {
                        deuce = true;

                    }

                    if (players[pointPWinner].getCurrentScore().equals(SCORES[4])) {

                        gameWinner = true;

                        players[pointPWinner].addGame();
                        if (deuce) {
                            deuce = false;
                        }
                        playersScoreIndex[0] = 0;
                        playersScoreIndex[1] = 0;
                        players[0].setCurrentScore(SCORES[0]);
                        players[1].setCurrentScore(SCORES[0]);

                    }
                }

                int otherPlayer = leadingGamesPlayer() ^ 1;
                //6 games & 2-game dfference
                if (players[leadingGamesPlayer()].getGamesWinned() >= 6
                        && players[leadingGamesPlayer()].getGamesWinned()
                        - players[otherPlayer].getGamesWinned() >= 2) {

                    players[leadingGamesPlayer()].addSet();

                    setWinner = true;
                    servePlayer ^= 1;
                    historial[leadingGamesPlayer()] += " " + players[leadingGamesPlayer()].getGamesWinned();
                    historial[otherPlayer] += " " + players[otherPlayer].getGamesWinned();
                    showSetResult(players[leadingGamesPlayer()].getName());

                } else if (players[0].getGamesWinned() == 6
                        && players[0].getGamesWinned() == players[1].getGamesWinned()) {
                    //Tie Break
                    playTieBreak();
                    setWinner = true;
                    servePlayer ^= 1;
                    showSetResult(players[leadingGamesPlayer()].getName());

                }
                if (setWinner) {

                    resetPlayersScore();
                }
            }

            int leadingSetsPlayer = leadingSetsPlayer();
            int maxSetsWinned = players[leadingSetsPlayer].getSetsWinned();

            if (maxSetsWinned == totalSets / 2 + 1) {
                //End of the match
                showMatchResult();
                for (int j = 0; j < players.length; j++) {
                    historial[j] = "";
                    players[j].setSetsWinned(0);
                }
                resetPlayersScore();
                break;
            }

        }
    }

    /*
     *  Starts the tie-break
     */
    private void playTieBreak() {
        int[] pointsCount = {0, 0};
        boolean winnerTieBreak = false;

        while (!winnerTieBreak) {

            int pWinner = playPoint();
            pointsCount[pWinner] += 1;
            showTieBreak(pointsCount[0], pointsCount[1]);

            int leadingPlayer = Math.max(pointsCount[0], pointsCount[1]);
            int loosingPlayer = Math.min(pointsCount[0], pointsCount[1]);

            if (leadingPlayer >= 7 && leadingPlayer - loosingPlayer >= 2) {
                players[pWinner].addSet();
                historial[pWinner] += " 7(" + leadingPlayer + ")";
                historial[pWinner ^ 1] += " 6(" + loosingPlayer + ")";
                winnerTieBreak = true;
            }
        }

    }

    /**
     * Resets the score of the players, used at each set
     */
    private void resetPlayersScore() {
        for (Player player : players) {
            player.setGamesWinned(0);
            player.setCurrentScore(SCORES[0]);
        }
    }

    /**
     * Looks for the player with most sets winned
     * @return index of the player
     */
    private int leadingSetsPlayer() {
        return players[0].getSetsWinned() > players[1].getSetsWinned() ? 0 : 1;
    }

    /**
     * Looks for the player with most games winned
     * @return index of the player
     */
    private int leadingGamesPlayer() {
        return players[0].getGamesWinned() > players[1].getGamesWinned() ? 0 : 1;
    }

    /**
     * Looks for the player with better winProbability
     * @return index of the player
     */
    private int maxWinProbabilityPlayer() {
        return players[0].getWinProbability() >= players[1].getWinProbability() ? 0 : 1;
    }

    /**
     * Shows the result of the match
     */
    private void showMatchResult() {
        System.out.println("******************************");
        System.out.println("      End of the Match");
        System.out.println(tournamentName);
        for (int i = 0; i < players.length; i++) {
            System.out.println(players[i].getName() + " |" + historial[i] + " | " + players[i].getSetsWinned());
        }
        System.out.println();
        System.out.println("*****************************");
        System.out.println();
    }

    /**
     * Shows the result of each point
     */

    private void showPointResult(String pointWinner) {
        System.out.println("------------------------");
        System.out.println("Point Winner: " + pointWinner);
        System.out.println("Scores:");
        for (int i = 0; i < players.length; i++) {
            System.out.println(players[i].getName() + " |" + historial[i] + " " + players[i].getGamesWinned() + " | " + players[i].getCurrentScore());
        }
        System.out.println("------------------------");
        System.out.println();
    }

    /**
     * Shows the result of the set
     */
    private void showSetResult(String pointWinner) {
        System.out.println(". . . . . . . . . . . . . . .");
        System.out.println("Set Winner: " + pointWinner);
        System.out.println("Sets Score:");
        for (int i = 0; i < players.length; i++) {
            //historial[i] += " " + players[i].getGamesWinned();
            System.out.println(players[i].getName() + " |" + historial[i]
                    + " | sets: " + players[i].getSetsWinned());
        }
        System.out.println(". . . . . . . . . . . . . . .");
        System.out.println("");
    }

    /**
     * Shows who serves
     */
    private void showServeResult(Player p) {
        System.out.println("- - - - - - - - - - -");
        System.out.println("serve::" + p.getName());
        System.out.println("- - - - - - - - - - -");
        System.out.println("");
    }

    /**
     * Shows the result of the tie-break
     */
    private void showTieBreak(int pointsP1, int pointsP2) {
        System.out.println("---------------------");
        System.out.println("Scores:");
        System.out.println(players[0].getName() + " |" + historial[0] + " | " + pointsP1);
        System.out.println(players[1].getName() + " |" + historial[1] + " | " + pointsP2);
        System.out.println("");
    }

    /**
     * Shows the initial score for the beggining of the match
     */
    private void showInitialScore() {
        System.out.println("------------------------");
        System.out.println(" - New Game -");
        System.out.println("Initial Scores:");
        for (int i = 0; i < players.length; i++) {
            System.out.println(players[i].getName() + " |" + historial[i] + " "
                    + players[i].getGamesWinned() + " | " + players[i].getCurrentScore());
        }
        System.out.println("------------------------");
        System.out.println();

    }
}

