package com.sucursalbancaria.Controllers.ControlVistas;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sucursalbancaria.Controllers.Logica.PersistenciaArchivo;
import com.sucursalbancaria.Controllers.Logica.SucursalBancaria;
import com.sucursalbancaria.Models.Solicitantes.Empresa;
import com.sucursalbancaria.Models.Solicitantes.Persona;
import com.sucursalbancaria.Models.Solicitantes.Solicitante;
import com.sucursalbancaria.Models.Solicitudes.SolicitudCredito;
import com.sucursalbancaria.Models.Solicitudes.SolicitudEmpresarial;
import com.sucursalbancaria.Models.Solicitudes.SolicitudPersonal;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    private SucursalBancaria sucursalBancaria = new SucursalBancaria();
    private GestorSolicitantes gestorSolicitantes;
    private GestorSolicitudes gestorSolicitudes;

    @FXML private TabPane tabPaneObject;
    @FXML private MenuItem personasTiempo, empresasTiempo;
    @FXML private MenuItem guardarEstadoMenuItem, cargarEstadoMenuItem;
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
        configurarTabla(tablaSolicitudes, nombresColumnasSolicitud(), SolicitudPersonal.class);
        
        manejarAccionBotonesTiempo(personasTiempo);
        manejarAccionBotonesTiempo(empresasTiempo);
        
        // Cargar datos guardados automáticamente al iniciar
        if(PersistenciaArchivo.existenDatosGuardados()) {
            try {
                PersistenciaArchivo.cargarTodo(
                    sucursalBancaria.controladorEmpresas,
                    sucursalBancaria.controladorPersonas,
                    sucursalBancaria.controladorSolicitudEmpresarial,
                    sucursalBancaria.controladorSolicitudPersonal
                );
                actualizarTablasDesdeDatosCargados();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("No se pudieron cargar datos previos: " + e.getMessage());
            }
        }
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
                tablaEmpresas.refresh();

        } else if ("Personas".equals(tipo)) {
            gestorSolicitantes.procesarSolicitantes(
                tablaPersonas.getSelectionModel().getSelectedItems(),
                sucursalBancaria.controladorPersonas::listarPersonas,
                sucursalBancaria.controladorPersonas::agregarPersona,
                sucursalBancaria.controladorPersonas::editarPersona,
                gestorSolicitantes::tieneCamposValidos,
                "persona",
                tablaPersonas);
                tablaPersonas.refresh();
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
    public void cerrarApp() {
        System.exit(0);
    }

    // Requisito 4: Personas que pueden recibir crédito
    @FXML
    public void rendPersonasAcreditables() {
        crearTabResultados("Personas que pueden recibir créditos (Capacidad >= 100)", 
            sucursalBancaria.controladorPersonas.puedenRecibirCredito());
    }

    // Requisito 5: Empresas que pueden recibir crédito (ordenadas por ministerio y código)
    @FXML
    public void rendEmpresasAcreditables() {
        crearTabResultados("Empresas que pueden recibir créditos (por ministerio y código)", 
            sucursalBancaria.controladorEmpresas.puedenRecibirCredito());
    }
    
    // Requisito 6: Tiempo de pago por persona (CI y tiempo)
    @FXML
    public void listadoTiemposPagoPersonas() {
        if(tabExiste("Tiempos de pago - Personas")) return;
        
        Tab tab = new Tab("Tiempos de pago - Personas");
        TableView<Object[]> tableView = new TableView<>();
        
        TableColumn<Object[], String> colCI = new TableColumn<>("Carné de Identidad");
        TableColumn<Object[], Integer> colTiempo = new TableColumn<>("Tiempo (días)");
        
        colCI.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty((String)cellData.getValue()[0]));
        colTiempo.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty((Integer)cellData.getValue()[1]).asObject());
        
        // Cargar datos
        javafx.collections.ObservableList<Object[]> datos = FXCollections.observableArrayList();
        for(Map.Entry<String, Integer> entry : sucursalBancaria.controladorPersonas.getTiemposPagoPersonas().entrySet()) {
            datos.add(new Object[]{entry.getKey(), entry.getValue()});
        }
        tableView.setItems(datos);
        
        tableView.getColumns().addAll(colCI, colTiempo);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        
        tab.setContent(tableView);
        tabPaneObject.getTabs().add(tab);
    }
    
    // Requisito 7: Personas con capacidad de pago > 200
    @FXML
    public void listadoCapacidadPagoMayor200() {
        if(tabExiste("Capacidad de pago > 200")) return;
        
        Tab tab = new Tab("Capacidad de pago > 200");
        TableView<Persona> tableView = new TableView<>();
        
        TableColumn<Persona, String> colNombre = new TableColumn<>("Nombre");
        TableColumn<Persona, String> colCI = new TableColumn<>("Carné de Identidad");
        TableColumn<Persona, Double> colCapacidad = new TableColumn<>("Capacidad de Pago");

        TablaConfigurador.configurarColumnaDouble(colCapacidad, "Capacidad de Pago");
        
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreSolicitante"));
        colCI.setCellValueFactory(new PropertyValueFactory<>("CI"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidadPago"));
        
        tableView.getColumns().addAll(colNombre, colCI, colCapacidad);
        tableView.setItems(FXCollections.observableArrayList(
            sucursalBancaria.controladorPersonas.personasCapacidadMayor200()));
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        
        tab.setContent(tableView);
        tabPaneObject.getTabs().add(tab);
    }
    
    // Requisito 8: Tiempo de pago por empresa
    @FXML
    public void listadoTiemposPagoEmpresas() {
        if(tabExiste("Tiempos de pago - Empresas")) return;
        
        Tab tab = new Tab("Tiempos de pago - Empresas");
        TableView<Object[]> tableView = new TableView<>();
        
        TableColumn<Object[], String> colEmpresa = new TableColumn<>("Empresa");
        TableColumn<Object[], Integer> colTiempo = new TableColumn<>("Tiempo (días)");
        
        colEmpresa.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty((String)cellData.getValue()[0]));
        colTiempo.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleIntegerProperty((Integer)cellData.getValue()[1]).asObject());
        
        // Cargar datos
        javafx.collections.ObservableList<Object[]> datos = FXCollections.observableArrayList();
        for(Map.Entry<String, Integer> entry : sucursalBancaria.controladorEmpresas.getTiemposPagoEmpresas().entrySet()) {
            datos.add(new Object[]{entry.getKey(), entry.getValue()});
        }
        tableView.setItems(datos);
        
        tableView.getColumns().addAll(colEmpresa, colTiempo);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        
        tab.setContent(tableView);
        tabPaneObject.getTabs().add(tab);
    }
    
    // Requisito 9: Guardar estado
    @FXML
    public void guardarEstado() {
        try {
            PersistenciaArchivo.guardarTodo(
                sucursalBancaria.controladorEmpresas,
                sucursalBancaria.controladorPersonas,
                sucursalBancaria.controladorSolicitudEmpresarial,
                sucursalBancaria.controladorSolicitudPersonal
            );
            UtilidadesVista.mostrarInfo("Éxito", "Estado guardado correctamente");
        } catch (IOException e) {
            UtilidadesVista.mostrarError("Error al guardar", e.getMessage());
        }
    }
    
    // Requisito 9: Cargar estado
    @FXML
    public void cargarEstado() {
        try {
            PersistenciaArchivo.cargarTodo(
                sucursalBancaria.controladorEmpresas,
                sucursalBancaria.controladorPersonas,
                sucursalBancaria.controladorSolicitudEmpresarial,
                sucursalBancaria.controladorSolicitudPersonal
            );
            actualizarTablasDesdeDatosCargados();
            UtilidadesVista.mostrarInfo("Éxito", "Estado cargado correctamente");
        } catch (IOException | ClassNotFoundException e) {
            UtilidadesVista.mostrarError("Error al cargar", e.getMessage());
        }
    }
    
    private void actualizarTablasDesdeDatosCargados() {
        tablaEmpresas.getItems().clear();
        tablaEmpresas.getItems().addAll(sucursalBancaria.controladorEmpresas.listarEmpresas());
        
        tablaPersonas.getItems().clear();
        tablaPersonas.getItems().addAll(sucursalBancaria.controladorPersonas.listarPersonas());
        
        tablaSolicitudes.getItems().clear();
        for(SolicitudCredito<Empresa> s : sucursalBancaria.controladorSolicitudEmpresarial.listarSolicitudes()) {
            tablaSolicitudes.getItems().add(s);
        }
        for(SolicitudCredito<Persona> s : sucursalBancaria.controladorSolicitudPersonal.listarSolicitudes()) {
            tablaSolicitudes.getItems().add(s);
        }
        
        tablaEmpresas.refresh();
        tablaPersonas.refresh();
        tablaSolicitudes.refresh();
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
            listadoTiemposPagoPersonas();
        } else if (texto.contains("Empresas")) {
            listadoTiemposPagoEmpresas();
        }
    }
    
    private void manejarAccionBotonesTiempo(MenuItem menuItem) {
        menuItem.setOnAction(event -> demoraPago(menuItem.getText()));
    }
    
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

    @FXML
    public void mostrarAcercaDe() {
        UtilidadesVista.mostrarInfo("Acerca de", 
            "Sucursal Bancaria\nVersión 1.0\n\n" +
            "Requisitos implementados:\n" +
            "CRUD de empresas, personas y solicitudes\n" +
            "Personas que pueden recibir crédito (ordenado por CI)\n" +
            "Empresas que pueden recibir crédito (por ministerio y código)\n" +
            "Listado con CI y tiempo de pago de personas\n" +
            "Personas con capacidad de pago > 200\n" +
            "Listado por empresa y tiempo de pago\n" +
            "Persistencia en ficheros\n" +
            "Interfaz gráfica con JavaFX");
    }

}