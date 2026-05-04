package com.sucursalbancaria.Controllers.ControlVistas;

import java.lang.reflect.Field;
import java.util.Map;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

public class TablaConfigurador {
    
    public static <T> void configurarColumnas(TableView<?> tabla, 
                                               Map<String, String> nombresColumnas, 
                                               Class<T> clazz) {
        tabla.getColumns().forEach(columna -> {
            String nombreCampo = nombresColumnas.get(columna.getText());
            if (nombreCampo == null) return;
            
            if (esClaseSolicitud(clazz)) {
                columna.setCellValueFactory(new PropertyValueFactory<>(nombreCampo));
                return;
            }
            
            Field campo = buscarCampoEnJerarquia(clazz, nombreCampo);
            if (campo == null) {
                System.out.println("No se encontró el campo: " + nombreCampo);
                return;
            }
            
            configurarColumna(columna, campo, nombreCampo);
        });
    }
    
    private static boolean esClaseSolicitud(Class<?> clazz) {
        try {
            return clazz == Class.forName("com.sucursalbancaria.Models.Solicitudes.SolicitudEmpresarial") ||
                   clazz == Class.forName("com.sucursalbancaria.Models.Solicitudes.SolicitudPersonal");
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    private static <T> Field buscarCampoEnJerarquia(Class<T> clazz, String nombreCampo) {
        Class<?> claseActual = clazz;
        while (claseActual != null) {
            try {
                Field campo = claseActual.getDeclaredField(nombreCampo);
                campo.setAccessible(true);
                return campo;
            } catch (NoSuchFieldException e) {
                claseActual = claseActual.getSuperclass();
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    private static <T> void configurarColumna(TableColumn<?, ?> columna, Field campo, String nombreCampo) {
        try {
            if (campo.getType() == String.class) {
                TableColumn<T, String> c = (TableColumn<T, String>) columna;
                configurarColumnaEditable(c, nombreCampo, campo, TextFieldTableCell.forTableColumn());
            } else if (campo.getType() == Double.class || campo.getType() == double.class) {
                TableColumn<T, Double> c = (TableColumn<T, Double>) columna;
                configurarColumnaEditable(c, nombreCampo, campo, TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            } else if (campo.getType() == Integer.class || campo.getType() == int.class) {
                TableColumn<T, Integer> c = (TableColumn<T, Integer>) columna;
                configurarColumnaEditable(c, nombreCampo, campo, TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            } else if (campo.getType() == Long.class || campo.getType() == long.class) {
                TableColumn<T, Long> c = (TableColumn<T, Long>) columna;
                configurarColumnaEditable(c, nombreCampo, campo, TextFieldTableCell.forTableColumn(new LongStringConverter()));
            }
        } catch (ClassCastException e) {
            System.out.println("Error de tipo en la columna: " + columna.getText());
        }
    }
    
    private static <T, U> void configurarColumnaEditable(
            TableColumn<T, U> columna, String nombreCampo, Field campo,
            Callback<TableColumn<T, U>, TableCell<T, U>> cellFactory) {
        columna.setCellValueFactory(new PropertyValueFactory<>(nombreCampo));
        columna.setCellFactory(cellFactory);
        columna.setOnEditCommit(e -> {
            try {
                campo.set(e.getRowValue(), e.getNewValue());
            } catch (IllegalAccessException ex) {
                System.out.println("Error al asignar valor: " + ex.getMessage());
            }
        });
    }
}