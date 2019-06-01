package com.orion.stocks.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

import org.json.JSONObject;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Util {

    public static final DecimalFormat moneyDisplayFormat;
    static {
        moneyDisplayFormat = new DecimalFormat("#,##0.00000");
    }

    public static final DecimalFormat shareNumberDisplayFormat;
    static {
        shareNumberDisplayFormat = new DecimalFormat();
        shareNumberDisplayFormat.setMinimumFractionDigits(0);
    }

    public static boolean isBlankOrNull(String str) {
        return str == null || str.length() == 0;
    }

    public static JSONObject readJsonFromUrl(String url) {
        JSONObject json = null;
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            json = new JSONObject(sb.toString());
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json;
    }

    /**
     * Applies text color animation in table cell
     * 
     * @param rgb double array of size 3 that represents the colors red, green and blue
     * @param cell text container that will apply the animation
     */
    public static void applyTextFieldTableCellAnimation(double [] rgb, TextFieldTableCell<?, ?> cell) {
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(3000));
                setInterpolator(Interpolator.EASE_BOTH);
            }
            @Override
            protected void interpolate(double frac) {
                Color vColor = new Color(rgb[0], rgb[1], rgb[2], 0 + frac);
                cell.setTextFill(vColor);
            }
        };
        animation.play();
    }
}
