package com.sucursalbancaria.Controllers.ControlVistas;

import com.sucursalbancaria.Models.Solicitudes.*;

import java.util.function.Function;
import com.sucursalbancaria.Controllers.Logica.ControladorSolicitud;
import com.sucursalbancaria.Models.Solicitantes.Empresa;
import com.sucursalbancaria.Models.Solicitantes.Persona;
import com.sucursalbancaria.Models.Solicitantes.Solicitante;
import com.sucursalbancaria.Models.Solicitudes.SolicitudCredito;
import javafx.scene.control.TableView;

public class GestorSolicitudes {
    
    private final TableView<SolicitudCredito<? extends Solicitante>> tablaSolicitudes;
    
    public GestorSolicitudes(TableView<SolicitudCredito<? extends Solicitante>> tablaSolicitudes) {
        this.tablaSolicitudes = tablaSolicitudes;
    }
    
    public void manejarSolicitudEmpresa(Empresa seleccionada, 
                                         ControladorSolicitud<Empresa> controlador) {
        procesarSolicitud(seleccionada, SolicitudEmpresarial::new, controlador);
    }
    
    public void manejarSolicitudPersona(Persona seleccionada,
                                         ControladorSolicitud<Persona> controlador) {
        procesarSolicitud(seleccionada, SolicitudPersonal::new, controlador);
    }
    
    private <T extends Solicitante> void procesarSolicitud(
            T solicitante,
            Function<T, SolicitudCredito<T>> creadorSolicitud,
            ControladorSolicitud<T> controlador) {
        
        if (solicitante == null) return;
        
        boolean existe = controlador.existeSolicitud(solicitante);
        
        if (!existe) {
            SolicitudCredito<T> nueva = creadorSolicitud.apply(solicitante);
            controlador.listarSolicitudes().add(nueva);
            tablaSolicitudes.getItems().add(nueva);
            return;
        }
        
        if (UtilidadesVista.confirmarSobrescrituraSolicitud()) {
            SolicitudCredito<T> nueva = creadorSolicitud.apply(solicitante);
            controlador.editarSolicitud(nueva);
            tablaSolicitudes.getItems().setAll(controlador.listarSolicitudes());
        }
    }
}