package com.sucursalbancaria.Models.Solicitantes;

public abstract class Solicitante {

    private String nombreSolicitante;
    private double valorCredito;
    private String direccionSolicitante;

    public Solicitante() {}

    public Solicitante(String nombreSolicitante, double valorCredito, String direccionSolicitante) throws Exception {

        if(nombreSolicitante == null || direccionSolicitante == null){

            throw new Exception("Todos los campos son Obligatorios");
        }

        this.nombreSolicitante = nombreSolicitante;
        this.valorCredito = valorCredito;
        this.direccionSolicitante = direccionSolicitante;

    }


    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public Double getValorCredito() {
        return valorCredito;
    }

    public void setValorCredito(Double valorCredito) {
        this.valorCredito = valorCredito;
    }

    public String getDireccionSolicitante() {
        return direccionSolicitante;
    }

    public void setDireccionSolicitante(String direccionSolicitante) {
        this.direccionSolicitante = direccionSolicitante;
    }
 
}
