package com.sucursalbancaria.Controllers.Logica;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import com.sucursalbancaria.Models.Solicitantes.Empresa;

public class ControladorEmpresas {

    public Map<Long, Empresa> mapaEmpresas;

    public ControladorEmpresas() { 
        mapaEmpresas = new HashMap<>(); 
    }

    public void crearEmpresa(Empresa empresa){
        if(!mapaEmpresas.containsKey(empresa.getCodigo())){
            mapaEmpresas.put(empresa.getCodigo(), empresa);
        }
    }

    public void eliminarEmpresa(Empresa empresa) throws Exception {
        if(mapaEmpresas.containsKey(empresa.getCodigo())){
            mapaEmpresas.remove(empresa.getCodigo());
        } else {
            throw new Exception("No se encontró el dato solicitado");
        }
    }

    public void editarEmpresa(Empresa empresa) {
        if(mapaEmpresas.containsKey(empresa.getCodigo())){
            mapaEmpresas.replace(empresa.getCodigo(), empresa);
        }
    }

    public List<Empresa> listarEmpresas(){
        return new ArrayList<>(mapaEmpresas.values());
    }

    public void ordenarMinisterioCodigo(List<Empresa> lista){
        lista.sort(Comparator.comparing(Empresa::getMinisterio)
                  .thenComparing(Empresa::getCodigo));
    }

    // Requisito 5: Empresas que pueden recibir crédito, ordenadas por ministerio y código
    public List<Empresa> puedenRecibirCredito(){
        List<Empresa> lista = new ArrayList<>(mapaEmpresas.values());
        List<Empresa> resultado = new ArrayList<>();
        for(Empresa e : lista){
            try {
                e.comprobarRequisitoGanancia();
                resultado.add(e);
            } catch(Exception ex) {}
        }
        ordenarMinisterioCodigo(resultado);
        return resultado;
    }

    // Requisito 8: Tiempos de pago por empresa
    public Map<String, Integer> getTiemposPagoEmpresas(){
        Map<String, Integer> resultados = new HashMap<>();
        for(Empresa e : mapaEmpresas.values()){
            resultados.put(e.getNombreSolicitante(), e.getDemoraPago());
        }
        return resultados;
    }

    public void ordenarPorCodigo(List<Empresa> lista) {
        Collections.sort(lista);
    }
}