/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MW;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Leszek
 */
public class SwingPaint {

    JButton randBtn, startBtn, stopBtn, evenlyRandBtn, circRandBtn, recrystBtn;
    JTextField cellField, radiusField, cellSizeField;
    JLabel cellLabel, radiusLabel, cellSizeLabel;
    JCheckBox periodicBox, continuousBox;
    JList<String> surroundList;
    String[] surrounds = {"Moore", "von Neumann", "Pentagonal Left", "Pentagonal Right", "Hexagonal Left", "Hexagonal Right", "Penatgonal Random", "Hexagonal Random"};

    DrawArea drawArea;
    ActionListener actionListener = new ActionListener() {
        Thread thread = new Thread();

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == randBtn) {
                drawArea.cellNumber = Integer.parseInt(cellField.getText());
                if (Integer.parseInt(cellSizeField.getText()) > 5) {
                	cellSizeField.setText(Integer.toString(5));
                }
                drawArea.size = Integer.parseInt(cellSizeField.getText());
                drawArea.random();
            }

            if (e.getSource() == evenlyRandBtn) {
                drawArea.cellNumber = Integer.parseInt(cellField.getText());
                if (Integer.parseInt(cellSizeField.getText()) > 5) {
                	cellSizeField.setText(Integer.toString(5));
                }
                drawArea.size = Integer.parseInt(cellSizeField.getText());
                drawArea.evenlyRandom();
            }

            if (e.getSource() == circRandBtn) {
                drawArea.cellNumber = Integer.parseInt(cellField.getText());
                if (Integer.parseInt(cellSizeField.getText()) > 5) {
                	cellSizeField.setText(Integer.toString(5));
                }
                drawArea.size = Integer.parseInt(cellSizeField.getText());
                drawArea.radius = Integer.parseInt(radiusField.getText());
                drawArea.circleRandom();
            }
            if (e.getSource() == startBtn) {
                drawArea.isAlive = true;
                if (thread.isAlive()) {
                    thread.stop();
                }
                thread = new Thread() {
                    @Override
                    public void run() {
                        drawArea.gameOfLife();
                    }
                };
                thread.start();
            }
            
            if (e.getSource() == recrystBtn) {
                drawArea.isAlive = true;
                if (thread.isAlive()) {
                    thread.stop();
                }
                thread = new Thread() {
                    @Override
                    public void run() {
                        drawArea.dynamicRecrystallization();
                    }
                };
                thread.start();
            }

            if (e.getSource() == stopBtn) {
                drawArea.isAlive = false;
                thread.stop();
            }
            if (e.getSource() == periodicBox) {
                drawArea.periodic = periodicBox.isSelected();
            }
            if (e.getSource() == continuousBox) {
                drawArea.continuous = continuousBox.isSelected();
            }
        }
    };

    public void show() {
        JFrame frame = new JFrame("Naiwny rozrost ziaren");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());
        drawArea = new DrawArea();

        content.add(drawArea, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        surroundList = new JList<>(surrounds);
        surroundList.setSelectedIndex(0);
        surroundList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                int index = surroundList.getSelectedIndex();
                if (index == 0) {
                    drawArea.moore = true;
                    drawArea.vonNeumann = false;
                    drawArea.pentagonalLeft = false;
                    drawArea.pentagonalRight = false;
                    drawArea.hexagonalLeft = false;
                    drawArea.hexagonalRight = false;
                    drawArea.pentagonalRandom = false;
                    drawArea.hexagonalRandom = false;
                }
                if (index == 1) {
                    drawArea.moore = false;
                    drawArea.vonNeumann = true;
                    drawArea.pentagonalLeft = false;
                    drawArea.pentagonalRight = false;
                    drawArea.hexagonalLeft = false;
                    drawArea.hexagonalRight = false;
                    drawArea.pentagonalRandom = false;
                    drawArea.hexagonalRandom = false;
                }
                if (index == 2) {
                    drawArea.moore = false;
                    drawArea.vonNeumann = false;
                    drawArea.pentagonalLeft = true;
                    drawArea.pentagonalRight = false;
                    drawArea.hexagonalLeft = false;
                    drawArea.hexagonalRight = false;
                    drawArea.pentagonalRandom = false;
                    drawArea.hexagonalRandom = false;
                }
                if (index == 3) {
                    drawArea.moore = false;
                    drawArea.vonNeumann = false;
                    drawArea.pentagonalLeft = false;
                    drawArea.pentagonalRight = true;
                    drawArea.hexagonalLeft = false;
                    drawArea.hexagonalRight = false;
                    drawArea.pentagonalRandom = false;
                    drawArea.hexagonalRandom = false;
                }
                if (index == 4) {
                    drawArea.moore = false;
                    drawArea.vonNeumann = false;
                    drawArea.pentagonalLeft = false;
                    drawArea.pentagonalRight = false;
                    drawArea.hexagonalLeft = true;
                    drawArea.hexagonalRight = false;
                    drawArea.pentagonalRandom = false;
                    drawArea.hexagonalRandom = false;
                }
                if (index == 5) {
                    drawArea.moore = false;
                    drawArea.vonNeumann = false;
                    drawArea.pentagonalLeft = false;
                    drawArea.pentagonalRight = false;
                    drawArea.hexagonalLeft = false;
                    drawArea.hexagonalRight = true;
                    drawArea.pentagonalRandom = false;
                    drawArea.hexagonalRandom = false;
                }
                if (index == 6) {
                    drawArea.moore = false;
                    drawArea.vonNeumann = false;
                    drawArea.pentagonalLeft = false;
                    drawArea.pentagonalRight = false;
                    drawArea.hexagonalLeft = false;
                    drawArea.hexagonalRight = false;
                    drawArea.pentagonalRandom = true;
                    drawArea.hexagonalRandom = false;
                }
                if (index == 7) {
                    drawArea.moore = false;
                    drawArea.vonNeumann = false;
                    drawArea.pentagonalLeft = false;
                    drawArea.pentagonalRight = false;
                    drawArea.hexagonalLeft = false;
                    drawArea.hexagonalRight = false;
                    drawArea.pentagonalRandom = false;
                    drawArea.hexagonalRandom = true;
                }
            }
        });
        cellLabel = new JLabel("Number of cells:");
        cellField = new JTextField("1000");
        radiusLabel = new JLabel("Radius:");
        radiusField = new JTextField("100");
        cellSizeLabel = new JLabel("Radius:");
        cellSizeField = new JTextField("1");
        randBtn = new JButton("Random");
        randBtn.addActionListener(actionListener);
        evenlyRandBtn = new JButton("Random Evenly");
        evenlyRandBtn.addActionListener(actionListener);
        circRandBtn = new JButton("Circles");
        circRandBtn.addActionListener(actionListener);
        startBtn = new JButton("Start");
        startBtn.addActionListener(actionListener);
        stopBtn = new JButton("Stop");
        stopBtn.addActionListener(actionListener);
        recrystBtn = new JButton("Dynamic Recrystallization");
        recrystBtn.addActionListener(actionListener);
        periodicBox = new JCheckBox("Periodic", true);
        periodicBox.addActionListener(actionListener);
        continuousBox = new JCheckBox("Continuous growth", false);
        continuousBox.addActionListener(actionListener);

        JPanel list = new JPanel();
        JPanel neighbours = new JPanel();
        neighbours.add(surroundList);
        
        list.add(cellLabel);
        list.add(cellField);
        list.add(periodicBox);
        list.add(continuousBox);
        list.add(radiusLabel);
        list.add(radiusField);
        list.add(cellSizeLabel);
        list.add(cellSizeField);
        
        controls.add(randBtn);
        controls.add(evenlyRandBtn);
        controls.add(circRandBtn);
        controls.add(startBtn);
        controls.add(stopBtn);
        controls.add(recrystBtn);
        
        content.add(neighbours, BorderLayout.WEST);
        content.add(list, BorderLayout.NORTH);
        content.add(controls, BorderLayout.SOUTH);

        frame.setSize(785, 750);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
