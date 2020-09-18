/* *****************************************************************************
 *  Name: Tulip Pandey
 *  Date: 29th March 2020
 *  Description: Algorithms Part 1 : Assignment 1 :Title - Percolation : Submission file 1: Percolation.java
 **************************************************************************** */
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

    public class Percolation {
        private int[][] sites;
        private final int gridSize;                              // stores the size of the grid like n for an n by n grid
        private final WeightedQuickUnionUF grid;       // the union-find object that will be used to call methods of weightedquickunionfind in the methods of percolation
        private int openCounter;                    //counts the number of open sites

        // creates n-by-n grid, with all sites initially blocked
        public Percolation(int n) {
            if (n <= 0) throw new IllegalArgumentException("given grid size is not a positive integer");
            grid = new WeightedQuickUnionUF(n * n+2); //the last two elements are the virtual top and bottom respectively
            gridSize = n;
            sites = new int[n + 1][n + 1]; //the sites begin from index 1 and end at n so n+1 sized grid is chosen to take care of the problem of getting n as an index and not 0
            for (int rowNum = 1; rowNum <= n; rowNum++)
                for (int columnNum = 1; columnNum <= n; columnNum++)
                    sites[rowNum][columnNum] = 0;    // 0 represents a blocked site, 1 represents an open site ; here, all sites have been initialised to be blocked at the start


            for(int i=0;i<n;i++){
                grid.union(i,n*n);  //the virtual top is being connected to all the first row sites
            //    grid.union(n*n-n+i,n*n+1);    // the virtual bottom is being connected to all the last row elements
            }

        }

        private int one_d(int row, int col) {
            return (gridSize * (row - 1) + (col - 1));   //gives the index of elemnt in the union-find object corresponding to a site location in the site
        }

        private int[][] neighbours(int row, int col) {
            int[][] nbrs;
            if ((row == 1) || (row == gridSize)) {
                if ((col == 1) || (col == gridSize)) {
                    nbrs = new int[2][2];
                    if (row == 1) {
                        nbrs[0][0] = row + 1;
                    } //row of first nbr;
                    else {
                        nbrs[0][0] = row - 1;
                    }
                    nbrs[0][1] = col; //col of first nbr
                    nbrs[1][0] = row;
                    if (col == 1) {
                        nbrs[1][1] = col + 1;
                    }
                    else {
                        nbrs[1][1] = col - 1;
                    }
                }
                else {
                    nbrs = new int[3][2];
                    int j = -1;
                    for (int i = 0; i < 2; i++) {
                        nbrs[i][0] = row;
                        nbrs[i][1] = col + j;
                        j += 2;
                    }
                    nbrs[2][1] = col;
                    if (row == 1) {
                        nbrs[2][0] = row + 1;
                    }
                    else {
                        nbrs[2][0] = row - 1;
                    }
                }
            }
            else if ((col == 1) || (col == gridSize)) {
                nbrs = new int[3][2];
                int j = -1;
                for (int i = 0; i < 2; i++) {
                    nbrs[i][1] = col;
                    nbrs[i][0] = row + j;
                    j += 2;
                }
                nbrs[2][0] = row;
                if (col == 1) {
                    nbrs[2][1] = col + 1;
                }
                else {
                    nbrs[2][1] = col - 1;
                }

            }
            else {
                nbrs = new int[4][2];
                for (int i = 0; i < 2; i++) {
                    nbrs[i][0] = row;
                    nbrs[0][1] = col - 1;
                    nbrs[1][1] = col + 1;
                }
                for (int i = 2; i < 4; i++) {
                    nbrs[i][1] = col;
                    nbrs[2][0] = row - 1;
                    nbrs[3][0] = row + 1;
                }
            }
            return nbrs;   // returns an array containing the site location of neighbours of a particular site
        }

        // opens the site (row, col) if it is not open already
        public void open(int row, int col) {
            if (row <= 0 || row > gridSize ) throw new IllegalArgumentException("row index out of bounds");
            if (col <= 0 || col > gridSize ) throw new IllegalArgumentException("col index out of bounds");
            if(!isOpen(row,col)) openCounter++;   // number of open sites increases by one
            sites[row][col] = 1;      // site is opened denoted by site value being changed to 1
            for (int i = 0; i < neighbours(row, col).length; i++) {
                if((row==1)&&(col==1)) return;
                if((row==gridSize)&&(col==gridSize)) return;
                if (isOpen(neighbours(row, col)[i][0], neighbours(row, col)[i][1])) { // if neighbouring site is open
                    if (!grid.connected(one_d(row, col), one_d(neighbours(row, col)[i][0], neighbours(row, col)[i][1]))) { // if neighbouring open site is not connected
                        grid.union(one_d(row, col), one_d(neighbours(row, col)[i][0], neighbours(row, col)[i][1])); //connecting to neighbouring open site, if not already cnnected
                    }
                }
            }
        }

        // is the site (row, col) open?
        public boolean isOpen(int row, int col) {
            if (row <= 0 || row > gridSize ) throw new IllegalArgumentException("row index out of bounds");
            if (col <= 0 || col > gridSize ) throw new IllegalArgumentException("col index out of bounds");
            return (sites[row][col] == 1); // if site value is 1, site is open, else blocked
        }

        // is the site (row, col) full?
        public boolean isFull(int row, int col) {
            if(!isOpen(row,col)) return false;
            if (row <= 0 || row > gridSize ) throw new IllegalArgumentException("row index out of bounds");
            if (col <= 0 || col > gridSize ) throw new IllegalArgumentException("col index out of bounds");
            if(row==1) return isOpen(row,col); // for row 1 elements, if the site is open, it is full as it is connected to itself(a top row site) and it is open
            else return grid.connected(one_d(row,col),gridSize*gridSize); //if a site is connected to the virtual top, it is full
        }

        // returns the number of open sites
        public int numberOfOpenSites() {
            return openCounter; // returns number of open sites
        }

        // does the system percolate?
        public boolean percolates() {
        return grid.connected(gridSize*gridSize +1,gridSize*gridSize); //if virtual top is connected to virtual bottom, it percolates
        }

        // test client (optional)
        public static void main(String[] args) {
            Percolation first = new Percolation(3);
            first.open(2, 3);
            first.open(1, 3);
            first.open(3, 3);
            first.open(3, 1);
            first.open(2, 1);
            first.open(1,1);
            // System.out.println(first.percolates());
           // first.print_nbrs(3,1);
            System.out.println(first.isFull(1,2));
        }
    }




