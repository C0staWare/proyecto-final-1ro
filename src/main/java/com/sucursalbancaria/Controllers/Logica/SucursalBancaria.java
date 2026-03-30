package com.sucursalbancaria.Controllers.Logica;

import com.sucursalbancaria.Models.Solicitantes.Empresa;
import com.sucursalbancaria.Models.Solicitantes.Persona;
import com.sucursalbancaria.Models.Solicitudes.SolicitudEmpresarial;
import com.sucursalbancaria.Models.Solicitudes.SolicitudPersonal;

public class SucursalBancaria {
    
    ControladorEmpresas controladorEmpresas;
    ControladorPersonas controladorPersonas;
    ControladorSolicitudEmpresarial controladorSolicitudEmpresarial;
    ControladorSolicitudPersonal controladorSolicitudPersonal;

    public SucursalBancaria(){

        controladorEmpresas = new ControladorEmpresas();
        controladorPersonas = new ControladorPersonas();
        controladorSolicitudEmpresarial = new ControladorSolicitudEmpresarial();
        controladorSolicitudPersonal = new ControladorSolicitudPersonal();
        
    }

    
}
