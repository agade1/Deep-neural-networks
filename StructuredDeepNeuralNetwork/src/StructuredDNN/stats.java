package StructuredDNN;

import java.util.*;

public class stats {
    public Double sum(ArrayList<Double> a) {
        if (a.size() > 0) {
            Double sum = 0.;

            for (Double i : a) {
                sum += i;
            }
            return sum;
        }
        return 0.;
    }

    public double mean(ArrayList<Double> a) {
        Double sum = sum(a);
        double mean = 0;
        mean = sum / (a.size() * 1.0);
        return mean;
    }

    public double median(ArrayList<Double> a) {
        int middle = a.size() / 2;

        if (a.size() % 2 == 1) {
            return a.get(middle);
        } else {
            return (a.get(middle - 1) + a.get(middle)) / 2.0;
        }
    }

    public double deviation(ArrayList<Double> a) {
        Double sum = 0.;
        double mean = mean(a);

        for (Double i : a)
            sum += Math.pow((i - mean), 2);
        return Math.sqrt(sum / (a.size() - 1)); // sample
    }

    public void printStats(ArrayList<Double> sqftlist, ArrayList<Double> errorPercent, int testset) {
        System.out.println("\nprice/SQFT Approach Statistics");
        Collections.sort(sqftlist);
        System.out.println("SQFT Median % error is: " + this.median(sqftlist));
        System.out.println("SQFT Mean % error is: " + this.mean(sqftlist));
        System.out.println("SQFT Standard Deviation is: " + this.deviation(sqftlist));


        int within5 = 0, within10 = 0, within20 = 0, above25 = 0;
        for (int i = 0; i < sqftlist.size(); i++) {
            if (sqftlist.get(i) <= 5) {
                within5++;
            }
            if (sqftlist.get(i) <= 10) {
                within10++;
            }
            if (sqftlist.get(i) <= 20) {
                within20++;
            }
            if (sqftlist.get(i) > 25) {
                above25++;
            }
        }

        System.out.println("Within 5% is = " + (within5 / (testset * .01))
                + "%");
        System.out.println("Within 10% is = " + (within10 / (testset * .01))
                + "%");
        System.out
                .println("Within 20% is = " + (within20 / (testset * .01)) + "%");
        System.out
                .println("above 25% is = " + (above25 / (testset * .01)) + "%");

        System.out.println("\n02127-Structured Deep Neural Network Approach Statistics");
        // System.out.println("avg is: "+ avg/testSetSize);
        Collections.sort(errorPercent);
        System.out.println("Median % error is: " + this.median(errorPercent));
        System.out.println("Mean % error is: " + this.mean(errorPercent));
        System.out.println("Standard Deviation is: " + this.deviation(errorPercent));

        within5 = 0;
        within10 = 0;
        within20 = 0;
        above25 = 0;
        for (int i = 0; i < errorPercent.size(); i++) {
            if (errorPercent.get(i) <= 5) {
                within5++;
            }
            if (errorPercent.get(i) <= 10) {
                within10++;
            }
            if (errorPercent.get(i) <= 20) {
                within20++;
            }
            if (errorPercent.get(i) > 25) {
                above25++;
            }
        }
        System.out.println("Within 5% is = " + (within5 / (testset * .01)) + "%");
        System.out.println("Within 10% is = " + (within10 / (testset * .01)) + "%");
        System.out.println("Within 20% is = " + (within20 / (testset * .01)) + "%");
        System.out.println("above 25% is = " + (above25 / (testset * .01)) + "%");
    }
}
