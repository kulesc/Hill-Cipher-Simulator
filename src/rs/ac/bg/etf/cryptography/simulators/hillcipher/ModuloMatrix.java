package rs.ac.bg.etf.cryptography.simulators.hillcipher;

import java.math.BigInteger;

import Jama.Matrix;

public class ModuloMatrix {

    private int rows;

    private int cols;

    private BigInteger[][] data;

    private static final BigInteger modulo = new BigInteger("26");

    public ModuloMatrix(Matrix matrix) {
        this.rows = matrix.getRowDimension();
        this.cols = matrix.getColumnDimension();
        this.data = new BigInteger[this.rows][this.cols];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.data[i][j] = new BigInteger("" + (int) matrix.get(i, j));
            }
        }
    }

    public ModuloMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        data = new BigInteger[rows][cols];
    }

    public int getRowCount() {
        return rows;
    }

    public int getColumnCount() {
        return cols;
    }

    public BigInteger get(int i, int j) {
        return data[i][j];
    }

    public void set(int i, int j, BigInteger value) {
        data[i][j] = value;
    }

    public Matrix getMatrix() {
        Matrix matrix = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.set(i, j, data[i][j].doubleValue());
            }
        }

        return matrix;
    }

    public static ModuloMatrix transpose(ModuloMatrix matrix) {
        ModuloMatrix transposed = new ModuloMatrix(matrix.getColumnCount(), matrix.getRowCount());
        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                transposed.set(j, i, matrix.get(i, j));
            }
        }
        return transposed;
    }

    public static BigInteger determinant(ModuloMatrix matrix) {

        if (matrix.getColumnCount() == 1) {
            return matrix.get(0, 0);
        }

        if (matrix.getColumnCount() == 2) {
            return (matrix.get(0, 0).multiply(matrix.get(1, 1)))
                    .subtract((matrix.get(0, 1).multiply(matrix.get(1, 0))));
        }

        BigInteger sum = new BigInteger("0");

        for (int i = 0; i < matrix.getColumnCount(); i++) {
            sum = sum
                    .add(revertSign(i).multiply(matrix.get(0, i).multiply(determinant(createSubMatrix(matrix, 0, i)))));
        }

        return sum;
    }

    private static BigInteger revertSign(int i) {
        return new BigInteger(i % 2 == 0 ? "1" : "-1");
    }

    public static ModuloMatrix createSubMatrix(ModuloMatrix matrix, int row, int col) {
        ModuloMatrix mat = new ModuloMatrix(matrix.getRowCount() - 1, matrix.getColumnCount() - 1);
        int r = -1;
        for (int i = 0; i < matrix.getRowCount(); i++) {
            if (i == row) {
                continue;
            }
            r++;
            int c = -1;
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                if (j == col) {
                    continue;
                }
                mat.set(r, ++c, matrix.get(i, j));
            }
        }
        return mat;
    }

    public static ModuloMatrix cofactor(ModuloMatrix matrix) {
        ModuloMatrix mat = new ModuloMatrix(matrix.getRowCount(), matrix.getColumnCount());
        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                mat.set(i, j,
                        (revertSign(i).multiply(revertSign(j)).multiply(determinant(createSubMatrix(matrix, i, j))))
                                .mod(modulo));
            }
        }

        return mat;
    }

    public static ModuloMatrix inverse(ModuloMatrix matrix) {
        return (transpose(cofactor(matrix)).dc(determinant(matrix)));
    }

    private ModuloMatrix dc(BigInteger d) {
        BigInteger inv = d.modInverse(modulo);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = (data[i][j].multiply(inv)).mod(modulo);
            }
        }
        return this;
    }
}
