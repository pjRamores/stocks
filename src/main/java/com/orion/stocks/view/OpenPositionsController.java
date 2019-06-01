package com.orion.stocks.view;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.orion.stocks.control.MainApp;
import com.orion.stocks.core.dao.PositionsDao;
import com.orion.stocks.core.dao.TickersDao;
import com.orion.stocks.core.model.Position;
import com.orion.stocks.core.model.Ticker;
import com.orion.stocks.util.DateUtil;
import com.orion.stocks.util.Util;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

public class OpenPositionsController {

    @FXML
    private TableView<Position> openPositionsTable;

    @FXML
    private TableColumn<Position, String> brokerColumn;

    @FXML
    private TableColumn<Position, String> stockCodeColumn;

    @FXML
    private TableColumn<Position, Number> numberOfSharesColumn;

    @FXML
    private TableColumn<Position, String> dateBoughtColumn;

    @FXML
    private TableColumn<Position, Number> buyPriceColumn;

    @FXML
    private TableColumn<Position, Number> totalExpensesColumn;

    @FXML
    private TableColumn<Position, Number> currentPriceColumn;

    @FXML
    private TableColumn<Position, Number> currentGainLossColumn;

    @FXML
    private TableColumn<Position, Number> plusFivePercentColumn;

    @FXML
    private TableColumn<Position, Number> gainColumn;

    @FXML
    private TableColumn<Position, Number> minusFivePercentColumn;

    @FXML
    private TableColumn<Position, Number> lossColumn;

    private Callback<TableColumn<Position, Number>, TableCell<Position, Number>> tableCellMoneyFormatter = p -> new TableCell<Position, Number>() {
        @Override
        public void updateItem(Number value, boolean empty) {
            super.updateItem(value, empty);
            if(value == null) {
                setText(null);
            } else {
                setText(Util.moneyDisplayFormat.format(value.doubleValue()));
            }
        }
    };

    // Reference to the main application.
    private MainApp mainApp = null;

    public OpenPositionsController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        Map<String, Ticker> tickers = TickersDao.getTickers();

        /* this is used for tracking the index of each active ticker */
        List<Position> positions = PositionsDao.getOpenPositions();
        Set<String> activeTickers = new LinkedHashSet<String>();
        positions.forEach(p -> activeTickers.add(p.getStockCode().getValue()));
        List<String> tickerList =  new ArrayList<String>(activeTickers);

        openPositionsTable.setRowFactory(position -> {
            TableRow<Position> row = new TableRow<Position>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())) {
                    Position item = row.getItem();
                    if(mainApp.showPositionDialog(item)) {
                        openPositionsTable.refresh();
                        openPositionsTable.sort();
                    }
                }
            });
            return row;
        });

        brokerColumn.setCellValueFactory(position -> position.getValue().getBroker());

        stockCodeColumn.setCellValueFactory(position -> position.getValue().getStockCode());
        stockCodeColumn.setCellFactory(p -> new TableCell<Position, String>() {
            @Override
            public void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                setText(value);
                getTableRow().getStyleClass().remove("tr-even");
                getTableRow().getStyleClass().remove("tr-odd");

                if(tickerList.indexOf(value) % 2 == 0) {
                    getTableRow().getStyleClass().add("tr-even");
                } else {
                    getTableRow().getStyleClass().add("tr-odd");
                }
            }
        });

        numberOfSharesColumn.setCellValueFactory(position -> position.getValue().getShares());

        dateBoughtColumn.setCellValueFactory(position -> {
            return new SimpleStringProperty(DateUtil.dateFormatter.format(position.getValue().getDateBought()));
        });

        buyPriceColumn.setCellValueFactory(position -> position.getValue().getBuyPrice());
        buyPriceColumn.setCellFactory(tableCellMoneyFormatter);

        totalExpensesColumn.setCellValueFactory(position -> position.getValue().getTotalExpenses());
        totalExpensesColumn.setCellFactory(tableCellMoneyFormatter);

        currentPriceColumn.setCellValueFactory(position -> {
            Ticker ticker = tickers.get(position.getValue().getStockCode().getValue());
            return ticker == null ? null : ticker.getCurrentPrice();
        });
        currentPriceColumn.setCellFactory(p -> new TextFieldTableCell<Position, Number>(new NumberStringConverter()) {
            @Override
            public void updateItem(Number value, boolean empty) {
                TextFieldTableCell<Position, Number> cell = this;
                cell.setTextFill(new Color(0, 0, 1, 1));
                if(!empty) {
                    this.getStyleClass().remove("na");
//                    Util.applyTextFieldTableCellAnimation(RGB.BLUE, this);
                } else {
                    this.getStyleClass().add("na");
                }
                super.updateItem(value, empty);
                if(!empty && value != null) {
                    setText(Util.moneyDisplayFormat.format(value.doubleValue()));
                }
            }
        });

        currentGainLossColumn.setCellValueFactory(position -> {
            Ticker ticker = tickers.get(position.getValue().getStockCode().getValue());
            if(ticker == null) {
                return null;
            }
            final DoubleProperty currentPrice = ticker.getCurrentPrice();
            final DoubleBinding totalCurrentValue = currentPrice.multiply(position.getValue().getShares().getValue());
            final NumberBinding gainLoss = totalCurrentValue.subtract(position.getValue().getTotalExpenses().getValue());
            return gainLoss;
        });
        currentGainLossColumn.setCellFactory(p -> new TextFieldTableCell<Position, Number>(new NumberStringConverter()) {
            @Override
            public void updateItem(Number value, boolean empty) {
                this.setTextFill(new Color(0, 0, 1, 1));
                if(!empty && value != null) {
                    this.getStyleClass().removeAll(new String[]{"na", "gain", "loss"});
                    if(value.doubleValue() > 0) {
                        this.getStyleClass().add("gain");
//                        Util.applyTextFieldTableCellAnimation(RGB.DARK_GREEN, this);
                    } else if(value.doubleValue() < 0) {
                        this.getStyleClass().add("loss");
//                        Util.applyTextFieldTableCellAnimation(RGB.RED, this);
                    } else {
//                        Util.applyTextFieldTableCellAnimation(RGB.BLUE, this);
                    }
                }
                super.updateItem(value, empty);
                if(!empty && value != null) {
                    setText(Util.moneyDisplayFormat.format(value.doubleValue()));
                }
            }
        });

        plusFivePercentColumn.setCellValueFactory(position -> position.getValue().getBuyPrice().multiply(1.05));
        plusFivePercentColumn.setCellFactory(tableCellMoneyFormatter);

        gainColumn.setCellValueFactory(position -> {
            final DoubleBinding plusFivePercent = position.getValue().getBuyPrice().multiply(1.05);
            final DoubleBinding totalPlusFivePercentValue = plusFivePercent.multiply(position.getValue().getShares());
            final DoubleBinding gain = totalPlusFivePercentValue.subtract(position.getValue().getTotalExpenses());
            return gain;
        });
        gainColumn.setCellFactory(tableCellMoneyFormatter);

        minusFivePercentColumn.setCellValueFactory(position -> position.getValue().getBuyPrice().multiply(0.95));
        minusFivePercentColumn.setCellFactory(tableCellMoneyFormatter);

        lossColumn.setCellValueFactory(position -> {
            final DoubleBinding minusFivePercent = position.getValue().getBuyPrice().multiply(0.95);
            final DoubleBinding totalMinusFivePercentValue = minusFivePercent.multiply(position.getValue().getShares());
            final DoubleBinding loss = totalMinusFivePercentValue.subtract(position.getValue().getTotalExpenses());
            return loss;
        });
        lossColumn.setCellFactory(tableCellMoneyFormatter);
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        openPositionsTable.setItems(mainApp.getOpenPositions());

        // set default sorting
        openPositionsTable.getSortOrder().add(stockCodeColumn);
        openPositionsTable.getSortOrder().add(buyPriceColumn);
    }

    public TableView<Position> getOpenPositionsTable() {
        return openPositionsTable;
    }
}
