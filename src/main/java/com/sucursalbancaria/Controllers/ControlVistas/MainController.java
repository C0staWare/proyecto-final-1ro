package com.sucursalbancaria.Controllers.ControlVistas;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import com.sucursalbancaria.Controllers.Logica.SucursalBancaria;
import com.sucursalbancaria.Models.Solicitantes.Empresa;
import com.sucursalbancaria.Models.Solicitantes.Persona;
import com.sucursalbancaria.Models.Solicitudes.SolicitudCredito;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;

public class MainController {
    
    SucursalBancaria sucursalBancaria = new SucursalBancaria();

    @FXML
    MenuItem nuevoBtn;

    @FXML
    TableView<Empresa> tablaEmpresas;
    
    @FXML
    TableView<Persona> tablaPersonas;

    @FXML
    TableView<SolicitudCredito<?>> tablaSolicitudes;

    @FXML
    public void initialize(){
        tablaEmpresas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tablaPersonas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tablaSolicitudes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    @FXML
    public void nuevoArchivo() throws IOException {

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Nuevo archivo");

        dialog.setHeaderText("Ingrese el nombre del archivo");

        Optional<String> resultado = dialog.showAndWait();

        resultado.ifPresent(nombre -> {
            File archivo;

            File carpeta = new File("Datos");

            if(!carpeta.exists()){

                carpeta.mkdir();

            }
            archivo = new File(carpeta, nombre+".bin");

            try{
                if(archivo.createNewFile()){
                    System.out.println("archivo creado correctamente");
                }
                else{
                    System.out.println("no se completo la creacion del archivo");
                }
            }catch(IOException err){

            }
        });
        
    }



}
