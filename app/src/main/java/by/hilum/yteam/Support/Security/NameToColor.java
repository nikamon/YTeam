package by.hilum.yteam.Support.Security;

public class NameToColor {

    /**
     * Supported Colors Array
     */
    private static final String[] colors = {"#ff8800", "#bb86fc", "#6200ee", "#03dac5", "#018786", "#b00020", "#cc0000", "#3335c5", "#0099cc", "#f17a0a", "#aa66cc", "#ff4444"};

    /**
     * Parse String to Color
     *
     * @param label String
     * @return String
     */
    public static String NameToColor(String label) {
        int size = label.length();

        return colors[size % 10];
    }
}
