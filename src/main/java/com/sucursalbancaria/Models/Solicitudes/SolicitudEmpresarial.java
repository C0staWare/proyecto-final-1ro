package com.sucursalbancaria.Models.Solicitudes;

import com.sucursalbancaria.Models.Solicitantes.Empresa;
import java.io.Serializable;

public class SolicitudEmpresarial implements SolicitudCredito<Empresa>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Empresa empresa;
    private double mensualidad;
    private Long id;

    public SolicitudEmpresarial(Empresa empresa){
        this.empresa = empresa;
        id = System.currentTimeMillis();
    }
    
    @Override
    public double getMensualidad(){
        mensualidad = empresa.calcularMensualidad();
        return mensualidad;
    }

    @Override
    public Empresa getSolicitante() {
        return empresa;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public Empresa getEmpresa(){
        return empresa;
    }

    @Override
    public String getTipoSolicitud(){
        return "Empresarial";
    }

    @Override
    public String getNombreSolicitante(){
        return empresa.getNombreSolicitante();
    }

    @Override
    public String getEstadoSolicitud(){
        try{
            empresa.comprobarRequisitoGanancia();
            return "Aprobada";
        }
        catch(Exception e){
            return "Rechazada";
        }
    }
}