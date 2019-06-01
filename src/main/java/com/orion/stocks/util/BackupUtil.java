package com.orion.stocks.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;

public class BackupUtil {

    public static void main(String[] args) {
        System.out.println(">>>>> BackupUtil");

        BackupUtil util = new BackupUtil();
        util.upload();

        System.exit(0);
    }

    private void upload() {
        File file = new File("C:\\Users\\padma\\Documents\\stocks\\stocks-bk20161113.xls");
        try {
            FileInputStream fis = new FileInputStream(file);
            POIFSFileSystem fs = new POIFSFileSystem(fis);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheet("Stocks");

            Iterator<Row> rowIterator = sheet.rowIterator();
            // rowIterator.next();
            // rowIterator.next();
            rowIterator.next();
            rowIterator.next();
            rowIterator.next();

            List<Map<String, String>> positions = new ArrayList<Map<String, String>>();
            while(rowIterator.hasNext()) {
                Map<String, String> position = new LinkedHashMap<String, String>();
                Row row = rowIterator.next();
                int index = 0;

                position.put("row", String.valueOf(row.getRowNum()));

                String broker = getCellValue(row.getCell(++index));

                if(Util.isBlankOrNull(broker)) {
                    continue;
                }

                String dateBought = getCellValue(row.getCell(++index), true);
                String dateSold = getCellValue(row.getCell(++index), true);
                String stockCode = getCellValue(row.getCell(++index));
                String shares = getCellValue(row.getCell(++index));
                String buyPrice = getCellValue(row.getCell(++index));
                String buyCommission = getCellValue(row.getCell(++index));
                String buyVat = getCellValue(row.getCell(++index));
                String buyTransFee = getCellValue(row.getCell(++index));
                String buySccp = getCellValue(row.getCell(++index));
                ++index;
                String totalExpenses = getCellValue(row.getCell(++index));

                if(Util.isBlankOrNull(totalExpenses)) {
                    continue;
                }

                ++index;
                String sellPrice = getCellValue(row.getCell(++index));
                String sellCommission = getCellValue(row.getCell(++index));
                String sellVat = getCellValue(row.getCell(++index));
                String sellTransFee = getCellValue(row.getCell(++index));
                String sellSccp = getCellValue(row.getCell(++index));
                String salesTax = getCellValue(row.getCell(++index));
                ++index;
                String netSale = getCellValue(row.getCell(++index));

                if(Util.isBlankOrNull(netSale)) {
                    continue;
                }

                String netGain = getCellValue(row.getCell(++index));

                if(broker != null) {
                    position.put("broker", broker);
                }
                if(dateBought != null) {
                    position.put("dateBought", dateBought);
                }
                if(dateSold != null) {
                    position.put("dateSold", dateSold);
                }
                if(stockCode != null) {
                    position.put("stockCode", stockCode);
                }
                if(shares != null) {
                    position.put("shares", shares);
                }
                if(buyPrice != null) {
                    position.put("buyPrice", buyPrice);
                }
                if(buyCommission != null) {
                    position.put("buyCommission", buyCommission);
                }
                if(buyVat != null) {
                    position.put("buyVat", buyVat);
                }
                if(buyTransFee != null) {
                    position.put("buyTransFee", buyTransFee);
                }
                if(buySccp != null) {
                    position.put("buySccp", buySccp);
                }
                if(totalExpenses != null) {
                    position.put("totalExpenses", totalExpenses);
                }
                if(sellPrice != null) {
                    position.put("sellPrice", sellPrice);
                }
                if(sellCommission != null) {
                    position.put("sellCommission", sellCommission);
                }
                if(sellVat != null) {
                    position.put("sellVat", sellVat);
                }
                if(sellTransFee != null) {
                    position.put("sellTransFee", sellTransFee);
                }
                if(sellSccp != null) {
                    position.put("sellSccp", sellSccp);
                }
                if(salesTax != null) {
                    position.put("salesTax", salesTax);
                }
                if(netSale != null) {
                    position.put("netSale", netSale);
                }
                if(netGain != null) {
                    position.put("netGain", netGain);
                }

                positions.add(position);
            }

            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = null;
            String sql = "INSERT INTO position VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE(), DEFAULT)";
            for(Map<String, String> position : positions) {
                System.out.println(position);

                pstmt = conn.prepareStatement(sql);

                int parameterIndex = 0;

                pstmt.setString(++parameterIndex, position.get("dateSold") == null ? "OPEN" : "CLOSED");
                pstmt.setString(++parameterIndex, position.get("broker"));
                pstmt.setString(++parameterIndex, position.get("stockCode"));
                pstmt.setInt(++parameterIndex, (int) Float.parseFloat(position.get("shares")));
                pstmt.setString(++parameterIndex, position.get("dateBought"));
                pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("buyPrice")));
                pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("buyCommission")));
                pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("buyVat")));
                pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("buyTransFee")));
                pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("buySccp")));
                pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("totalExpenses")));
                pstmt.setString(++parameterIndex, position.get("dateSold"));

                if(!Util.isBlankOrNull(position.get("dateSold")) && !Util.isBlankOrNull(position.get("sellPrice"))) {
                    pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("sellPrice")));
                } else {
                    pstmt.setNull(++parameterIndex, Types.DECIMAL);
                }

                if(!Util.isBlankOrNull(position.get("dateSold"))
                        && !Util.isBlankOrNull(position.get("sellCommission"))) {
                    pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("sellCommission")));
                } else {
                    pstmt.setNull(++parameterIndex, Types.DECIMAL);
                }

                if(!Util.isBlankOrNull(position.get("dateSold")) && !Util.isBlankOrNull(position.get("sellVat"))) {
                    pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("sellVat")));
                } else {
                    pstmt.setNull(++parameterIndex, Types.DECIMAL);
                }

                if(!Util.isBlankOrNull(position.get("dateSold")) && !Util.isBlankOrNull(position.get("sellTransFee"))) {
                    pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("sellTransFee")));
                } else {
                    pstmt.setNull(++parameterIndex, Types.DECIMAL);
                }

                if(!Util.isBlankOrNull(position.get("dateSold")) && !Util.isBlankOrNull(position.get("sellSccp"))) {
                    pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("sellSccp")));
                } else {
                    pstmt.setNull(++parameterIndex, Types.DECIMAL);
                }

                if(!Util.isBlankOrNull(position.get("dateSold")) && !Util.isBlankOrNull(position.get("salesTax"))) {
                    pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("salesTax")));
                } else {
                    pstmt.setNull(++parameterIndex, Types.DECIMAL);
                }

                if(!Util.isBlankOrNull(position.get("dateSold")) && !Util.isBlankOrNull(position.get("netSale"))) {
                    pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("netSale")));
                } else {
                    pstmt.setNull(++parameterIndex, Types.DECIMAL);
                }

                if(!Util.isBlankOrNull(position.get("dateSold")) && !Util.isBlankOrNull(position.get("netGain"))) {
                    pstmt.setBigDecimal(++parameterIndex, new BigDecimal(position.get("netGain")));
                } else {
                    pstmt.setNull(++parameterIndex, Types.DECIMAL);
                }

                pstmt.execute();
            }

            conn.close();
        } catch(FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch(ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch(SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String getCellValue(Cell cell) {
        return getCellValue(cell, false);
    }

    private String getCellValue(Cell cell, boolean isDate) {
        if(cell == null)
            return null;
        if(isDate) {
            Date date = cell.getDateCellValue();
            if(date == null) {
                return null;
            }
            return new DateTime(date.getTime()).toString("yyyy-MM-dd");
        }
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return cell.getStringCellValue();
        }
    }

}
