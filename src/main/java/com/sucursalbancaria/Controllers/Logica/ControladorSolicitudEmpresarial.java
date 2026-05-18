package com.sucursalbancaria.Controllers.Logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.sucursalbancaria.Models.Solicitantes.Empresa;
import com.sucursalbancaria.Models.Solicitudes.SolicitudCredito;
import com.sucursalbancaria.Models.Solicitudes.SolicitudEmpresarial;

public class ControladorSolicitudEmpresarial implements ControladorSolicitud<Empresa> {
    
    public List<SolicitudCredito<Empresa>> listaSolicitudesEmpresas;

    public ControladorSolicitudEmpresarial() { 
        listaSolicitudesEmpresas = new ArrayList<>(); 
    }

    @Override
    public boolean existeSolicitud(Empresa empresa) {
        return listaSolicitudesEmpresas.stream()
            .filter(s -> s instanceof SolicitudEmpresarial)
            .map(s -> (SolicitudEmpresarial) s)
            .anyMatch(s -> s.getEmpresa().getCodigo().equals(empresa.getCodigo()));
    }

    @Override
    public void crearSolicitud(SolicitudCredito<Empresa> solicitud) throws Exception {
        if(!listaSolicitudesEmpresas.contains(solicitud)) 
            listaSolicitudesEmpresas.add(solicitud);
        else 
            throw new Exception("La solicitud ya existe");
    }

    @Override
    public void eliminarSolicitud(SolicitudCredito<Empresa> solicitud) throws Exception {
        if(listaSolicitudesEmpresas.contains(solicitud)) 
            listaSolicitudesEmpresas.remove(solicitud);
        else 
            throw new Exception("No se encontró esa solicitud");
    }

    @Override
    public void editarSolicitud(SolicitudCredito<Empresa> solicitud) {
        Collections.sort(listaSolicitudesEmpresas, Comparator.comparingLong(SolicitudCredito<Empresa>::getId));
        int left = 0;
        int right = listaSolicitudesEmpresas.size() - 1;
        while(left <= right){
            int mid = left + (right - left)/2;
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

    @Override
    public List<SolicitudCredito<Empresa>> listarSolicitudes() {
        if(listaSolicitudesEmpresas != null) return listaSolicitudesEmpresas; 
        return new ArrayList<>();
    }
}