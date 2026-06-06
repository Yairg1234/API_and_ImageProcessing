package main;

import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MazePanel extends JPanel {
    final Color BACKGROUND_COLOR = Main.BACKGROUND_COLOR;
    final Font font = Main.font;

    JButton btnCheckSolution;

    public MazePanel(JSONObject jsonObject, int width, int height){
        String statusText = "סטטוס: ";

        this.setBackground(BACKGROUND_COLOR);
        this.setLayout(new BorderLayout(0, 16));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel textSolution = new JLabel(statusText);
        textSolution.setForeground(Color.WHITE);
        textSolution.setFont(font);

        headerPanel.add(textSolution);

        PrintMaze printMaze = new PrintMaze(jsonObject, width, height, this);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(printMaze);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        JPanel mazeCard = new JPanel(new BorderLayout());
        mazeCard.setBackground(BACKGROUND_COLOR);
        mazeCard.setBorder(new EmptyBorder(8, 8, 8, 8));
        mazeCard.add(scrollPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);

        btnCheckSolution = new JButton("בדיקת פתרון");
        btnCheckSolution.setFont(font);
        btnCheckSolution.setForeground(Color.white);
        btnCheckSolution.setBackground(Main.MAIN_COLOR);
        btnCheckSolution.setFocusPainted(false);
        btnCheckSolution.setPreferredSize(new Dimension(350, 38));
        btnCheckSolution.setEnabled(false);
        btnCheckSolution.addActionListener(e -> {
            btnCheckSolution.setEnabled(false);

            if(printMaze.checkSolution())
                textSolution.setText(statusText + "יש פתרון");
            else {
                textSolution.setText(statusText + "אין פתרון");
                btnCheckSolution.setEnabled(true);
            }

        });
        footerPanel.add(btnCheckSolution, BorderLayout.CENTER);

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(mazeCard, BorderLayout.CENTER);
        this.add(footerPanel, BorderLayout.SOUTH);
    }

    public void setBtnEnable(){
        btnCheckSolution.setEnabled(true);
    }
}
