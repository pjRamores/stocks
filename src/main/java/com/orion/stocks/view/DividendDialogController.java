package com.orion.stocks.view;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.stream.Collectors;

import com.orion.stocks.control.MainApp;
import com.orion.stocks.core.dao.DividendsDao;
import com.orion.stocks.core.dao.PositionsDao;
import com.orion.stocks.core.dao.TickersDao;
import com.orion.stocks.core.enums.BROKER;
import com.orion.stocks.core.enums.DIVIDEND_TYPE;
import com.orion.stocks.core.enums.POSITION_STATUS;
import com.orion.stocks.core.enums.TRANSACTION_TYPE;
import com.orion.stocks.core.model.Dividend;
import com.orion.stocks.core.model.Position;
import com.orion.stocks.util.DateUtil;
import com.orion.stocks.util.Formatter;
import com.orion.stocks.util.MoneyUtil;
import com.orion.stocks.util.Util;

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

public class DividendDialogController {

    @FXML
    private ComboBox<String> typeField;

    @FXML
    private ComboBox<String> brokerField;

    @FXML
    private ComboBox<String> stockCodeField;

    @FXML
    private TextField sharesField;

    @FXML
    private DatePicker paymentDatePicker = new DatePicker();

    @FXML
    private TextField openingPriceField;

    @FXML
    private TextField cashDividendField;

    @FXML
    private TextField grossAmountField;

    @FXML
    private TextField withholdingTaxField;

    @FXML
    private TextField netAmountField;

    // Reference to the main application.
    private MainApp mainApp = null;

    private Stage dialogStage;
    private Dividend dividend;
    private Position position;
    private boolean okClicked = false;

    private EventHandler<KeyEvent> updateCashDividendFieldAmountsEventHandler = e -> updateCashDividendFieldAmounts();

    public DividendDialogController() {

    }

    /***
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        this.okClicked = false;

        this.typeField.getItems().addAll(DIVIDEND_TYPE.getTypes());
        this.typeField.setOnAction(e -> handleTypeUpdate());

        this.brokerField.getItems().addAll(BROKER.getBrokers());

        this.stockCodeField.getItems().addAll(TickersDao.getTickers().values().stream().map(t -> t.getStockCode().getValue()).collect(Collectors.toList()));

        this.sharesField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.sharesField, newValue));
        this.sharesField.addEventHandler(KeyEvent.KEY_RELEASED, updateCashDividendFieldAmountsEventHandler);

        this.paymentDatePicker.setConverter(DateUtil.dateConverter);

        this.openingPriceField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.openingPriceField, newValue));

        this.cashDividendField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.cashDividendField, newValue));
        this.cashDividendField.addEventHandler(KeyEvent.KEY_RELEASED, updateCashDividendFieldAmountsEventHandler);

        this.grossAmountField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.grossAmountField, newValue));

        this.withholdingTaxField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.withholdingTaxField, newValue));

        this.netAmountField.textProperty().addListener((observable, oldValue, newValue) -> Formatter.setTextFieldToNumericValue(this.netAmountField, newValue));
    }

    public void setDividend(Dividend dividend) {
        this.dividend = dividend;

        if(dividend != null) {
            this.typeField.setDisable(true); // only cash dividend can be updated

            this.sharesField.setText(Util.shareNumberDisplayFormat.format(dividend.getShares().getValue()));

            this.paymentDatePicker.setValue(dividend.getPaymentDate());

            this.openingPriceField.setText(Util.moneyDisplayFormat.format(dividend.getOpeningPrice().getValue()));

            this.cashDividendField.setText(Util.moneyDisplayFormat.format(dividend.getCashDividend().getValue()));

            this.grossAmountField.setText(Util.moneyDisplayFormat.format(dividend.getGrossAmount().getValue()));

            this.withholdingTaxField.setText(Util.moneyDisplayFormat.format(dividend.getWithholdingTax().getValue()));

            this.netAmountField.setText(Util.moneyDisplayFormat.format(dividend.getNetAmount().getValue()));
        } else {
            this.typeField.setValue(DIVIDEND_TYPE.CASH.name());
            this.brokerField.setValue(BROKER.COL.name());
            this.paymentDatePicker.setValue(LocalDate.now());
        }

        if(this.dividend == null) {
            this.dividend = new Dividend();
        }

        this.openingPriceField.setDisable(true);
        this.grossAmountField.setDisable(true);
        this.withholdingTaxField.setDisable(true);
        this.netAmountField.setDisable(true);
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
        /** fields validation */
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

        switch(this.typeField.getValue()) {
            case "CASH" :
                if(Util.isBlankOrNull(this.cashDividendField.getText())) {
                    final ContextMenu validator = new ContextMenu();
                    validator.getItems().add(new MenuItem("This field is mandatory"));
                    validator.getStyleClass().add("alert");
                    validator.show(this.cashDividendField, Side.RIGHT, 10, 0);
                    hasError = true;
                }
                break;
            case "SHARE" :
                if(Util.isBlankOrNull(this.openingPriceField.getText())) {
                    final ContextMenu validator = new ContextMenu();
                    validator.getItems().add(new MenuItem("This field is mandatory"));
                    validator.getStyleClass().add("alert");
                    validator.show(this.openingPriceField, Side.RIGHT, 10, 0);
                    hasError = true;
                }
                break;
        }

        if(hasError) {
            return;
        }

        switch(this.typeField.getValue()) {
            case "CASH" :
                try {
                    this.dividend.setBroker(this.brokerField.getValue());
                    this.dividend.setStockCode(this.stockCodeField.getValue());
                    this.dividend.setShares(Util.shareNumberDisplayFormat.parse(this.sharesField.getText()).intValue());
                    this.dividend.setPaymentDate(this.paymentDatePicker.getValue());
                    this.dividend.setCashDividend(Util.shareNumberDisplayFormat.parse(this.cashDividendField.getText()).doubleValue());
                    this.dividend.setGrossAmount(Util.shareNumberDisplayFormat.parse(this.grossAmountField.getText()).doubleValue());
                    this.dividend.setWithholdingTax(Util.shareNumberDisplayFormat.parse(this.withholdingTaxField.getText()).doubleValue());
                    this.dividend.setNetAmount(Util.shareNumberDisplayFormat.parse(this.netAmountField.getText()).doubleValue());
                } catch(ParseException e) {
                    e.printStackTrace();
                }

                DividendsDao.saveDividend(this.dividend);

                break;
            case "SHARE" :
                if(this.position == null) {
                    this.position = new Position();
                }

                try {
                    this.position.setStatus(POSITION_STATUS.OPEN.name());
                    this.position.setBroker(this.brokerField.getValue());
                    this.position.setStockCode(this.stockCodeField.getValue());
                    this.position.setTransactionType(TRANSACTION_TYPE.DIVIDEND.name());
                    this.position.setShares(Util.shareNumberDisplayFormat.parse(this.sharesField.getText()).intValue());
                    this.position.setDateBought(this.paymentDatePicker.getValue());
                    this.position.setBuyPrice(Util.moneyDisplayFormat.parse(this.openingPriceField.getText()).doubleValue());
                    this.position.setTotalExpenses(Util.moneyDisplayFormat.parse("0.0").doubleValue());

                    this.mainApp.getOpenPositions().add(position);
                } catch(ParseException e) {
                    e.printStackTrace();
                }

                PositionsDao.savePosition(position);

                break;
        }

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

    private void updateCashDividendFieldAmounts() {
        if(!DIVIDEND_TYPE.CASH.name().equals(this.typeField.getValue())) {
            return;
        }

        if(!Util.isBlankOrNull(this.sharesField.getText()) && !Util.isBlankOrNull(this.cashDividendField.getText())) {
            this.dividend = MoneyUtil.calculateCashDividend(this.dividend, this.sharesField.getText(), this.cashDividendField.getText());

            this.grossAmountField.setText(Util.moneyDisplayFormat.format(this.dividend.getGrossAmount().doubleValue()));
            this.withholdingTaxField.setText(Util.moneyDisplayFormat.format(this.dividend.getWithholdingTax().doubleValue()));
            this.netAmountField.setText(Util.moneyDisplayFormat.format(this.dividend.getNetAmount().doubleValue()));
        }
    }


    private void handleTypeUpdate() {
        switch(this.typeField.getSelectionModel().getSelectedItem()) {
            case "CASH" :
                this.openingPriceField.setText(null);
                this.openingPriceField.setDisable(true);

                this.cashDividendField.setDisable(false);
                break;
            case "SHARE" :
                this.openingPriceField.setDisable(false);

                this.cashDividendField.setText(null);
                this.cashDividendField.setDisable(true);

                this.grossAmountField.setText(null);
                this.withholdingTaxField.setText(null);
                this.netAmountField.setText(null);
                break;
        }
    }
}
