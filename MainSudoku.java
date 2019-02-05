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
            System.out.println("Select an option to continue");
            System.out.println("1. generate a random solution");
            System.out.println("2. input a new puzzle");
            System.out.println(".. anything else to quit");
            ans = svar.next();
            ch = ans.toLowerCase().charAt(0);
            boolean valid;

            switch (ch){
                case '1':
                    if(sk.process())
                        sk.printsudo();
                    break;
                case '2':
                    sk.printsam();
                    if(sk.readsudo()){
                        if(sk.evalid()){
                            if(sk.etest(9)){
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
                    System.exit(0);
            }

        }while(true);
    }
}
