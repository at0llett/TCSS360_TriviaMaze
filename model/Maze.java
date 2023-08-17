package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

public class Maze implements Serializable {

    @Serial
    private static final long serialVersionUID = -2641303058682284534L;

    private final int rows = 5;
    private final int columns = 5;
    private String myCharName;
    private String myCharImage;

    private Room[][] myMaze = new Room[rows][columns];

    private int myX = 0;

    private int myY = 0;

    private boolean myGameStatus = true;
    private int myRoomNumber = 1;
    private int myScoreValue;

    private int myPotions = 1;

    private int myDiffLevel = 0;
    private int myQuestionNumber;


    private final PropertyChangeSupport myPcs;
    //private List<PropertyChangeListener> myListeners;
    private static Maze myInstance = new Maze();




    public static Maze getMyInstance() {
        return myInstance;
    }

    public Maze () {
        myPcs = new PropertyChangeSupport(this);
        startGame();
    }

    public int getRoomNumber() {
        return myRoomNumber;
    }
    public int getDiffLevel() {
        return myDiffLevel;
    }

    public void setDiffLevel(final int theDiffLevel) {
        myDiffLevel = theDiffLevel;
    }

    public void startGame() {
        myGameStatus = true;
        setQuestionNumber(1);
        setScore(0);
        setX(0);
        setY(0);
        roomSetup();
    }

    public boolean getGameStatus() {
        return myGameStatus;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        myPcs.addPropertyChangeListener(listener);
    }

    public void setPotions(final int thePotions) {
        myPotions = thePotions;
    }

    public int getPotions() {
        return myPotions;
    }

    public void gainPotions() {
        int rand = new Random().nextInt(10);
        if (rand == 1) {
            myPotions++;
        }
    }

    public void setScore(final int theScore) {
        myScoreValue = theScore;
    }

    public int getScore() {
        return myScoreValue;
    }

    public void roomSetup() {
        for (int x = 0; x < rows; ++x) {
            for (int y = 0; y < columns; ++y) {

                Door northDoor = null;
                Door southDoor = null;
                Door eastDoor = null;
                Door westDoor = null;

                if (x > 0) {
                    westDoor = new Door();
                    westDoor.setVisible(true);
                }

                if (y > 0) {
                    northDoor = new Door();
                    northDoor.setVisible(true);
                }

                if (x < rows - 1) {
                    eastDoor = new Door();
                    eastDoor.setVisible(true);
                }

                if (y < columns - 1) {
                    southDoor = new Door();
                    southDoor.setVisible(true);
                }
                myMaze[x][y] = new Room(northDoor, eastDoor, southDoor, westDoor, myRoomNumber);
                myRoomNumber++;
            }
        }
    }

    public int getX () {
        return myX;
    }

    public int getY () {
        return myY;
    }

    public void setX (int theX) {
        myX = theX;
    }

    public void setY (int theY) {
        myY = theY;
    }

    public void setLocation (int theX, int theY) {
        myX = theX;
        myY = theY;
    }

    public Room getCurrentRoom() {
        return myMaze[myX][myY];
    }

    public Room getRoom(final int theIndexX, final int theIndexY) {
        if (theIndexX >= myMaze.length || theIndexY >= myMaze.length) {
            throw new IllegalArgumentException("Index out of bounds: "
                    + theIndexX + " and " + theIndexY);
        }
        return myMaze[theIndexX][theIndexY];
    }


    public void endGame() {
        myGameStatus = false;
    }

    public boolean gameFinished() {
        if (myX == rows - 1 && myY == columns - 1) {
            endGame();
            return true;
        } else {
            return false;
        }
    }

    public boolean display (Direction myDir) {


        if (myDir == Direction.NORTH && myY - 1 < 0) {
            if (myMaze[myX][myY].getDoor(Room.NORTH_INDEX) != null) { //because some rooms we didn't create a door. like in top left, we didnt create a north door, so it'll be null.
                myMaze[myX][myY].getDoor(Room.NORTH_INDEX).setVisible(false);
            }
            return false;
        }

        if (myDir == Direction.EAST && myX + 1 >= columns) {
            if (myMaze[myX][myY].getDoor(Room.EAST_INDEX) != null) {
                myMaze[myX][myY].getDoor(Room.EAST_INDEX).setVisible(false);
            }
            return false;
        }

        if (myDir == Direction.SOUTH && myY + 1 >= rows) {
            if (myMaze[myX][myY].getDoor(Room.SOUTH_INDEX) != null) {
                myMaze[myX][myY].getDoor(Room.SOUTH_INDEX).setVisible(false);
            }
            return false;
//            myMaze[myX][myY].getDoor(Room.SOUTH_INDEX).setVisible(false);
//            //System.out.println("SOUTH updating?");
//            return false;
        }

        if (myDir == Direction.WEST && myX - 1 < 0) {
            if (myMaze[myX][myY].getDoor(Room.WEST_INDEX) != null) {
                myMaze[myX][myY].getDoor(Room.WEST_INDEX).setVisible(false);
            }
            return false;
//            myMaze[myX][myY].getDoor(Room.WEST_INDEX).setVisible(false);
//            return false;
        }
        return true;
    }

    //TODO figure this out
    //TODO call getLock on null
    public void movePlayer(Direction myDir) {

        //System.out.println("Is this getting called?");

        if (myMaze[myX][myY] == null) {
            System.out.println("Why is this null?");
            return;
        }

        if (myDir == Direction.NORTH && myMaze[myX][myY].getDoor(Room.NORTH_INDEX) != null && !myMaze[myX][myY].getDoor(Room.NORTH_INDEX).getLock()) {
            setLocation(myX, myY - 1);
            System.out.println(myY);
            System.out.println("NORTH GETTING CALLED???");
        }

        if (myDir == Direction.EAST && myMaze[myX][myY].getDoor(Room.EAST_INDEX) != null && !myMaze[myX][myY].getDoor(Room.EAST_INDEX).getLock()) { //exclimation mark
            setLocation(myX + 1, myY);
        }

        if (myDir == Direction.SOUTH && myMaze[myX][myY].getDoor(Room.SOUTH_INDEX) != null && !myMaze[myX][myY].getDoor(Room.SOUTH_INDEX).getLock()) {
            setLocation(myX, myY + 1);
            System.out.println("SOUTH getting called?");
        }

        if (myDir == Direction.WEST && myMaze[myX][myY].getDoor(Room.WEST_INDEX) != null && !myMaze[myX][myY].getDoor(Room.WEST_INDEX).getLock()) {
            setLocation(myX - 1, myY);
        }

        myPcs.firePropertyChange("ChangeX", myX, myX);
        myPcs.firePropertyChange("ChangeY", myY, myY);
    }

    public boolean canMoveDirection(final Direction theDir) {
        //Door localDoor =
        return true;
    }


    public boolean isPossible() {

        for (int x = 0; x < rows; ++x) {
            for (int y = 0; y < columns; ++y) {
                myMaze[x][y].setVisited(false);
            }
        }


        return isPossibleHelper(myX, myY);



    }

    private boolean isPossibleHelper (int theX, int theY) {
        if (!myMaze[theX][theY].getVisited()) {

            myMaze[theX][theY].setVisited(true);

            if (theX == 4 && theY == 4) {
                return true;
            }

            if (myMaze[theX][theY].getDoor(Room.NORTH_INDEX) != null
                    && !myMaze[theX][theY].getDoor(Room.NORTH_INDEX).getForeverLocked()) {
                boolean northCheck = isPossibleHelper(theX, theY - 1);
                if (northCheck) {
                    return true;
                }
            }

            if (myMaze[theX][theY].getDoor(Room.SOUTH_INDEX) != null
                    && !myMaze[theX][theY].getDoor(Room.SOUTH_INDEX).getForeverLocked()) {
                boolean southCheck = isPossibleHelper(theX, theY + 1);
                if (southCheck) {
                    return true;
                }
            }

            if (myMaze[theX][theY].getDoor(Room.EAST_INDEX) != null
                    && !myMaze[theX][theY].getDoor(Room.EAST_INDEX).getForeverLocked()) {
                boolean eastCheck = isPossibleHelper(theX + 1, theY);
                if (eastCheck) {
                    return true;
                }
            }

            if (myMaze[theX][theY].getDoor(Room.WEST_INDEX) != null
                    && !myMaze[theX][theY].getDoor(Room.WEST_INDEX).getForeverLocked()) {
                boolean westCheck = isPossibleHelper(theX - 1, theY);
                if (westCheck) {
                    return true;
                }
            }
        }
        return false;
    }

    public Room[][] getMaze() {
        return myMaze;
    }


    public void unlockingDoors(final Direction theDir) {
        if (theDir == Direction.NORTH) {
            System.out.println("UL WEST: " + getX());
            getRoom(getX(), getY() - 1).getDoor(Room.SOUTH_INDEX).unlock();
        } else if (theDir == Direction.SOUTH) {
            System.out.println("UL SOUTH: " + getY());
            getRoom(getX(), getY() + 1).getDoor(Room.NORTH_INDEX).unlock();
        } else if (theDir == Direction.WEST) {
            System.out.println("UL WEST: " + getX());
            getRoom(getX() - 1, getY()).getDoor(Room.EAST_INDEX).unlock();
        } else if (theDir == Direction.EAST) {
            System.out.println("UL EAST: " + getX());
            getRoom(getX() + 1, getY()).getDoor(Room.WEST_INDEX).unlock();
        }
    }

    public void lockingDoors(final Direction theDir) {
        if (theDir == Direction.NORTH) {
            System.out.println("NORTH: " + getY());
            getRoom(getX(), getY() - 1).getDoor(Room.SOUTH_INDEX).setForeverLocked(true);
        } else if (theDir == Direction.SOUTH) {
            System.out.println("SOUTH: " + getY());
            getRoom(getX(), getY() + 1).getDoor(Room.NORTH_INDEX).setForeverLocked(true);
        } else if (theDir == Direction.WEST) {
            System.out.println("WEST: " + getX());
            getRoom(getX() - 1, getY()).getDoor(Room.EAST_INDEX).setForeverLocked(true);
        } else if (theDir == Direction.EAST) {
            System.out.println("EAST: " + getX());
            getRoom(getX() + 1, getY()).getDoor(Room.WEST_INDEX).setForeverLocked(true);
        }
    }

    public void setCharName(String theCharName) {
        myCharName = theCharName;
    }

    public String getCharName() {
        return myCharName;
    }

    public void setCharImage(String theCharImage) {
        myCharImage = theCharImage;
    }

    public String getCharImage() {
        return myCharImage;
    }

    public void setQuestionNumber(final int theQuestionNumber) {
        myQuestionNumber = theQuestionNumber;
    }

    public int getQuestionNumber() {
        return myQuestionNumber;
    }



}
