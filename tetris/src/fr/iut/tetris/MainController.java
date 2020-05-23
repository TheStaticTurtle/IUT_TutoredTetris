package fr.iut.tetris;

import fr.iut.tetris.controllers.MenuController;
import fr.iut.tetris.models.MenuModel;
import fr.iut.tetris.vues.MenuVue;

public class MainController {
	MainVue mainVue;

	MenuModel menuModel;
	MenuController menuCtrl;

	MenuVue menuVue;

	public MainController() {
		mainVue = new MainVue();

		menuModel = new MenuModel();
		menuCtrl = new MenuController(menuModel);

		menuVue = new MenuVue(menuModel, menuCtrl);
		//gameVue = new GameVue();

		mainVue.setCurrentVue(menuVue);
		mainVue.display();
	}
}
