package fr.iut.tetris;

import fr.iut.tetris.vues.MenuVue;

import javax.swing.*;

public class MainVue extends JFrame {
	JPanel currentVue;

	public MainVue() {
		setTitle("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}

	public void setCurrentVue(JPanel p) {
		this.currentVue = p;

		getContentPane().removeAll();
		setContentPane(this.currentVue);
		validate();
		pack();
		setLocationRelativeTo(null);
	}

	public void display() {
		setVisible(true);
	}
	public void undisplay() {
		setVisible(false);
	}
}
