package rs.ac.bg.etf.cryptography.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import Jama.Matrix;

public class Common {
    public static String fill(String text, String fill, int size) {
        String filled = text;
        int fillNeeded = text.length() % size == 0 ? 0 : size - text.length() % size;

        while (fillNeeded-- != 0) {
            filled += fill;
        }

        return filled;
    }

    public static Matrix getMatrixFromJson(JsonObject json, int keySize) {
        Matrix matrix = new Matrix(keySize, keySize);
        for (int i = 0; i < keySize; i++) {
            String[] row = json.get("" + i).getAsString().split(" ");
            for (int j = 0; j < row.length; j++) {
                matrix.set(i, j, Integer.parseInt(row[j]));
            }
        }

        return matrix;
    }

    public static String getMatrixMultiplication(Matrix a, Matrix b, int row, int column) {
        StringBuilder product = new StringBuilder();

        for (int i = 0; i < b.getColumnDimension(); i++) {
            product.append("" + (int) a.get(row, i)).append(" * ").append("" + (int) b.get(i, column));
            if (i < b.getColumnDimension() - 1) {
                product.append(" + ");
            }
        }

        return product.toString();
    }

    public static Matrix moduloMatrix(Matrix matrix, int row, int col) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int val = (int) matrix.get(i, j);
                matrix.set(i, j, val % 26);
            }
        }

        return matrix;
    }

    public static List<Integer> mapLetters(String message) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < message.length(); i++) {
            numbers.add(message.charAt(i) - 'A');
        }

        return numbers;
    }
}
