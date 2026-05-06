package com.sucursalbancaria.Controllers.Logica;

import java.util.List;

import java.util.ArrayList;
import java.util.Comparator;

import com.sucursalbancaria.Models.Solicitantes.Empresa;

public class ControladorEmpresas {

    List<Empresa> listaEmpresas;

    public ControladorEmpresas() { listaEmpresas = new ArrayList<>(); }


    //crear
    public void crearEmpresa(Empresa empresa){

        try{
            if(!listaEmpresas.contains(empresa)){
            empresa.comprobarRequisitoGanancia();
            listaEmpresas.add(empresa);
            System.out.println("Empresa agregada con exito");
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

    }

    //eliminar
    public void eliminarEmpresa(Empresa empresa) throws Exception {

        if(listaEmpresas.contains(empresa)) listaEmpresas.remove(empresa);

        else throw new Exception("No se encontro el dato solicitado");
    }

    //editar
    public void editarEmpresa(Empresa empresa) {
        
        ordenarPorCodigo(listaEmpresas, 0, listaEmpresas.size() - 1);
        
        int left = 0;
        int right = listaEmpresas.size() - 1;
        boolean encontrado = false;
        
        while(left <= right && !encontrado) {
            int mid = left + (right - left) / 2;
            
            if(listaEmpresas.get(mid).getCodigo().equals(empresa.getCodigo())) {
                for(int i = 0; i < listaEmpresas.size(); i++) {
                    if(listaEmpresas.get(i).getCodigo().equals(empresa.getCodigo())) {
                        listaEmpresas.set(i, empresa);
                        encontrado = true;
                        break;
                    }
                }
            }
            else if(listaEmpresas.get(mid).getCodigo() < empresa.getCodigo()) {
                left = mid + 1;
            }
            else {
                right = mid - 1;
            }
        }
    }

    //listar
    public List<Empresa> listarEmpresas(){
        
        if(listaEmpresas != null) return listaEmpresas;
        
        return null;
    }
    //ordenar por ministerio y codigo
    public void ordenarMinisterioCodigo(List<Empresa> lista){

        lista.sort(Comparator.comparing(Empresa::getMinisterio).thenComparing(Empresa::getCodigo));

    }

    public List<Empresa> puedenRecibirCredito(){

        List<Empresa> listaOrdenada = listaEmpresas;

        ordenarMinisterioCodigo(listaOrdenada);

        return listaOrdenada;

    }

    //utilidad
    public void ordenarPorCodigo(List<Empresa> lista, int low, int high) {

        if(low < high){

            int pivote = partirLista(lista, low, high);

            ordenarPorCodigo(lista, low, pivote - 1);
            ordenarPorCodigo(lista, pivote + 1, high);
        }
        
    }

    public int partirLista(List<Empresa> lista, int low, int high) {

        Long pivote = lista.get(high).getCodigo();
        int i = low - 1;
        
        for(int j = low; j < high; j++) { 
            if(lista.get(j).getCodigo() <= pivote) { 
                i++;
                intercambiar(lista, i, j);
            }
        }
        intercambiar(lista, i + 1, high);
        return i + 1;
    }

    public void intercambiar(List<Empresa> lista, int a, int b) {

        Empresa temp = lista.get(a);
        lista.set(a, lista.get(b)); 
        lista.set(b, temp);
    }
}
