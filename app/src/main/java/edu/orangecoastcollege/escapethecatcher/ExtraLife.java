package edu.orangecoastcollege.escapethecatcher;

/**
 * Created by gabye on 11/4/2016.
 */

public class ExtraLife {
    private int mRow;
    private int mCol;

    public ExtraLife(int row, int col)
    {
        mRow = row;
        mCol = col;
    }

    public int getRow() {
        return mRow;
    }

    public void setRow(int row) {
        mRow = row;
    }

    public int getCol() {
        return mCol;
    }

    public void setCol(int col) {
        mCol = col;
    }
}
