import java.util.Scanner;

public class MainSudoku {

    public static void main(String[] args) {

        Scanner svar;
        String ans;
        Sudoku sk;
        char ch;

        do{
            sk = new Sudoku(); 
            svar = new Scanner(System.in);
            System.out.println();
            System.out.println(">>> Sudoku solution using backtracking algorithm <<<");
            System.out.println("Select an option to continue");
            System.out.println("1. generate a random solution");
            System.out.println("2. input a new puzzle");
            System.out.println(".. anything else to quit");
            ans = svar.next(); /* read the option selected */
            ch = ans.toLowerCase().charAt(0); /* convert in lower case */
            
            switch (ch){
                case '1': /* generate a random solution */
                    if(sk.process())
						sk.printsudo();
                    break;
                case '2': /* read the puzzle from user */
                    sk.printsam(); /* print a sample puzzle */
                    if(sk.readsudo()){ /* if read was successful */
                        if(sk.evalid()){ /* if the input is a valid Sudoku */
                            if(sk.etest(9)){ /* if the input is fully solved */
                                System.out.println();
                                System.out.println("Your input is a valid Sudoku !!!");
                            }
                            else {
                                if(sk.process())
									sk.printsudo();
                            }
                        }
                        else{
                            System.out.println();
                            System.out.println("Not a valid Sudoku...");
                        }
                    }
                    break;
                default:
                    svar.close();
                    System.exit(0);
            }
        }while(true);        
    }
}
