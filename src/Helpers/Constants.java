package Helpers;

import java.awt.*;

/**
 * Created by Kemal BAYINDIR on 12/4/2014.
 */
public class Constants {
    public static int SIZE_OF_SQUARE = 50;
    public static int FORM_SIZE = 600;
    public static int ROW_COUNT = (int)(FORM_SIZE / SIZE_OF_SQUARE);
    public static int SLEEP = 20;
    public static int SOLVE_SLEEP = 100;
    public static int LINE_WIDTH = 3;

    public static Color BLOCK_COLOR = Color.decode("#2e6d82");
    public static Color BACKGROUND_COLOR = Color.decode("#103a51");//new Color(102, 178, 255);
    public static Color LINE_COLOR = Color.white;//new Color(76, 0, 53);
    public static Color START_POINT_COLOR = Color.red;
    public static Color EXIT_POINT_COLOR = Color.decode("#92d13d");
    public static Color BACK_TRACK_COLOR = Color.LIGHT_GRAY; //new Color(224, 224, 224);
    public static Color SEARCHED_COLOR = Color.YELLOW; //new Color(255, 255, 153);
    public static Color PATH_COLOR = new Color(255, 200, 200);
    public static Color VISITED_COLOR = new Color(170, 170, 170);
}
