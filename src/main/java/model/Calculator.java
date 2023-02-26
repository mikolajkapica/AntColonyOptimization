package main.java.model;

import java.util.Random;

public class Calculator {
    private final int NUMBER_OF_CITIES;
    private final Coordinate[] COORDINATES;
    private final int NUMBER_OF_ANTS;

    public Calculator(int NUMBER_OF_CITIES, Coordinate[] COORDINATES, int NUMBER_OF_ANTS) {
        this.NUMBER_OF_CITIES = NUMBER_OF_CITIES;
        this.COORDINATES = COORDINATES;
        this.NUMBER_OF_ANTS = NUMBER_OF_ANTS;
    }

    public float[][] fillIntensity() {
        float[][] intensity = new float[NUMBER_OF_CITIES][NUMBER_OF_CITIES];
        for (int i = 0; i < NUMBER_OF_CITIES; i++) {
            for (int j = 0; j < NUMBER_OF_CITIES; j++) {
                intensity[i][j] = 0.1f;
            }
        }
        return intensity;
    }

    public float[][] fillVisibility() {
        float[][] visibility = new float[NUMBER_OF_CITIES][NUMBER_OF_CITIES];
        for (int i = 0; i < NUMBER_OF_CITIES; i++) {
            for (int j = 0; j < NUMBER_OF_CITIES; j++) {
                float distance = getDistanceBetweenCities(i, j);
                if (distance != 0) {
                    visibility[i][j] = 1 / distance;
                }
            }
        }
        return visibility;
    }


    private float getDistanceBetweenCities(int startingCity, int destinationCity) {
        return getdistanceBetweenCoordinates(COORDINATES[startingCity], COORDINATES[destinationCity]);
    }

    private float getdistanceBetweenCoordinates(Coordinate startingCoordinate, Coordinate destinationCoordinate) {
        float xDifference = startingCoordinate.getX() - destinationCoordinate.getX();
        float yDifference = startingCoordinate.getY() - destinationCoordinate.getY();
        return (float) Math.sqrt(xDifference * xDifference + yDifference * yDifference);
    }

    public float[][][] calculateProbability(float[][] intensity, float[][] visibility) {
        float[][][] probability = new float[NUMBER_OF_ANTS][NUMBER_OF_CITIES][NUMBER_OF_CITIES];
        for (int k = 0; k < NUMBER_OF_ANTS; k++) {
            for (int i = 0; i < NUMBER_OF_CITIES; i++) {
                float mianownik = 0;
                for (int s = 0; s < NUMBER_OF_CITIES; s++) {
                    mianownik += intensity[i][s] * visibility[i][s];
                }
                for (int j = 0; j < NUMBER_OF_CITIES; j++) {
                    float licznik = intensity[i][j] * visibility[i][j];
                    probability[k][i][j] = licznik / mianownik;
                }
            }
        }
        return probability;
    }
    public float[][][] calculateProbability(float[][] intensity, float[][] visibility, float ALPHA_COEFFICIENT, float BETA_COEFFICIENT) {
        float[][][] probability = new float[NUMBER_OF_ANTS][NUMBER_OF_CITIES][NUMBER_OF_CITIES];
        for (int k = 0; k < NUMBER_OF_ANTS; k++) {
            for (int i = 0; i < NUMBER_OF_CITIES; i++) {
                float mianownik = 0;
                for (int s = 0; s < NUMBER_OF_CITIES; s++) {
                    mianownik += Math.pow(intensity[i][s], ALPHA_COEFFICIENT) * Math.pow(visibility[i][s], BETA_COEFFICIENT);
                }
                for (int j = 0; j < NUMBER_OF_CITIES; j++) {
                    float licznik = (float) (Math.pow(intensity[i][j], ALPHA_COEFFICIENT) * Math.pow(visibility[i][j], BETA_COEFFICIENT));
                    probability[k][i][j] = licznik / mianownik;
                    if (probability[k][i][j] <= 0) {
                        probability[k][i][j] = Float.MIN_VALUE;
                    }
                }
            }
        }
        return probability;
    }


    public int chooseNextCity(int antNumber, int antCurrentPosition, float[][][] probability) {
        float[] antNextStepProbabilitic = probability[antNumber][antCurrentPosition];
        float[] antNextStepCumulativeSum = new float[NUMBER_OF_CITIES];

        int nextCity = -1;

        for (int i = 0; i < NUMBER_OF_CITIES; i++) {
            for (int j = i; j < NUMBER_OF_CITIES; j++) {
                antNextStepCumulativeSum[i] += antNextStepProbabilitic[j];
            }
        }

        float randomNumber = new Random().nextFloat(antNextStepCumulativeSum[0]);

        for (int i = 0; i < antNextStepCumulativeSum.length; i++) {
            if (randomNumber < antNextStepCumulativeSum[i]) {
                nextCity = i;
            }
        }
        return nextCity;
    }

    public float calculateLengthOfTour(int[] visited) {
        float distance = 0;
        for (int i = 0; i < NUMBER_OF_CITIES; i++) {
            distance += getDistanceBetweenCities(visited[i], visited[i+1]);
        }
        return distance;
    }

    public float[][] updateIntensity(float[][] intensity, Ant[] ants, float EVAPORATION_QUANTITY, float PHEROMONE_QUANTITY) {
        float[][] newIntensity = new float[NUMBER_OF_CITIES][NUMBER_OF_CITIES];
        float[][] deltaIntensity = new float[NUMBER_OF_CITIES][NUMBER_OF_CITIES];
        for (int k = 0; k < NUMBER_OF_ANTS; k++) {
            float lengthOfTourK = calculateLengthOfTour(ants[k].getVisited());
            for (int i = 0; i < NUMBER_OF_CITIES; i++) {
                int cityStart = ants[k].getVisited()[i];
                int cityEnd = ants[k].getVisited()[i+1];
                deltaIntensity[cityStart][cityEnd] += PHEROMONE_QUANTITY / lengthOfTourK;
            }
        }
        for (int i = 0; i < NUMBER_OF_CITIES; i++) {
            for (int j = 0; j < NUMBER_OF_CITIES; j++) {
                newIntensity[i][j] = EVAPORATION_QUANTITY * intensity[i][j] + deltaIntensity[i][j];
            }
        }
        return newIntensity;
    }
    public static float getStandardDeviation(float[] array) {
        float sum = 0.0f;
        float mean;
        float temp = 0.0f;

        // calculate the mean of the array
        for (float num : array) {
            sum += num;
        }
        mean = sum / array.length;

        // calculate the standard deviation
        for (float num : array) {
            temp += (num - mean) * (num - mean);
        }
        float variance = temp / array.length;

        return (float) Math.sqrt(variance);
    }
}
