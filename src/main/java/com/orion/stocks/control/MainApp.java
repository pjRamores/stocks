package com.orion.stocks.control;

import java.io.IOException;

import com.orion.stocks.core.dao.PositionsDao;
import com.orion.stocks.core.dao.TickersDao;
import com.orion.stocks.core.model.Dividend;
import com.orion.stocks.core.model.Position;
import com.orion.stocks.core.model.Ticker;
import com.orion.stocks.view.DividendDialogController;
import com.orion.stocks.view.PositionDialogController;
import com.orion.stocks.view.RootController;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private RootController rootController;

    private ObservableList<Ticker> tickers = FXCollections.observableArrayList(ticker -> new Observable[]{ticker.getCurrentPrice()});
    private ObservableList<Position> openPositions = FXCollections.observableArrayList();

    public MainApp() {
        tickers.addAll(TickersDao.getTickersAsList());
        openPositions.addAll(PositionsDao.getOpenPositions());
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("My Stocks Portfolio");

        setUserAgentStylesheet(STYLESHEET_CASPIAN);
//        setUserAgentStylesheet(STYLESHEET_MODENA);

        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Load sub-controllers
            rootController = loader.getController();
            rootController.setMainApp(this);
            rootController.getOpenPositionsPaneController().setMainApp(this);
            rootController.getTickersController().setMainApp(this);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to add or edit details for the specified position.
     * <br> If the user clicks OK, the changes are saved into the provided position object and true is returned.
     * 
     * @param position - the position object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPositionDialog(Position position) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/PositionDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle(position == null ? "New Position" : "Edit Position");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the position into the controller.
            PositionDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            controller.setPosition(position);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Opens a dialog to add or edit dividend details.
     * <br>If the user clicks OK, the changes are saved into the provided position object and true is returned.
     * 
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showDividendDialog(Dividend dividend) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/DividendDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Dividend");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the position into the controller.
            DividendDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            controller.setDividend(dividend);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public RootController getRootController() {
        return rootController;
    }

    /**
     * Returns the data as an observable list of Position.
     * 
     * @return
     */
    public ObservableList<Position> getOpenPositions() {
        return openPositions;
    }

    /**
     * Returns the data as an observable list of Ticker.
     * 
     * @return
     */
    public ObservableList<Ticker> getTickers() {
        return tickers;
    }

    /**
     * Returns the main stage.
     * 
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
