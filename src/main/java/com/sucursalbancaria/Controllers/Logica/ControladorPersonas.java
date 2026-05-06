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

    public ControladorPersonas(){ mapaPersonas = new HashMap<>(); }


    //crear
    public void agregarPersona(Persona persona) {

        if(mapaPersonas.isEmpty()) mapaPersonas.put(persona.getCI(), persona);

        
        if(!mapaPersonas.values().contains(persona)){

            mapaPersonas.put(persona.getCI(), persona);

            System.out.println("persona agregada con exito");

        }
    
    }

    //eliminar

    public void eliminarPersona(Persona persona) throws Exception {

        if(mapaPersonas.values().contains(persona)) mapaPersonas.remove(persona.getCI());

        else throw new Exception("no se encontro el dato especificado");
    }


    //editar
    public void editarPersona(Persona persona){

        

    }

    public List<Persona> puedenRecibirCredito(){

        List<Persona> listaOrdenada = new ArrayList<>((List<Persona>)mapaPersonas.values());

        Collections.sort((List<Persona>)mapaPersonas.values());

        return listaOrdenada.stream().
                            filter(persona -> persona.capacidadPago() > 100)
                            .collect(Collectors.toList());

    }


    //listar
    public List<Persona> listarPersonas(){
        
        if(!mapaPersonas.isEmpty()) return (List<Persona>)mapaPersonas.values();

        return null;
    }
    
}
