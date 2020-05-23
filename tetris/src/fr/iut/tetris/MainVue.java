package fr.iut.tetris;

import fr.iut.tetris.vues.MenuVue;

import javax.swing.*;

public class MainVue extends JFrame {
	JPanel currrentVue;

	public MainVue() {
		setTitle("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}

	public void setCurrentVue(JPanel p) {
		this.currrentVue = p;

		getContentPane().removeAll();
		setContentPane(this.currrentVue);
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
