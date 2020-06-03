package fr.iut.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.iut.tetris.controllers.CreditController;
import fr.iut.tetris.controllers.MenuController;
import fr.iut.tetris.controllers.SoloController;
import fr.iut.tetris.models.CreditModel;
import fr.iut.tetris.models.MenuModel;
import fr.iut.tetris.models.SoloModel;
import fr.iut.tetris.vues.CreditVue;
import fr.iut.tetris.vues.MenuVue;
import fr.iut.tetris.vues.SoloVue;

public class MainController implements ActionListener {
	MainVue mainVue;

	MenuModel menuModel;
	CreditModel creditModel;
	SoloModel soloModel;
	
	MenuController menuCtrl;
	CreditController creditCtrl;
	SoloController soloCtrl;
	
	MenuVue menuVue;
	CreditVue creditVue;
	SoloVue soloVue;


	public MainController() {
		mainVue = new MainVue();

		menuModel = new MenuModel();
		creditModel = new CreditModel();
		soloModel = new SoloModel();
		
		menuCtrl = new MenuController(this, menuModel);
		creditCtrl = new CreditController(this, creditModel);
		soloCtrl = new SoloController(this, soloModel);
		
		menuVue = new MenuVue(menuModel, menuCtrl);
		creditVue = new CreditVue(creditModel, creditCtrl);
		soloVue = new SoloVue(soloModel, soloCtrl);
		
		
		// gameVue = new GameVue();

		mainVue.setCurrentVue(menuVue);
		mainVue.display();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand() ) {
		
			case "CLICK:MENU:SOLO":
				soloModel = new SoloModel();
				soloVue = new SoloVue(soloModel, soloCtrl);
				mainVue.setCurrentVue(soloVue);
				break;
		
			case "CLICK:MENU:CREDIT":
				creditVue = new CreditVue(creditModel, creditCtrl);
				mainVue.setCurrentVue(creditVue);
				break;
			case "CLICK:CREDIT:BACK":
				menuVue = new MenuVue(menuModel, menuCtrl);
				mainVue.setCurrentVue(menuVue);
				break;
			case "CLICK:MENU:QUIT":
				System.exit(0);
				break;
			default:
				break;
		}
	}
}
