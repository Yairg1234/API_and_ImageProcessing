package main;

import com.formdev.flatlaf.FlatDarkLaf;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final Color BACKGROUND_COLOR = new Color(45, 45, 45);
    public static final Color MAIN_COLOR = new Color(255, 111, 0);
    public static final int normalSizeText = 20;
    public static Font font = new Font("Tahoma", Font.BOLD, normalSizeText);

    private static JFrame window;
    private static CardLayout cardLayout;
    private static MenuPanel menuPanel;
    private static MazePanel mazePanel;

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        UIManager.put("TitlePane.background", new Color(30, 30 ,30));
        UIManager.put("TitlePane.unifiedBackground", new Color(30, 30, 30));
        UIManager.put("TitlePane.foreground", MAIN_COLOR);
        UIManager.put("TitlePane.font", font.deriveFont(12f));
        UIManager.put("Component.arc", 15);
        UIManager.put("Button.arc", 15);
        FlatDarkLaf.setup();

        window = new JFrame("יצירת מבוך");

        cardLayout = new CardLayout();

        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        window.getContentPane().setLayout(cardLayout);

        showMenuPanel();
        window.setVisible(true);
    }

    public static void showMenuPanel() {
        if(menuPanel != null){
            window.getContentPane().remove(menuPanel);
        }
        menuPanel = new MenuPanel();

        window.getContentPane().add(menuPanel, "MenuPanel");

        window.getContentPane().revalidate();
        window.getContentPane().repaint();

        cardLayout.show(window.getContentPane(), "MenuPanel");
    }

    public static void showMazePanel(JSONObject jsonObject, int width, int height){
        mazePanel = new MazePanel(jsonObject, width, height);
        window.getContentPane().add(mazePanel, "MazePanel");

        cardLayout.show(window.getContentPane(), "MazePanel");
    }
}
