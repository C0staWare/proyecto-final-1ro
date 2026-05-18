package com.sucursalbancaria.Controllers.Logica;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.sucursalbancaria.Models.Solicitantes.Persona;

public class ControladorPersonas {

    public Map<String, Persona> mapaPersonas;

    public ControladorPersonas(){ 
        mapaPersonas = new HashMap<>(); 
    }

    public void agregarPersona(Persona persona) {
        if(!mapaPersonas.containsKey(persona.getCI())){
            mapaPersonas.put(persona.getCI(), persona);
        }
    }

    public void eliminarPersona(Persona persona) throws Exception {
        if(mapaPersonas.containsKey(persona.getCI())){
            mapaPersonas.remove(persona.getCI());
        } else {
            throw new Exception("No se encontró el dato especificado");
        }
    }

    public void editarPersona(Persona persona){
        if(mapaPersonas.containsKey(persona.getCI())){
            mapaPersonas.replace(persona.getCI(), persona);
        }
    }

    // Requisito 4: Personas que pueden recibir crédito (capacidadPago >= 100)
    public List<Persona> puedenRecibirCredito(){
        List<Persona> lista = new ArrayList<>(mapaPersonas.values());
        lista = lista.stream()
            .filter(p -> p.capacidadPago() >= 100)
            .collect(Collectors.toList());
        Collections.sort(lista);
        return lista;
    }

    // Requisito 7: Personas con capacidad de pago > 200
    public List<Persona> personasCapacidadMayor200(){
        return mapaPersonas.values().stream()
            .filter(p -> p.capacidadPago() > 200)
            .collect(Collectors.toList());
    }

    public List<Persona> listarPersonas(){
        return new ArrayList<>(mapaPersonas.values());
    }
    
    public Map<String, Integer> getTiemposPagoPersonas(){
        Map<String, Integer> resultados = new HashMap<>();
        for(Persona p : mapaPersonas.values()){
            resultados.put(p.getCI(), p.getDemoraPago());
        }
        return resultados;
    }
}