package main.java;

import main.java.model.Coordinate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Loader {
    private final File instanceFile;
    private int NUM_CITIES = 0;

    public Loader(File instanceFile) {
        this.instanceFile = instanceFile;
    }

    public Coordinate[] loadInstance() {
        Coordinate[] coordinates = new Coordinate[0];
        try (BufferedReader reader = new BufferedReader(new FileReader(instanceFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splittedLine = line.split("\\s+");
                boolean lineStartsWithAnIndex = splittedLine[0].equals(String.valueOf(coordinates.length + 1));
                boolean lineConsistsOfThreeElements = splittedLine.length == 3;
                boolean lineIsCoordinate = lineStartsWithAnIndex && lineConsistsOfThreeElements;
                if (lineIsCoordinate) {
                    NUM_CITIES++;
                    coordinates = Arrays.copyOf(coordinates, NUM_CITIES);
                    float coordinateX = Float.parseFloat(splittedLine[1]);
                    float coordinateY = Float.parseFloat(splittedLine[2]);
                    coordinates[NUM_CITIES - 1] = new Coordinate(coordinateX, coordinateY);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coordinates;
    }

    public int getNUM_CITIES() {
        return NUM_CITIES;
    }
}
