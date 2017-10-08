/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MW;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JComponent;

/**
 *
 * @author Leszek
 */
public class DrawArea extends JComponent {

	int cellsMax = 650;
    int size = 1;
    int cells = cellsMax / size;
    Cell[][] tab = new Cell[cells][cells];
    Cell[][] tab1 = new Cell[cells][cells];
    double[][] disTab = new double[cells][cells];
    double[][] disTab1 = new double[cells][cells];

    List<Cell> colors;
    List<Cell> colorsRecrystallized;
    static int ID = 0;
    static int ID2 = 0;
    boolean isAlive;
    boolean periodic = true;
    boolean continuous = false;
    boolean moore = true;
    boolean vonNeumann, pentagonalLeft, pentagonalRight, hexagonalLeft, hexagonalRight, pentagonalRandom, hexagonalRandom = false;
    int cellNumber = 0;
    int radius = 0;
    int los = 0;
    int numberRecrystallized = 0;
    private Image image;
    private Graphics2D g2;

    double Rho = 0.0;
    double Sigma = 0.0;
    //Rho critical = 4,21584E+12/(cells*cells);
    double Rho_critical = 7494826.92;
    double A_rho = 86710969050178.5;
    double A_sigma = 0.000000000257;
    double B_rho = 9.41268203527779;
    double B_sigma = 80000000000.0;
    double dTau = 0.001;
    double time = 0.0;

    public DrawArea() {
        setDoubleBuffered(true);
        createTables();
        colors = new ArrayList<>();
        addMouseListener(new MouseAdapter() {

            int x;
            int y;

            @Override
            public void mousePressed(MouseEvent e) {
                if (!isAlive) {
                    x = e.getX() / size;
                    y = e.getY() / size;
                    if (tab[x][y].ID == -1) {
                        tab[x][y] = new Cell(ID++);
                        tab1[x][y] = tab[x][y];
                        colors.add(tab[x][y]);
                        cellNumber++;
                        draw();
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {

            int x;
            int y;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isAlive) {
                    x = e.getX() / size;
                    y = e.getY() / size;
                    if (tab[x][y].ID == -1) {
                        tab[x][y] = new Cell(ID++);
                        tab1[x][y] = tab[x][y];
                        colors.add(tab[x][y]);
                        cellNumber++;
                        draw();
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }
        g.drawImage(image, 0, 0, null);
    }

    public void clear() {
        createTables();
        g2.setPaint(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.BLACK);
        draw();
        repaint();
    }

    public void createTables() {
        for (int i = 0; i < cells; i++) {
            for (int j = 0; j < cells; j++) {
                tab[i][j] = new Cell();
                tab1[i][j] = new Cell();
            }
        }
    }

    private void draw() {
        for (int i = 0; i < cells; i++) {
            for (int j = 0; j < cells; j++) {
                g2.setPaint(tab[i][j].color);
                g2.fillRect(i * size, j * size, size, size);
            }
        }
        repaint();
    }

    public void clearTables() {
        for (int i = 0; i < cells; i++) {
            for (int j = 0; j < cells; j++) {
                tab[i][j].ID = -1;
                tab[i][j].color = Color.WHITE;
                tab1[i][j] = tab[i][j];
            }
        }
        colors = null;
        ID = 0;
        draw();
    }

    public void random() {
        clearTables();
        Random rand = new Random();
        colors = new ArrayList<>();
        for (int i = 0; i < cellNumber; i++) {
            int x = rand.nextInt(cells);
            int y = rand.nextInt(cells);
            tab[x][y] = new Cell(ID++);
            tab1[x][y] = tab[x][y];
            colors.add(tab[x][y]);
        }
        draw();
    }

    public void evenlyRandom() {
        clearTables();
        colors = new ArrayList<>();
        int counter = 0;
        int dis = (int) (cells / Math.sqrt(cellNumber));
        for (int x = dis / 2; x < cells; x += dis) {
            for (int y = dis / 2; y < cells; y += dis) {
                counter++;
                tab[x][y] = new Cell(ID++);
                tab1[x][y] = tab[x][y];
                colors.add(tab[x][y]);
            }
        }
        cellNumber = counter;
        draw();
    }

    public void circleRandom() {
        clearTables();
        colors = new ArrayList<>();
        Random rand = new Random();
        int liczba = 0;
        for (int i = 0; i < cellNumber; i++) {
            int x = 0;
            int y = 0;
            boolean free = true;
            for (int j = 0; j < 100000; j++) {
                x = rand.nextInt(cells);
                y = rand.nextInt(cells);
                free = true;
                for (Cell item : colors) {
                    if (!periodic) {
                        if (((x - item.x) * (x - item.x)) + ((y - item.y) * (y - item.y)) <= (radius * radius)) {
                            free = false;
                        }
                    } else {
                        int tempX = x;
                        int tempY = y;
                        if (x <= radius && item.x >= (cells - radius)) {
                            tempX = x + cells;
                        }
                        if (x >= (cells - radius) && (item.x <= radius)) {
                            tempX = -(cells - x);
                        }
                        if (y <= radius && item.y >= (cells - radius)) {
                            tempY = y + cells;
                        }
                        if (y >= (cells - radius) && (item.y <= radius)) {
                            tempY = -(cells - y);
                        }
                        if (((tempX - item.x) * (tempX - item.x)) + ((tempY - item.y) * (tempY - item.y)) <= (radius * radius)) {
                            free = false;
                        }
                    }
                    if (!free) {
                        break;
                    }
                }
                if (free) {
                    break;
                }
            }
            if (!free) {
                System.out.println("Brak wolnych miejsc!");
                cellNumber = liczba;
                draw();
                return;
            }
            tab[x][y] = new Cell(ID++);
            tab[x][y].x = x;
            tab[x][y].y = y;
            tab1[x][y] = tab[x][y];
            colors.add(tab[x][y]);
            liczba++;
        }
        draw();
    }

    public void gameOfLife() {
        while (isAlive) {
            if (continuous) {
                growSeed();
            }
            draw();
            for (int i = 0; i < cells; i++) {
                for (int j = 0; j < cells; j++) {
                    if (tab[i][j].ID != -1) {
                        checkNeighbourhood(i, j);
                    }
                }
            }
            for (int i = 0; i < cells; i++) {
                for (int j = 0; j < cells; j++) {
                    tab[i][j] = tab1[i][j];
                }
            }
        }
    }

    public void checkNeighbourhood(int x, int y) {
        Random rand = new Random();
        los = rand.nextInt(2);
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i == x && j == y) {
                    continue;
                }
                if (vonNeumann) {
                    if (i == (x - 1)) {
                        if ((j == (y - 1)) || (j == (y + 1))) {
                            continue;
                        }
                    }
                    if (i == (x + 1)) {
                        if ((j == (y - 1)) || (j == (y + 1))) {
                            continue;
                        }
                    }
                }
                if (pentagonalLeft || (pentagonalRandom && los == 0)) {
                    if (i == (x + 1)) {
                        continue;
                    }
                }
                if (pentagonalRight || (pentagonalRandom && los == 1)) {
                    if (i == (x - 1)) {
                        continue;
                    }
                }
                if (hexagonalLeft || (hexagonalRandom && los == 0)) {
                    if (i == (x - 1) && j == (y + 1)) {
                        continue;
                    }
                    if (i == (x + 1) && j == (y - 1)) {
                        continue;
                    }
                }
                if (hexagonalRight || (hexagonalRandom && los == 1)) {
                    if (i == (x - 1) && j == (y - 1)) {
                        continue;
                    }
                    if (i == (x + 1) && j == (y + 1)) {
                        continue;
                    }
                }
                int tempX = i;
                int tempY = j;
                if (periodic) {
                    if (i < 0) {
                        tempX = cells - 1;
                    }
                    if (j < 0) {
                        tempY = cells - 1;
                    }
                    if (i == cells) {
                        tempX = 0;
                    }
                    if (j == cells) {
                        tempY = 0;
                    }
                } else {
                    if (i < 0) {
                        continue;
                    }
                    if (j < 0) {
                        continue;
                    }
                    if (i == cells) {
                        continue;
                    }
                    if (j == cells) {
                        continue;
                    }
                }
                if (tab[tempX][tempY].ID == -1) {
                    int tmp = countNeighbours(tempX, tempY);
                    tab1[tempX][tempY] = (Cell) colors.get(tmp);
                }
            }
        }
    }

    public int countNeighbours(int x, int y) {
        int result = 0;
        int temp[] = new int[cellNumber];
        for (int i = 0; i < cellNumber; i++) {
            temp[i] = 0;
        }
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i == x && j == y) {
                    continue;
                }
                int tempX = i;
                int tempY = j;
                if (periodic) {
                    if (i < 0) {
                        tempX = cells - 1;
                    }
                    if (j < 0) {
                        tempY = cells - 1;
                    }
                    if (i == cells) {
                        tempX = 0;
                    }
                    if (j == cells) {
                        tempY = 0;
                    }
                } else {
                    if (i < 0) {
                        continue;
                    }
                    if (j < 0) {
                        continue;
                    }
                    if (i == cells) {
                        continue;
                    }
                    if (j == cells) {
                        continue;
                    }
                }
                if (tab[tempX][tempY].ID != -1) {
                    temp[tab[tempX][tempY].ID]++;
                }
            }
        }
        int max = temp[0];
        for (int k = 1; k < cellNumber; k++) {
            if (temp[k] > max) {
                max = temp[k];
                result = k;
            }
        }
        int ileMax = 0;
        for (int k = 0; k < cellNumber; k++) {
            if (temp[k] == max) {
                ileMax++;
            }
        }
        if (ileMax != 1) {
            int temp1[] = new int[ileMax];
            int iter = 0;
            for (int k = 0; k < cellNumber; k++) {
                if (temp[k] == max) {
                    temp1[iter++] = k;
                }
            }
            Random rand = new Random();
            result = temp1[rand.nextInt(ileMax)];
        }
        return result;
    }

    private void growSeed() {
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(cells);
            int y = rand.nextInt(cells);
            if (tab[x][y].ID == -1) {
                tab[x][y] = new Cell(ID++);
                tab1[x][y] = tab[x][y];
                colors.add(tab[x][y]);
                cellNumber++;
                break;
            }
        }
    }

    public void dynamicRecrystallization() {
        colorsRecrystallized = new ArrayList<>();
        for (double t = time; t < 0.200; t += dTau) {
            draw();
            double tempRho = A_rho / B_rho + (1 - A_rho / B_rho) * Math.exp(-B_rho * t);
            double dislocations = tempRho - Rho;
            System.out.println("Krok czasowy: " + t + " | dyslokacje: " + dislocations);
            Rho = tempRho;
            dislocationCannon(dislocations);
            for (int i = 0; i < cells; i++) {
                for (int j = 0; j < cells; j++) {
                    if (!tab[i][j].recristallized) {
                        if (disTab[i][j] > Rho_critical) {
                            recrystallize(i, j);
                        }
                    }
                }
            }
            for (int i = 0; i < cells; i++) {
                for (int j = 0; j < cells; j++) {
                    if (tab[i][j].recristallized) {
                        recrystallizeNeighbours(i, j);
                    }
                }
            }
//            if (!colorsRecrystallized.isEmpty()) {
//                for (int i = 0; i < cells; i++) {
//                    for (int j = 0; j < cells; j++) {
//                        int tmp = neighboursRecrystallized(i, j);
//                        if (!tab[i][j].recristallized && tmp != -1) {
//                            tab1[i][j] = (Cell) colorsRecrystallized.get(tmp);
//                        }
//                    }
//                }
            for (int i = 0; i < cells; i++) {
                for (int j = 0; j < cells; j++) {
                    tab[i][j] = tab1[i][j];
                }
            }
//            }
        }
    }

    public void recrystallizeNeighbours(int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i == x && j == y) {
                    continue;
                }
                if (vonNeumann) {
                    if (i == (x - 1)) {
                        if ((j == (y - 1)) || (j == (y + 1))) {
                            continue;
                        }
                    }
                    if (i == (x + 1)) {
                        if ((j == (y - 1)) || (j == (y + 1))) {
                            continue;
                        }
                    }
                }
                if (pentagonalLeft || (pentagonalRandom && los == 0)) {
                    if (i == (x + 1)) {
                        continue;
                    }
                }
                if (pentagonalRight || (pentagonalRandom && los == 1)) {
                    if (i == (x - 1)) {
                        continue;
                    }
                }
                if (hexagonalLeft || (hexagonalRandom && los == 0)) {
                    if (i == (x - 1) && j == (y + 1)) {
                        continue;
                    }
                    if (i == (x + 1) && j == (y - 1)) {
                        continue;
                    }
                }
                if (hexagonalRight || (hexagonalRandom && los == 1)) {
                    if (i == (x - 1) && j == (y - 1)) {
                        continue;
                    }
                    if (i == (x + 1) && j == (y + 1)) {
                        continue;
                    }
                }
                int tempX = i;
                int tempY = j;
                if (periodic) {
                    if (i < 0) {
                        tempX = cells - 1;
                    }
                    if (j < 0) {
                        tempY = cells - 1;
                    }
                    if (i == cells) {
                        tempX = 0;
                    }
                    if (j == cells) {
                        tempY = 0;
                    }
                } else {
                    if (i < 0) {
                        continue;
                    }
                    if (j < 0) {
                        continue;
                    }
                    if (i == cells) {
                        continue;
                    }
                    if (j == cells) {
                        continue;
                    }
                }
                if (!tab1[tempX][tempY].recristallized) {
                    int tmp = neighboursRecrystallized(tempX, tempY);
                    tab1[tempX][tempY] = (Cell) colors.get(tmp);
                    disTab1[x][y] = 0.0;
                    disTab[x][y] = 0.0;
                }
            }
        }
    }

    public int neighboursRecrystallized(int x, int y) {
        int result = 0;
        int temp[] = new int[cellNumber];
        for (int i = 0; i < cellNumber; i++) {
            temp[i] = 0;
        }
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i == x && j == y) {
                    continue;
                }
                int tempX = i;
                int tempY = j;
                if (periodic) {
                    if (i < 0) {
                        tempX = cells - 1;
                    }
                    if (j < 0) {
                        tempY = cells - 1;
                    }
                    if (i == cells) {
                        tempX = 0;
                    }
                    if (j == cells) {
                        tempY = 0;
                    }
                } else {
                    if (i < 0) {
                        continue;
                    }
                    if (j < 0) {
                        continue;
                    }
                    if (i == cells) {
                        continue;
                    }
                    if (j == cells) {
                        continue;
                    }
                }
                if (tab[tempX][tempY].recristallized) {
                    temp[tab[tempX][tempY].ID]++;
                }
            }
        }
        int max = temp[0];
        for (int k = 1; k < cellNumber; k++) {
            if (temp[k] > max) {
                max = temp[k];
                result = k;
            }
        }
        int ileMax = 0;
        for (int k = 0; k < cellNumber; k++) {
            if (temp[k] == max) {
                ileMax++;
            }
        }
        if (ileMax != 1) {
            int temp1[] = new int[ileMax];
            int iter = 0;
            for (int k = 0; k < cellNumber; k++) {
                if (temp[k] == max) {
                    temp1[iter++] = k;
                }
            }
            Random rand = new Random();
            result = temp1[rand.nextInt(ileMax)];
        }
        return result;
    }

    public void recrystallize(int x, int y) {
//        System.out.println("Tworze zarodek!");
        tab1[x][y] = new Cell(ID++, true);
        tab[x][y] = tab1[x][y];
        cellNumber++;
        colors.add(tab[x][y]);
        disTab1[x][y] = 0.0;
        disTab[x][y] = 0.0;
    }

    public void dislocationCannon(double dis) {
        double dislocations = dis;
        double cellDislocations = dislocations / (cells * cells);
        for (int i = 0; i < cells; i++) {
            for (int j = 0; j < cells; j++) {
                if (isBorder(i, j)) {
                    double tempDislocations = 0.8 * cellDislocations;
                    disTab1[i][j] += tempDislocations;
                    disTab[i][j] = disTab1[i][j];
                    dislocations -= tempDislocations;
                } else {
                    double tempDislocations = 0.2 * cellDislocations;
                    disTab1[i][j] += tempDislocations;
                    disTab[i][j] = disTab1[i][j];
                    dislocations -= tempDislocations;
                }
            }
        }
        System.out.println("Pozostale dyslokacje:" + dislocations);
        double N = 8.0;
        Random rand = new Random();
        cellDislocations = dislocations / N;
        while (dislocations > cellDislocations) {
            int x = rand.nextInt(cells);
            int y = rand.nextInt(cells);
            if (isBorder(x, y)) {
                disTab[x][y] += cellDislocations;
                disTab1[x][y] += cellDislocations;
                dislocations -= cellDislocations;
            }
        }
    }

//    public void dislocationCannon(double dis) {
//        double dislocations = dis;
//        double cellDislocations = dislocations / (cells * cells);
//        for (int i = 0; i < cells; i++) {
//            for (int j = 0; j < cells; j++) {
//                if (isBorder(i, j)) {
//                    double tempDislocations = 0.8 * cellDislocations;
//                    tab1[i][j].dislocations += tempDislocations;
//                    tab[i][j].dislocations = tab1[i][j].dislocations;
//                    dislocations -= tempDislocations;
//                } else {
//                    double tempDislocations = 0.2 * cellDislocations;
//                    tab1[i][j].dislocations += tempDislocations;
//                    tab[i][j].dislocations = tab1[i][j].dislocations;
//                    dislocations -= tempDislocations;
//                }
//            }
//        }
//        double N = 30.0;
//        Random rand = new Random();
//        cellDislocations = dislocations / N;
//        while (dislocations > cellDislocations) {
//            int x = rand.nextInt(cells);
//            int y = rand.nextInt(cells);
//            if (isBorder(x, y)) {
//                tab[x][y].dislocations += cellDislocations;
//                dislocations -= cellDislocations;
//            }
//        }
//    }
    public boolean isBorder(int x, int y) {
        boolean state = false;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i == x && j == y) {
                    continue;
                }
                int tempX = i;
                int tempY = j;
                if (periodic) {
                    if (i < 0) {
                        tempX = cells - 1;
                    }
                    if (j < 0) {
                        tempY = cells - 1;
                    }
                    if (i == cells) {
                        tempX = 0;
                    }
                    if (j == cells) {
                        tempY = 0;
                    }
                } else {
                    if (i < 0) {
                        continue;
                    }
                    if (j < 0) {
                        continue;
                    }
                    if (i == cells) {
                        continue;
                    }
                    if (j == cells) {
                        continue;
                    }
                }
                if (tab[tempX][tempY].ID != tab[x][y].ID) {
                    state = true;
                }
            }
        }
        return state;
    }

    public class Cell {

        int ID;
        Color color;
        public int x;
        public int y;
        public double dislocations;
        public boolean recristallized = false;

        public Cell(int id) {
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            this.ID = id;
            color = new Color(r, g, b);
            dislocations = 0.0;
        }

        public Cell(int id, boolean recristallized) {
            Random rand = new Random();
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            this.ID = id;
            color = new Color(r, g, b);
            dislocations = 0.0;
            this.recristallized = recristallized;
        }

        public Cell() {
            this.ID = -1;
            color = Color.WHITE;
            dislocations = 0.0;
        }
    }

}
