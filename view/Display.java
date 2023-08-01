package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicReference;

public class Display extends JPanel {

    //maybe just connect panels
    private JButton myNorthDoor;
    private JButton myWestDoor;
    private JButton mySouthDoor;
    private JButton myEastDoor;
    private Maze myMaze = Maze.getMyInstance();
   // private Maze myMaze = new Maze();
    private JButton optionA;
    private JButton optionB;
    private JButton optionC;
    private JButton optionD;
    private ImageIcon doorIcon;
    private ImageIcon lockIcon;
    private ImageIcon playerIcon;
    private JTextArea myJTextArea;
    private JTextField myJTextField;

    private JLabel answer_labelA;
    private JLabel answer_labelB;
    private JLabel answer_labelC;
    private JLabel answer_labelD;
    private JLabel myJTextRoom;

    private int myQuestionNumber = 1;

    public Display() {
        myMaze.roomSetup(); //we set up every room in the maze.
    }

    @Override
    public void paintComponent (Graphics g) {
//        JPanel mapPanel = new JPanel();
//        mapPanel.setBounds(0, 0, 421, 396);
//        mapPanel.setBackground(Color.WHITE);
//        add(mapPanel);
        super.paintComponent(g);
        Room [][] mazeMap = myMaze.getMaze();
        Graphics2D g2d = (Graphics2D) g;
        playerIcon = new ImageIcon("images/Comic_Books.png");

        //draw border for playing field
        g2d.setColor(Color.YELLOW); //sets the border/line to yellow

        //scales the player icon down a little bit.
        Image image = playerIcon.getImage();
        Image newing = image.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        playerIcon = new ImageIcon(newing);

        int boxWidth = 70;
        int boxHeight = 70;
        for (int currentX = 0; currentX < mazeMap.length * boxWidth; currentX += boxWidth) {
            for (int currentY = 0; currentY < mazeMap[0].length * boxHeight; currentY += boxHeight) {
                g2d.drawRect(currentX, currentY, boxWidth, boxHeight);
                if (currentX == myMaze.getX() * boxWidth && currentY == myMaze.getY() * boxHeight) {
                    g2d.drawImage(playerIcon.getImage(), currentX + boxWidth/2 - playerIcon.getIconWidth()/2,
                            currentY + boxHeight/2 - playerIcon.getIconHeight()/2, null);
                }
            }
        }


        g.setColor(Color.LIGHT_GRAY);
        drawLock(g);
    }

    void drawLock(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Room [][] mazeMap = myMaze.getMaze();
        lockIcon = new ImageIcon("images/lock.png");
        Image image = lockIcon.getImage();
        Image newing = image.getScaledInstance(23, 23, java.awt.Image.SCALE_SMOOTH);
        lockIcon = new ImageIcon(newing);


        int boxWidth = 70;
        int boxHeight = 70;
        for (int i = 0; i < mazeMap.length; i++) { //want to go through each room.
            for (int j = 0; j < mazeMap[i].length; j++) {
                for (int doorIndex = 0; doorIndex < 4; doorIndex++) {
                    int xCoords = i * boxWidth;
                    int yCoords = j * boxHeight;
                    if (myMaze.getRoom(i, j).getDoor(doorIndex) != null && myMaze.getRoom(i, j).getDoor(doorIndex).getForeverLocked()) {
                        if (doorIndex == Room.NORTH_INDEX) { //north
                            g2d.drawImage(lockIcon.getImage(), xCoords + boxWidth/2 - lockIcon.getIconWidth()/2,
                                    yCoords + boxHeight/2 - lockIcon.getIconHeight() - 26, null);
                        } else if (doorIndex == Room.EAST_INDEX) { //east
                            g2d.drawImage(lockIcon.getImage(), xCoords + boxWidth/2 + lockIcon.getIconWidth() + 2,
                                    yCoords + boxHeight/2 - lockIcon.getIconHeight()/2, null);
                        } else if (doorIndex == Room.SOUTH_INDEX) { //south
                            g2d.drawImage(lockIcon.getImage(), xCoords + boxWidth/2 - lockIcon.getIconWidth()/2,
                                    yCoords + boxHeight/2 + lockIcon.getIconHeight() - 2, null);
                        } else { //west
                            g2d.drawImage(lockIcon.getImage(), xCoords + boxWidth/2 - lockIcon.getIconWidth() - 22,
                                    yCoords + boxHeight/2 - lockIcon.getIconHeight()/2, null);
                        }
                    }
                }
            }
        }
        drawRectangle(g);
    }

    void drawRectangle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.YELLOW);
//        Stroke stroke = new BasicStroke(6f);
//        g2d.setStroke(stroke);
        g2d.drawRect(488, 104, 150, 150);
       // createDoorButtons();
        createNorthDoor(); //create north door
        createWestDoor(); //create west door
        createSouthDoor(); //create south door
        createEastDoor(); //create east door
        addListeners(); //method for button listener.
        drawQuestionRectangle(g); //drawing rectangle where we put our question in.
    }

    void drawQuestionRectangle(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.CYAN);
        g2d.setStroke(new BasicStroke(6));
        g2d.drawRect(80, 410, 636, 335);

        displayRoomNumber();
    }

    public void displayRoomNumber() {
        myJTextRoom = new JLabel();
        myJTextRoom.setBounds(425, 36, 360, 31);
        myJTextRoom.setBackground(Color.BLACK);
        myJTextRoom.setForeground(Color.MAGENTA);
        myJTextRoom.setFont(new Font("Aharoni", Font.PLAIN, 20));
        myJTextRoom.setText("You are currently at Room: " + myMaze.getCurrentRoom().getRoomNumber());
        add(myJTextRoom);
    }


    public void createNorthDoor() {
        doorIcon = new ImageIcon("images/door.png");

        //scaling image down.
        Image image = doorIcon.getImage();
        Image newing = image.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        doorIcon = new ImageIcon(newing);
        myNorthDoor = new JButton(doorIcon);
        myNorthDoor.setBackground(Color.RED);
        myNorthDoor.setOpaque(true);
        myNorthDoor.setBounds(545, 105, 35, 35);

        if (!myMaze.display(Direction.NORTH) || myMaze.getCurrentRoom().getDoor(Room.NORTH_INDEX).getForeverLocked()) {  //&& myMaze.getDoor()) {
            myNorthDoor.setBackground(Color.GRAY);
            //myNorthDoor.setOpaque(true);
            myNorthDoor.setEnabled(false);
           // System.out.println("calling");
        } else {
            //System.out.println("North door open");
            myNorthDoor.setEnabled(true);
            myNorthDoor.setBorderPainted(false);
        }
        add(myNorthDoor);
    }

    public void createWestDoor() {
        doorIcon = new ImageIcon("images/door.png");
        //scaling image down.
        Image image = doorIcon.getImage();
        Image newing = image.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        doorIcon = new ImageIcon(newing);
        myWestDoor = new JButton(doorIcon);
        myWestDoor.setBounds(489, 160, 35, 35);
        myWestDoor.setBackground(Color.RED);
        myWestDoor.setOpaque(true);

        if (!myMaze.display(Direction.WEST) || myMaze.getCurrentRoom().getDoor(Room.WEST_INDEX).getForeverLocked()) {
            myWestDoor.setBackground(Color.GRAY);
            myWestDoor.setOpaque(true);
            myWestDoor.setEnabled(false);
        } else {
        //    System.out.println("West door open");
            myWestDoor.setEnabled(true);
            myWestDoor.setBorderPainted(false);
        }
        add(myWestDoor);
    }

    public void createSouthDoor() {
        doorIcon = new ImageIcon("images/door.png");

        //scaling image down.
        Image image = doorIcon.getImage();
        Image newing = image.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        doorIcon = new ImageIcon(newing);


        mySouthDoor = new JButton(doorIcon);
        mySouthDoor.setBackground(Color.RED);
        mySouthDoor.setOpaque(true);

        mySouthDoor.setBounds(545, 219, 35, 35);
        if (!myMaze.display(Direction.SOUTH) || myMaze.getCurrentRoom().getDoor(Room.SOUTH_INDEX).getForeverLocked()) {  //&& myMaze.getDoor()) {
            mySouthDoor.setBackground(Color.GRAY);
            mySouthDoor.setOpaque(true);
            mySouthDoor.setEnabled(false);
        } else {
            mySouthDoor.setEnabled(true);
            mySouthDoor.setBorderPainted(false);
        }
        add(mySouthDoor);
    }

    public void createEastDoor() {
        doorIcon = new ImageIcon("images/door.png");

        //scaling image down.
        Image image = doorIcon.getImage();
        Image newing = image.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        doorIcon = new ImageIcon(newing);
        myEastDoor = new JButton(doorIcon);
        myEastDoor.setBackground(Color.RED);
        myEastDoor.setOpaque(true);

        myEastDoor.setBounds(601, 160, 35, 35);
        if (!myMaze.display(Direction.EAST) || myMaze.getCurrentRoom().getDoor(Room.EAST_INDEX).getForeverLocked()) {  //if there's a display of a door east. Or if the east door is forever locked
            myEastDoor.setBackground(Color.GRAY);
            myEastDoor.setOpaque(true);
            myEastDoor.setEnabled(false);
        } else {
            myEastDoor.setEnabled(true);
            myEastDoor.setBorderPainted(false);
        }
        add(myEastDoor);
    }

    public void addListeners() {
        int size = myMaze.getMaze().length - 1; //to make sure door is disabled if we're at edge
       // super.paint(g);
        //myMaze.display();
        Room [][] myMazeRoom = myMaze.getMaze();
        //correct = false;
        myNorthDoor.addActionListener(e -> {
            if (!myMaze.getCurrentRoom().getDoor(Room.NORTH_INDEX).getLock()) { //if the door isn't locked we could just move the player through it.
                myMaze.movePlayer(Direction.NORTH);
                removeAll();
                revalidate();
                repaint();
            } else {
                myEastDoor.setEnabled(false);
                myEastDoor.setBackground(Color.GRAY);
                myEastDoor.setOpaque(true);

                myWestDoor.setEnabled(false);
                myWestDoor.setBackground(Color.GRAY);
                myWestDoor.setOpaque(true);

                myNorthDoor.setEnabled(false);
                myNorthDoor.setBackground(Color.GRAY);
                myNorthDoor.setOpaque(true);

                mySouthDoor.setEnabled(false);
                mySouthDoor.setBackground(Color.GRAY);
                mySouthDoor.setOpaque(true);

                createQuestionLayout(Direction.NORTH);

                revalidate();
            }
            System.out.println(myMaze.getY());
        });

        myEastDoor.addActionListener(e -> {
           // myMaze.setX(myMaze.getX() + 1);
            if (!myMaze.getCurrentRoom().getDoor(Room.EAST_INDEX).getLock()) { //if the door isn't locked we could just move the player through it.
                myMaze.movePlayer(Direction.EAST);
                removeAll();
                revalidate();
                repaint();
            } else {
                myEastDoor.setEnabled(false);
                myEastDoor.setBackground(Color.GRAY);
                myEastDoor.setOpaque(true);

                myWestDoor.setEnabled(false);
                myWestDoor.setBackground(Color.GRAY);
                myWestDoor.setOpaque(true);

                myNorthDoor.setEnabled(false);
                myNorthDoor.setBackground(Color.GRAY);
                myNorthDoor.setOpaque(true);

                mySouthDoor.setEnabled(false);
                mySouthDoor.setBackground(Color.GRAY);
                mySouthDoor.setOpaque(true);
                createQuestionLayout(Direction.EAST);

                revalidate();
            }
//            System.out.println(myMaze.getCurrentRoom().getRoomNumber());
            System.out.println(myMaze.getX());
        });

        mySouthDoor.addActionListener(e -> {
            //myMaze.setY(myMaze.getY() + 1);
            if (!myMaze.getCurrentRoom().getDoor(Room.SOUTH_INDEX).getLock()) { //if the door isn't locked we could just move the player through it.
                myMaze.movePlayer(Direction.SOUTH);
                removeAll();
                revalidate();
                repaint();
            } else {
                myEastDoor.setEnabled(false);
                myEastDoor.setBackground(Color.GRAY);
                myEastDoor.setOpaque(true);

                myWestDoor.setEnabled(false);
                myWestDoor.setBackground(Color.GRAY);
                myWestDoor.setOpaque(true);

                myNorthDoor.setEnabled(false);
                myNorthDoor.setBackground(Color.GRAY);
                myNorthDoor.setOpaque(true);

                mySouthDoor.setEnabled(false);
                mySouthDoor.setBackground(Color.GRAY);
                mySouthDoor.setOpaque(true);
                createQuestionLayout(Direction.SOUTH);

                revalidate();
            }
//            System.out.println(myMaze.getCurrentRoom().getRoomNumber());
            System.out.println(myMaze.getY());
        });

        myWestDoor.addActionListener(e -> {
            if (!myMaze.getCurrentRoom().getDoor(Room.WEST_INDEX).getLock()) { //this is already set in the moveplayer method
                myMaze.movePlayer(Direction.WEST);
                removeAll();
                revalidate();
                repaint();
            } else {
                myEastDoor.setEnabled(false);
                myEastDoor.setBackground(Color.GRAY);
                myEastDoor.setOpaque(true);

                myWestDoor.setEnabled(false);
                myWestDoor.setBackground(Color.GRAY);
                myWestDoor.setOpaque(true);

                myNorthDoor.setEnabled(false);
                myNorthDoor.setBackground(Color.GRAY);
                myNorthDoor.setOpaque(true);

                mySouthDoor.setEnabled(false);
                mySouthDoor.setBackground(Color.GRAY);
                mySouthDoor.setOpaque(true);

                createQuestionLayout(Direction.WEST);
                revalidate();
            }
           // System.out.println(myMaze.getCurrentRoom().getRoomNumber());
            System.out.println(myMaze.getX());
        });


    }


    public void createQuestionLayout(final Direction theDir) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.BLACK);

        panel.setBounds(83, 413, 630, 329);

        int index;
        if (theDir == Direction.NORTH) {
            index = 0;
        } else if (theDir == Direction.EAST){
            index = 1;
        } else if (theDir == Direction.SOUTH) {
            index = 2;
        } else {
            index = 3;
        }

        Door door = myMaze.getCurrentRoom().getDoor(index);

        optionA = new JButton();
        optionB = new JButton();
        optionC = new JButton();
        optionD = new JButton();
        myJTextArea = new JTextArea();
        myJTextField = new JTextField();
        answer_labelA = new JLabel();
        answer_labelB = new JLabel();
        answer_labelC = new JLabel();
        answer_labelD = new JLabel();

//        myJTextField.setBounds(90,474,616,54);
        myJTextField.setBounds(0, 54, 630, 54);
        myJTextField.setBackground(new Color(25,25,25));
        myJTextField.setForeground(new Color(255,165,0));
        myJTextField.setFont(new Font("Comic Sans MS",Font.BOLD,20));
        myJTextField.setBorder(BorderFactory.createBevelBorder(1));
        myJTextField.setHorizontalAlignment(JTextField.CENTER);
        myJTextField.setEditable(false);
        myJTextField.setText(door.getQuestion());
        myJTextArea.setLineWrap(true);
        myJTextArea.setWrapStyleWord(true);

        //myJTextArea.setBounds(90,420,616,54);
        myJTextArea.setBounds(0, 0, 630, 54);
        myJTextArea.setLineWrap(true);
        myJTextArea.setWrapStyleWord(true);
        myJTextArea.setBackground(new Color(25,25,25));
        myJTextArea.setForeground(new Color(255,255,0));
        myJTextArea.setFont(new Font("Comic Sans MS",Font.BOLD,25));
        myJTextArea.setBorder(BorderFactory.createBevelBorder(1));
        myJTextArea.setEditable(false);
        myJTextArea.setText("Question " + myQuestionNumber);

        //optionA.setBounds(90,540,40,40);
        optionA.setBounds(0, 120, 40, 40);
        optionA.setFont(new Font("MV Boli",Font.BOLD,35));
        optionA.setFocusable(false);
      //  optionA.addActionListener((ActionListener) this);
        optionA.setText("A");

        // optionB.setBounds(90,591,40,40);
        optionB.setBounds(0, 171, 40, 40);
        optionB.setFont(new Font("MV Boli",Font.BOLD,35));
        optionB.setFocusable(false);
      //  optionB.addActionListener((ActionListener) this);
        optionB.setText("B");

      //  optionC.setBounds(90,642,40,40);
        optionC.setBounds(0, 222, 40, 40);
        optionC.setFont(new Font("MV Boli",Font.BOLD,35));
        optionC.setFocusable(false);
      //  optionC.addActionListener((ActionListener) this);
        optionC.setText("C");

       // optionD.setBounds(90,693,40,40);
        optionD.setBounds(0, 273, 40, 40);
        optionD.setFont(new Font("MV Boli",Font.BOLD,35));
        optionD.setFocusable(false);
     //   optionD.addActionListener((ActionListener) this);
        optionD.setText("D");

        //answer_labelA.setBounds(140,540,566,40);
        answer_labelA.setBounds(50, 120, 566, 40);
        answer_labelA.setBackground(new Color(50,50,50));
        answer_labelA.setForeground(new Color(230,230,250));
        answer_labelA.setFont(new Font("MV Boli",Font.PLAIN,20));
        answer_labelA.setText(door.getOptionA());

       // answer_labelB.setBounds(140,591,566,40);
        answer_labelB.setBounds(50, 171, 566, 40);
        answer_labelB.setBackground(new Color(50,50,50));
        answer_labelB.setForeground(new Color(230,230,250));
        answer_labelB.setFont(new Font("MV Boli",Font.PLAIN,20));
        answer_labelB.setText(door.getOptionB());

      //  answer_labelC.setBounds(140,642,566,40);
        answer_labelC.setBounds(50, 222, 566, 40);
        answer_labelC.setBackground(new Color(50,50,50));
        answer_labelC.setForeground(new Color(230,230,250));
        answer_labelC.setFont(new Font("MV Boli",Font.PLAIN,20));
        answer_labelC.setText(door.getOptionC());

      //  answer_labelD.setBounds(140,693,566,40);
        answer_labelD.setBounds(50, 273, 566, 40);
        answer_labelD.setBackground(new Color(50,50,50));
        answer_labelD.setForeground(new Color(230,230,250));
        answer_labelD.setFont(new Font("MV Boli",Font.PLAIN,20));
        answer_labelD.setText(door.getOptionD());

        panel.add(optionA);
        panel.add(optionB);
        panel.add(optionC);
        panel.add(optionD);
        panel.add(myJTextArea);
        panel.add(myJTextField);
        panel.add(answer_labelA);
        panel.add(answer_labelB);
        panel.add(answer_labelC);
        panel.add(answer_labelD);
        add(panel);
        panel.revalidate();
        panel.repaint();


        addListenersOptions(theDir);
        myQuestionNumber++;
    }

    public void addListenersOptions(Direction theDir) {
        optionA.setEnabled(true);
        optionB.setEnabled(true);
        optionC.setEnabled(true);
        optionD.setEnabled(true);
        int index;
        if (theDir == Direction.NORTH) {
            index = 0;
        } else if (theDir == Direction.EAST){
            index = 1;
        } else if (theDir == Direction.SOUTH) {
            index = 2;
        } else {
            index = 3;
        }

        Door theDoor = myMaze.getCurrentRoom().getDoor(index);
        optionA.addActionListener(e -> {
       //     System.out.println("HI");
            if (!theDoor.getOptionA().equals(theDoor.getAnswer())) {
                theDoor.setForeverLocked(true);
                lockingDoors(theDir);
            } else {
           //     System.out.println("Cool they got it right");
                theDoor.setForeverLocked(false);
                unlockingDoors(theDir);
                theDoor.unlock();
                myMaze.movePlayer(theDir);
            }
            removeAll();
            revalidate();
            repaint();
        });

        optionB.addActionListener(e -> {
        //    System.out.println("HI");
            if (!theDoor.getOptionB().equals(theDoor.getAnswer())) {
                theDoor.setForeverLocked(true);
                lockingDoors(theDir);
            } else {
            //    System.out.println("Cool they got it right");
                theDoor.setForeverLocked(false);
                unlockingDoors(theDir);
                theDoor.unlock();
                myMaze.movePlayer(theDir);
            }
            removeAll();
            revalidate();
            repaint();
        });

        optionC.addActionListener(e -> {
        //   System.out.println("HI");
            if (!theDoor.getOptionC().equals(theDoor.getAnswer())) {
                theDoor.setForeverLocked(true);
                lockingDoors(theDir);
            } else {
          //      System.out.println("Cool they got it right");
                theDoor.setForeverLocked(false);
                unlockingDoors(theDir);
                theDoor.unlock();
                myMaze.movePlayer(theDir);
            }
            removeAll();
            revalidate();
            repaint();
        });

        optionD.addActionListener(e -> {
       //     System.out.println("HI");
            if (!theDoor.getOptionD().equals(theDoor.getAnswer())) {
                theDoor.setForeverLocked(true);
                lockingDoors(theDir);
            } else {
         //       System.out.println("Cool they got it right");
                theDoor.setForeverLocked(false);
                unlockingDoors(theDir);
                theDoor.unlock();
                myMaze.movePlayer(theDir);
            }
            removeAll();
            revalidate();
            repaint();
        });
    }

    private void unlockingDoors(final Direction theDir) {
        if (theDir == Direction.NORTH) {
            System.out.println("UL WEST: " + myMaze.getX());
            myMaze.getRoom(myMaze.getX(), myMaze.getY() - 1).getDoor(Room.SOUTH_INDEX).unlock();
        } else if (theDir == Direction.SOUTH) {
            System.out.println("UL SOUTH: " + myMaze.getY());
            myMaze.getRoom(myMaze.getX(), myMaze.getY() + 1).getDoor(Room.NORTH_INDEX).unlock();
        } else if (theDir == Direction.WEST) {
            System.out.println("UL WEST: " + myMaze.getX());
            myMaze.getRoom(myMaze.getX() - 1, myMaze.getY()).getDoor(Room.EAST_INDEX).unlock();
        } else if (theDir == Direction.EAST) {
            System.out.println("UL EAST: " + myMaze.getX());
            myMaze.getRoom(myMaze.getX() + 1, myMaze.getY()).getDoor(Room.WEST_INDEX).unlock();
        }
    }

    private void lockingDoors(final Direction theDir) {
        if (theDir == Direction.NORTH) {
            System.out.println("NORTH: " + myMaze.getY());
            myMaze.getRoom(myMaze.getX(), myMaze.getY() - 1).getDoor(Room.SOUTH_INDEX).setForeverLocked(true);
        } else if (theDir == Direction.SOUTH) {
            System.out.println("SOUTH: " + myMaze.getY());
            myMaze.getRoom(myMaze.getX(), myMaze.getY() + 1).getDoor(Room.NORTH_INDEX).setForeverLocked(true);
        } else if (theDir == Direction.WEST) {
            System.out.println("WEST: " + myMaze.getX());
            myMaze.getRoom(myMaze.getX() - 1, myMaze.getY()).getDoor(Room.EAST_INDEX).setForeverLocked(true);
        } else if (theDir == Direction.EAST) {
            System.out.println("EAST: " + myMaze.getX());
            myMaze.getRoom(myMaze.getX() + 1, myMaze.getY()).getDoor(Room.WEST_INDEX).setForeverLocked(true);
        }
    }

    public void displayAnswer(final Door theDoor) {
        optionA.setEnabled(false);
        optionB.setEnabled(false);
        optionC.setEnabled(false);
        optionD.setEnabled(false);

        if(theDoor.getAnswer() != theDoor.getOptionA())
            answer_labelA.setForeground(new Color(255,0,0));
        if(theDoor.getAnswer() != theDoor.getOptionB())
            answer_labelB.setForeground(new Color(255,0,0));
        if(theDoor.getAnswer() != theDoor.getOptionC())
            answer_labelC.setForeground(new Color(255,0,0));
        if(theDoor.getAnswer() != theDoor.getOptionD())
            answer_labelD.setForeground(new Color(255,0,0));
    }

    public void setPlayerIcon(final ImageIcon theFileName) {
         playerIcon = theFileName;
    }


}
