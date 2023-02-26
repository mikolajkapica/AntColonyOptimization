package main.java.model;

import java.util.Arrays;

public class Ant {
    private int currentPosition;
    private int[] visited;
    private int citiesVisitedCounter;

    public Ant(int startingPosition, int NUMBER_OF_CITIES) {
        visited = new int[NUMBER_OF_CITIES + 1];
        Arrays.fill(visited, -1);
        citiesVisitedCounter = 0;
        visit(startingPosition);
    }
    public void visit(int city) {
        visited[citiesVisitedCounter] = city;
        currentPosition = city;
        citiesVisitedCounter++;
    }
    public boolean isVisited(int city) {
        for (int cityVisited : visited) {
            if (cityVisited == city) {
                return true;
            }
        }
        return false;
    }

    public void setCitiesVisitedCounter(int citiesVisitedCounter) {
        this.citiesVisitedCounter = citiesVisitedCounter;
    }

    public int[] getVisited() {
        return visited;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setVisited(int[] visited) {
        this.visited = visited;
    }
}
