package com.orion.stocks.view;

import java.time.LocalDateTime;

import com.orion.stocks.control.MainApp;
import com.orion.stocks.core.dao.TickersDao;
import com.orion.stocks.core.model.Ticker;
import com.orion.stocks.util.DateUtil;
import com.orion.stocks.util.RGB;
import com.orion.stocks.util.StocksUtil;
import com.orion.stocks.util.Util;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.NumberStringConverter;

public class TickersController {

    @FXML
    private Button refreshButton;

    @FXML
    private TableView<Ticker> tickersTable;

    @FXML
    private TableColumn<Ticker, String> stockCodeColumn;

    @FXML
    private TableColumn<Ticker, Number> currentPriceColumn;

    @FXML
    private TableColumn<Ticker, Number> averagePriceColumn;

    @FXML
    private TableColumn<Ticker, String> lastUpdatedDate;

    @FXML
    private TableColumn<Ticker, Number> totalSharesColumn;

    @FXML
    private TableColumn<Ticker, Number> gainLossColumn;

    @FXML
    private TableColumn<Ticker, Number> dailyChangeAmount;

    @FXML
    private TableColumn<Ticker, Number> dailyChangePercent;

    // Reference to the main application.
    private MainApp mainApp = null;

    public TickersController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        tickersTable.setEditable(true);

        stockCodeColumn.setCellValueFactory(ticker -> ticker.getValue().getStockCode());

        currentPriceColumn.setCellValueFactory(ticker -> ticker.getValue().getCurrentPrice());
        currentPriceColumn.setCellFactory(p -> new TextFieldTableCell<Ticker, Number>(new NumberStringConverter()) {
            @Override
            public void updateItem(Number value, boolean empty) {
                TextFieldTableCell<Ticker, Number> cell = this;
                cell.setTextFill(new Color(0, 0, 1, 1));
                if(!empty && value != null) {
                    getTableRow().getStyleClass().remove("tr-even");
                    getTableRow().getStyleClass().remove("tr-odd");
                    if(getIndex() % 2 == 0) {
                        getTableRow().getStyleClass().add("tr-even");
                    } else {
                        getTableRow().getStyleClass().add("tr-odd");
                    }
                    Util.applyTextFieldTableCellAnimation(RGB.BLUE, cell);
                }
                super.updateItem(value, empty);
                if(!empty && value != null) {
                    setText(Util.moneyDisplayFormat.format(value.doubleValue()));
                }
            }
        });
        currentPriceColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<Ticker, Number> t) -> {
                Ticker ticker = (t.getTableView().getItems().get(t.getTablePosition().getRow()));
                ticker.getCurrentPrice().set(t.getNewValue().doubleValue());
            }
        );

        lastUpdatedDate.setCellValueFactory(ticker -> ticker.getValue().getLastUpdatedDate());
        lastUpdatedDate.setCellFactory(p -> new TextFieldTableCell<Ticker, String>(new DefaultStringConverter()) {
            @Override
            public void updateItem(String value, boolean empty) {
                TextFieldTableCell<Ticker, String> cell = this;
                cell.setTextFill(new Color(0, 0, 1, 1));
                if(!empty && value != null) {
                    Util.applyTextFieldTableCellAnimation(RGB.BLUE, cell);
                }
                super.updateItem(value, empty);
                if(!empty && value != null) {
                    setText(value);
                }
            }
        });

        averagePriceColumn.setCellValueFactory(ticker -> ticker.getValue().getAveragePrice());
        averagePriceColumn.setCellFactory(p -> new TextFieldTableCell<Ticker, Number>(new NumberStringConverter()) {
            @Override
            public void updateItem(Number value, boolean empty) {
                this.setTextFill(new Color(0, 0, 1, 1));
                if(!empty && value != null) {
                    Util.applyTextFieldTableCellAnimation(RGB.BLUE, this);
                }
                super.updateItem(value, empty);
                if(!empty && value != null) {
                    setText(Util.moneyDisplayFormat.format(value.doubleValue()));
                }
            }
        });

        totalSharesColumn.setCellValueFactory(ticker -> ticker.getValue().getTotalShares());
        totalSharesColumn.setCellFactory(p -> new TextFieldTableCell<Ticker, Number>(new NumberStringConverter()) {
            @Override
            public void updateItem(Number value, boolean empty) {
                this.setTextFill(new Color(0, 0, 1, 1));
                if(!empty && value != null) {
                    Util.applyTextFieldTableCellAnimation(RGB.BLUE, this);
                }
                super.updateItem(value, empty);
                if(!empty && value != null) {
                    setText(Util.shareNumberDisplayFormat.format(value.doubleValue()));
                }
            }
        });

        gainLossColumn.setCellValueFactory(ticker -> ticker.getValue().getGainLoss());
        gainLossColumn.setCellFactory(p -> new TextFieldTableCell<Ticker, Number>(new NumberStringConverter()) {
            @Override
            public void updateItem(Number value, boolean empty) {
                this.setTextFill(new Color(0, 0, 1, 1));
                if(!empty && value != null) {
                    if(value.doubleValue() > 0) {
                        Util.applyTextFieldTableCellAnimation(RGB.DARK_GREEN, this);
                    } else if(value.doubleValue() < 0) {
                        Util.applyTextFieldTableCellAnimation(RGB.RED, this);
                    } else {
                        Util.applyTextFieldTableCellAnimation(RGB.BLUE, this);
                    }
                }
                super.updateItem(value, empty);
                if(!empty && value != null) {
                    setText(Util.moneyDisplayFormat.format(value.doubleValue()));
                }
            }
        });

        dailyChangeAmount.setCellValueFactory(ticker -> ticker.getValue().getDailyChangeAmout());
        dailyChangeAmount.setCellFactory(p -> new TextFieldTableCell<Ticker, Number>(new NumberStringConverter()) {
            @Override
            public void updateItem(Number value, boolean empty) {
                this.setTextFill(new Color(0, 0, 1, 1));
                if(!empty && value != null) {
                    String stockCode = tickersTable.getItems().get(getIndex()).getStockCode().getValue();
                    Ticker ticker = TickersDao.getTickers().get(stockCode);
                    if(ticker.getDailyChangePercent().get() > 0) {
                        Util.applyTextFieldTableCellAnimation(RGB.DARK_GREEN, this);
                    } else if(ticker.getDailyChangePercent().get() < 0) {
                        Util.applyTextFieldTableCellAnimation(RGB.RED, this);
                    } else {
                        Util.applyTextFieldTableCellAnimation(RGB.BLUE, this);
                    }
                }
                super.updateItem(value, empty);
                if(!empty && value != null) {
                    setText(Util.moneyDisplayFormat.format(value.doubleValue()));
                }
            }
        });

        dailyChangePercent.setCellValueFactory(ticker -> ticker.getValue().getDailyChangePercent());
        dailyChangePercent.setCellFactory(p -> new TextFieldTableCell<Ticker, Number>(new NumberStringConverter()) {
            @Override
            public void updateItem(Number value, boolean empty) {
                this.setTextFill(new Color(0, 0, 1, 1));
                if(!empty && value != null) {
                    if(value.doubleValue() > 0) {
                        Util.applyTextFieldTableCellAnimation(RGB.DARK_GREEN, this);
                    } else if(value.doubleValue() < 0) {
                        Util.applyTextFieldTableCellAnimation(RGB.RED, this);
                    } else {
                        Util.applyTextFieldTableCellAnimation(RGB.BLUE, this);
                    }
                }
                super.updateItem(value, empty);
                if(!empty && value != null) {
                    setText(Util.moneyDisplayFormat.format(value.doubleValue()));
                }
            }
        });
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        tickersTable.setItems(mainApp.getTickers());

        // set default sorting
        tickersTable.getSortOrder().add(stockCodeColumn);
    }

    public TableView<Ticker> getTickersTable() {
        return tickersTable;
    }

    @FXML
    private void refreshTickers() {
        Runnable task = new Runnable() {
            public void run() {
                runTask();
            }
        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void runTask() {
        this.mainApp.getTickers().forEach(ticker -> {
            try {
                String stockCode = ticker.getStockCode().getValue();
                double [] currentPrice = StocksUtil.getCurrentPriceFromPhisix(stockCode);
                ticker.getCurrentPrice().set(currentPrice[0]);
                ticker.getLastUpdatedDate().set(DateUtil.dateTimeFormatter.format(LocalDateTime.now()));
                ticker.getDailyChangePercent().set(currentPrice[1]);
                ticker.getDailyChangeAmout().set((currentPrice[0] / (100 - currentPrice[1])) * ticker.getTotalShares().intValue());
//                ticker.getDailyChangeAmout().set(currentPrice[0] - (currentPrice[0] / ((100 + currentPrice[1]) / 100)));
            } catch(Exception e) {
                System.err.println(e.getLocalizedMessage());
            } finally {
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
