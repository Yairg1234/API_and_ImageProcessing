package main;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MazeSolver {
    boolean[][] isWall;
    List<Point> solutionPath;
    int cols;
    int rows;

    public MazeSolver(boolean[][] isWall, int cols, int rows) {
        this.isWall = isWall;
        this.cols = cols;
        this.rows = rows;

        this.solutionPath = new ArrayList<>();
    }

    public boolean hasSolution(){
        if (!this.solutionPath.isEmpty())
            this.solutionPath.clear();

        if(isWall[0][0] || isWall[this.cols -1][this.rows - 1])
            return false;

        return solveMaze(this.cols - 1, this.rows - 1, 0, 0, new boolean[this.cols][this.rows]);
    }

    private boolean solveMaze(int col, int row, int destCol, int destRow, boolean[][] visited){
        if (col < 0 || row < 0 || col >= this.cols || row >= this.rows || isWall[col][row] || visited[col][row])
            return false;

        if(col == destCol && row == destRow) {
            this.solutionPath.add(new Point(col, row));
            return true;
        }

        visited[col][row] = true;


        if (solveMaze(col + 1, row, destCol, destRow, visited) ||
            solveMaze(col - 1, row, destCol, destRow, visited) ||
            solveMaze(col, row + 1, destCol, destRow, visited) ||
            solveMaze(col, row - 1, destCol, destRow, visited)){

            this.solutionPath.add(new Point(col, row));
            return true;
        }

        return false;
    }

    public List<Point> getSolution(){
        return this.solutionPath;
    }


}
