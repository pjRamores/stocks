package com.orion.stocks.core.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.orion.stocks.core.DaoException;
import com.orion.stocks.core.model.Position;
import com.orion.stocks.util.DBUtil;
import com.orion.stocks.util.DateUtil;
import com.orion.stocks.util.Util;

public class PositionsDao {

    private static List<Position> positions = null;

    public static List<Position> getPositions() {
        if(positions != null) {
            return positions;
        }

        positions = new ArrayList<Position>();
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM `position` WHERE `STATUS` = 'OPEN' ORDER BY STOCK_CODE, BUY_PRICE";
            ResultSet rs = stmt.executeQuery(sql);

            if(rs != null) {
                while(rs.next()) {
                    Position position = new Position();
                    position.setId(rs.getInt("ID"));
                    position.setStatus(rs.getString("STATUS"));
                    position.setBroker(rs.getString("BROKER"));
                    position.setStockCode(rs.getString("STOCK_CODE"));
                    position.setShares(rs.getInt("SHARES"));

                    position.setDateBought(LocalDate.parse(rs.getString("DATE_BOUGHT"), DateUtil.dateFormatter));
                    position.setBuyPrice(rs.getDouble("BUY_PRICE"));
                    position.setBuyCommission(rs.getDouble("BUY_COMMISSION"));
                    position.setBuyVat(rs.getDouble("BUY_VAT"));
                    position.setBuyTransFee(rs.getDouble("BUY_TRANS_FEE"));
                    position.setBuySccp(rs.getDouble("BUY_SCCP"));
                    position.setTotalExpenses(rs.getDouble("TOTAL_EXPENSES"));

                    if(!Util.isBlankOrNull(rs.getString("DATE_SOLD"))) {
                        position.setDateSold(LocalDate.parse(rs.getString("DATE_SOLD"), DateUtil.dateFormatter));
                    }

                    if(!Util.isBlankOrNull(rs.getString("SELL_PRICE"))) {
                        position.setSellPrice(rs.getDouble("SELL_PRICE"));
                    }

                    if(!Util.isBlankOrNull(rs.getString("SELL_COMMISSION"))) {
                        position.setSellCommission(rs.getDouble("SELL_COMMISSION"));
                    }

                    if(!Util.isBlankOrNull(rs.getString("SELL_VAT"))) {
                        position.setSellVat(rs.getDouble("SELL_VAT"));
                    }

                    if(!Util.isBlankOrNull(rs.getString("SELL_TRANS_FEE"))) {
                        position.setSellTransFee(rs.getDouble("SELL_TRANS_FEE"));
                    }

                    if(!Util.isBlankOrNull(rs.getString("SELL_SCCP"))) {
                        position.setSellSccp(rs.getDouble("SELL_SCCP"));
                    }

                    if(!Util.isBlankOrNull(rs.getString("SALES_TAX"))) {
                        position.setSalesTax(rs.getDouble("SALES_TAX"));
                    }

                    if(!Util.isBlankOrNull(rs.getString("NET_SALE"))) {
                        position.setNetSale(rs.getDouble("NET_SALE"));
                    }

                    if(!Util.isBlankOrNull(rs.getString("NET_GAIN"))) {
                        position.setNetGain(rs.getDouble("NET_GAIN"));
                    }

                    positions.add(position);
                }
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if(stmt != null) {
                try {
                    stmt.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return positions;
    }

    public static void savePosition(Position position) {
        if(position == null) {
            throw new DaoException("Position object is null");
        }

        Connection conn = null;
        Statement stmt = null;
        StringBuilder sql = new StringBuilder();

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();

            if(position.getId() > 0) {
                sql.append("UPDATE `position` SET ");
                sql.append("`STATUS` = ").append(position.getStatus() == null ? "DEFAULT" : "'" + position.getStatus().getValue() + "'");
                sql.append(", `BROKER` = ").append(position.getBroker() == null ? "DEFAULT" : "'" + position.getBroker().getValue() + "'");
                sql.append(", `STOCK_CODE` = ").append(position.getStockCode() == null ? "DEFAULT" : "'" + position.getStockCode().getValue() + "'");
                sql.append(", `TRANSACTION_TYPE` = ").append(position.getTransactionType() == null ? "DEFAULT" : "'" + position.getTransactionType().getValue() + "'");
                sql.append(", `SHARES` = ").append(position.getShares() == null ? "DEFAULT" : "'" + position.getShares().getValue() + "'");
                sql.append(", `DATE_BOUGHT` = ").append(position.getDateBought() == null ? "DEFAULT" : "'" + DateUtil.dateFormatter.format(position.getDateBought()) + "'");
                sql.append(", `BUY_PRICE` = ").append(position.getBuyPrice() == null ? "DEFAULT" : "'" + position.getBuyPrice().getValue() + "'");
                sql.append(", `BUY_COMMISSION` = ").append(position.getBuyCommission() == null ? "DEFAULT" : "'" + position.getBuyCommission().getValue() + "'");
                sql.append(", `BUY_VAT` = ").append(position.getBuyVat() == null ? "DEFAULT" : "'" + position.getBuyVat().getValue() + "'");
                sql.append(", `BUY_TRANS_FEE` = ").append(position.getBuyTransFee() == null ? "DEFAULT" : "'" + position.getBuyTransFee().getValue() + "'");
                sql.append(", `BUY_SCCP` = ").append(position.getBuySccp() == null ? "DEFAULT" : "'" + position.getBuySccp().getValue() + "'");
                sql.append(", `TOTAL_EXPENSES` = ").append(position.getTotalExpenses() == null ? "DEFAULT" : "'" + position.getTotalExpenses().getValue() + "'");
                sql.append(", `DATE_SOLD` = ").append(position.getDateSold() == null ? "DEFAULT" : "'" + DateUtil.dateFormatter.format(position.getDateSold()) + "'");
                sql.append(", `SELL_PRICE` = ").append(position.getSellPrice() == null ? "DEFAULT" : "'" + position.getSellPrice().getValue() + "'");
                sql.append(", `SELL_COMMISSION` = ").append(position.getSellCommission() == null ? "DEFAULT" : "'" + position.getSellCommission().getValue() + "'");
                sql.append(", `SELL_VAT` = ").append(position.getSellVat() == null ? "DEFAULT" : "'" + position.getSellVat().getValue() + "'");
                sql.append(", `SELL_TRANS_FEE` = ").append(position.getSellTransFee() == null ? "DEFAULT" : "'" + position.getSellTransFee().getValue() + "'");
                sql.append(", `SELL_SCCP` = ").append(position.getSellSccp() == null ? "DEFAULT" : "'" + position.getSellSccp().getValue() + "'");
                sql.append(", `SALES_TAX` = ").append(position.getSalesTax() == null ? "DEFAULT" : "'" + position.getSalesTax().getValue() + "'");
                sql.append(", `NET_SALE` = ").append(position.getNetSale() == null ? "DEFAULT" : "'" + position.getNetSale().getValue() + "'");
                sql.append(", `NET_GAIN` = ").append(position.getNetGain() == null ? "DEFAULT" : "'" + position.getNetGain().getValue() + "'");
                sql.append(" WHERE `ID` = ").append(position.getId());

                stmt.executeUpdate(sql.toString());
                System.out.println("Updated position id " + position.getId());
            } else {
                sql.append("INSERT INTO `position` VALUES(DEFAULT");
                sql.append(", ").append(position.getStatus() == null ? "DEFAULT" : "'" + position.getStatus().getValue() + "'");
                sql.append(", ").append(position.getBroker() == null ? "DEFAULT" : "'" + position.getBroker().getValue() + "'");
                sql.append(", ").append(position.getStockCode() == null ? "DEFAULT" : "'" + position.getStockCode().getValue() + "'");
                sql.append(", ").append(position.getTransactionType() == null ? "DEFAULT" : "'" + position.getTransactionType().getValue() + "'");
                sql.append(", ").append(position.getShares() == null ? "DEFAULT" : "'" + position.getShares().getValue() + "'");
                sql.append(", ").append(position.getDateBought() == null ? "DEFAULT" : "'" + DateUtil.dateFormatter.format(position.getDateBought()) + "'");
                sql.append(", ").append(position.getBuyPrice() == null ? "DEFAULT" : "'" + position.getBuyPrice().getValue() + "'");
                sql.append(", ").append(position.getBuyCommission() == null ? "DEFAULT" : "'" + position.getBuyCommission().getValue() + "'");
                sql.append(", ").append(position.getBuyVat() == null ? "DEFAULT" : "'" + position.getBuyVat().getValue() + "'");
                sql.append(", ").append(position.getBuyTransFee() == null ? "DEFAULT" : "'" + position.getBuyTransFee().getValue() + "'");
                sql.append(", ").append(position.getBuySccp() == null ? "DEFAULT" : "'" + position.getBuySccp().getValue() + "'");
                sql.append(", ").append(position.getTotalExpenses() == null ? "DEFAULT" : "'" + position.getTotalExpenses().getValue() + "'");
                sql.append(", ").append(position.getDateSold() == null ? "DEFAULT" : "'" + DateUtil.dateFormatter.format(position.getDateSold()) + "'");
                sql.append(", ").append(position.getSellPrice() == null ? "DEFAULT" : "'" + position.getSellPrice().getValue() + "'");
                sql.append(", ").append(position.getSellCommission() == null ? "DEFAULT" : "'" + position.getSellCommission().getValue() + "'");
                sql.append(", ").append(position.getSellVat() == null ? "DEFAULT" : "'" + position.getSellVat().getValue() + "'");
                sql.append(", ").append(position.getSellTransFee() == null ? "DEFAULT" : "'" + position.getSellTransFee().getValue() + "'");
                sql.append(", ").append(position.getSellSccp() == null ? "DEFAULT" : "'" + position.getSellSccp().getValue() + "'");
                sql.append(", ").append(position.getSalesTax() == null ? "DEFAULT" : "'" + position.getSalesTax().getValue() + "'");
                sql.append(", ").append(position.getNetSale() == null ? "DEFAULT" : "'" + position.getNetSale().getValue() + "'");
                sql.append(", ").append(position.getNetGain() == null ? "DEFAULT" : "'" + position.getNetGain().getValue() + "'");
                sql.append(", NOW()");
                sql.append(", DEFAULT");
                sql.append(")");

                stmt.executeUpdate(sql.toString(), Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = stmt.getGeneratedKeys();
                if(rs.next()) {
                    int id = rs.getInt(1);
                    position.setId(id);
                }
                rs.close();
                System.out.println("Inserted position id " + position.getId());
            }
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
            System.err.println("sql : " + sql.toString());
        } finally {
            if(stmt != null) {
                try {
                    stmt.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<Position> getOpenPositions() {
        List<Position> openPositions = new ArrayList<Position>();

        for(Position position : getPositions()) {
            if("OPEN".equals(position.getStatus().getValue())) {
                openPositions.add(position);
            }
        }

        return openPositions;
    }

    public static List<Position> getOpenPositions(String stockCode) {
        List<Position> openPositions = new ArrayList<Position>();

        for(Position position : getPositions()) {
            if("OPEN".equals(position.getStatus().getValue()) && stockCode != null && stockCode.equals(position.getStockCode().getValue())) {
                openPositions.add(position);
            }
        }

        return openPositions;
    }
}
