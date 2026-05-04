package com.sucursalbancaria.Controllers.ControlVistas;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import com.sucursalbancaria.Models.Solicitantes.Empresa;
import com.sucursalbancaria.Models.Solicitantes.Persona;
import com.sucursalbancaria.Models.Solicitantes.Solicitante;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class GestorSolicitantes {
    
    private final TableView<Empresa> tablaEmpresas;
    private final TableView<Persona> tablaPersonas;
    
    public GestorSolicitantes(TableView<Empresa> tablaEmpresas, TableView<Persona> tablaPersonas) {
        this.tablaEmpresas = tablaEmpresas;
        this.tablaPersonas = tablaPersonas;
    }
    
    public void agregarFila(String tipo) throws Exception {
        if ("Empresas".equals(tipo)) {
            tablaEmpresas.getItems().add(new Empresa());
        } else if ("Personas".equals(tipo)) {
            tablaPersonas.getItems().add(new Persona());
        }
    }
    
    public void eliminarFilaSeleccionada(String tipo, 
                                          ObservableList<Empresa> empresasSeleccionadas,
                                          ObservableList<Persona> personasSeleccionadas,
                                          Supplier<List<Empresa>> listarEmpresas,
                                          Supplier<List<Persona>> listarPersonas) {
        if ("Empresas".equals(tipo)) {
            tablaEmpresas.getItems().removeAll(empresasSeleccionadas);
            listarEmpresas.get().removeAll(empresasSeleccionadas);
        } else if ("Personas".equals(tipo)) {
            tablaPersonas.getItems().removeAll(personasSeleccionadas);
            listarPersonas.get().removeAll(personasSeleccionadas);
        }
    }
    
    public <T extends Solicitante> void procesarSolicitantes(
            List<T> seleccionados,
            Supplier<List<T>> listar,
            Consumer<T> crear,
            Consumer<T> editar,
            Predicate<T> validar,
            String tipo,
            TableView<?> tabla) {
        
        for (T solicitante : seleccionados) {
            try {
                if (!validar.test(solicitante)) {
                    UtilidadesVista.mostrarCamposInvalidos();
                    continue;
                }
                
                boolean existe = listar.get().contains(solicitante);
                
                if (existe) {
                    if (!UtilidadesVista.confirmarSobrescritura(tipo)) {
                        continue;
                    }
                    editar.accept(solicitante);
                } else {
                    crear.accept(solicitante);
                }
            } catch (Exception e) {
                int fila = obtenerFila(solicitante, tipo);
                UtilidadesVista.mostrarError("Error", e.getMessage() + " (" + tipo + " en la fila: " + fila + ")");
            }
        }
        
        tabla.refresh();
    }
    
    private int obtenerFila(Solicitante solicitante, String tipo) {
        if ("empresa".equalsIgnoreCase(tipo)) {
            return tablaEmpresas.getItems().indexOf(solicitante) + 1;
        } else {
            return tablaPersonas.getItems().indexOf(solicitante) + 1;
        }
    }
    
    // Métodos de validación
    public boolean tieneCamposValidos(Empresa e) {
        return e.getNombreSolicitante() != null && !e.getNombreSolicitante().isEmpty()
            && e.getValorCredito() != null
            && e.getDireccionSolicitante() != null && !e.getDireccionSolicitante().isEmpty()
            && e.getDireccionDirector() != null && !e.getDireccionDirector().isEmpty()
            && e.getNombreDirector() != null && !e.getNombreDirector().isEmpty()
            && e.getGananciaPromedioAnual() != null
            && e.getCantidadTrabajadores() != 0
            && e.getCodigo() != null
            && e.getMinisterio() != null && !e.getMinisterio().isEmpty();
    }
    
    public boolean tieneCamposValidos(Persona p) {
        return p.getNombreSolicitante() != null && !p.getNombreSolicitante().isEmpty()
            && p.getValorCredito() != null
            && p.getDireccionSolicitante() != null && !p.getDireccionSolicitante().isEmpty()
            && p.getCI() != null
            && p.getPersonasQueSustenta() >= 1
            && p.getSalarioNucleo() > 0;
    }
}