package rs.ac.bg.etf.cryptography.controllers;

import java.util.HashMap;
import java.util.Map;

import rs.ac.bg.etf.cryptography.simulators.HillCipher;
import rs.ac.bg.etf.cryptography.ui.MainPage;
import rs.ac.bg.etf.cryptography.utils.UI;

public class Simulator {

    public static enum SimMode {
        ENCRYPTION_SIM, DECRYPTION_SIM, ENCRYPTION_TEST, DECRYPTION_TEST, ATTACKS
    };

    public static Map<SimMode, String> modeLabels;

    private static SimMode mode = SimMode.ENCRYPTION_SIM;

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

    public static void reset() {
        Simulator.mode = SimMode.ENCRYPTION_SIM;
        UI.switchScene(HillCipher.window, new MainPage().getScene());
    }

}
