package bruno.tennis;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * The Tennis program implements an application that
 * simulates a tennis match
 *
 * @author Bruno
 */
public class Tennis {
    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println(" - Tournament -");
        System.out.print("Name:");
        String tournamentName = sc.nextLine();
        int setsCount=validateSetsCount(sc);
        System.out.println(" - First Player -");
        System.out.print("Name:");
        String player1Name = String.format("%1$6s",sc.next());
        int probP1= validateFirstPlayerWinProb(sc);
        System.out.println(" - Second Player -");
        System.out.print("Name:");
        String player2Name = String.format("%1$6s",sc.next());
        int probP2= validateSecondPlayerWinProb(sc,probP1);
        
        Player p1= new Player(player1Name,probP1);
        Player p2= new Player(player2Name,probP2);

        Match match=new Match(tournamentName,setsCount,p1,p2);
        match.startMatch();
        
        if(askRematch(sc)){
            match.startMatch();
        }
        System.out.println(" - End of program -");
        
    }
    
    /**
     * Validates the input probability for the first player
     */
    private static int validateFirstPlayerWinProb(Scanner sc) {
        int num=0;
        do {
            System.out.print("Winning probability (0-100):");
            while (!sc.hasNextInt()) {
                System.out.print("Enter a valid number (0-100)ª:");
                try{sc.next();
                }catch(InputMismatchException ex){}
                
            }
            num = validarInput(sc);
        } while (num < 0 || num>100);
        
        return num;
    }
    /**
     *  Validates the input probability for the second player, which has to be
     *  complementary to the firstProb 
     */
    private static int validateSecondPlayerWinProb(Scanner sc,int firstProb) {
        int num=0;
        
        do{
            System.out.print("Winning probability ("+(100-firstProb)+"):");
            while (!sc.hasNextInt()) {
                System.out.println("Must enter ("+(100-firstProb)+"):");
                sc.next();
            }
            num=validarInput(sc);
        }while(num !=100-firstProb);
        return num;
    }
    /**
     * validates the input for setCount
     */
    private static int validateSetsCount(Scanner sc) {
        int num=0;
        do{
            System.out.print("Sets format(3/5):");
            while (!sc.hasNextInt()) {
                System.out.print("Enter valid option!(3/5):");
                try{sc.next();
                }catch(InputMismatchException ex){}
            }
            num=validarInput(sc);
        }while(num !=3 && num!=5);
        return num;
    }
   
    
    /**
     *  Asks user for a rematch
     * @return if the user selected yes
     */
    private static boolean askRematch(Scanner sc){
        System.out.println("Play rematch?(y/n):");
        String rematchOption="";
        while(!rematchOption.equals("y") && !rematchOption.equals("n")) {
            System.out.print("Enter a valid option:");
            rematchOption=sc.next();
        }
        return rematchOption.equals("y");
        
    }
    
    private static int validarInput(Scanner sc) {
        int num=0;
        try {
            num=sc.nextInt();
        }catch(InputMismatchException ex) {}
        return num;
    }
  
}
