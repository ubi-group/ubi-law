package com.itcag.legalyzer.util.inference;

import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.util.MathToolbox;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 */
public class Matrix {

    private final double[][] weights;
    
    private final Categories categories;
    
    public Matrix(double[][] weights, Categories categories) {
        this.weights = weights;
        this.categories = categories;
    }

    public TreeMap<Integer, Double> evaluate(TreeMap<Integer, Category> evaluationScores) {

        TreeMap<Integer, Double> retVal = new TreeMap<>();
        
        for (int i = 0; i < weights.length; i++) {
            
            double totalWeightedScore = 0.00;
            
            for (Map.Entry<Integer, Category> entry : evaluationScores.entrySet()) {

                int predictedCategory = entry.getKey();
                double score = entry.getValue().getScore();

                double weight = weights[i][predictedCategory];
                
                double weightedScore = weight * score;
                totalWeightedScore += weightedScore;

            }

            retVal.put(i, totalWeightedScore);
            
        }
        
        return retVal;
        
    }

    public void print() {

        for (int i = 0; i < this.weights.length; i++) {
            if (i == 0) {
                System.out.print("    " + i);
            } else if (i < 10) {
                System.out.print("     " + i);
            } else {
                System.out.print("    " + i);
            }
        }
        System.out.print("\n");
        
        for (int i = 0; i < this.weights.length; i++) {
            System.out.print("  ");
            for (int j = 0; j < this.weights.length; j++) {
                double rounded = MathToolbox.roundDouble(this.weights[i][j], 2);
                String value = Double.toString(rounded);
                while (value.length() < 4) value += "0";
                System.out.print(value + "  ");
            }
            String label = this.categories.get().get(i).getLabel();
            if (i < 10) {
                System.out.print("|   " + i + " = " + label + "\n");
            } else {
                System.out.print("|  " + i + " = " + label + "\n");
            }
        }
        
    }
 
    public void printCrossReference() {
        
        for (int i = 0; i < this.weights.length; i++) {
            
            System.out.println(this.categories.get().get(i).getIndex() + " " + this.categories.get().get(i).getLabel());
            
            for (int j = 0; j < this.weights.length; j++) {
                
                if (i == j) continue;
                if (i == 0) continue;
                if (j == 0) continue;

                double rounded = MathToolbox.roundDouble(this.weights[i][j], 2);
                if (rounded > 0.10) {
                    System.out.println("\t" + rounded + " " + this.categories.get().get(j).getIndex() + " " + this.categories.get().get(j).getLabel());
                }
            
            }
            
            System.out.println();
            
        }
        
    }
    
}
