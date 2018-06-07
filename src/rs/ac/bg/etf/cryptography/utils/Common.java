package rs.ac.bg.etf.cryptography.utils;

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

}
