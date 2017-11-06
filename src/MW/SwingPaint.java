/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MW;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Leszek
 */
public class SwingPaint {

	JButton randBtn, startBtn, stopBtn, recrystBtn, clearBtn;
	JTextField cellField, cellSizeField, inclusionSizeField, probabilityField;
	JLabel cellLabel, cellSizeLabel, inclusionSizeLabel, inclusionRoundLabel, probabilityLabel;
	JCheckBox periodicBox, inclusionRoundBox, phase2Box;
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem;

	DrawArea drawArea;

	ActionListener actionListener = new ActionListener() {
		Thread thread = new Thread();

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == clearBtn) {
				drawArea.clearTables();
			}
			if (e.getSource() == randBtn) {
				if (Integer.parseInt(cellSizeField.getText()) > 9) {
					cellSizeField.setText(Integer.toString(9));
				}
				drawArea.size = Integer.parseInt(cellSizeField.getText());
				if (drawArea.phase2) {
					drawArea.random(Integer.parseInt(cellField.getText()), 2);
				}
				else {
					drawArea.random(Integer.parseInt(cellField.getText()), 0);
				}
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
			if (e.getSource() == inclusionRoundBox) {
				drawArea.inclusionRound = inclusionRoundBox.isSelected();
			}
			if (e.getSource() == phase2Box) {
				drawArea.phase2 = phase2Box.isSelected();
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

		cellLabel = new JLabel("Number of cells:");
		cellField = new JTextField("1000");
		cellSizeLabel = new JLabel("Cell Size:");
		cellSizeField = new JTextField("1");
		clearBtn = new JButton("Clear");
		clearBtn.addActionListener(actionListener);
		randBtn = new JButton("Random");
		randBtn.addActionListener(actionListener);
		startBtn = new JButton("Start");
		startBtn.addActionListener(actionListener);
		stopBtn = new JButton("Stop");
		stopBtn.addActionListener(actionListener);
		recrystBtn = new JButton("Dynamic Recrystallization");
		recrystBtn.addActionListener(actionListener);
		periodicBox = new JCheckBox("Periodic", true);
		periodicBox.addActionListener(actionListener);
		inclusionSizeLabel = new JLabel("Inclusion size:");
		inclusionSizeField = new JTextField("10");
		inclusionRoundBox = new JCheckBox("Inclusion Round", false);
		inclusionRoundBox.addActionListener(actionListener);
		probabilityLabel = new JLabel("Probability:");
		probabilityField = new JTextField("10");
		phase2Box = new JCheckBox("Phase 2", false);
		phase2Box.addActionListener(actionListener);

		inclusionSizeField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateInclusionSize();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				updateInclusionSize();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
			}

			void updateInclusionSize() {
				drawArea.inclusionSize = Integer.parseInt(inclusionSizeField.getText());
			}
		});

		cellSizeField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateCellSize();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				updateCellSize();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
			}

			void updateCellSize() {
				drawArea.size = Integer.parseInt(cellSizeField.getText());
			}
		});

		probabilityField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateProbability();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				updateProbability();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
			}

			void updateProbability() {
				drawArea.rule4Probability = Integer.parseInt(probabilityField.getText());
			}
		});

		menuBar = new JMenuBar();
		menu = new JMenu("File");
		menuBar.add(menu);
		menuItem = new JMenuItem("Export");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					drawArea.importToFile();
				} catch (FileNotFoundException ex) {
					Logger.getLogger(SwingPaint.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

		});
		menu.add(menuItem);
		menuItem = new JMenuItem("Import");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					drawArea.exportFromFile();
				} catch (FileNotFoundException ex) {
					Logger.getLogger(SwingPaint.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

		});
		menu.add(menuItem);
		menuItem = new JMenuItem("exportToBMP");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					drawArea.importToBMP();
				} catch (FileNotFoundException ex) {
					Logger.getLogger(SwingPaint.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		menu.add(menuItem);

		menuItem = new JMenuItem("importFromBMP");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					drawArea.exportFromBMP();
				} catch (FileNotFoundException ex) {
					Logger.getLogger(SwingPaint.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		menu.add(menuItem);

		JPanel list = new JPanel();

		list.add(cellLabel);
		list.add(cellField);
		list.add(periodicBox);
		list.add(cellSizeLabel);
		list.add(cellSizeField);
		list.add(inclusionSizeLabel);
		list.add(inclusionSizeField);
		list.add(inclusionRoundBox);
		list.add(probabilityLabel);
		list.add(probabilityField);

		controls.add(clearBtn);
		controls.add(randBtn);
		controls.add(startBtn);
		controls.add(stopBtn);
		controls.add(phase2Box);
		// controls.add(recrystBtn);

		content.add(list, BorderLayout.NORTH);
		content.add(controls, BorderLayout.SOUTH);

		frame.setJMenuBar(menuBar);
		frame.setSize(650, 750);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
