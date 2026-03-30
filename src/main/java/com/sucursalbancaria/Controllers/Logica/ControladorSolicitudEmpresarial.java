package com.sucursalbancaria.Controllers.Logica;

import java.util.ArrayList;
import java.util.List;

import com.sucursalbancaria.Models.Solicitantes.Empresa;
import com.sucursalbancaria.Models.Solicitudes.SolicitudCredito;

public class ControladorSolicitudEmpresarial {
    
    List<SolicitudCredito<Empresa>> listaSolicitudesEmpresas;

    public ControladorSolicitudEmpresarial() { listaSolicitudesEmpresas = new ArrayList<>(); }

    //crear
    public void crearSolicitud(SolicitudCredito<Empresa> solicitud) throws Exception{
        
        if(!listaSolicitudesEmpresas.contains(solicitud)) listaSolicitudesEmpresas.add(solicitud);

        else throw new Exception("La solicitud ya existe");
    }
    //eliminar
    public void eliminarSolicitud(SolicitudCredito<Empresa> solicitud) throws Exception {
        
        if(listaSolicitudesEmpresas.contains(solicitud)) listaSolicitudesEmpresas.remove(solicitud);

        else throw new Exception("No se encontro esa solicitud");
    }
    //editar
    public void editarSolicitud(SolicitudCredito<Empresa> solicitud) {
        
        int left = 0;
        int right = listaSolicitudesEmpresas.size() - 1;

        while(left < right){

            int mid = (left + right)/2;
            if(listaSolicitudesEmpresas.get(mid).getId() == solicitud.getId()){

                listaSolicitudesEmpresas.set(mid, solicitud);

                break;
            }
            else if(listaSolicitudesEmpresas.get(mid).getId() < solicitud.getId()){

                left = mid + 1;
            }

            else right = mid - 1;
        }
    }   
    //listar
    public List<SolicitudCredito<Empresa>> listarSolicitudes() {
        
        if(listaSolicitudesEmpresas != null) return listaSolicitudesEmpresas; 

        return null;
    }
}
