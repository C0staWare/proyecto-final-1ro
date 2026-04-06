package com.sucursalbancaria.Models.Solicitudes;

import com.sucursalbancaria.Models.Solicitantes.Persona;

public class SolicitudPersonal implements SolicitudCredito<Persona> {
    
    Persona persona;
    double mensualidad;
    Long id;

    public SolicitudPersonal(Persona persona){

        this.persona = persona;
        id = System.currentTimeMillis();
    }

    @Override
    public double getMensualidad() {
        
        double capacidadPago = persona.capacidadPago();

        if(capacidadPago >= 100 & capacidadPago <= 120 ) mensualidad = 30.0;
        else if(capacidadPago > 120 & capacidadPago <= 140) mensualidad = 40.0;
        else mensualidad = 50;
        
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
