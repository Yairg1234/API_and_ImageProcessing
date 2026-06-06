package main;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class MenuPanel extends JPanel {
    int normalSizeText = Main.normalSizeText;
    Font font = Main.font;
    final Color BACKGROUND_COLOR = Main.BACKGROUND_COLOR;
    final Color MAIN_COLOR = Main.MAIN_COLOR;
    final int DEFAULT_VALUE = 30;
    private JSONObject mazeSetting;

    public MenuPanel() {
        this.setBackground(BACKGROUND_COLOR);
        this.setLayout(new BorderLayout());

        try {
//            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Maze-settings.json");
//            String jsonText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
//            mazeSetting = new JSONObject(jsonText);
            mazeSetting = getJsonAPI();
        } catch (Exception e) {
            System.out.println("לא הצליח להשיג את בקשת הAPI");
            System.exit(0);
        }

        //<הפאנל כותרת>
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(30, 30, 30));

        JLabel title = new JLabel("הגדרות יצירת מבוך");
        textStyle(title, 25, titlePanel ,true);

        //<הפנאל ההגדרות>
        JPanel settingPanel = new JPanel();
        settingPanel.setLayout(new GridLayout(0, 2, 10, 10));
        settingPanel.setBackground(BACKGROUND_COLOR);
        settingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        settingPanel.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JLabel textWallColor = new JLabel("צבע הקירות: ");
        textStyle(textWallColor, normalSizeText, settingPanel, false);

        JLabel wallColor = new JLabel("     ");
        wallColor.setBackground(Color.decode(mazeSetting.getString("wallCellColor")));
        wallColor.setOpaque(true);
        textStyle(wallColor, normalSizeText, settingPanel, false);

        JLabel textPathColor = new JLabel("צבע הנתיב: ");
        textStyle(textPathColor, normalSizeText, settingPanel, false);

        JLabel pathColor = new JLabel("     ");
        pathColor.setBackground(Color.decode(mazeSetting.getString("pathColor")));
        pathColor.setOpaque(true);
        textStyle(pathColor, normalSizeText, settingPanel, false);

        JLabel textDrawGrid = new JLabel("עם רשת: ");
        textStyle(textDrawGrid, normalSizeText, settingPanel, false);

        JLabel drawGrid = new JLabel(Boolean.toString(mazeSetting.getBoolean("drawGrid")), SwingConstants.RIGHT);
        textStyle(drawGrid, normalSizeText, settingPanel, true);

        JLabel textGridColor = new JLabel("צבע הרשת: ");
        textStyle(textGridColor, normalSizeText, settingPanel, false);

        JLabel gridColor = new JLabel("     ");
        gridColor.setBackground(Color.decode(mazeSetting.getString("gridColor")));
        gridColor.setOpaque(true);
        textStyle(gridColor, normalSizeText, settingPanel, false);

        JLabel textDelayAnim = new JLabel("דיליי אנימציה: ");
        textStyle(textDelayAnim, normalSizeText, settingPanel, false);

        JLabel delayAnim = new JLabel(mazeSetting.getInt("animationDelayMs") + " מילישניות", SwingConstants.RIGHT);
        textStyle(delayAnim, normalSizeText, settingPanel, true);


        //<הפאמל הסופי>
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setLayout(new GridLayout(6, 1, 0, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 150, 10, 150));

        JButton btnReload = new JButton("ריענון");
        btnReload.setFocusPainted(false);
        btnReload.setFont(font);
        btnReload.setForeground(MAIN_COLOR);
        btnReload.setBackground(settingPanel.getBackground());
        btnReload.setBorder(new LineBorder(titlePanel.getBackground(), 1));
        btnReload.setPreferredSize(new Dimension(100, 50));
        btnReload.addActionListener(e -> {
            Main.showMenuPanel();
        });
        bottomPanel.add(btnReload);

        JLabel textWidth = new JLabel("רוחב:", SwingConstants.CENTER);
        textStyle(textWidth, normalSizeText, bottomPanel, false);

        JSpinner width = new JSpinner(new SpinnerNumberModel(DEFAULT_VALUE, 5, 100, 1));
        bottomPanel.add(width);

        JLabel textHeight = new JLabel("אורך:", SwingConstants.CENTER);
        textStyle(textHeight, normalSizeText, bottomPanel, false);

        JSpinner height = new JSpinner(new SpinnerNumberModel(DEFAULT_VALUE, 5, 100, 1));
        bottomPanel.add(height);

        JButton btnGetMaze = setBtnMaze(width, height);
        bottomPanel.add(btnGetMaze);


        this.add(titlePanel, BorderLayout.NORTH);
        this.add(settingPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void textStyle(JLabel text, float size, JPanel panel, boolean isOrange){
        if (isOrange){
            text.setForeground(MAIN_COLOR);
        }else {
            text.setForeground(Color.white);
        }
        text.setFont(font.deriveFont(size));


        panel.add(text);
    }

    private JButton setBtnMaze(JSpinner width, JSpinner height){
        JButton btn = new JButton("קבל מבוך");
        btn.setForeground(Color.white);
        btn.setBackground(MAIN_COLOR);
        btn.setFont(font);

        btn.addActionListener(event -> {
            Main.showMazePanel(this.mazeSetting, (Integer) width.getValue(), (Integer) height.getValue());
        });

        return btn;
    }

    private JSONObject getJsonAPI() throws Exception{
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://backend-qcf9.onrender.com/fm1/get-render-config")
                .build();
        Response response = client.newCall(request).execute();
        String mazeSettingsData = response.body().string();

        JSONObject jsonObject = new JSONObject(mazeSettingsData);
        return jsonObject;
    }
}
