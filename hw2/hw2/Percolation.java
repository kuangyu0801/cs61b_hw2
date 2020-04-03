package hw2;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // create N-by-N grid, with all sites initially blocked
    private int size;
    private int openSite;
    private boolean[][] openState; // open: true, close: false
    private boolean[][] fullState; // open: true, close: false
    private boolean isPercolate;
    private WeightedQuickUnionUF openSet; // set to track union
    private int[] openLastRow; // to keep track the column of open site in last row
    private int sizeOpenLastRow; // keep track of the number
    private boolean[] isRowOpen;
    private int numRowOpen;

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        size = N;
        openSite = 0;
        openState = new boolean[N][N];
        fullState = new boolean[N][N];
        isRowOpen = new boolean[N];
        openLastRow = new int[N];
        sizeOpenLastRow = 0;
        numRowOpen = 0;
        isPercolate = false;
        /** let 0 be the root, with  */
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                openState[i][j] = false;
                fullState[i][j] = false;
            }
            isRowOpen[i] = false;
        }
        openSet =  new WeightedQuickUnionUF(N * N); // every site is a one-element set

    }

    private void validateRowCol(int row, int col) {
        if (row > (size - 1) || col > (size  - 1)) {
            throw new java.lang.IndexOutOfBoundsException();
        } else if (row < 0 || col < 0) {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    private int toIndex(int row, int col) {
        return (row * size) + col;
    }

    private int toRow(int index) {
        return index / this.size;
    }

    private int toCol(int index) {
        return index % this.size;
    }

    private void updateOpenLastRow(int row, int col) {
        if (row == size - 1) {
            openLastRow[sizeOpenLastRow] = col;
            sizeOpenLastRow += 1;
        }
    }

    private void updateIsRowOpen(int row, int col) {
        if (!isRowOpen[row]) {
            isRowOpen[row] = true;
            numRowOpen += 1;
        }
    }

    /** check the site and its set root is full */
    private boolean isSetFull(int row, int col) {
        int curIndex = toIndex(row, col);
        int rootIndex = openSet.find(curIndex);
        boolean isRootFull = isFull(toRow(rootIndex), toCol(rootIndex));
        return isRootFull || isFull(row, col);
    }

    /** set the site and its set root is full */
    private void updateFullSiteAndSet(int row, int col) {
        int curIndex = toIndex(row, col);
        int rootIndex = openSet.find(curIndex);
        int rootRow = toRow(rootIndex);
        int rootCol = toCol(rootIndex);
        fullState[row][col] = true;
        fullState[rootRow][rootCol] = true;
    }

    /** check and update current and neighbor status*/
    private void updateOpenAndFull(int curRow, int curCol, int nbrRow, int nbrCol) {
        boolean isNeighborUnionFull = isFull(nbrRow, nbrCol);
        boolean isCurrentFull = isFull(curRow, curCol);
        int neighborIndex = toIndex(nbrRow, nbrCol);
        int currentIndex = toIndex(curRow, curCol);

        if (isCurrentFull && !isNeighborUnionFull) {
            updateFullSiteAndSet(nbrRow, nbrCol);
        } else if (!isCurrentFull && isNeighborUnionFull) {
            updateFullSiteAndSet(curRow, curCol);
        }
        openSet.union(currentIndex, neighborIndex);
    }

    /**
     * step:
     * check whether neighbor is open by iterate through each neighbor, and then check fullness
     * 1. update open union check one neighbor: if open union with this neighbor
     * 2. update fullness:
     * if neighbor full or neighbor root and current empty:  update current fullness
     * if neighbor empty and current full: update neighbor and root of neighbor
     * 2-2. check mu
     * */
    private void updateOpenSet(int row, int col) {
        boolean isUpOpen = (row - 1 < 0) ? false : this.isOpen(row - 1, col);
        boolean isDownOpen = (row + 1 >= this.size) ? false : this.isOpen(row + 1, col);
        boolean isLeftOpen = (col - 1 < 0) ? false : this.isOpen(row, col - 1);
        boolean isRightOpen = (col + 1 >= this.size) ? false : this.isOpen(row, col + 1);

        if (isUpOpen) {
            updateOpenAndFull(row, col, row - 1, col);
        }

        if (isDownOpen) {
            updateOpenAndFull(row, col, row + 1, col);
        }

        if (isLeftOpen) {
            updateOpenAndFull(row, col, row, col - 1);
        }

        if (isRightOpen) {
            updateOpenAndFull(row, col, row, col + 1);
        }
    }

    private boolean isNeighborOrSetFull(int row, int col) {
        boolean isUpFull = (row - 1 < 0) ? false : fullState[row - 1][col];
        boolean isDownFull = (row + 1 >= this.size) ? false : fullState[row + 1][col];
        boolean isLeftFull = (col - 1 < 0) ? false : fullState[row][col - 1];
        boolean isRightFull = (col + 1 >= this.size) ? false : fullState[row][col + 1];
        return isLeftFull || isRightFull || isUpFull || isDownFull;
    }

    /** open the site (row, col) if it is not open already*/
    public void open(int row, int col) {
        validateRowCol(row, col);

        if (!openState[row][col]) {
            // setting site to open
            openState[row][col] = true;

            // update full status
            if ((row == 0) || isNeighborOrSetFull(row, col)) {
                fullState[row][col] = true;
            }

            // record open last row
            updateOpenLastRow(row, col);
            // record row open
            updateIsRowOpen(row, col);
            // update openSet by checking neighbor
            updateOpenSet(row, col);
            //updatePercolate(row, col);
            openSite += 1;
        }
    }

    /** is the site (row, col) open? */
    public boolean isOpen(int row, int col) {
        validateRowCol(row, col);
        return openState[row][col];
    }

    private void isFullHelper(int row, int col) {
        int rootIndex = openSet.find(toIndex(row, col));
        int rootRow = toRow(rootIndex);
        int rootCol = toCol(rootIndex);
        // update itself with root state
        fullState[row][col] = fullState[row][col] || fullState[rootRow][rootCol];

        if (row == this.size - 1) {
            isPercolate = isPercolate || fullState[row][col];
        }
    }
    /** is the site (row, col) full? */
    public boolean isFull(int row, int col) {
        validateRowCol(row, col);
        isFullHelper(row, col);
        return fullState[row][col];
    }

    /** number of open sites */
    public int numberOfOpenSites() {
        return openSite;
    }

    private void updatePercolation() {
        if (numRowOpen == size) {
            for (int i = 0; i < sizeOpenLastRow; i += 1) {
                isPercolate = isPercolate || isFull(this.size - 1, openLastRow[i]);
            }
        }
    }

    /** does the system percolate?
     * if any bottom is connected with any top then it will percolate*/
    public boolean percolates() {
        updatePercolation();
        return isPercolate;
    }

    /** use for unit testing (not required) */
    public static void main(String[] args) {

    }


}
