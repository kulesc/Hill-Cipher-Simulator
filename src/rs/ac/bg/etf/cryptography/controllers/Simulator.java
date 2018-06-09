package rs.ac.bg.etf.cryptography.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import Jama.Matrix;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import rs.ac.bg.etf.cryptography.math.ModuloMatrix;
import rs.ac.bg.etf.cryptography.simulators.HillCipher;
import rs.ac.bg.etf.cryptography.ui.MainPage;
import rs.ac.bg.etf.cryptography.utils.Common;
import rs.ac.bg.etf.cryptography.utils.UI;

public class Simulator {

    public static enum SimMode {
        ENCRYPTION_SIM, DECRYPTION_SIM, TEST, ATTACKS
    };

    public static Map<SimMode, String> modeLabels;

    private static SimMode mode = SimMode.ENCRYPTION_SIM;

    private static Matrix key;

    private static Matrix inverseKey;

    private static String plaintext = "";

    private static Integer keySize = 2;

    private static String fillCharacter = "X";

    private static String ciphertext = "";

    private static String plaintextAnswer = "";

    private static String ciphertextAnswer = "";

    static {
        modeLabels = new HashMap<>();
        modeLabels.put(SimMode.ATTACKS, "Attacks simulation");
        modeLabels.put(SimMode.DECRYPTION_SIM, "Decryption simulation");
        modeLabels.put(SimMode.TEST, "Test");
        modeLabels.put(SimMode.ENCRYPTION_SIM, "Encryption simulation");
    }

    public static SimMode getMode() {
        return mode;
    }

    public static void setMode(SimMode mode) {
        Simulator.mode = mode;
    }

    public static Matrix getKey() {
        return key;
    }

    public static void setKey(Matrix key) {
        Simulator.key = key;
    }

    public static String getPlaintext() {
        return plaintext;
    }

    public static void setPlaintext(String plaintext) {
        Simulator.plaintext = plaintext;
    }

    public static Integer getKeySize() {
        return keySize;
    }

    public static void setKeySize(Integer keySize) {
        Simulator.keySize = keySize;
    }

    public static String getFillCharacter() {
        return fillCharacter;
    }

    public static void setFillCharacter(String fillCharacter) {
        Simulator.fillCharacter = fillCharacter;
    }

    public static String getCiphertext() {
        return ciphertext;
    }

    public static void setCiphertext(String ciphertext) {
        Simulator.ciphertext = ciphertext;
    }

    public static Matrix getInverseKey() {
        return inverseKey;
    }

    public static void setInverseKey(Matrix inverseKey) {
        Simulator.inverseKey = inverseKey;
    }

    public static String getPlaintextAnswer() {
        return plaintextAnswer;
    }

    public static void setPlaintextAnswer(String plaintextAnswer) {
        Simulator.plaintextAnswer = plaintextAnswer;
    }

    public static String getCiphertextAnswer() {
        return ciphertextAnswer;
    }

    public static void setCiphertextAnswer(String ciphertextAnswer) {
        Simulator.ciphertextAnswer = ciphertextAnswer;
    }

    public static void reset() {
        Simulator.mode = SimMode.ENCRYPTION_SIM;
        UI.switchScene(HillCipher.window, new MainPage().getScene());
        Simulator.key = null;
        Simulator.inverseKey = null;
        Simulator.plaintext = "";
        Simulator.keySize = 2;
        Simulator.fillCharacter = "X";
        Simulator.ciphertext = "";
        Simulator.plaintextAnswer = "";
        Simulator.ciphertextAnswer = "";
    }

    public static boolean loadTestFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Test File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Test files in .json format.", "*.json"));
        File testFile = fileChooser.showOpenDialog(HillCipher.window);

        try (JsonReader reader = new JsonReader(new FileReader(testFile))) {
            JsonParser parser = new JsonParser();
            JsonObject test = parser.parse(new FileReader(testFile)).getAsJsonObject();
            Simulator.mode = SimMode.TEST;
            Simulator.keySize = test.get("key_size").getAsInt();
            Simulator.key = Common.getMatrixFromJson(test.get("key").getAsJsonObject(), Simulator.keySize);

            switch (test.get("test").getAsString()) {
            case "encryption":
                Simulator.plaintext = test.get("plaintext").getAsString();
                Simulator.fillCharacter = test.get("fill").getAsString();
                break;
            case "decryption":
                Simulator.ciphertext = test.get("ciphertext").getAsString();
                Simulator.inverseKey = ModuloMatrix.inverse(new ModuloMatrix(Simulator.key)).getMatrix();
                break;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } catch (Exception e) {
            Simulator.reset();
            return false;
        }

        return true;
    }

}
