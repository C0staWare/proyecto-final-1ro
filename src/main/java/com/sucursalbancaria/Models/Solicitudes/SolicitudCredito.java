package com.sucursalbancaria.Models.Solicitudes;


import com.sucursalbancaria.Models.Solicitantes.Solicitante;

public interface SolicitudCredito<T extends Solicitante> {

    public double getMensualidad();
    public Long getId();
    public T getSolicitante();
    public String getTipoSolicitud();
    public String getNombreSolicitante();
    public String getEstadoSolicitud();
    
}
