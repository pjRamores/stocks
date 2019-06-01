package com.orion.stocks.core.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.orion.stocks.core.DaoException;
import com.orion.stocks.core.model.Dividend;
import com.orion.stocks.util.DBUtil;
import com.orion.stocks.util.DateUtil;

public class DividendsDao {

    private static List<Dividend> dividends = null;

    public static List<Dividend> getDividends() {
        if(dividends != null) {
            return dividends;
        }

        dividends = new ArrayList<Dividend>();
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM `dividend` LIMIT 500";
            ResultSet rs = stmt.executeQuery(sql);

            if(rs != null) {
                while(rs.next()) {

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

        return dividends;
    }

    public static void saveDividend(Dividend dividend) {
        if(dividend == null) {
            throw new DaoException("Dividend object is null");
        }

        Connection conn = null;
        Statement stmt = null;
        String sql = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();

            if(dividend.getId() > 0) {

                System.out.println("Updated dividend id " + dividend.getId());
            } else {
                sql = "INSERT INTO `dividend` VALUES("
                        + "DEFAULT"
                        + ", " + (dividend.getBroker() == null ? "DEFAULT" : "'" + dividend.getBroker().getValue() + "'")
                        + ", " + (dividend.getStockCode() == null ? "DEFAULT" : "'" + dividend.getStockCode().getValue() + "'")
                        + ", " + (dividend.getShares() == null ? "DEFAULT" : "'" + dividend.getShares().getValue() + "'")
                        + ", " + (dividend.getPaymentDate() == null ? "DEFAULT" : "'" + DateUtil.dateFormatter.format(dividend.getPaymentDate()) + "'")
                        + ", " + (dividend.getCashDividend() == null ? "DEFAULT" : "'" + dividend.getCashDividend().getValue() + "'")
                        + ", " + (dividend.getGrossAmount() == null ? "DEFAULT" : "'" + dividend.getGrossAmount().getValue() + "'")
                        + ", " + (dividend.getWithholdingTax() == null ? "DEFAULT" : "'" + dividend.getWithholdingTax().getValue() + "'")
                        + ", " + (dividend.getNetAmount() == null ? "DEFAULT" : "'" + dividend.getNetAmount().getValue() + "'")
                        + ", NOW()"
                        + ", DEFAULT)";

                stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = stmt.getGeneratedKeys();
                if(rs.next()) {
                    int id = rs.getInt(1);
                    dividend.setId(id);
                }
                rs.close();
                System.out.println("Inserted dividend id " + dividend.getId());
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

}
