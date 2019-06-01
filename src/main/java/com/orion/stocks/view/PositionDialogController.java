package com.orion.stocks.view;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.stream.Collectors;

import com.orion.stocks.control.MainApp;
import com.orion.stocks.core.dao.PositionsDao;
import com.orion.stocks.core.dao.TickersDao;
import com.orion.stocks.core.enums.BROKER;
import com.orion.stocks.core.enums.POSITION_STATUS;
import com.orion.stocks.core.model.Position;
import com.orion.stocks.util.DateUtil;
import com.orion.stocks.util.Formatter;
import com.orion.stocks.util.MoneyUtil;
import com.orion.stocks.util.Util;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class PositionDialogController {

    @FXML
    private ComboBox<String> statusField;

    @FXML
    private ComboBox<String> brokerField;

    @FXML
    private ComboBox<String> stockCodeField;

    @FXML
    private TextField sharesField;

    @FXML
    DatePicker dateBoughtDatePicker = new DatePicker();

    @FXML
    private TextField buyPriceField;

    @FXML
    private TextField buyCommissionField;

    @FXML
    private TextField buyVATField;

    @FXML
    private TextField buyTransFeeField;

    @FXML
    private TextField buySCCPField;

    @FXML
    private TextField totalExpensesField;

    @FXML
    DatePicker dateSoldDatePicker = new DatePicker();

    @FXML
    private TextField sellPriceField;

    @FXML
    private TextField sellCommissionField;

    @FXML
    private TextField sellVATField;

    @FXML
    private TextField sellTransFeeField;

    @FXML
    private TextField sellSCCPField;

    @FXML
    private TextField salesTaxField;

    @FXML
    private TextField netSaleField;

    @FXML
    private TextField netGainField;

    // Reference to the main application.
    private MainApp mainApp = null;

    private Stage dialogStage;
    private Position position;
    private boolean okClicked = false;
    private boolean isNew = false;

    private ChangeListener<Boolean> updateBuyTransactionFeesListener = (observable, oldValue, newValue) -> {
        if(!newValue) {
            updateBuyTransactionFees(true);
        }
    };

    private ChangeListener<Boolean> updateSellTransactionFeesListener = (observable, oldValue, newValue) -> {
        if(!newValue) {
            updateSellTransactionFees(true);
        }
    };

    private EventHandler<KeyEvent> updateBuyTransactionFeesEventHandler = e -> updateBuyTransactionFees(false);

    private EventHandler<KeyEvent> updateSellTransactionFeesEventHandler = e -> updateSellTransactionFees(false);

    /***
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        this.okClicked = false;

        this.statusField.getItems().addAll(POSITION_STATUS.getStatusList());
        this.statusField.setOnAction(e -> handleStatusUpdate());

        this.brokerField.getItems().addAll(BROKER.getBrokers());

        this.stockCodeField.getItems().addAll(TickersDao.getTickers().values().stream().map(t -> t.getStockCode().getValue()).collect(Collectors.toList()));

        this.sharesField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.sharesField, newValue));
        this.sharesField.focusedProperty().addListener(updateBuyTransactionFeesListener);
        this.sharesField.focusedProperty().addListener(updateSellTransactionFeesListener);
        this.sharesField.addEventHandler(KeyEvent.KEY_RELEASED, updateBuyTransactionFeesEventHandler);
        this.sharesField.addEventHandler(KeyEvent.KEY_RELEASED, updateSellTransactionFeesEventHandler);

        this.dateBoughtDatePicker.setConverter(DateUtil.dateConverter);

        this.buyPriceField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.buyPriceField, newValue));
        this.buyPriceField.focusedProperty().addListener(updateBuyTransactionFeesListener);
        this.buyPriceField.addEventHandler(KeyEvent.KEY_RELEASED, updateBuyTransactionFeesEventHandler);

        this.buyCommissionField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.buyCommissionField, newValue));

        this.buyVATField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.buyVATField, newValue));

        this.buyTransFeeField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.buyTransFeeField, newValue));

        this.buySCCPField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.buySCCPField, newValue));

        this.totalExpensesField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.totalExpensesField, newValue));

        this.dateSoldDatePicker.setConverter(DateUtil.dateConverter);

        this.sellPriceField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.sellPriceField, newValue));
        this.sellPriceField.focusedProperty().addListener(updateSellTransactionFeesListener);
        this.sellPriceField.addEventHandler(KeyEvent.KEY_RELEASED, updateSellTransactionFeesEventHandler);

        this.sellCommissionField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.sellCommissionField, newValue));

        this.sellVATField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.sellVATField, newValue));

        this.sellTransFeeField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.sellTransFeeField, newValue));

        this.sellSCCPField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.sellSCCPField, newValue));

        this.salesTaxField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.salesTaxField, newValue));

        this.netSaleField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.netSaleField, newValue));

        this.netGainField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.netGainField, newValue));
    }

    /**
     * Sets the position to be edited in the dialog.
     * 
     * @param position
     */
    public void setPosition(Position position) {
        this.position = position;
        this.isNew = position == null;

        if(position != null) {
            this.statusField.setValue(position.getStatus().getValue());

            this.brokerField.setValue(position.getBroker().getValue());

            this.stockCodeField.setValue(position.getStockCode().getValue());

            this.sharesField.setText(Util.shareNumberDisplayFormat.format(position.getShares().getValue()));

            this.dateBoughtDatePicker.setValue(position.getDateBought());

            this.buyPriceField.setText(Util.moneyDisplayFormat.format(position.getBuyPrice().getValue()));

            this.buyCommissionField.setText(Util.moneyDisplayFormat.format(position.getBuyCommission().getValue()));
            this.buyCommissionField.setDisable(true);

            this.buyVATField.setText(Util.moneyDisplayFormat.format(position.getBuyVat().getValue()));
            this.buyVATField.setDisable(true);

            this.buyTransFeeField.setText(Util.moneyDisplayFormat.format(position.getBuyTransFee().getValue()));
            this.buyTransFeeField.setDisable(true);

            this.buySCCPField.setText(Util.moneyDisplayFormat.format(position.getBuySccp().getValue()));
            this.buySCCPField.setDisable(true);

            this.totalExpensesField.setText(Util.moneyDisplayFormat.format(position.getTotalExpenses().getValue()));
            this.totalExpensesField.setDisable(true);
        } else {
            // set default values
            this.statusField.setValue(POSITION_STATUS.OPEN.name());
            this.brokerField.setValue(BROKER.COL.name());
            this.dateBoughtDatePicker.setValue(LocalDate.now());
        }

        if(position != null && POSITION_STATUS.CLOSED.name().equals(position.getStatus().getValue())) {
//          this.dateSoldField.setText(position.getDateSold().toString("yyyy-MM-dd"));
//          this.sellPriceField.setText(position.getSellPrice().getValue());
//          this.sellCommissionField.setText(position.getSellCommission().getValue());
//          this.sellVATField.setText(position.getSellVat().getValue());
//          this.sellTransFeeField.setText(position.getSellTransFee().getValue());
//          this.sellSCCPField.setText(position.getSellSccp().getValue());
//          this.salesTaxField.setText(position.getSalesTax().getValue());
//          this.netSaleField.setText(position.getNetSale().getValue());
//          this.netGainField.setText(position.getNetGain().getValue());
        } else {
            this.dateSoldDatePicker.setDisable(true);
            this.sellPriceField.setDisable(true);
        }

        this.buyCommissionField.setDisable(true);
        this.buyVATField.setDisable(true);
        this.buyTransFeeField.setDisable(true);
        this.buySCCPField.setDisable(true);
        this.totalExpensesField.setDisable(true);

        this.sellCommissionField.setDisable(true);
        this.sellVATField.setDisable(true);
        this.sellTransFeeField.setDisable(true);
        this.sellSCCPField.setDisable(true);
        this.salesTaxField.setDisable(true);
        this.netSaleField.setDisable(true);
        this.netGainField.setDisable(true);

        if(this.position == null) {
            this.position = new Position();
        }
    }

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     * 
     * @return
     */
    public boolean isOkClicked() {
        return this.okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        /* fields validation */
        boolean hasError = false;

        if(Util.isBlankOrNull(this.stockCodeField.getValue())) {
            final ContextMenu validator = new ContextMenu();
            validator.getItems().add(new MenuItem("This field is mandatory"));
            validator.getStyleClass().add("alert");
            validator.show(this.stockCodeField, Side.RIGHT, 10, 0);
            hasError = true;
        }

        if(Util.isBlankOrNull(this.sharesField.getText())) {
            final ContextMenu validator = new ContextMenu();
            validator.getItems().add(new MenuItem("This field is mandatory"));
            validator.getStyleClass().add("alert");
            validator.show(this.sharesField, Side.RIGHT, 10, 0);
            hasError = true;
        }

        if(Util.isBlankOrNull(this.buyPriceField.getText())) {
            final ContextMenu validator = new ContextMenu();
            validator.getItems().add(new MenuItem("This field is mandatory"));
            validator.getStyleClass().add("alert");
            validator.show(this.buyPriceField, Side.RIGHT, 10, 0);
            hasError = true;
        }

        if(Util.isBlankOrNull(this.sellPriceField.getText()) && POSITION_STATUS.CLOSED.name().equals(this.statusField.getValue())) {
            final ContextMenu validator = new ContextMenu();
            validator.getItems().add(new MenuItem("This field is mandatory"));
            validator.getStyleClass().add("alert");
            validator.show(this.sellPriceField, Side.RIGHT, 10, 0);
            hasError = true;
        }

        if(hasError) {
            return;
        }

        this.position.setStatus(this.statusField.getValue());
        this.position.setBroker(this.brokerField.getValue());
        this.position.setStockCode(this.stockCodeField.getValue());
        try {
            this.position.setShares(Util.shareNumberDisplayFormat.parse(this.sharesField.getText()).intValue());
            this.position.setDateBought(this.dateBoughtDatePicker.getValue());
            this.position.setBuyPrice(Util.moneyDisplayFormat.parse(this.buyPriceField.getText()).doubleValue());
            this.position.setBuyCommission(Util.moneyDisplayFormat.parse(this.buyCommissionField.getText()).doubleValue());
            this.position.setBuyVat(Util.moneyDisplayFormat.parse(this.buyVATField.getText()).doubleValue());
            this.position.setBuyTransFee(Util.moneyDisplayFormat.parse(this.buyTransFeeField.getText()).doubleValue());
            this.position.setBuySccp(Util.moneyDisplayFormat.parse(this.buySCCPField.getText()).doubleValue());
            this.position.setTotalExpenses(Util.moneyDisplayFormat.parse(this.totalExpensesField.getText()).doubleValue());
        } catch(ParseException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(POSITION_STATUS.CLOSED.name().equals(this.statusField.getValue())) {
            this.position.setDateSold(this.dateSoldDatePicker.getValue());
            try {
                this.position.setSellPrice(Util.moneyDisplayFormat.parse(this.sellPriceField.getText()).doubleValue());
                this.position.setSellCommission(Util.moneyDisplayFormat.parse(this.sellCommissionField.getText()).doubleValue());
                this.position.setSellVat(Util.moneyDisplayFormat.parse(this.sellVATField.getText()).doubleValue());
                this.position.setSellTransFee(Util.moneyDisplayFormat.parse(this.sellTransFeeField.getText()).doubleValue());
                this.position.setSellSccp(Util.moneyDisplayFormat.parse(this.sellSCCPField.getText()).doubleValue());
                this.position.setSalesTax(Util.moneyDisplayFormat.parse(this.salesTaxField.getText()).doubleValue());
                this.position.setNetSale(Util.moneyDisplayFormat.parse(this.netSaleField.getText()).doubleValue());
                this.position.setNetGain(Util.moneyDisplayFormat.parse(this.netGainField.getText()).doubleValue());
            } catch(ParseException e) {
                e.printStackTrace();
            }

            this.mainApp.getOpenPositions().remove(position);
        } else if(this.isNew) {
            this.mainApp.getOpenPositions().add(position);
        }

        PositionsDao.savePosition(position);

        this.okClicked = true;

        dialogStage.close();
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private void updateBuyTransactionFees(boolean format) {
        if(!Util.isBlankOrNull(this.buyPriceField.getText()) && !Util.isBlankOrNull(this.sharesField.getText())) {
            this.position = MoneyUtil.calculateBuyFees(this.position, this.buyPriceField.getText(), this.sharesField.getText());

            this.buyCommissionField.setText(Util.moneyDisplayFormat.format(this.position.getBuyCommission().doubleValue()));
            this.buyVATField.setText(Util.moneyDisplayFormat.format(this.position.getBuyVat().doubleValue()));
            this.buyTransFeeField.setText(Util.moneyDisplayFormat.format(this.position.getBuyTransFee().doubleValue()));
            this.buySCCPField.setText(Util.moneyDisplayFormat.format(this.position.getBuySccp().doubleValue()));
            this.totalExpensesField.setText(Util.moneyDisplayFormat.format(this.position.getTotalExpenses().doubleValue()));
        }

        if(format && !Util.isBlankOrNull(this.buyPriceField.getText())) {
            try {
                this.buyPriceField.setText(Util.moneyDisplayFormat.format(Util.moneyDisplayFormat.parse(this.buyPriceField.getText())));
            } catch(ParseException e) {
                e.printStackTrace();
            }
        }

        if(format && !Util.isBlankOrNull(this.sharesField.getText())) {
            try {
                this.sharesField.setText(Util.shareNumberDisplayFormat.format(Util.shareNumberDisplayFormat.parse(this.sharesField.getText())));
            } catch(ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSellTransactionFees(boolean format) {
        if(!Util.isBlankOrNull(this.sellPriceField.getText()) && !Util.isBlankOrNull(this.sharesField.getText())) {
            this.position = MoneyUtil.calculateSellFees(this.position, this.sellPriceField.getText(), this.sharesField.getText());

            this.sellCommissionField.setText(Util.moneyDisplayFormat.format(this.position.getSellCommission().doubleValue()));
            this.sellVATField.setText(Util.moneyDisplayFormat.format(this.position.getSellVat().doubleValue()));
            this.sellTransFeeField.setText(Util.moneyDisplayFormat.format(this.position.getSellTransFee().doubleValue()));
            this.sellSCCPField.setText(Util.moneyDisplayFormat.format(this.position.getSellSccp().doubleValue()));
            this.salesTaxField.setText(Util.moneyDisplayFormat.format(this.position.getSalesTax().doubleValue()));
            this.netSaleField.setText(Util.moneyDisplayFormat.format(this.position.getNetSale().doubleValue()));
            this.netGainField.setText(Util.moneyDisplayFormat.format(this.position.getNetGain().doubleValue()));
        }

        if(format && !Util.isBlankOrNull(this.sellPriceField.getText())) {
            try {
                this.sellPriceField.setText(Util.moneyDisplayFormat.format(Util.moneyDisplayFormat.parse(this.sellPriceField.getText())));
            } catch(ParseException e) {
                e.printStackTrace();
            }
        }

        if(format && !Util.isBlankOrNull(this.sharesField.getText())) {
            try {
                this.sharesField.setText(Util.shareNumberDisplayFormat.format(Util.shareNumberDisplayFormat.parse(this.sharesField.getText())));
            } catch(ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleStatusUpdate() {
        switch(this.statusField.getSelectionModel().getSelectedItem()) {
            case "OPEN" :
                if(this.dateSoldDatePicker.getValue() != null) {
                    this.position.setDateSold(this.dateSoldDatePicker.getValue()); //remember old value
                }

                this.dateSoldDatePicker.setValue(null);
                this.sellPriceField.setText(null);
                this.sellCommissionField.setText(null);
                this.sellVATField.setText(null);
                this.sellTransFeeField.setText(null);
                this.sellSCCPField.setText(null);
                this.salesTaxField.setText(null);
                this.netSaleField.setText(null);
                this.netGainField.setText(null);

                this.dateSoldDatePicker.setDisable(true);
                this.sellPriceField.setDisable(true);
                break;
            case "CLOSED" :
                if(this.position != null) {
                    /* restore old value if available */
                    this.dateSoldDatePicker.setValue(this.position.getDateSold() == null ? LocalDate.now() : this.position.getDateSold());
                    this.sellPriceField.setText(this.position.getSellPrice() == null ? null : Util.moneyDisplayFormat.format(this.position.getSellPrice().doubleValue()));
                    this.sellCommissionField.setText(this.position.getSellCommission() == null ? null : Util.moneyDisplayFormat.format(this.position.getSellCommission().doubleValue()));
                    this.sellVATField.setText(this.position.getSellVat() == null ? null : Util.moneyDisplayFormat.format(this.position.getSellVat().doubleValue()));
                    this.sellTransFeeField.setText(this.position.getSellTransFee() == null ? null : Util.moneyDisplayFormat.format(this.position.getSellTransFee().doubleValue()));
                    this.sellSCCPField.setText(this.position.getSellSccp() == null ? null : Util.moneyDisplayFormat.format(this.position.getSellSccp().doubleValue()));
                    this.salesTaxField.setText(this.position.getSalesTax() == null ? null : Util.moneyDisplayFormat.format(this.position.getSalesTax().doubleValue()));
                    this.netSaleField.setText(this.position.getNetSale() == null ? null : Util.moneyDisplayFormat.format(this.position.getNetSale().doubleValue()));
                    this.netGainField.setText(this.position.getNetGain() == null ? null : Util.moneyDisplayFormat.format(this.position.getNetGain().doubleValue()));
                }

                this.dateSoldDatePicker.setDisable(false);
                this.sellPriceField.setDisable(false);
                break;
        }
    }
}
