package rs.ac.bg.etf.cryptography.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Jama.Matrix;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import rs.ac.bg.etf.cryptography.math.ModuloMatrix;

public class UI {

    public static void switchScene(Stage window, Scene scene) {
        window.setScene(scene);
        window.setResizable(false);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        window.setX((screenBounds.getWidth() - window.getWidth()) / 2);
        window.setY((screenBounds.getHeight() - window.getHeight()) / 2);
        window.show();
    }

    public static Matrix getKeyMatrix(int size, List<TextField> key) {
        Matrix keyMatrix = new Matrix(size, size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                keyMatrix.set(i, j, Integer.parseInt(key.get(i * size + j).getText()));
            }
        }

        return keyMatrix;
    }

    public static TableView<AlphabetMapping> generateLetterMappingTable() {
        TableView<AlphabetMapping> table = new TableView<>();

        table.getColumns().addAll(IntStream.rangeClosed('A', 'Z').mapToObj(character -> {
            String columnName = "" + (char) character;
            TableColumn<AlphabetMapping, String> column = new TableColumn<>(columnName);
            column.setCellValueFactory(new PropertyValueFactory<AlphabetMapping, String>(columnName.toLowerCase()));
            return column;
        }).collect(Collectors.toList()));

        table.getItems().add(new AlphabetMapping());
        table.setFixedCellSize(30);
        table.prefHeightProperty().bind(Bindings.size(table.getItems()).multiply(table.getFixedCellSize()).add(30));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }

    public static void limitPlainTextInput(TextField field, String oldValue, String newValue) {
        if (!newValue.isEmpty()) {
            if (newValue.chars().allMatch(Character::isLetter)) {
                field.setText(newValue.toUpperCase());
            } else {
                field.clear();
                field.setText(oldValue);
            }
        }
    }

    public static boolean isKeyFilled(List<TextField> key) {
        for (TextField x : key) {
            if (x.getText().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public static GridPane getPaneFromMatrix(ModuloMatrix matrix) {
        int row = 0;
        int col = 0;

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < matrix.getColumnCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                TextField tf = new TextField("" + matrix.get(i, j));
                tf.setEditable(false);
                tf.setMaxWidth(50);
                tf.setMaxHeight(30);
                grid.add(tf, col + j, row);
            }
            row++;
        }

        return grid;
    }

    public static GridPane getPaneFromMatrixWithTooltips(Matrix m) {
        List<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                numbers.add((int) m.get(i, j));
            }
        }

        return getPaneFromNumbersWithTooltips(numbers, m.getRowDimension(), m.getColumnDimension());
    }

    public static GridPane getPaneFromNumbersWithTooltips(List<Integer> numbers, int rowLength, int collength) {
        int row = 0;
        int col = 0;

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < collength; j++) {
                Integer value = numbers.get(i * rowLength + j);
                String tooltip = "" + (char) (value % 26 + 'A');
                String text = "" + value;
                TextField tf = new TextField(text);
                tf.setTooltip(new Tooltip(tooltip));
                tf.setEditable(false);
                tf.setMaxWidth(50);
                tf.setMaxHeight(30);
                grid.add(tf, col + j, row);
            }
            row++;
        }
        grid.alignmentProperty().set(Pos.CENTER);

        return grid;
    }

    public static StackPane createRectangle(String caption, int width, int height) {
        Rectangle r = new Rectangle(width, height);
        r.setFill(Color.TRANSPARENT);
        r.setFill(Color.GRAY);
        r.setStroke(Color.GRAY);
        Text text = new Text(caption);
        StackPane stack = new StackPane();
        stack.getChildren().addAll(r, text);

        return stack;
    }

    public static Matrix getMatrixFromTextFields(List<TextField> rows) {
        int n = rows.size();
        Matrix m = new Matrix(n, n);

        for (int i = 0; i < n; i++) {
            String row = rows.get(i).getText();
            for (int j = 0; j < row.length(); j++) {
                m.set(i, j, row.charAt(j) - 'A');
            }
        }

        return m;
    }
}
