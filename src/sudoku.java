
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Lewis Shemery
 */
public class sudoku {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
            //System.out.printf("\nEnter the name of a file to read: ");
            //String filename = in.next();
            int[][] sudoku_initial = readSudoku("sudoku_hard.txt");
            printSudoku(sudoku_initial);
            System.out.printf("\nSolving...\n\n");
            sudoku(sudoku_initial);
    }

    public static void sudoku(int[][] sudoku_initial) {
        int[][] sudoku_final = new int[9][9];
        for (int i = 0; i < sudoku_initial.length; i++) {
            sudoku_final[i] = sudoku_initial[i].clone();
        }
        solve(sudoku_final, 0, 0);
        printSudoku(sudoku_final);
    }
    
    //Depth-first search algorithm to solve sudoku. 
    public static boolean solve(int[][] sudoku, int row, int col){
        if(col == 9){
            col = 0;
            row++;
        }
        //if row gets incremented to 9, the puzzle is solved and it returns true
        if(row == 9){
            return true;
        }
        //if the cell has a number in it, increment the column snd call the solve 
        //function agian.
        if(sudoku[row][col] != 0){
            return solve(sudoku, row, col+1);
        }
        //for loop to iterate through possible numbers and check if that number is valid
        //with the isValid function. If it finds a valid number, it calls solve recursively
        //until it either solves the sudoku and returns true or reaches a point where there
        //are no possible numbers and returns false. At which point it back tracks to a previous
        //state and tries again.
        for(int i = 1; i < 10; i++){
            boolean valid = isValid(sudoku, row, col, i);
            if(valid){
                sudoku[row][col] = i;
                //depth search
                boolean next = solve(sudoku, row, col+1);
                if(next){
                    return true;
                }
                //sets the cell back to zero so it can be processed again later.
                else{
                    sudoku[row][col] = 0;
                }
            }
        }
        //causes backtracking
        return false;
    }

    public static boolean isValid(int[][] sudoku, int row, int col, int number) {
        //check column
        for(int i = 0; i < 9; i++){
            if(sudoku[i][col] == number){
                return false;
            }
        }
        //check row
        for (int i = 0; i < 9; i++) {
            if(sudoku[row][i] == number){
                return false;
            }
        }
        //check box
        int box_row = (row/3) * 3;
        int box_col = (col/3) * 3;

        for (int x = box_row; x < box_row + 3; x++) {
            for (int y = box_col; y < box_col + 3; y++) {
                if(sudoku[x][y] == number){
                    return false;
                }
            }
        }
        return true;
        }

    public static int[][] readSudoku(String filename) {
        String[][] data = readSpreadsheet(filename);
        if (data.length != 9) {
            System.out.printf("Error: %d lines\n", data.length);
            return null;
        }

        int[][] result = new int[9][9];
        for (int row = 0; row < 9; row++) {
            if (data[row].length != 9) {
                System.out.printf("Error: %d columns on row %d\n", data[row].length, row);
                return null;
            }
            for (int col = 0; col < 9; col++) {
                String value = data[row][col];
                if ((value.length() == 0) || (value.equals(" "))) {
                    result[row][col] = 0;
                } else {
                    int number;
                    try {
                        number = Integer.parseInt(value);
                    } catch (Exception e) {
                        System.out.printf("Error: non-integer entry %s at (%d, %d)", value, row, col);
                        return null;
                    }
                    if ((number >= 0) && (number <= 9)) {
                        result[row][col] = number;
                    } else {
                        System.out.printf("Error: illegal integer entry %s at (%d, %d)", value, row, col);
                        return null;
                    }
                }
            }
        }

        return result;
    }

    public static void printSudoku(int[][] data) {
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                if (data[row][col] == 0) {
                    System.out.printf("  -");
                } else {
                    System.out.printf("%3d", data[row][col]);
                }
                if ((col == 2) || (col == 5)) {
                    System.out.printf(" |");
                }
            }
            if ((row == 2) || (row == 5)) {
                System.out.printf("\n----------------------------------");
            }
            System.out.printf("\n");
        }
    }

    public static String[][] readSpreadsheet(String filename) {
        ArrayList<String> lines = read_file(filename);
        if (lines == null) {
            return null;
        }
        String[][] result = new String[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            result[i] = line.split(",");
        }
        return result;
    }

    public static ArrayList<String> read_file(String filename) {
        File temp = new File(filename);
        Scanner input_file;
        try {
            input_file = new Scanner(temp);
        } catch (Exception e) {
            System.out.printf("Failed to open file %s\n", filename);
            return null;
        }

        ArrayList<String> result = new ArrayList<String>();
        while (input_file.hasNextLine()) {
            String line = input_file.nextLine();
            result.add(line);
        }

        input_file.close();
        return result;
    }

    public static boolean sanity_check(String[][] data) {
        if (data == null) {
            System.out.printf("No data has been loaded\n");
            return false;
        }

        if (data.length < 2) {
            System.out.printf("There aren't enough rows\n");
            return false;
        }
        return true;
    }
}
