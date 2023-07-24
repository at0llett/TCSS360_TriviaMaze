package model;

public class Room {

    private Door[] myDoors = new Door[4];

    private boolean visited = false;

    public static final int NORTH_INDEX = 0;
    public static final int EAST_INDEX = 1;
    public static final int SOUTH_INDEX = 2;
    public static final int WEST_INDEX = 3;

    public Room(Door theNorthDoor, Door theEastDoor, Door theSouthDoor, Door theWestDoor) {
        myDoors[NORTH_INDEX] = theNorthDoor;
        myDoors[EAST_INDEX] = theEastDoor;
        myDoors[SOUTH_INDEX] = theSouthDoor;
        myDoors[WEST_INDEX] = theWestDoor;
    }

    public Room() {

    }

    public Door getDoor(final int index) {
        return myDoors[index];
    }

    public void setVisited(boolean theVisited) {
        visited = theVisited;
    }

    public boolean getVisited() {
        return visited;
    }

}