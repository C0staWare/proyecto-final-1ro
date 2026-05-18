package com.sucursalbancaria.Models.Solicitantes;

import java.io.Serializable;

public class Empresa extends Solicitante implements Comparable<Empresa>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String nombreDirector;
    private String direccionDirector;
    private double gananciaPromedioAnual;
    private int cantidadTrabajadores;
    private String ministerio;
    private Long codigo;

    public Empresa() throws Exception {
        super("", 0.0, "");
        nombreDirector = "";
        direccionDirector = "";
        gananciaPromedioAnual = 0.0;
        cantidadTrabajadores = 1;
        ministerio = "";
        codigo = 0l;
    }
    
    public Empresa(String nombreSolicitante, double valorCredito, String direccionSolicitante,
            String nombreDirector, String direccionDirector, double gananciaPromedioAnual, int cantidadTrabajadores,
            String ministerio, Long codigo) throws Exception {
        super(nombreSolicitante, valorCredito, direccionSolicitante);
        this.nombreDirector = nombreDirector;
        this.direccionDirector = direccionDirector;
        this.gananciaPromedioAnual = gananciaPromedioAnual;
        this.cantidadTrabajadores = cantidadTrabajadores;
        this.ministerio = ministerio;
        this.codigo = codigo;
    }

    public String tipoEmpresa(){
        if(cantidadTrabajadores <= 100) return "pequena";
        else if(cantidadTrabajadores > 100 && cantidadTrabajadores <= 300) return "mediana";
        else return "grande";
    }

    public void comprobarRequisitoGanancia() throws Exception {
        if(tipoEmpresa().equals("pequena") && gananciaPromedioAnual < 1000)
            throw new Exception("La empresa no cumple el requisito: Ganancia Promedio Anual");
        else if(tipoEmpresa().equals("mediana") && gananciaPromedioAnual < 5000)
            throw new Exception("La empresa no cumple el requisito: Ganancia Promedio Anual");
        else if(tipoEmpresa().equals("grande") && gananciaPromedioAnual < 10000)
            throw new Exception("La empresa no cumple el requisito: Ganancia Promedio Anual");
    }

    public double calcularMensualidad(){
        double mensualidad;
        String tipoEmpresa = tipoEmpresa();
        double valorCredito = getValorCredito();
        if(tipoEmpresa.equals("pequena")) mensualidad = valorCredito * 0.2;
        else if(tipoEmpresa.equals("mediana")) mensualidad = valorCredito * 0.3;
        else mensualidad = valorCredito * 0.4;
        return mensualidad;
    }

    public int getDemoraPago(){
        return (int)(super.getValorCredito() / calcularMensualidad());
    }

    public String getNombreDirector() { return nombreDirector; }
    public void setNombreDirector(String nombreDirector) { this.nombreDirector = nombreDirector; }
    public String getDireccionDirector() { return direccionDirector; }
    public void setDireccionDirector(String direccionDirector) { this.direccionDirector = direccionDirector; }
    public Double getGananciaPromedioAnual() { return gananciaPromedioAnual; }
    public void setGananciaPromedioAnual(Double gananciaPromedioAnual) { this.gananciaPromedioAnual = gananciaPromedioAnual; }
    public int getCantidadTrabajadores() { return cantidadTrabajadores; }
    public void setCantidadTrabajadores(int cantidadTrabajadores) { this.cantidadTrabajadores = cantidadTrabajadores; }
    public String getMinisterio() { return ministerio; }
    public void setMinisterio(String ministerio) { this.ministerio = ministerio; }
    public Long getCodigo() { return codigo; }
    public void setCodigo(Long codigo) { this.codigo = codigo; }

    @Override
    public int compareTo(Empresa otra){
        return this.getCodigo().compareTo(otra.getCodigo());
    }
}