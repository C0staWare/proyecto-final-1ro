package com.sucursalbancaria.Controllers.Logica;

import java.util.List;
import com.sucursalbancaria.Models.Solicitantes.Solicitante;
import com.sucursalbancaria.Models.Solicitudes.SolicitudCredito;

public interface ControladorSolicitud <T extends Solicitante>{
    public boolean existeSolicitud(T solicitante);
    public void crearSolicitud(SolicitudCredito<T> solicitud) throws Exception;
    public void eliminarSolicitud(SolicitudCredito<T> solicitud) throws Exception;
    public void editarSolicitud(SolicitudCredito<T> solicitud);
    public List<SolicitudCredito<T>> listarSolicitudes();
}