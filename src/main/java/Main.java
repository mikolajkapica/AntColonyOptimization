package main.java;

import main.java.model.AntColonyOptimizationAlgorithm;
import main.java.model.Calculator;
import main.java.model.Coordinate;
import main.java.view.CoordinatesToView;
import processing.core.PApplet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Main extends PApplet {
    static final int WINDOW_DIMENSION_X = 800;
    static final int WINDOW_DIMENSION_Y = 800;
    static final int BACKGROUND_COLOR = 20;
    static final int FOREGROUND_COlOR = 216;
    static private Coordinate[] COORDINATES;
    static private int NUMBER_OF_CITIES;
    static private int NUMBER_OF_ANTS;
    static private float ALPHA_COEFFICIENT;
    static private float BETA_COEFFICIENT;
    static private float EVAPORATION_QUANTITY;
    static private float PHEROMONE_QUANTITY;
    static private AntColonyOptimizationAlgorithm a;
    static private BufferedWriter bw;

    @Override
    public void settings() {
        size(WINDOW_DIMENSION_X, WINDOW_DIMENSION_Y);
        pixelDensity(displayDensity());
    }

    @Override
    public void setup() {
        surface.setTitle("TSP Problem: Ant Colony Optimization Technique");
        File TSPINSTANCE = new File(new File("").getAbsolutePath() + "/src/main/assets/tspinstances/dj38.tsp");
        final Loader LOADER = new Loader(TSPINSTANCE);
        COORDINATES = LOADER.loadInstance();
        NUMBER_OF_CITIES = LOADER.getNUM_CITIES();
        NUMBER_OF_ANTS = 600;
        ALPHA_COEFFICIENT = 1;
        BETA_COEFFICIENT = 1;
        EVAPORATION_QUANTITY = 0.7f;
        PHEROMONE_QUANTITY = 5;

        a = new AntColonyOptimizationAlgorithm(
                NUMBER_OF_ANTS,
                ALPHA_COEFFICIENT,
                BETA_COEFFICIENT,
                EVAPORATION_QUANTITY,
                PHEROMONE_QUANTITY,
                COORDINATES,
                NUMBER_OF_CITIES
        );
        try {
            bw = new BufferedWriter(new FileWriter("ants-data.csv"));
            bw.write("iteration,avg_dist,max_dist,min_dist,std\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    int t = 0;
    public void draw() {
        t++;
        System.out.println("iteracja nr " + t);
        a.algorithm();
        float[] tl = a.getToursLengths();
        Arrays.sort(tl);
        try {
            bw = new BufferedWriter(new FileWriter("ants-data.csv", true));
            bw.write(t + "," + tl[NUMBER_OF_ANTS/2] + "," + tl[NUMBER_OF_ANTS-1] + "," + tl[0] + "," + Calculator.getStandardDeviation(tl) +"\n");
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (a.isNewTour()) {
            COORDINATES = a.getShortestTourCoordinates();
            System.out.println(a.getShortestLength());
            System.out.println(Arrays.toString(a.getShortestTourOrder()));

            CoordinatesToView coordinatesToView = new CoordinatesToView(COORDINATES);
            Coordinate[] COORDINATES_VIEW = coordinatesToView.resizePoints(WINDOW_DIMENSION_X, WINDOW_DIMENSION_Y);

            background(BACKGROUND_COLOR);
            noFill();
            stroke(FOREGROUND_COlOR);
            beginShape();
            for (int i = 0; i < NUMBER_OF_CITIES + 1; i++) {
                vertex((COORDINATES_VIEW[i].getX()), COORDINATES_VIEW[i].getY());
            }
            endShape();
        }
    }

    public static void main(String[] args) {
        PApplet.main("main.java.Main");
    }

}