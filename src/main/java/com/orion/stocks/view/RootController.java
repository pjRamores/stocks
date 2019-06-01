package com.orion.stocks.view;

import com.orion.stocks.control.MainApp;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class RootController {

    @FXML
    private AnchorPane openPositions;

    @FXML
    private OpenPositionsController openPositionsController;

    @FXML
    private AnchorPane tickers;

    @FXML
    private TickersController tickersController;

    // Reference to the main application.
    private MainApp mainApp = null;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /*
     * Called when the user clicks new position menu.
     */
    @FXML
    private void createNewPosition() {
        if(mainApp.showPositionDialog(null)) {
            mainApp.getRootController().getOpenPositionsPaneController().getOpenPositionsTable().refresh();
            mainApp.getRootController().getOpenPositionsPaneController().getOpenPositionsTable().sort();
        }
    }

    /*
     * Called when the user clicks new position menu.
     */
    @FXML
    private void createNewDividend() {
        if(mainApp.showDividendDialog(null)) {
            // @TODO - sort transactions > dividends table view

            // sort open positions table view in case dividend share is added
            mainApp.getRootController().getOpenPositionsPaneController().getOpenPositionsTable().refresh();
            mainApp.getRootController().getOpenPositionsPaneController().getOpenPositionsTable().sort();
        }
    }

    /**
     * Called when the user clicks close menu.
     */
    @FXML
    private void handleExitMenu() {
        this.mainApp.getPrimaryStage().close();
    }

    public OpenPositionsController getOpenPositionsPaneController() {
        return openPositionsController;
    }

    public TickersController getTickersController() {
        return tickersController;
    }

}
