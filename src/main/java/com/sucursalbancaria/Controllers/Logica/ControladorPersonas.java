package com.sucursalbancaria.Controllers.Logica;


import java.util.List;
import java.util.ArrayList;

import com.sucursalbancaria.Models.Solicitantes.Persona;

public class ControladorPersonas {


    public List<Persona> listaPersonas;

    public ControladorPersonas(){ listaPersonas = new ArrayList<>(); }


    //crear
    public void agregarPersona(Persona persona) throws Exception{

        if(!listaPersonas.contains(persona)){

            listaPersonas.add(persona);

            System.out.println("persona agregada con exito");
        }
        else throw new Exception("esa persona ya existe");
    }

    //eliminar

    public void eliminarPersona(Persona persona) throws Exception {

        if(listaPersonas.contains(persona)) listaPersonas.remove(persona);

        else throw new Exception("no se encontro el dato especificado");
    }


    //editar
    public void editarPersona(Persona persona){

        List<Persona> listaOrdenada = listaPersonas;

        ordenarPorCI(listaOrdenada, 0, listaPersonas.size() - 1);

        int left = 0;
        int right = listaOrdenada.size() - 1;

        while (left <= right) {
            
            int mid = (left + right)/2;

            if(listaOrdenada.get(mid).getCI() == persona.getCI()){

                listaPersonas.set(mid, persona);

                break;
            }
            else if(listaOrdenada.get(mid).getCI() < persona.getCI()){

                left = mid + 1;
            }
            else right = mid - 1;
        }
    }


    //listar
    public List<Persona> listarPersonas(){
        
        if(listaPersonas != null) return listaPersonas;

        return null;
    }
    

    //utilidad
    public void ordenarPorCI(List<Persona> lista, int low, int high){

        if(low < high) {
            int pivote = partirLista(lista, low, high);

            ordenarPorCI(lista, low, pivote - 1);
            ordenarPorCI(lista, pivote + 1, high);
        }
    }
    public int partirLista(List<Persona> lista, int high, int low){

        Long pivote = lista.get(high).getCI();
        int i = low - 1;

        for(int j = 0; j < high; j ++){

            if(lista.get(j).getCI() <= pivote){
                i++;
                intercambiar(lista, i, j);
            }
        }

        intercambiar(lista, i + 1, high);
        return i + 1;
    }
    public void intercambiar(List<Persona> lista, int a, int b){

        Persona temp = lista.get(a);
        lista.set(a, lista.get(b));
        lista.set(b, temp);
    }
}
