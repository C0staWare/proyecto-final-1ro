package com.sucursalbancaria.Controllers.ControlVistas;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.sucursalbancaria.Controllers.Logica.SucursalBancaria;
import com.sucursalbancaria.Models.Solicitantes.Empresa;
import com.sucursalbancaria.Models.Solicitantes.Persona;
import com.sucursalbancaria.Models.Solicitantes.Solicitante;
import com.sucursalbancaria.Models.Solicitudes.SolicitudCredito;
import com.sucursalbancaria.Models.Solicitudes.SolicitudEmpresarial;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    private SucursalBancaria sucursalBancaria = new SucursalBancaria();
    private GestorSolicitantes gestorSolicitantes;
    private GestorSolicitudes gestorSolicitudes;

    @FXML private TabPane tabPaneObject;
    @FXML private MenuItem nuevoBtn, personasTiempo, empresasTiempo;
    @FXML private TableView<Empresa> tablaEmpresas;
    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableView<SolicitudCredito<? extends Solicitante>> tablaSolicitudes;
    @FXML private Button nuevaFila, eliminarFila, guardarFilas, nuevaSolicitud;

    @FXML
    public void initialize() throws Exception {
        gestorSolicitantes = new GestorSolicitantes(tablaEmpresas, tablaPersonas);
        gestorSolicitudes = new GestorSolicitudes(tablaSolicitudes);
        
        configurarTabla(tablaEmpresas, nombresColumnasEmpresa(), Empresa.class);
        configurarTabla(tablaPersonas, nombresColumnasPersona(), Persona.class);
        configurarTabla(tablaSolicitudes, nombresColumnasSolicitud(), SolicitudEmpresarial.class);
        
        manejarAccionBotonesTiempo(personasTiempo);
        manejarAccionBotonesTiempo(empresasTiempo);
    }
    
    private <T> void configurarTabla(TableView<?> tabla, Map<String, String> columnas, Class<T> clazz) {
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tabla.setEditable(true);
        tabla.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tabla.setItems(FXCollections.observableArrayList());
        TablaConfigurador.configurarColumnas(tabla, columnas, clazz);
    }

    @FXML
    public void guardarSolicitante() {
        String tipo = tabPaneObject.getSelectionModel().getSelectedItem().getText();
        
        if ("Empresas".equals(tipo)) {
            gestorSolicitantes.procesarSolicitantes(
                tablaEmpresas.getSelectionModel().getSelectedItems(),
                sucursalBancaria.controladorEmpresas::listarEmpresas,
                sucursalBancaria.controladorEmpresas::crearEmpresa,
                sucursalBancaria.controladorEmpresas::editarEmpresa,
                gestorSolicitantes::tieneCamposValidos,
                "empresa",
                tablaEmpresas);
        } else if ("Personas".equals(tipo)) {
            gestorSolicitantes.procesarSolicitantes(
                tablaPersonas.getSelectionModel().getSelectedItems(),
                sucursalBancaria.controladorPersonas::listarPersonas,
                sucursalBancaria.controladorPersonas::agregarPersona,
                sucursalBancaria.controladorPersonas::editarPersona,
                gestorSolicitantes::tieneCamposValidos,
                "persona",
                tablaPersonas);
        }
    }

    @FXML
    public void agregarFila() throws Exception {
        String tipo = tabPaneObject.getSelectionModel().getSelectedItem().getText();
        gestorSolicitantes.agregarFila(tipo);
    }

    @FXML
    public void eliminarFilaSeleccionada() {
        String tipo = tabPaneObject.getSelectionModel().getSelectedItem().getText();
        gestorSolicitantes.eliminarFilaSeleccionada(tipo,
            FXCollections.observableArrayList(tablaEmpresas.getSelectionModel().getSelectedItems()),
            FXCollections.observableArrayList(tablaPersonas.getSelectionModel().getSelectedItems()),
            sucursalBancaria.controladorEmpresas::listarEmpresas,
            sucursalBancaria.controladorPersonas::listarPersonas);
    }

    @FXML
    public void agregarSolicitud() {
        String tipo = tabPaneObject.getSelectionModel().getSelectedItem().getText();
        
        if ("Empresas".equals(tipo)) {
            gestorSolicitudes.manejarSolicitudEmpresa(
                tablaEmpresas.getSelectionModel().getSelectedItem(),
                sucursalBancaria.controladorSolicitudEmpresarial);
        } else if ("Personas".equals(tipo)) {
            gestorSolicitudes.manejarSolicitudPersona(
                tablaPersonas.getSelectionModel().getSelectedItem(),
                sucursalBancaria.controladorSolicitudPersonal);
        }
    }

    @FXML
    public void nuevoArchivo() {
        Optional<String> resultado = UtilidadesVista.pedirNombreArchivo();
        
        resultado.ifPresent(nombre -> {
            File carpeta = new File("Datos");
            if (!carpeta.exists()) carpeta.mkdir();
            
            File archivo = new File(carpeta, nombre + ".bin");
            try {
                if (archivo.createNewFile()) {
                    UtilidadesVista.mostrarInfo("Éxito", "Archivo creado correctamente");
                } else {
                    UtilidadesVista.mostrarError("Error", "No se pudo crear el archivo");
                }
            } catch (IOException err) {
                UtilidadesVista.mostrarError("Error", err.getMessage());
            }
        });
    }

    @FXML
    public void cerrarApp() {
        System.exit(0);
    }

    @FXML
    public void rendPersonasAcreditables() {
        crearTabResultados("Personas que pueden recibir créditos", 
            sucursalBancaria.controladorPersonas.puedenRecibirCredito());
    }

    @FXML
    public void rendEmpresasAcreditables() {
        crearTabResultados("Empresas que pueden recibir créditos", 
            sucursalBancaria.controladorEmpresas.puedenRecibirCredito());
    }
    
    private <T> void crearTabResultados(String titulo, java.util.List<T> items) {
        if (tabExiste(titulo)) return;
        
        Tab tab = new Tab(titulo);
        TableView<T> tableView = new TableView<>();
        tableView.setItems(FXCollections.observableArrayList(items));
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tab.setContent(tableView);
        tabPaneObject.getTabs().add(tab);
    }
    
    private boolean tabExiste(String titulo) {
        return tabPaneObject.getTabs().stream().anyMatch(t -> titulo.equals(t.getText()));
    }

    public void demoraPago(String texto) {
        if (texto.contains("Personas")) {
            mostrarTabTiempo("Personas - Tiempo que demorarán en pagar", 
                tablaPersonas.getItems(), "nombreSolicitante", "demoraPago");
        } else if (texto.contains("Empresas")) {
            mostrarTabTiempo("Empresas - Tiempo que demorarán en pagar", 
                tablaEmpresas.getItems(), "nombreSolicitante", "demoraPago");
        }
    }
    
    private <T> void mostrarTabTiempo(String titulo, javafx.collections.ObservableList<T> items, 
                                      String campoNombre, String campoTiempo) {
        if (tabExiste(titulo)) return;
        
        Tab tab = new Tab(titulo);
        TableView<T> tableView = new TableView<>();
        TableColumn<T, String> colNombre = new TableColumn<>("Nombre");
        TableColumn<T, Integer> colTiempo = new TableColumn<>("Tiempo (días)");
        
        colNombre.setCellValueFactory(new PropertyValueFactory<>(campoNombre));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>(campoTiempo));
        
        tableView.getColumns().addAll(colNombre, colTiempo);
        tableView.setItems(items);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        
        tab.setContent(tableView);
        tabPaneObject.getTabs().add(tab);
    }

    private void manejarAccionBotonesTiempo(MenuItem menuItem) {
        menuItem.setOnAction(event -> demoraPago(menuItem.getText()));
    }
    
    // Mapas de columnas (se mantienen igual)
    private Map<String, String> nombresColumnasEmpresa() {
        Map<String, String> nombres = new HashMap<>();
        nombres.put("Nombre", "nombreSolicitante");
        nombres.put("Valor del Crédito", "valorCredito");
        nombres.put("Dirección", "direccionSolicitante");
        nombres.put("Director", "nombreDirector");
        nombres.put("Dirección del Director", "direccionDirector");
        nombres.put("Ganancia Promedio Anual", "gananciaPromedioAnual");
        nombres.put("Cantidad de Trabajadores", "cantidadTrabajadores");
        nombres.put("Ministerio", "ministerio");
        nombres.put("Código", "codigo");
        return nombres;
    }
    
    private Map<String, String> nombresColumnasPersona() {
        Map<String, String> nombres = new HashMap<>();
        nombres.put("Nombre", "nombreSolicitante");
        nombres.put("Valor del Crédito", "valorCredito");
        nombres.put("Dirección", "direccionSolicitante");
        nombres.put("CI", "CI");
        nombres.put("Salario del Núcleo Familiar", "salarioNucleo");
        nombres.put("Personas que Sustenta", "personasQueSustenta");
        return nombres;
    }
    
    private Map<String, String> nombresColumnasSolicitud() {
        Map<String, String> nombres = new HashMap<>();
        nombres.put("Tipo", "tipoSolicitud");
        nombres.put("Estado", "estadoSolicitud");
        nombres.put("A nombre de", "nombreSolicitante");
        nombres.put("Mensualidad", "mensualidad");
        return nombres;
    }
}