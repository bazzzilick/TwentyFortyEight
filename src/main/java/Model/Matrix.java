package Model;

public class Matrix {

    private int size;
    private int[][] matrix;

    public Matrix(int size) {
        this.size = size;
        matrix = new int[size][size];
    }

    int get(int x, int y){
        if(inRange(x, y))
            return matrix[x][y];
        return -1;
    }

    void set(int x, int y, int val) {
        if(inRange(x, y))
            matrix[x][y] = val;
    }

    private boolean inRange(int x, int y) {
        return  x >= 0 && x < size &&
                y >= 0 && y < size;
    }
}
