package com.sucursalbancaria.Controllers.Logica;

import java.util.ArrayList;
import java.util.List;

import com.sucursalbancaria.Models.Solicitantes.Persona;
import com.sucursalbancaria.Models.Solicitudes.SolicitudCredito;


public class ControladorSolicitudPersonal {
    
    List<SolicitudCredito<Persona>> listaSolicitudesPersonas;

    public ControladorSolicitudPersonal() { listaSolicitudesPersonas = new ArrayList<>(); }

    //crear
    public void crearSolicitud(SolicitudCredito<Persona> solicitud) throws Exception {
        
        if(!listaSolicitudesPersonas.contains(solicitud)) listaSolicitudesPersonas.add(solicitud);

        else throw new Exception("La solicitud ya existe");
    }
    //eliminar
    public void eliminarSolicitud(SolicitudCredito<Persona> solicitud) throws Exception {
        
        if(listaSolicitudesPersonas.contains(solicitud)) listaSolicitudesPersonas.remove(solicitud);

        else throw new Exception("No se encontro la solicitud");
    }
    //editar
    public void editarSolicitud(SolicitudCredito<Persona> solicitud) {
        
        int left = 0;
        int right = listaSolicitudesPersonas.size() - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if(listaSolicitudesPersonas.get(mid).getId() == solicitud.getId()){

                listaSolicitudesPersonas.set(mid, solicitud);

                break;
            }
            else if(listaSolicitudesPersonas.get(mid).getId() < solicitud.getId()){

                left = mid + 1;
            }
            else right = mid - 1;
        }
    }
    //listar
    public List<SolicitudCredito<Persona>> listarSolicitudes() {
        
        if(listaSolicitudesPersonas != null) return listaSolicitudesPersonas;
        return null;
    }
}
