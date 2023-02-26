package main.java.model;

import java.util.Arrays;
import java.util.Random;

public class AntColonyOptimizationAlgorithm {
    private final int NUMBER_OF_ANTS;
    private final float ALPHA_COEFFICIENT;
    private final float BETA_COEFFICIENT;
    private final float EVAPORATION_QUANTITY;
    private final float PHEROMONE_QUANTITY;
    private final Coordinate[] COORDINATES;
    private final int NUMBER_OF_CITIES;
    private final Coordinate[] shortestTourCoordinates;
    private final Calculator CALCULATOR;
    private final float[][] visibility;
    private final Ant[] ants;
    private final int[] startingPositionForAnt;
    private int[] shortestTourOrder;
    private float shortestLength;
    private float[][] intensity;
    private float[][][] probability;
    private boolean isNewTour;
    private float[] toursLengths;

    public AntColonyOptimizationAlgorithm(int NUMBER_OF_ANTS, float ALPHA_COEFFICIENT, float BETA_COEFFICIENT, float EVAPORATION_QUANTITY, float PHEROMONE_QUANTITY, Coordinate[] COORDINATES, int NUMBER_OF_CITIES) {
        this.NUMBER_OF_ANTS = NUMBER_OF_ANTS;
        this.ALPHA_COEFFICIENT = ALPHA_COEFFICIENT;
        this.BETA_COEFFICIENT = BETA_COEFFICIENT;
        this.EVAPORATION_QUANTITY = EVAPORATION_QUANTITY;
        this.PHEROMONE_QUANTITY = PHEROMONE_QUANTITY;
        this.COORDINATES = COORDINATES;
        this.NUMBER_OF_CITIES = NUMBER_OF_CITIES;

        shortestTourOrder = new int[NUMBER_OF_CITIES + 1];
        shortestTourCoordinates = new Coordinate[NUMBER_OF_CITIES + 1];
        shortestLength = Float.MAX_VALUE;
        CALCULATOR = new Calculator(NUMBER_OF_CITIES, COORDINATES, NUMBER_OF_ANTS);

        intensity = CALCULATOR.fillIntensity();
        visibility = CALCULATOR.fillVisibility();
        probability = new float[NUMBER_OF_ANTS][NUMBER_OF_CITIES][NUMBER_OF_CITIES];

        ants = new Ant[NUMBER_OF_ANTS];
        startingPositionForAnt = new int[NUMBER_OF_ANTS];

        Random RANDOM = new Random();
        for (int k = 0; k < NUMBER_OF_ANTS; k++) {
            startingPositionForAnt[k] = RANDOM.nextInt(NUMBER_OF_CITIES);
            ants[k] = new Ant(startingPositionForAnt[k], NUMBER_OF_CITIES);
        }
    }

    public void algorithm() {
        if (ALPHA_COEFFICIENT == 1 && BETA_COEFFICIENT == 1) {
            probability = CALCULATOR.calculateProbability(intensity, visibility);
        } else {
            probability = CALCULATOR.calculateProbability(intensity, visibility, ALPHA_COEFFICIENT, BETA_COEFFICIENT);
        }
        for (int k = 0; k < NUMBER_OF_ANTS; k++) {
            for (int i = 0; i < NUMBER_OF_CITIES - 1; i++) {
                int antCurrentPosition = ants[k].getCurrentPosition();

                for (int j = 0; j < NUMBER_OF_CITIES; j++) {
                    if (ants[k].isVisited(j)) {
                        probability[k][antCurrentPosition][j] = 0;
                    }
                }
                int nextCity = CALCULATOR.chooseNextCity(k, antCurrentPosition, probability);

                ants[k].visit(nextCity);
            }
        }

        isNewTour = false;
        toursLengths = new float[NUMBER_OF_ANTS];
        for (int k = 0; k < NUMBER_OF_ANTS; k++) {
            // Move the ant k from visited k(n) to visited k(1).
            ants[k].visit(startingPositionForAnt[k]);

            // Compute the tour length L k traveled by ant k.

            toursLengths[k] = CALCULATOR.calculateLengthOfTour(ants[k].getVisited());

            // Update the shortest tour found.

            if (toursLengths[k] < shortestLength) {
                shortestLength = toursLengths[k];
                shortestTourOrder = ants[k].getVisited();
                isNewTour = true;
            }
        }

        intensity = CALCULATOR.updateIntensity(intensity, ants, EVAPORATION_QUANTITY, PHEROMONE_QUANTITY);

        // reset
        for (int k = 0; k < NUMBER_OF_ANTS; k++) {
            //   Empty all visitedk
            ants[k].setCitiesVisitedCounter(0);
            ants[k].setVisited(new int[NUMBER_OF_CITIES + 1]);
            Arrays.fill(ants[k].getVisited(), -1);
            ants[k].visit(startingPositionForAnt[k]);
        }
        for (int i = 0; i < NUMBER_OF_CITIES + 1; i++) {
            shortestTourCoordinates[i] = COORDINATES[shortestTourOrder[i]];
        }
    }

    public int[] getShortestTourOrder() {
        return shortestTourOrder;
    }

    public Coordinate[] getShortestTourCoordinates() {
        return shortestTourCoordinates;
    }

    public float getShortestLength() {
        return shortestLength;
    }

    public boolean isNewTour() {
        return isNewTour;
    }

    public float[] getToursLengths() {
        return toursLengths;
    }
}
