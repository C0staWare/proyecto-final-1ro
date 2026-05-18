package com.sucursalbancaria.Controllers.Logica;

public class SucursalBancaria {
    
    public ControladorEmpresas controladorEmpresas;
    public ControladorPersonas controladorPersonas;
    public ControladorSolicitudEmpresarial controladorSolicitudEmpresarial;
    public ControladorSolicitudPersonal controladorSolicitudPersonal;

    public SucursalBancaria(){
        controladorEmpresas = new ControladorEmpresas();
        controladorPersonas = new ControladorPersonas();
        controladorSolicitudEmpresarial = new ControladorSolicitudEmpresarial();
        controladorSolicitudPersonal = new ControladorSolicitudPersonal();
    }
}