package com.sucursalbancaria.Controllers.ControlVistas;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import javafx.util.StringConverter;

public class DoubleSinNotacionConverter extends StringConverter<Double> {
    
    private final DecimalFormat formatter;
    
    public DoubleSinNotacionConverter() {
        formatter = new DecimalFormat("#");
        formatter.setGroupingUsed(false);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(0);
    }
    
    @Override
    public String toString(Double object) {
        if (object == null) return "";
        
        BigDecimal bd = new BigDecimal(object.toString());
        
        String resultado = bd.toPlainString();
        
        if (resultado.contains(".")) {
            String[] partes = resultado.split("\\.");
            if (partes[1].length() > 2) {
                resultado = String.format("%.2f", object);
            }
        }
        
        return resultado;
    }
    
    @Override
    public Double fromString(String string) {
        if (string == null || string.trim().isEmpty()) return 0.0;
        try {
            string = string.trim().replace(",", ".");
            
            return Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
