import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.*;
import java.util.Random;

public class Utils {

    public static String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static Border createEmptyBorder(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right);
    }

    public static Color createTransparentColor(int r, int g, int b, int a) {
        return new Color(r, g, b, a);
    }
}