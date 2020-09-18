/* *****************************************************************************
 *  Name: Tulip Pandey
 *  Date: 29th March 2020
 *  Description:  Algorithms Part 1 : Assignment 1 :Title - Percolation : Submission file 2: PercolationStats.java
 **************************************************************************** */
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {



    private final double[] results ;  // stores the fraction of open sites at which the system just percolates for different trials
    private final int numTrials; // stores the number of trials to be performed
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 ) throw new IllegalArgumentException("given grid size is not a positive integer");
        if (trials <= 0 ) throw new IllegalArgumentException("given number of trials is not a positive integer");

        Percolation[] sample; // Percolation object used for calling methods of Percolation
        numTrials = trials;
        results = new double[trials] ;
        int i = 0;
        sample = new Percolation[trials] ;
        while(i<trials){
            sample[i] = new Percolation(n) ;
            while(!sample[i].percolates()){
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if(!sample[i].isOpen(row,col))
                    sample[i].open(row,col);
            }
            double sites_num = n*n ;
            results[i] = sample[i].numberOfOpenSites()/sites_num ;
            i++;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results) ;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results) ;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return ( mean() - ((1.96*stddev())/Math.sqrt(numTrials)) );
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return ( mean() + ((1.96*stddev())/Math.sqrt(numTrials)) );
    }

    // test client (see below)
    public static void main(String[] args){
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats example = new PercolationStats(n,trials );


        System.out.print("mean                    = ") ;
        System.out.println(example.mean());
        System.out.print("stddev                  = ") ;
        System.out.println(example.stddev()) ;
        System.out.print("95% confidence interval = [") ;
        System.out.print(example.confidenceLo());
        System.out.print(", ");
        System.out.print(example.confidenceHi());
        System.out.println("]") ;




    }


}