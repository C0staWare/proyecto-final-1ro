package com.sucursalbancaria.Models.Solicitantes;

public class Empresa extends Solicitante {
    
    private String nombreDirector;
    private String direccionDirector;
    private double gananciaPromedioAnual;
    private int cantidadTrabajadores;
    private String ministerio;
    private Long codigo;


    public Empresa(String nombreSolicitante, double valorCredito, String direccionSolicitante,
            String nombreDirector, String direccionDirector, double gananciaPromedioAnual, int cantidadTrabajadores,
            String ministerio, Long codigo) throws Exception {

        super(nombreSolicitante, valorCredito, direccionSolicitante);

        if(nombreDirector == null || direccionDirector == null || ministerio == null){
            throw new Exception("Todos los campos son obligatorios");
        }

        this.nombreDirector = nombreDirector;
        this.direccionDirector = direccionDirector;
        this.gananciaPromedioAnual = gananciaPromedioAnual;
        this.cantidadTrabajadores = cantidadTrabajadores;
        this.ministerio = ministerio;
        this.codigo = codigo;
    
        if(tipoEmpresa().equals("pequena") & gananciaPromedioAnual <= 1000)throw new Exception("La empresa no cumple el requicito: Ganancia Promedio Anual");
        else if(tipoEmpresa().equals("mediana") & gananciaPromedioAnual > 1000 & gananciaPromedioAnual <= 5000)throw new Exception("La empresa no cumple el requicito: Ganancia Promedio Anual");
        else if(tipoEmpresa().equals("grande") & gananciaPromedioAnual <= 10000) throw new Exception("La empresa no cumple el requicito: Ganancia Promedio Anual");
    }

    public String tipoEmpresa(){

        if(cantidadTrabajadores <= 100) return "pequena";
        else if(cantidadTrabajadores > 100 & cantidadTrabajadores <= 300) return "mediana";
        else return "grande";
    }



    public String getNombreDirector() {
        return nombreDirector;
    }
    public void setNombreDirector(String nombreDirector) {
        this.nombreDirector = nombreDirector;
    }
    public String getDireccionDirector() {
        return direccionDirector;
    }
    public void setDireccionDirector(String direccionDirector) {
        this.direccionDirector = direccionDirector;
    }
    public Double getGananciaPromedioAnual() {
        return gananciaPromedioAnual;
    }
    public void setGananciaPromedioAnual(Double gananciaPromedioAnual) {
        this.gananciaPromedioAnual = gananciaPromedioAnual;
    }
    public int getCantidadTrabajadores() {
        return cantidadTrabajadores;
    }
    public void setCantidadTrabajadores(int cantidadTrabajadores) {
        this.cantidadTrabajadores = cantidadTrabajadores;
    }
    public String getMinisterio() {
        return ministerio;
    }
    public void setMinisterio(String ministerio) {
        this.ministerio = ministerio;
    }
    public Long getCodigo() {
        return codigo;
    }
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    
}
