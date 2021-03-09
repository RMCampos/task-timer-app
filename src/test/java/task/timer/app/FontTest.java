package task.timer.app;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.Locale;

public class FontTest {

    @Test
    public void getFontsTest() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] allFonts = ge.getAllFonts();
        for (Font font : allFonts) {
            System.out.println(font.getFontName());
        }

        Font notoSansRegular = new Font("Noto Sans Regular", Font.PLAIN, 12);
        Font notoSans = new Font("Noto Sans", Font.PLAIN, 12);

        Assert.assertNotNull(notoSansRegular);
        Assert.assertNotNull(notoSans);
    }

}
