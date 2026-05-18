package com.sucursalbancaria.Models.Solicitudes;

import com.sucursalbancaria.Models.Solicitantes.Persona;
import java.io.Serializable;

public class SolicitudPersonal implements SolicitudCredito<Persona>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Persona persona;
    private double mensualidad;
    private Long id;

    public SolicitudPersonal(Persona persona){
        this.persona = persona;
        id = System.currentTimeMillis();
    }

    @Override
    public double getMensualidad() {
        mensualidad = persona.calcularMensualidad();
        return mensualidad;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Persona getSolicitante(){
        return persona;
    }

    public Persona getPersona(){
        return persona;
    }

    @Override
    public String getTipoSolicitud(){
        return "Personal";
    }

    @Override
    public String getNombreSolicitante(){
        return persona.getNombreSolicitante();
    }

    @Override
    public String getEstadoSolicitud(){
        if(persona.capacidadPago() < 100) return "Rechazada";
        else return "Aprobada";
    }
}