package Core;

import Helpers.Constants;

import java.awt.*;

/**
 * Created by Kemal BAYINDIR on 12/5/2014.
 */
public class MazeCell {

    private boolean isBackTrack = false;
    private boolean isSearched = false;
    private boolean isOldPath = false;
    private boolean isVisited = false;
    private boolean isExitPoint = false;
    private boolean isAccessible = true;
    private boolean isLeftOpen = false;
    private boolean isRightOpen = false;
    private boolean isTopOpen = false;
    private boolean isBottomOpen = false;
    private boolean isStartPoint = false;
    private Dimension size;
    private Point position;
    private int rowId, colId;

    public MazeCell(Dimension size, Point position) {
        this.size = size;
        this.position = position;
    }

    public void draw(Graphics2D g) {

        if (isOldPath) {
            g.setColor(Constants.PATH_COLOR);
            g.fillOval(position.x + 5, position.y + 5, size.width - 10, size.height - 10);
            if (isStartPoint) {
                g.setColor(Constants.BACKGROUND_COLOR);
                g.fillOval(position.x + 5, position.y + 5, size.width - 10, size.height - 10);
            }
        }


         //else {
            //g.setColor(Color.red);
            //g.drawRect(position.x, position.y, size.width, size.height);
        //}


        if (isSearched) {
            g.setColor(Constants.SEARCHED_COLOR);
            g.fillRect(position.x + 5, position.y + 5, size.width - 10, size.height - 10);
        }

        if (isBackTrack) {
            g.setColor(Constants.BACK_TRACK_COLOR);
            g.fillRect(position.x + 5, position.y + 5, size.width - 10, size.height - 10);
        }


        if (isOldPath) {
            g.setColor(Constants.PATH_COLOR);
            g.fillOval(position.x + 5, position.y + 5, size.width - 10, size.height - 10);
        }

        if (isAccessible && !isStartOrExit()) {
            g.setStroke(new BasicStroke(Constants.LINE_WIDTH));
            g.setColor(Constants.LINE_COLOR);

            if (!isTopOpen)
                g.drawLine(position.x, position.y, position.x + size.width , position.y);

            if (!isBottomOpen)
                g.drawLine(position.x, position.y + size.height, position.x + size.width, position.y + size.height);

            if (!isLeftOpen)
                g.drawLine(position.x, position.y, position.x, position.y + size.height);

            if (!isRightOpen)
                g.drawLine(position.x + size.width, position.y, position.x + size.width, position.y + size.height);

        } else if (!isAccessible) {
            g.setColor(Constants.BLOCK_COLOR);
            g.fillRect(position.x + 1, position.y + 1, size.width, size.height);
            //g.drawLine(position.x, position.y, position.x + size.width, position.y + size.height);
            //g.drawLine(position.x + size.width, position.y, position.x, position.y + size.height);
        }


        if (isExitPoint) {
            g.setColor(Constants.EXIT_POINT_COLOR);
            g.fillRect(position.x + 1, position.y + 1, size.width, size.height);
        }

        if (isStartPoint) {
            if (isOldPath)
                isOldPath = false;
            g.setColor(Constants.START_POINT_COLOR);
            g.fillOval(position.x + 10, position.y + 10, size.width - 20, size.height - 20);
        }
    }

    public void reCalculatePosition() {
        // row means y column
        // col means x column
        this.position = new Point(this.getColId() * Constants.SIZE_OF_SQUARE, this.getRowId() * Constants.SIZE_OF_SQUARE);
    }

    public void setAsBackTrack() { this.isBackTrack = true; }

    public boolean isSearched() { return isSearched; }

    public boolean isAccessible() { return isAccessible; }

    public void setAsInaccessible() {
        this.isAccessible = !isAccessible;
    }

    public boolean isStartOrExit() {
        return this.isStartPoint || this.isExitPoint;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setAsStartPoint() {
        this.isStartPoint = true;
        setAsVisited();
    }

    public void setAsExitPoint() {
        this.isExitPoint = true;
        this.setTopOpen(true);
        this.setBottomOpen(true);
        this.setLeftOpen(true);
        this.setRightOpen(true);
        setAsVisited();
    }

    public void setAsVisited() {
        this.isVisited = true;
    }

    public void setRowCol(int colId, int rowId) {
        this.rowId = rowId;
        this.colId = colId;
    }

    public int getRowId() { return rowId; }

    public int getColId() { return colId; }

    public void setLeftOpen(boolean isLeftOpen) {
        this.isLeftOpen = isLeftOpen;
    }

    public void setRightOpen(boolean isRightOpen) {
        this.isRightOpen = isRightOpen;
    }

    public void setTopOpen(boolean isTopOpen) {
        this.isTopOpen = isTopOpen;
    }

    public void setBottomOpen(boolean isBottomOpen) {
        this.isBottomOpen = isBottomOpen;
    }

    public boolean isTopOpen() {return isTopOpen;}

    public boolean isBottomOpen() {return isBottomOpen;}

    public boolean isLeftOpen() {return isLeftOpen;}

    public boolean isRightOpen() {return isRightOpen;}

    public boolean isOldPath() { return isOldPath; }

    public void setAsOldPath(boolean state) {
        this.isOldPath = state;
    }

    public void setAsSearched(boolean state) {
        isSearched = state;
    }

    public boolean isExitPoint() { return isExitPoint; }
}
