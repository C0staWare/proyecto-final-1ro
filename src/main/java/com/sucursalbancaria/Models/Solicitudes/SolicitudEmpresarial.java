package com.sucursalbancaria.Models.Solicitudes;
import com.sucursalbancaria.Models.Solicitantes.Empresa;


public class SolicitudEmpresarial implements SolicitudCredito<Empresa> {
    

    Empresa empresa;
    double mensualiad;
    Long id;

    public SolicitudEmpresarial(Empresa empresa){

        this.empresa = empresa;
        id = System.currentTimeMillis();
    }

    public double calcularMensualidad(){

        String tipoEmpresa = empresa.tipoEmpresa();

        double promedioAnual = empresa.getGananciaPromedioAnual();

        if(tipoEmpresa.equals("pequena")) mensualiad = promedioAnual * 0.2;
        else if(tipoEmpresa.equals("mediana")) mensualiad = promedioAnual * 0.3;
        else mensualiad = promedioAnual * 0.4;

        return mensualiad;
    }

    @Override
    public Long getId() {
        
        return id;
    }

    public Empresa getEmpresa(){

        return empresa;
    }
}
