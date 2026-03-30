package com.sucursalbancaria.Models.Solicitudes;


import com.sucursalbancaria.Models.Solicitantes.Solicitante;

public interface SolicitudCredito<T extends Solicitante> {

    public double calcularMensualidad();
    public Long getId();
    
}
