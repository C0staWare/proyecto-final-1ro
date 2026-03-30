package com.sucursalbancaria.Models.Solicitantes;

public class Persona extends Solicitante {
    
    private Long CI;
    private double salarioNucleo;
    private int personasQueSustenta = 1;


    
    public Persona(String nombreSolicitante, double valorCredito, String direccionSolicitante, Long CI,
            double salarioNucleo, int personasQueSustenta) throws Exception {

        super(nombreSolicitante, valorCredito, direccionSolicitante);

        if(CI == null){
            throw new Exception("Todos los campos son obligatorios");
        }

        

        this.CI = CI;
        this.salarioNucleo = salarioNucleo;
        this.personasQueSustenta = personasQueSustenta;

        super.aumentarTotalSolicitudes();
        
        if(capacidadPago() < 100.0) throw new Exception("No se cumple el requicito: Capacidad de Pago");
    }

    public double capacidadPago(){

        return this.salarioNucleo - (this.personasQueSustenta * 50);
    }

    public Long getCI() {
        return CI;
    }



    public void setCI(Long cI) {
        CI = cI;
    }



    public double getSalarioNucleo() {
        return salarioNucleo;
    }



    public void setSalarioNucleo(double salarioNucleo) {
        this.salarioNucleo = salarioNucleo;
    }



    public int getPersonasQueSustenta() {
        return personasQueSustenta;
    }



    public void setPersonasQueSustenta(int personasQueSustenta) {
        this.personasQueSustenta = personasQueSustenta;
    }

    
}
