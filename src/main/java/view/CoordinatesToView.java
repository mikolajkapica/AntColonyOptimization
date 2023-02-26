package main.java.view;

import main.java.model.Coordinate;

public class CoordinatesToView {
    private final Coordinate[] coordinates;
    private final Coordinate[] coordinatesView;

    public CoordinatesToView(Coordinate[] coordinates) {
        this.coordinates = coordinates;
        coordinatesView = new Coordinate[coordinates.length];
    }

    public Coordinate[] resizePoints(int windowDimensionX, int windowDimensionY) {
        for (int i = 0; i < coordinates.length; i++) {
            coordinatesView[i] = new Coordinate(coordinates[i].getX(), coordinates[i].getY());
        }

        float minY = minY();
        float minX = minX();

        for (Coordinate coordinate : coordinatesView) {
            coordinate.setX(coordinate.getX() - minX);
            coordinate.setY(coordinate.getY() - minY);
        }

        float maxX = maxX();
        float maxY = maxY();
        for (Coordinate coordinate : coordinatesView) {
            coordinate.setX(coordinate.getX() - maxX / 2);
            coordinate.setY(coordinate.getY() - maxY / 2);
        }
        for (Coordinate coordinate : coordinatesView) {
            coordinate.setX(coordinate.getX() * 0.8f);
            coordinate.setY(coordinate.getY() * 0.8f);
        }
        for (Coordinate coordinate : coordinatesView) {
            coordinate.setX(coordinate.getX() / (maxX / windowDimensionX));
            coordinate.setY(coordinate.getY() / (maxY / windowDimensionY));
        }
        for (Coordinate coordinate : coordinatesView) {
            coordinate.setX(coordinate.getX() + windowDimensionX / 2f);
            coordinate.setY(coordinate.getY() + windowDimensionY / 2f);
        }
        return coordinatesView;
    }


    private float maxX() {
        float maxX = coordinatesView[0].getX();
        for (Coordinate coordinate : coordinatesView) {
            if (maxX < coordinate.getX()) {
                maxX = coordinate.getX();
            }
        }
        return maxX;
    }

    private float minX() {
        float minX = coordinatesView[0].getX();
        for (Coordinate coordinate : coordinatesView) {
            if (minX > coordinate.getX()) {
                minX = coordinate.getX();
            }
        }
        return minX;
    }

    private float maxY() {
        float maxY = coordinatesView[0].getY();
        for (Coordinate coordinate : coordinatesView) {
            if (maxY < coordinate.getY()) {
                maxY = coordinate.getY();
            }
        }
        return maxY;
    }

    private float minY() {
        float minY = coordinatesView[0].getY();
        for (Coordinate coordinate : coordinatesView) {
            if (minY > coordinate.getY()) {
                minY = coordinate.getY();
            }
        }
        return minY;
    }
}
