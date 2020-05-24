package fr.iut.tetris;

import fr.iut.tetris.controllers.CreditController;
import fr.iut.tetris.controllers.MenuController;
import fr.iut.tetris.models.CreditModel;
import fr.iut.tetris.models.MenuModel;
import fr.iut.tetris.vues.CreditVue;
import fr.iut.tetris.vues.MenuVue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainController implements ActionListener {
	MainVue mainVue;

	MenuModel menuModel;
	CreditModel creditModel;

	MenuController menuCtrl;
	CreditController creditCtrl;

	MenuVue menuVue;
	CreditVue creditVue;

	public MainController() {
		mainVue = new MainVue();

		menuModel = new MenuModel();
		creditModel = new CreditModel();

		menuCtrl = new MenuController(this, menuModel);
		creditCtrl = new CreditController(this, creditModel);

		menuVue = new MenuVue(menuModel, menuCtrl);
		creditVue = new CreditVue(creditModel, creditCtrl);
		// gameVue = new GameVue();

		mainVue.setCurrentVue(menuVue);
		mainVue.display();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand() ) {
			case "CLICK:MENU:CREDIT":
				creditVue = new CreditVue(creditModel, creditCtrl);
				mainVue.setCurrentVue(creditVue);
				break;
			case "CLICK:CREDIT:BACK":
				menuVue = new MenuVue(menuModel, menuCtrl);
				mainVue.setCurrentVue(menuVue);
				break;
			default:
				break;
		}
	}
}
