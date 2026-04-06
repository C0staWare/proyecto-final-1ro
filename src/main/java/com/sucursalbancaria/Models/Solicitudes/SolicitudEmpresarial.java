package com.sucursalbancaria.Models.Solicitudes;
import com.sucursalbancaria.Models.Solicitantes.Empresa;


public class SolicitudEmpresarial implements SolicitudCredito<Empresa> {
    

    Empresa empresa;
    double mensualidad;
    Long id;

    public SolicitudEmpresarial(Empresa empresa){

        this.empresa = empresa;
        id = System.currentTimeMillis();
    }
    
    @Override
    public double getMensualidad(){

        String tipoEmpresa = empresa.tipoEmpresa();

        double promedioAnual = empresa.getGananciaPromedioAnual();

        if(tipoEmpresa.equals("pequena")) mensualidad = promedioAnual * 0.2;
        else if(tipoEmpresa.equals("mediana")) mensualidad = promedioAnual * 0.3;
        else mensualidad = promedioAnual * 0.4;

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
