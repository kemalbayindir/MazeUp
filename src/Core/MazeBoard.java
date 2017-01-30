package Core;

import Helpers.Constants;
import Helpers.Sides;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Kemal BAYINDIR on 12/4/2014.
 */
public class MazeBoard extends JPanel {

    MazeCell[][] cells;
    MazeCell exitCell, startCell, currentCell;
    boolean done = false;
    boolean mazeIsReady = false;
    boolean mazeSolved = false;
    int firstRowId, firstColId;

    public MazeBoard(int width, int height) {
        this.setBackground(Constants.BACKGROUND_COLOR);
        this.setSize(new Dimension(width, height));
        this.setVisible(true);
        mazeIsReady = false;
        mazeSolved = false;
        done = false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        for (int row = 0; row < Constants.ROW_COUNT; row++) {

            for (int col = 0; col < Constants.ROW_COUNT; col++) {
                cells[col][row].draw(g2d);
            }

        }
    }

    public void assignCells() {

        Dimension cellDim = new Dimension(Constants.SIZE_OF_SQUARE, Constants.SIZE_OF_SQUARE);
        cells = new MazeCell[Constants.ROW_COUNT][Constants.ROW_COUNT];

        for (int row = 0; row < Constants.ROW_COUNT; row++) {

            for (int col = 0; col < Constants.ROW_COUNT; col++) {

                MazeCell cell = new MazeCell(cellDim, new Point(col * Constants.SIZE_OF_SQUARE, row * Constants.SIZE_OF_SQUARE));
                cell.setRowCol(col, row);
                cells[col][row] = cell;

            }

        }

    }

    public void createStartPoint() {
        int row = new Random().nextInt(Constants.ROW_COUNT - 1) + 1;
        int col = new Random().nextInt(Constants.ROW_COUNT - 1) + 1;

        firstRowId = row;
        firstColId = col;

        cells[col][row].setAsStartPoint();
        startCell = cells[col][row];
        currentCell = startCell;

        startCell.reCalculatePosition();
        repaint();
}

    public void relocateStartCell() {
        cells[firstColId][firstRowId].setAsStartPoint();
        startCell.setRowCol(firstColId, firstRowId);
        startCell = cells[firstColId][firstRowId];
        currentCell = startCell;
        startCell.reCalculatePosition();
        repaint();
    }

    public void createExitCell() {
        // 0-up 1-right 2-down 3-left
        int side = new Random().nextInt(4);
        int x = 0, y = 0;
        if (side == 0 || side == 2) {
            x =  new Random().nextInt(Constants.ROW_COUNT);
            y = (side == 0 ? 0 : Constants.ROW_COUNT - 1);
        }
        if (side == 1 || side == 3) {
            x =  (side == 1 ? 0 : Constants.ROW_COUNT - 1);
            y = new Random().nextInt(Constants.ROW_COUNT);
        }

        if (x - 1 >= 0 && x - 1 < Constants.ROW_COUNT)
            cells[x - 1][y].setRightOpen(true);
        if (y - 1 >= 0 && y - 1 < Constants.ROW_COUNT)
            cells[x][y - 1].setBottomOpen(true);
        if (x + 1 > 0 && x + 1 < Constants.ROW_COUNT)
            cells[x + 1][y].setLeftOpen(true);
        if (y + 1 > 0 && y + 1 < Constants.ROW_COUNT)
            cells[x][y + 1].setTopOpen(true);

        cells[x][y].setAsExitPoint();
        exitCell = cells[x][y];
    }

    public void solveMaze() {
        while (!isMazeSolved()) {

            Random rnd = new Random();
            int nextSide = rnd.nextInt(4);
            Sides side = Sides.values()[nextSide];
            currentCell = startCell;

            // 0-up
            if (side == Sides.up)
                DFSSolve( startCell.getColId(), startCell.getRowId() - 1, side );

                // 1-right
            else if (side == Sides.right)
                DFSSolve( startCell.getColId() + 1, startCell.getRowId(), side );

                // 2-down
            else if (side == Sides.down)
                DFSSolve( startCell.getColId(), startCell.getRowId() + 1, side );

                // 3-left
            else if (side == Sides.left)
                DFSSolve(startCell.getColId() -1, startCell.getRowId(), side);

        }
    }

    public void drawMaze(boolean manuelSolve) {
        Random rnd = new Random();
        int nextSide = rnd.nextInt(4);
        Sides side = Sides.values()[nextSide];
        currentCell = startCell;

        // 0-up
        if (side == Sides.up)
            if (manuelSolve)
                DFS( startCell.getColId(), startCell.getRowId() - 1, side );
            else
                DFSSolve( startCell.getColId(), startCell.getRowId() - 1, side );

        // 1-right
        else if (side == Sides.right)
            if (manuelSolve)
                DFS( startCell.getColId() + 1, startCell.getRowId(), side );
            else
                DFSSolve( startCell.getColId() + 1, startCell.getRowId(), side );

        // 2-down
        else if (side == Sides.down)
            if (manuelSolve)
                DFS( startCell.getColId(), startCell.getRowId() + 1, side );
            else
                DFSSolve( startCell.getColId(), startCell.getRowId() + 1, side );

        // 3-left
        else if (side == Sides.left)
            if (manuelSolve)
                DFS( startCell.getColId() - 1, startCell.getRowId(), side );
            else
                DFSSolve(startCell.getColId() -1, startCell.getRowId(), side);

        // maze is ready to user move(s)
        mazeIsReady = true;

        // exit cell can move, there is no wall for it
        doReachable(exitCell, true);
    }

    public void DFS(int colId, int rowId, Sides side) {
        // IF both row and col are valid AND this cell is not visited, THEN
        if (!done && rowId >= 0 && rowId < Constants.ROW_COUNT && colId >= 0 && colId < Constants.ROW_COUNT) {
            if (!cells[colId][rowId].isVisited() && cells[colId][rowId].isAccessible()) {
                //Convert the goingTo variable to comingFrom
                //MazeCell goingTo = cells[rowId][colId];
                MazeCell comingFrom = currentCell;

                //cells[rowId][colId].setAsCurrent(true);
                //this.repaint();
                //cells[rowId][colId].setAsCurrent(false);

                // Mark this cell (row, col) as visited
                cells[colId][rowId].setAsVisited();

                // Remove the wall in the direction of comingFrom
                List<Sides> neighbourSides = new ArrayList<Sides>();

                if (side == Sides.up) {
                    cells[comingFrom.getColId()][comingFrom.getRowId()].setTopOpen(true);
                    cells[colId][rowId].setBottomOpen(true);
                } else neighbourSides.add(Sides.up);

                if (side == Sides.right) {
                    cells[comingFrom.getColId()][comingFrom.getRowId()].setRightOpen(true);
                    cells[colId][rowId].setLeftOpen(true);
                } else neighbourSides.add(Sides.right);

                if (side == Sides.left) {
                    cells[comingFrom.getColId()][comingFrom.getRowId()].setLeftOpen(true);
                    cells[colId][rowId].setRightOpen(true);
                } else neighbourSides.add(Sides.left);

                if (side == Sides.down) {
                    cells[comingFrom.getColId()][comingFrom.getRowId()].setBottomOpen(true);
                    cells[colId][rowId].setTopOpen(true);
                } else neighbourSides.add(Sides.down);

                currentCell = cells[colId][rowId];
                System.out.printf("\n rowId : %d colId : %d side : %s", rowId, colId, side.toString());
                repaint();

                // Pick the order of visiting 3 other directions randomly and save them in an array
                shuffleArray(neighbourSides);
                sleepWell(Constants.SLEEP);

                MazeCell tempCurr = currentCell;
                //FOR EACH direction in the array
                for (Iterator<Sides> nside = neighbourSides.iterator(); nside.hasNext(); ) {
                    Sides s = nside.next();
                    currentCell = tempCurr;
                    if (s == Sides.up)
                        DFS(colId, rowId - 1, Sides.up);

                    else if (s == Sides.right)
                        DFS(colId + 1, rowId, Sides.right);

                    else if (s == Sides.down)
                        DFS(colId, rowId + 1, Sides.down);

                    else if (s == Sides.left)
                        DFS(colId - 1, rowId, Sides.left);

                }

            } //else if (cells[rowId][colId].isStartOrExit()) this.done = true;
            //else if (!cells[rowId][colId].isStartOrExit()) cells[rowId][colId].setAsBackTracking();
        }
    }

    public void DFSSolve(int colId, int rowId, Sides side) {

        if (!done) {

            if (rowId >= 0 && rowId < Constants.ROW_COUNT && colId >= 0 && colId < Constants.ROW_COUNT && cells[colId][rowId].isAccessible()) {
                done = (exitCell.getColId() == currentCell.getColId()) && (exitCell.getRowId() == currentCell.getRowId());
                if (done)
                    JOptionPane.showMessageDialog(null, "Çıkış bulundu.", "Çözüm", JOptionPane.INFORMATION_MESSAGE);

                if (!cells[colId][rowId].isSearched()) {
                    // Remove the wall in the direction of comingFrom
                    List<Sides> neighbourSides = new ArrayList<Sides>();

                    boolean setCurrent = false;
                    if (side == Sides.up) {
                        setCurrent = (cells[colId][rowId].isBottomOpen());
                    } else neighbourSides.add(Sides.up);

                    if (side == Sides.right) {
                        setCurrent = (cells[colId][rowId].isLeftOpen());
                    } else neighbourSides.add(Sides.right);

                    if (side == Sides.left) {
                        setCurrent = (cells[colId][rowId].isRightOpen());
                    } else neighbourSides.add(Sides.left);

                    if (side == Sides.down) {
                        setCurrent = (cells[colId][rowId].isTopOpen());
                    } else neighbourSides.add(Sides.down);

                    System.out.printf("\n searching exit on rowId : %d colId : %d side : %s", rowId, colId, side.toString());

                    if (setCurrent) {




                        // Mark this cell (row, col) as visited
                        cells[colId][rowId].setAsSearched(true);
                        cells[colId][rowId].reCalculatePosition();
                        currentCell = cells[colId][rowId];
                        this.repaint();
                        sleepWell(Constants.SOLVE_SLEEP);
                        shuffleArray(neighbourSides);

                        MazeCell tempCurr = currentCell;
                        //FOR EACH direction in the array
                        for (Iterator<Sides> nside = neighbourSides.iterator(); nside.hasNext(); ) {
                            Sides s = nside.next();
                            currentCell = tempCurr;
                            if (s == Sides.up)
                                DFSSolve(colId, rowId - 1, Sides.up);

                            else if (s == Sides.right)
                                DFSSolve(colId + 1, rowId, Sides.right);

                            else if (s == Sides.down)
                                DFSSolve(colId, rowId + 1, Sides.down);

                            else if (s == Sides.left)
                                DFSSolve(colId - 1, rowId, Sides.left);

                        }
                    }
                } else {
                    cells[colId][rowId].setAsBackTrack();
                }
            }
        }
    }

    public void shuffleArray(List<Sides> sides) {
        Random rnd = new Random();
        for (int i = sides.size() - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Sides a = sides.get(index);
            sides.set(index, sides.get(i));
            sides.set(i, a);
        }
    }

    public void sleepWell(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentCellAsInaccessible() {
        cells[startCell.getColId()][startCell.getRowId()].setAsInaccessible();
    }

    public void movePlayer(Sides side, boolean handicapMode) {
        int row = startCell.getRowId();
        int col = startCell.getColId();

        if (side == Sides.up && row - 1 >= 0) {
            if (cells[col][row].isTopOpen() || handicapMode)
                startCell.setRowCol(col, row - 1);
        } else if (side == Sides.down && row + 1 < Constants.ROW_COUNT) {
            if (cells[col][row].isBottomOpen() || handicapMode)
                startCell.setRowCol(col, row + 1);
        } else if (side == Sides.right && col + 1 < Constants.ROW_COUNT) {
            if (cells[col][row].isRightOpen() || handicapMode)
                startCell.setRowCol(col + 1, row);
        } else if (side == Sides.left && col - 1 >= 0) {
            if (cells[col][row].isLeftOpen() || handicapMode)
                startCell.setRowCol(col - 1, row);
        }
        startCell.reCalculatePosition();

        if (!handicapMode) {
            if ((startCell.getRowId() != row) || (startCell.getColId() != col)) {
                if (cells[startCell.getColId()][startCell.getRowId()].isOldPath())
                    cells[startCell.getColId()][startCell.getRowId()].setAsOldPath(false);
                else
                    cells[col][row].setAsOldPath(!cells[col][row].isOldPath());
            }

            if (startCell.getColId() == exitCell.getColId() && startCell.getRowId() == exitCell.getRowId()) {
                mazeSolved = true;
            }
        }

        repaint();
    }

    public void doReachable(MazeCell cell, boolean state) {
        int x = cell.getColId();
        int y = cell.getRowId();

        cells[x][y].setTopOpen(state);
        cells[x][y].setBottomOpen(state);
        cells[x][y].setLeftOpen(state);
        cells[x][y].setRightOpen(state);

        if (x + 1 < Constants.ROW_COUNT)
            cells[x + 1][y].setTopOpen(state);
        if (x - 1 >= 0)
            cells[x - 1][y].setBottomOpen(state);
        if (y + 1 < Constants.ROW_COUNT)
            cells[x][y + 1].setLeftOpen(state);
        if (y - 1 >= 0)
            cells[x][y - 1].setRightOpen(state);
    }

    public boolean isMazeSolved() { return mazeSolved; }

    public boolean isMazeIsReady() { return mazeIsReady; }
}
