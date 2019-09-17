import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Sudoku {

    Random rnum;
    Scanner sc ;
    long bt = 0;
    long stime,etime;
    long mins,secs,msec;
    int[][] nsudo; /* Sudoku */
    int[][] nstag; /* point to the last proposed solution */
    int[][][] ncube; /* list of all solutions */
    int[] tarray =  {1,2,3,4,5,6,7,8,9}; /* temp array */
    int[][] sam =   {{8,0,0,0,0,0,0,0,0} /* sample Sudoku puzzle */
                    ,{0,0,3,6,0,0,0,0,0}
                    ,{0,7,0,0,9,0,2,0,0}
                    ,{0,5,0,0,0,7,0,0,0}
                    ,{0,0,0,0,4,5,7,0,0}
                    ,{0,0,0,1,0,0,0,3,0}
                    ,{0,0,1,0,0,0,0,6,8}
                    ,{0,0,8,5,0,0,0,1,0}
                    ,{0,9,0,0,0,0,4,0,0}};

    public Sudoku() {
        rnum = new Random();
        sc = new Scanner(System.in);
        nsudo = new int[9][9];
        nstag = new int[9][9];
        ncube = new int[9][9][9];
        bt = 0;
    }

    public boolean process(){

        stime = System.nanoTime(); /* save the start time */

        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){

                shuffle(i,j); /* shuffle temp array and map ncube[i][j][*] */
                nsudo[i][j] = check(i,j,nstag[i][j]); /* solve at position(i,j) */

                if (nsudo[i][j] == 0) { /* if not able to solve */

                    nstag[i][j] = 0; /* reset the pointer */
                    printsudo(i);
                    //printstag(i);
                    //printcube(i,j);

                    if (etest()) { /* if all solutions exhausted */
                        System.out.println();
                        System.out.println();
                        System.out.println("Phew !!! Not solvable...");
                        return(false);
                    }

                    do {
                        i = backtraci(i,j); /* backtrace to prev row */
                        j = backtracj(i,j); /* backtrace to prev col */
                        bt++; 
                    } while(cval(i,j)); /* skip already solved positions */
                    nstag[i][j] = backtracn(i,j); /* set the pointer to new solution */
                    j--;
                }
            }
            printsudo(i);
            //printstag(i);
        }

        etime = System.nanoTime() - stime; /* elapsed time */
		return(true);
    }

    public void shuffle(int i, int j){

        if (ncube[i][j][0] == 0){ /* shuffle only if this is not already done */
            for(int ni=0;ni<9;ni++){
                int itemp = rnum.nextInt(9); /* fetch a random number b/w 0 and 8 */

                /* swap values in temp array */
                int ntemp = tarray[ni]; 
                tarray[ni] = tarray[itemp];
                tarray[itemp] = ntemp;

                /* map temp array to ncube[i][j][*] */
                ncube[i][j][ni] = tarray[ni];
                ncube[i][j][itemp] = tarray[itemp];
            }
        }
    }

    public int check(int i, int j, int c) { 

        if (nstag[i][j] == 9) /* exit condition for recursive call */
            return nsudo[i][j];

        if(nsudo[i][j] == ncube[i][j][c]) /* if the solution is already proposed */
            return 0;

        nsudo[i][j] = ncube[i][j][c];
        nstag[i][j] = c;

        if (verify(i, j)) /* check the value proposed*/
            return nsudo[i][j];
        else{
            if (c < 8) /* iterate all solutions */
                return (check(i,j,++c));
            else return 0;
        }
    }

    public void printsudo(){

        System.out.println();
        System.out.println();
        System.out.println("Solution:");

        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                if (nsudo[i][j] != 0){
                    System.out.print(nsudo[i][j]+" ");
                }
                else{
                    System.out.print("_ ");
                }
            }
            System.out.println();
        }

        mins = TimeUnit.MINUTES.convert(etime,TimeUnit.NANOSECONDS);
        secs = TimeUnit.SECONDS.convert(etime,TimeUnit.NANOSECONDS);
        msec = TimeUnit.MILLISECONDS.convert(etime,TimeUnit.NANOSECONDS);
        msec = msec - secs*1000;
        secs = secs - mins*60;

        System.out.println();
        System.out.println("Summary:");
        System.out.println("Backtracked "+bt+" times");
        System.out.println("Elapsed "+mins+" min "+secs+" sec "+msec+" ms");
    }

    public void printsudo(int i){

        System.out.println();
        System.out.print("["+i+"] ");

        for (int j=0;j<9;j++){
            System.out.print(nsudo[i][j]+" ");
        }
    }

    public boolean readsudo(){
        System.out.println();
        System.out.println("Enter 33 to abort anytime");
        System.out.println("Enter 88 to restart");
        System.out.println("Enter the puzzle below: ");
        System.out.println();

        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                try {
                    nsudo[i][j]= sc.nextInt();
                    if(nsudo[i][j] != 0)
                        nstag[i][j] = 9; /* set pointer as 9 if already solved */
                    if(nsudo[i][j] == 33)
                        System.exit(0);
                    if(nsudo[i][j] == 88)
                        return(false);
                } catch (InputMismatchException e) {
					System.err.println();
                    System.err.println("Input Mismatch Exception: Invalid input" );
                    System.err.println("Program restarted" );
                    return(false);
                }
            }
        }
        return (true);
    }

    public void printsam(){
        System.out.println();
        System.out.println("Enter the details as a 9 x 9 table");
        System.out.println("Every number separated by spaces");
        System.out.println("Enter 0 in the place of a missing value");
        System.out.println();
        System.out.println("Sample input: ");
        System.out.println();

        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++)
                System.out.print(sam[i][j]+" ");
            System.out.println();
        }
    }

    public int backtraci(int i, int j){ /* backtrace to prev row */
        if (j==0) return (--i);
        else return i;
    }

    public int backtracj(int i, int j){ /* backtrace to prev col */
        if (j==0) return 8;
        else return (--j);
    }

    public int backtracn(int i, int j){ 
        if (nstag[i][j] < 8) return (++nstag[i][j]);
        else return (nstag[i][j]);
    }

    public boolean cval(int i, int j){ /* check if the position is already solved */
        if (nstag[i][j] == 9) return (true);
        else return (false);
    }

    public boolean etest(){  /* check if all solutions exhausted */
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
                if ((nstag[i][j] != 0)&&(nstag[i][j] != 9))
                    return (false);
        return (true);
    }

    public boolean etest(int x){
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
                if (nstag[i][j] != x)
                    return (false);
        return (true);
    }

    public boolean evalid(){  /* check if the puzzle is valid */
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++){
                if ((!verify(i,j)) &&(nsudo[i][j] != 0))
                    return (false);
                if((nsudo[i][j]<0)||(nsudo[i][j]>9))
                    return (false);
            }
        return (true);
    }

    public void printstag(){

        System.out.println();
        System.out.println();

        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++)
                System.out.print(nstag[i][j]+"  ");
            System.out.println();
        }
    }

    public void printstag(int i){

        System.out.println();
        System.out.print("("+i+") ");

        for (int j=0;j<9;j++)
            System.out.print(nstag[i][j]+" ");
    }

    public void printcube(){

        System.out.println();
        System.out.println();

        for (int i=0;i<9;i++)
            for (int j=0;j<9;j++){
                for(int k=0;k<9;k++)
                    System.out.print(ncube[i][j][k]+"  ");
                System.out.println();
            }
    }

    public void printcube(int i,int j){

        System.out.println();
        System.out.print(i+":"+j+" ");

        for (int k=0;k<9;k++)
            System.out.print(ncube[i][j][k]+" ");
    }

    public boolean verify(int i, int j) { /* verify the rules in a Sudoku */

        for(int xi = 0; xi < 9; xi++){
            if ((nsudo[xi][j] == nsudo[i][j]) && (xi != i)) {
                return(false);
            }
        }

        for (int xj = 0; xj < 9; xj++) {
            if ((nsudo[i][xj] == nsudo[i][j]) && (xj != j)) {
                return(false);
            }
        }

        for (int zi = ((i / 3) * 3); zi <= ((i / 3) * 3) + 2; zi++) {
            for (int zj = ((j / 3) * 3); zj <= ((j / 3) * 3) + 2; zj++) {
                if (nsudo[zi][zj] == nsudo[i][j]) {
                    if ((zi == i) && (zj == j));
                    else
                        return (false);
                }
            }
        }
        return (true);
    }
}
