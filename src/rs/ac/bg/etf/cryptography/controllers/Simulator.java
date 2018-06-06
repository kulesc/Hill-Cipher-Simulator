package rs.ac.bg.etf.cryptography.controllers;

import java.util.HashMap;
import java.util.Map;

import Jama.Matrix;
import rs.ac.bg.etf.cryptography.simulators.HillCipher;
import rs.ac.bg.etf.cryptography.ui.MainPage;
import rs.ac.bg.etf.cryptography.utils.UI;

public class Simulator {

    public static enum SimMode {
        ENCRYPTION_SIM, DECRYPTION_SIM, ENCRYPTION_TEST, DECRYPTION_TEST, ATTACKS
    };

    public static Map<SimMode, String> modeLabels;

    private static SimMode mode = SimMode.ENCRYPTION_SIM;

    private static Matrix key;

    private static Matrix inverseKey;

    private static String plaintext = "";

    private static Integer keySize = 2;

    private static String fillCharacter = "X";

    private static String ciphertext = "";

    static {
        modeLabels = new HashMap<>();
        modeLabels.put(SimMode.ATTACKS, "Attacks simulation");
        modeLabels.put(SimMode.DECRYPTION_SIM, "Decryption simulation");
        modeLabels.put(SimMode.DECRYPTION_TEST, "Decryption test");
        modeLabels.put(SimMode.ENCRYPTION_SIM, "Encryption simulation");
        modeLabels.put(SimMode.ENCRYPTION_TEST, "Encryption test");
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

    public static void reset() {
        Simulator.mode = SimMode.ENCRYPTION_SIM;
        UI.switchScene(HillCipher.window, new MainPage().getScene());
        Simulator.key = null;
        Simulator.plaintext = "";
        Simulator.keySize = 2;
        Simulator.fillCharacter = "X";
        Simulator.ciphertext = "";
    }

}
