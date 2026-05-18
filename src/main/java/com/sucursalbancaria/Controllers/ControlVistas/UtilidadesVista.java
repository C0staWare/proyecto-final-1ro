package com.sucursalbancaria.Controllers.ControlVistas;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class UtilidadesVista {
    
    public static void mostrarError(String titulo, String contenido) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
    
    public static void mostrarInfo(String titulo, String contenido) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
    
    public static void mostrarAdvertencia(String titulo, String contenido) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
    
    public static boolean confirmar(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        ButtonType si = new ButtonType("Sí", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(si, no);
        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get().getButtonData() == ButtonBar.ButtonData.OK_DONE;
    }
    
    public static Optional<String> pedirNombreArchivo() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuevo archivo");
        dialog.setHeaderText("Ingrese el nombre del archivo");
        return dialog.showAndWait();
    }
    
    public static void mostrarCamposInvalidos() {
        mostrarAdvertencia("Campos inválidos", "Existen campos con valores no válidos.");
    }
    
    public static boolean confirmarSobrescritura(String tipo) {
        return confirmar("Confirmar edición", "Este " + tipo + " ya existe. ¿Desea sobrescribir los cambios?");
    }
    
    public static boolean confirmarSobrescrituraSolicitud() {
        return confirmar("Advertencia", "Este solicitante ya tiene una solicitud activa. ¿Desea sobreescribirla?");
    }
}