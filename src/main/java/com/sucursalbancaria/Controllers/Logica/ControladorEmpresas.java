package com.sucursalbancaria.Controllers.Logica;

import java.util.List;

import java.util.ArrayList;
import java.util.Comparator;

import com.sucursalbancaria.Models.Solicitantes.Empresa;

public class ControladorEmpresas {

    List<Empresa> listaEmpresas;

    public ControladorEmpresas() { listaEmpresas = new ArrayList<>(); }


    //crear
    public void crearEmpresa(Empresa empresa) throws Exception {

        if(!listaEmpresas.contains(empresa)){

            listaEmpresas.add(empresa);
            System.out.println("Empresa agregada con exito");
        }

        else throw new Exception("Empresa ya existe");
    }

    //eliminar
    public void eliminarEmpresa(Empresa empresa) throws Exception {

        if(listaEmpresas.contains(empresa)) listaEmpresas.remove(empresa);

        else throw new Exception("No se encontro el dato solicitado");
    }

    //editar
    public void editarEmpresa(Empresa empresa){

        List<Empresa> listaOrdenada = listaEmpresas;

        ordenarPorCodigo(listaOrdenada, 0, listaEmpresas.size() - 1);

        int left = 0;
        int right = listaOrdenada.size();

        while(left <= right){

            int mid = (left + right)/2;
            if(listaOrdenada.get(mid).getCodigo() == empresa.getCodigo()){

                listaEmpresas.set(mid, empresa);
            }
            else if(listaOrdenada.get(mid).getCodigo() < empresa.getCodigo()){

                left = mid + 1;
            }
            else right = mid - 1;
        }
    }

    //listar
    public List<Empresa> listarEmpresas(){
        
        if(listaEmpresas != null) return listaEmpresas;
        
        return null;
    }
    //ordenar por ministerio y codigo
    public void ordenarMinisterioCodigo(){

        listaEmpresas.sort(Comparator.comparing(Empresa::getMinisterio).thenComparing(Empresa::getCodigo));

    }



    //utilidad
    public void ordenarPorCodigo(List<Empresa> lista, int low, int high){

        if(low < high){

            int pivote = partirLista(lista, low, high);

            ordenarPorCodigo(lista, low, pivote - 1);
            ordenarPorCodigo(lista, pivote + 1, high);
        }
    }
    public int partirLista(List<Empresa> lista, int low, int high){

        Long pivote = listaEmpresas.get(high).getCodigo();
        int i = low - 1;

        for(int j = 0; j < listaEmpresas.size(); j ++){
            if(listaEmpresas.get(j).getCodigo() <= pivote){
                i++;
                intercambiar(lista, i, j);
            }
        }
        intercambiar(lista, i + 1, high);
        return i + 1;
    }
    public void intercambiar(List<Empresa> lista, int a, int b){
        Empresa temp = lista.get(a);
        lista.set(a, listaEmpresas.get(b));
        listaEmpresas.set(b, temp);
    }
}
