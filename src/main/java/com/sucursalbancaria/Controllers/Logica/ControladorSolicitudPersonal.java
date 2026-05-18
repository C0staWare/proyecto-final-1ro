package com.sucursalbancaria.Controllers.Logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.sucursalbancaria.Models.Solicitantes.Persona;
import com.sucursalbancaria.Models.Solicitudes.SolicitudCredito;
import com.sucursalbancaria.Models.Solicitudes.SolicitudPersonal;

public class ControladorSolicitudPersonal implements ControladorSolicitud<Persona> {
    
    public List<SolicitudCredito<Persona>> listaSolicitudesPersonas;

    public ControladorSolicitudPersonal() { 
        listaSolicitudesPersonas = new ArrayList<>(); 
    }

    @Override
    public boolean existeSolicitud(Persona persona) {
        return listaSolicitudesPersonas.stream()
            .filter(s -> s instanceof SolicitudPersonal)
            .map(s -> (SolicitudPersonal) s)
            .anyMatch(s -> s.getPersona().getCI().equals(persona.getCI()));
    }

    @Override
    public void crearSolicitud(SolicitudCredito<Persona> solicitud) throws Exception {
        if(!listaSolicitudesPersonas.contains(solicitud)) 
            listaSolicitudesPersonas.add(solicitud);
        else 
            throw new Exception("La solicitud ya existe");
    }

    @Override
    public void eliminarSolicitud(SolicitudCredito<Persona> solicitud) throws Exception {
        if(listaSolicitudesPersonas.contains(solicitud)) 
            listaSolicitudesPersonas.remove(solicitud);
        else 
            throw new Exception("No se encontró la solicitud");
    }

    @Override
    public void editarSolicitud(SolicitudCredito<Persona> solicitud) {
        Collections.sort(listaSolicitudesPersonas, Comparator.comparingLong(SolicitudCredito<Persona>::getId));
        int left = 0;
        int right = listaSolicitudesPersonas.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
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

    @Override
    public List<SolicitudCredito<Persona>> listarSolicitudes() {
        if(listaSolicitudesPersonas != null) return listaSolicitudesPersonas;
        return new ArrayList<>();
    }
}