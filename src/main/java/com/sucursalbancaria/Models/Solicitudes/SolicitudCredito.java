package com.sucursalbancaria.Models.Solicitudes;

import com.sucursalbancaria.Models.Solicitantes.Solicitante;
import java.io.Serializable;

public interface SolicitudCredito<T extends Solicitante> extends Serializable {
    public double getMensualidad();
    public Long getId();
    public T getSolicitante();
    public String getTipoSolicitud();
    public String getNombreSolicitante();
    public String getEstadoSolicitud();
}