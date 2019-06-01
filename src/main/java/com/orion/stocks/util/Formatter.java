package com.orion.stocks.util;

import javafx.scene.control.TextField;

public class Formatter {

    /**
     * Filter-out non-numeric inputs.
     * 
     * @param textField
     * @param value
     */
    public static void setTextFieldToNumericValue(TextField textField, String value) {
        if(value == null) {
            return;
        }
        textField.setText(value.replaceAll("[^\\d\\.,]", ""));
    }

}
