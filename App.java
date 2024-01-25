// Version 1.2

// import Scanner to read inputs
import java.util.Scanner;

//import to create and write on files
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.BufferedWriter;

public class App{

    // 2d array to store the people and their status, int to handle multiple status' S = susceptible, I = infected, R = recovered
    private static String[][] grid;

    // 2d array to store previous timeSteps
    private static String[][] prevGrid;

    //2d arrary parallel to grid, used to store if the position is in : 3 = corner, 2 = edge, 3 = inside
    private static int[][] type;

    // create variables
    private static int individuals;
    private static int size;
    private static int timeSteps;
    private static double infectionRate, recoveryRate;
    private static int numInfected = 0;
    private static int numRecovered = 0;




    // main method
    public static void main(String[] args) throws Exception {
        // Create scanner object to recieve input
        Scanner keyboard = new Scanner(System.in);

        // Request for size of grid
        System.out.println("\nEnter size of square grid (Must be a perfect square): ");
        
            // try and catch used to catch if input is not a number
            try{
                individuals = keyboard.nextInt();
                while(Math.sqrt(individuals)%1 != 0){ //If the square root is not a integer, it is not a perfect square

                    System.out.println("Not a perfect square");
                    System.out.println("Please enter a perfect square:");
                    individuals = keyboard.nextInt();

                }
            } catch(Exception e){

                System.out.println("Not a number...");
                System.exit(0);
                
            }

        //set size of grid and type
        size = (int)Math.sqrt(individuals);
        grid = new String[size][size];
        prevGrid = new String[size][size];
        type = new int[size][size];

        //Since grid and grid 2 will be empty, iterate through both and fill with S
        for(int i=0;i<grid.length;i++){

            for(int j=0;j<grid[i].length;j++){

                grid[i][j] = "S";
                prevGrid[i][j] = "S";

            }

        }
        
        //generate random number to determine patient zero
        int random1=randomNum(size-1);
        int random2=randomNum(size-1);
        grid[random1][random2]="I";
        
        //Initialize type array, 3=corner, 2=edge, 1=inside for easier detection of surroundings later
        for(int i=0; i<type.length;i++){

            for(int j=0; j<type[i].length;j++){

                if((i==0 && j==0) || (i==type.length-1 && j==0) || (i==0 && j==type[i].length-1) || (i==type.length-1 && j==type[i].length-1)) //corner
                type[i][j]=3;
                else if ((i==0)||(j==0)||(i==type.length-1)||(j==type[i].length-1)) //edge
                type[i][j]=2;
                else //inside
                type[i][j]=1; 
                
            }

        }

        System.out.println("\nEnter time steps (Positive number):");
            
            // try and catch used to catch if input is not a number
            try{
                timeSteps = keyboard.nextInt();
                while(timeSteps <= 0){

                    System.out.println("Not a valid number");
                    System.out.println("Please enter a positive number:");
                    timeSteps = keyboard.nextInt();

                }
            } catch(Exception e){
                System.out.println("Not a number...");
                System.exit(0);
            }
             
        System.out.println("\nEnter infection rate (Between 0 and 1):");

            // try and catch used to catch if input is not a number
            try{
                infectionRate = keyboard.nextDouble();
                while(infectionRate < 0 || infectionRate > 1){

                    System.out.println("Not a valid number");
                    System.out.println("Please enter a number between 0 and 1:");
                    infectionRate = keyboard.nextDouble();

                }
            } catch(Exception e){
                System.out.println("Not a number...");
                System.exit(0);
            }

        System.out.println("\nEnter recovery rate (Between 0 and 1):");

            // try and catch used to catch if input is not a number
            try{
                recoveryRate = keyboard.nextDouble();
                while(recoveryRate<0 || recoveryRate>1){

                    System.out.println("Not a valid number");
                    System.out.println("Please enter a number between 0 and 1:");
                    recoveryRate = keyboard.nextDouble();
                    
                }
            } catch(Exception e){
                System.out.println("Not a number...");
                System.exit(0);
            }

        System.out.println("\nS = susceptible, I = infected, R = recovered");

        simulate();
        
        keyboard.close();
    }

    //static method to copy the values of grid to prevGrid
    static void copy(){
        
        for(int i=0;i<grid.length;i++){

            for(int j=0;j<grid[i].length;j++){

                prevGrid[i][j]=grid[i][j];

            }

        }

    }
    
    //static method to generate a random number
    static int randomNum(int number){
        
        int random =(int)(Math.random()*(number+1));
        return random;
        
    }

    //static method to check if someone gets infected or not (*100 is done for easier comparison, comparing two integers instead of using two doubles)
    static  boolean infect(){
        
        int random = randomNum(101);
        return random < (int)(infectionRate*100) ? true: false;
        
    }

    //static method to check if someone recovers or not (*100 is done for easier comparison, comparing two integers instead of using two doubles)
    static  void recover(){
        for(int i=0;i<grid.length;i++){

            for(int j=0;j<grid[i].length;j++){
                
                if(prevGrid[i][j].equals("I")){

                    int random = randomNum(101);
                    if(random < ((int)(recoveryRate*100)) )
                    grid[i][j]="R";

                }
            }

        }
    }
    

    // static methods to check the surroundings cells
    static void checkTop(int index1, int index2){

        if(prevGrid[index1-1][index2] .equals("I")){
            
            if(infect())
                grid[index1][index2] = "I";
        
        }
        
    }

    static void checkBottom(int index1, int index2){

        if(prevGrid[index1+1][index2] .equals("I")){
            
            if(infect())
                grid[index1][index2] = "I";
        
        }

    }

    static void checkLeft(int index1, int index2){

        if(prevGrid[index1][index2-1] .equals("I")){
            
            if(infect())
                grid[index1][index2] = "I";
        
        }

    }

    static void checkRight(int index1, int index2){

        if(prevGrid[index1][index2+1] .equals("I")){

            if(infect())
                grid[index1][index2] = "I";

        }

    }

    // static method to check the surroundings of corner type
    static void checkCorner(int index1, int index2){

        if((index1 == 0) && (index2 == 0)){

            checkRight(index1, index2);
            checkBottom(index1, index2);

        } else if ((index1 == 0) && (index2 == grid.length-1)){

            checkLeft(index1, index2);
            checkBottom(index1,index2);

        } else if ((index1 == grid[index1].length-1) && (index2 == 0)){

            checkRight(index1, index2);
            checkTop(index1,index2);

        } else {

            checkLeft(index1, index2);
            checkTop(index1, index2);

        }

    }

    // static method to check the surroundings of corner edge
    static void checkEdge(int index1, int index2){

        if(index1 == 0){

            checkLeft(index1, index2);
            checkRight(index1, index2);
            checkBottom(index1, index2);

        } else if(index1 == grid.length-1){

            checkLeft(index1, index2);
            checkRight(index1, index2);
            checkTop(index1, index2);

        } else if(index2 == 0){

            checkRight(index1, index2);
            checkTop(index1, index2);
            checkBottom(index1, index2);

        } else {

            checkLeft(index1, index2);
            checkTop(index1, index2);
            checkBottom(index1, index2);

        } 
    
    }

    // static method to check the surroundings of inside type, due to the nature of a square grid, inside cells will always have cells around them
    static void checkInside(int index1, int index2){

        checkLeft(index1,index2);
        checkRight(index1,index2);
        checkTop(index1,index2);
        checkBottom(index1,index2);

    }

    //Check the grid to count how many individuals are infected 
    static void checkStatus(){

        for(int i=0;i<grid.length;i++){

            for(int j=0;j<grid[i].length;j++){

                numInfected += (grid[i][j].equals("I")) ? 1:0;
                numRecovered += (grid[i][j].equals("R")) ? 1:0;
                

            }

        }

    }

    //simulate the spreading of the disease
    static void simulate(){
        for(int times=1;times<=timeSteps;times++){
            copy();
            numInfected = 0;
            numRecovered = 0;
            
            for(int i = 0; i<prevGrid.length;i++){
                if(size==1){
                    recover();
                    break;
                }
                for(int j= 0; j<prevGrid[i].length;j++){
                    
                    if(prevGrid[i][j].equals("R")){ //skip if recovered

                        continue;

                    }

                    if(type[i][j] == 3 ){

                        checkCorner(i, j);

                    } else if(type[i][j]==2){

                        checkEdge(i, j);

                    } else {

                        checkInside(i,j);

                    }

                }

            }

            //write to a file which creats a model of the simulation
            try{
                File model = new File("GridModel.txt");
                FileWriter fw = new FileWriter(model);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("Timestep: "+times+"\n");

                for(int i=0;i<grid.length;i++){

                    for(int j=0;j<grid[i].length;j++){

                        bw.write(grid[i][j]);

                    }

                    bw.write("\n");
                    
                }
                bw.write("\n");
                
                bw.close();
            } catch (IOException e){

                System.out.println("Error occured");

            }

            recover();

            System.out.println("Timestep: " + times);

            checkStatus();

            System.out.println("Total Individuals: " + (individuals));

            System.out.println("Number of infected individuals: " + numInfected);

            System.out.println("Number of recovered individuals: " + numRecovered);

            if(numInfected!=0){

                System.out.println("Ratio of Number of infected to Total Individuals: 1 : " + individuals/numInfected);

                System.out.println("1 individual is infected for approximately every " + individuals/numInfected + " individuals\n");

            } else{

            System.out.println("No one is infected\n");

            }
            
        }

    }

}
