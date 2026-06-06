package main;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PrintMaze extends JPanel {
    private final MazePanel mazePanel;
    private final MazeSolver mazeSolver;
    private final String wallCellColor;
    private final String pathColor;
    private final boolean drawGrid;
    private final String gridColor;
    private final int animationDelayMs;
    private final int width;
    private final int height;
    private final int cellSize = 20; //16
    private int cols;
    private int rows;
    private boolean[][] isWall;
    private BufferedImage mazeImage;
    private boolean isMazePainted;
    private Timer timer;
    private int currentStep = 0;
    private List<Point> solutionPath;
    private final BasicStroke stroke = new BasicStroke(3);

    public PrintMaze(JSONObject jsonObject, int width, int height, MazePanel mazePanel){
        this.setBackground(Color.black);


        this.mazePanel = mazePanel;
        this.wallCellColor = jsonObject.getString("wallCellColor");
        this.pathColor = jsonObject.getString("pathColor");
        this.drawGrid = jsonObject.getBoolean("drawGrid");
        this.gridColor = jsonObject.getString("gridColor");
        this.animationDelayMs = jsonObject.getInt("animationDelayMs");
        this.width = width;
        this.height = height;

        //File file = new File("src/main/resources/get-maze-image.png");
        try {
            //this.mazeImage = ImageIO.read(file);
            this.mazeImage = ImageIO.read(getUrlMaze());
        } catch (IOException e) {
            System.out.println("לא ניתן היה לטעון את התמונה");
            System.exit(0);
        }

        this.setPreferredSize(new Dimension(mazeImage.getWidth(), mazeImage.getHeight()));

        this.cols = this.mazeImage.getWidth() / cellSize;
        this.rows = this.mazeImage.getHeight() / cellSize;

        this.isWall = new boolean[this.cols][this.rows];
        for (int i = 0; i < this.cols; i++) {
            for (int j = 0; j < this.rows; j++) {
                isWall[i][j] = this.mazeImage.getRGB(i * this.cellSize, j * this.cellSize) != Color.WHITE.getRGB();
            }
        }

        this.mazeSolver = new MazeSolver(this.isWall, this.cols, this.rows);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < this.cols; i++) {
            for (int j = 0; j < this.rows; j++) {
                if(isWall[i][j]) {
                    g2.setColor(Color.decode(this.wallCellColor));
                }else {
                    g2.setColor(Color.white);
                }
                g2.fillRect(i * this.cellSize, j * this.cellSize, this.cellSize, this.cellSize);

                if (this.drawGrid) {
                    g2.setColor(Color.decode(this.gridColor));
                    g2.setStroke(stroke);
                    g2.drawRect(i * this.cellSize, j * this.cellSize, this.cellSize, this.cellSize);
                }
            }
        }

        if(!isMazePainted){
            this.mazePanel.setBtnEnable();
            isMazePainted = true;
        }

        if (solutionPath != null && currentStep > 0) {
            paintSolution(g2);
        }
    }

    public URL getUrlMaze(){
        URL url = null;
        try {
            url = new URL(
                    "https://backend-qcf9.onrender.com/fm1/get-maze-image?width="
                            + this.width
                            + "&height="
                            + this.height
            );
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    public boolean checkSolution(){
        if(this.mazeSolver.hasSolution()){
            this.solutionPath = this.mazeSolver.getSolution();
            startAnimation();
            return true;
        }return false;

    }

    private void paintSolution(Graphics2D g2){
        g2.setColor(Color.decode(this.pathColor));

        for (int i = 0; i < currentStep; i++) {
            Point p = solutionPath.get(i);
            g2.fillRect(p.x * cellSize, p.y * cellSize, cellSize, cellSize);
        }
    }

    private void startAnimation(){
        currentStep = 0;

        timer = new Timer(animationDelayMs, e -> {
            currentStep++;

            repaint();

            if (currentStep >= solutionPath.size()) {
                timer.stop();
                this.mazePanel.setBtnEnable();
            }
        });
        timer.start();
    }
}
