package com.sucursalbancaria.Controllers.ControlVistas;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.sucursalbancaria.Controllers.Logica.SucursalBancaria;
import com.sucursalbancaria.Models.Solicitantes.Empresa;
import com.sucursalbancaria.Models.Solicitantes.Persona;
import com.sucursalbancaria.Models.Solicitantes.Solicitante;
import com.sucursalbancaria.Models.Solicitudes.SolicitudCredito;
import com.sucursalbancaria.Models.Solicitudes.SolicitudEmpresarial;
import com.sucursalbancaria.Models.Solicitudes.SolicitudPersonal;
import com.sucursalbancaria.Controllers.Logica.ControladorSolicitud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

public class MainController {

    SucursalBancaria sucursalBancaria = new SucursalBancaria();

    @FXML
    TabPane tabPaneObject;

    @FXML
    MenuItem nuevoBtn;

    @FXML
    MenuItem personasTiempo;
    
    @FXML
    MenuItem empresasTiempo;

    @FXML
    TableView<Empresa> tablaEmpresas;
    
    @FXML
    TableView<Persona> tablaPersonas;

    @FXML
    TableView<SolicitudCredito<? extends Solicitante>> tablaSolicitudes;

    @FXML
    Button nuevaFila;

    @FXML
    Button eliminarFila;

    @FXML
    Button guardarFilas;

    @FXML
    Button nuevaSolicitud;

    @FXML
    public void initialize() throws Exception {

        tablaEmpresas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tablaPersonas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tablaSolicitudes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tablaEmpresas.setEditable(true);
        tablaEmpresas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tablaEmpresas.setItems(FXCollections.observableArrayList());
        configurarColumnas(tablaEmpresas, nombresColumnasEmpresa(), Empresa.class);

        tablaPersonas.setEditable(true);
        tablaPersonas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tablaPersonas.setItems(FXCollections.observableArrayList());
        configurarColumnas(tablaPersonas, nombresColumnasPersona(), Persona.class);

        tablaSolicitudes.setEditable(true);
        tablaSolicitudes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tablaSolicitudes.setItems(FXCollections.observableArrayList());
        configurarColumnas(tablaSolicitudes, nombresColumnasSolicitud(), SolicitudEmpresarial.class);

        manejarAccionBotonesTiempo(personasTiempo);
        manejarAccionBotonesTiempo(empresasTiempo);
        
    }

    public <T> void configurarColumnas(TableView<?> tabla, Map<String, String> nombresColumnas, Class<T> clazz) {

        tabla.getColumns().forEach(columna -> {
            String nombreCampo = nombresColumnas.get(columna.getText());
            if (nombreCampo == null) return;

            if (clazz == SolicitudEmpresarial.class || clazz == SolicitudPersonal.class) {
                columna.setCellValueFactory(new PropertyValueFactory<>(nombreCampo));
                return;
            }

            Field campo = buscarCampoEnJerarquia(clazz, nombreCampo);
            if (campo == null) {
                System.out.println("No se encontró el campo: " + nombreCampo);
                return;
            }

            configurarColumna(columna, campo, nombreCampo);
        });
    }

    @FXML
    public void guardarSolicitante() throws Exception {
        Tab seleccionado = tabPaneObject.getSelectionModel().getSelectedItem();
        String tipo = seleccionado.getText();

        try{
            if ("Empresas".equals(tipo)) {
            procesarSolicitantes(tablaEmpresas.getSelectionModel().getSelectedItems(),
                    sucursalBancaria.controladorEmpresas::listarEmpresas,
                    sucursalBancaria.controladorEmpresas::crearEmpresa,
                    sucursalBancaria.controladorEmpresas::editarEmpresa,
                    this::tieneCamposValidos,
                    "empresa");

            } else if ("Personas".equals(tipo)) {
                procesarSolicitantes(tablaPersonas.getSelectionModel().getSelectedItems(),
                        sucursalBancaria.controladorPersonas::listarPersonas,
                        sucursalBancaria.controladorPersonas::agregarPersona,
                        sucursalBancaria.controladorPersonas::editarPersona,
                        this::tieneCamposValidos,
                        "persona");
            }

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    public void agregarFila() throws Exception {

        Tab tabSeleccionada = tabPaneObject.getSelectionModel().getSelectedItem();
        if(tabSeleccionada.getText().equals("Empresas")){

            tablaEmpresas.getItems().add(new Empresa());

        }
        else if(tabSeleccionada.getText().equals("Personas")){

            tablaPersonas.getItems().add(new Persona());

        }
        else{

            
        }
    }

    @FXML
    public void eliminarFilaSeleccionada(){ 

        Tab tabSeleccionada = tabPaneObject.getSelectionModel().getSelectedItem();
        if(tabSeleccionada.getText().equals("Empresas")){

            ObservableList<Empresa> seleccionadas = FXCollections.observableArrayList(tablaEmpresas.getSelectionModel().getSelectedItems());

            tablaEmpresas.getItems().removeAll(seleccionadas);
            sucursalBancaria.controladorEmpresas.listarEmpresas().removeAll(seleccionadas);

        }
        else if(tabSeleccionada.getText().equals("Personas")){

            ObservableList<Persona> seleccionadas = FXCollections.observableArrayList(tablaPersonas.getSelectionModel().getSelectedItems());

            tablaPersonas.getItems().removeAll(seleccionadas);
            sucursalBancaria.controladorPersonas.listarPersonas().removeAll(seleccionadas);

        }
    }

    @FXML
    public void agregarSolicitud(){

        Tab seleccionado = tabPaneObject.getSelectionModel().getSelectedItem();
        String tipo = seleccionado.getText(); // "Empresas" o "Personas"

        if (tipo.equals("Empresas")) {
            manejarSolicitudEmpresa();
        } 
        else if (tipo.equals("Personas")) {
            manejarSolicitudPersona();
        }

    }

    @FXML
    public void nuevoArchivo() throws IOException {

        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Nuevo archivo");

        dialog.setHeaderText("Ingrese el nombre del archivo");

        Optional<String> resultado = dialog.showAndWait();

        resultado.ifPresent(nombre -> {
            File archivo;

            File carpeta = new File("Datos");

            if(!carpeta.exists()){

                carpeta.mkdir();

            }
            archivo = new File(carpeta, nombre+".bin");

            try{
                if(archivo.createNewFile()){
                    System.out.println("archivo creado correctamente");
                }
                else{
                    System.out.println("no se completo la creacion del archivo");
                }
            }catch(IOException err){

            }
        });
        
    }

    @FXML
    public void cerrarApp(){

        System.exit(1);

    }

    @FXML
    public void rendPersonasAcreditables(){

        Tab existente = tabPaneObject.getTabs()
                                    .stream()
                                    .filter(t -> "Personas que pueden recibir créditos".equals(t.getText()))
                                    .findFirst().orElse(null);

        if(existente == null){
            Tab tab = new Tab();

            TableView<Persona> personasAcreditables = new TableView<>();
            TableColumn<Persona, String> columnaNombre = new TableColumn<>("Nombre");
            TableColumn<Persona, Long> columnaCI = new TableColumn<>("CI");
            
            
            personasAcreditables.getColumns().addAll(columnaNombre, columnaCI);
            personasAcreditables.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
            
            personasAcreditables.getItems().setAll(sucursalBancaria.controladorPersonas.puedenRecibirCredito());
            
            columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombreSolicitante"));
            columnaCI.setCellValueFactory(new PropertyValueFactory<>("CI"));

            tab.setContent(personasAcreditables);

            tab.setText("Personas que pueden recibir créditos");

            tabPaneObject.getTabs().add(tab);

        }

    }

    @FXML
    public void rendEmpresasAcreditables(){

        Tab existente = tabPaneObject.getTabs()
                                    .stream().filter(t -> "Empresas que pueden recibir créditos".equals(t.getText()))
                                    .findFirst().orElse(null);

        if(existente == null){

            Tab tab = new Tab();

            TableView<Empresa> empresasAcreditables = new TableView<>();
            TableColumn<Empresa, String> columnaNombre = new TableColumn<>("Nombre");
            TableColumn<Empresa, String> columnaMinisterio = new TableColumn<>("Ministerio");
            TableColumn<Empresa, Long> columnaCodigo = new TableColumn<>("Código");

            empresasAcreditables.getColumns().addAll(columnaNombre, columnaMinisterio, columnaCodigo);
            empresasAcreditables.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

            empresasAcreditables.getItems().setAll(sucursalBancaria.controladorEmpresas.puedenRecibirCredito());

            columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombreSolicitante"));
            columnaMinisterio.setCellValueFactory(new PropertyValueFactory<>("ministerio"));
            columnaCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));

            tab.setContent(empresasAcreditables);

            tab.setText("Empresas que pueden recibir créditos");

            tabPaneObject.getTabs().add(tab);

        }
    }

    //UTILIDAD

    public void demoraPago(String texto){
    
        if(texto.equals("Personas - Tiempo que demorarán en pagar")) {

            Tab existente = tabPaneObject.getTabs()
                                            .stream()
                                            .filter(t -> "Personas - Tiempo que demorarán en pagar".equals(t.getText()))
                                            .findFirst()
                                            .orElse(null);

            if(existente == null){
                Tab tab  = new Tab();

                TableView<Persona> personas = new TableView<>();
                TableColumn<Persona, String> columnaNombre = new TableColumn<>("Nombre");
                TableColumn<Persona, Integer> columnaTiempo = new TableColumn<>("Tiempo que demorará en pagar(Días)");

                personas.getColumns().addAll(columnaNombre, columnaTiempo);
                personas.getItems().setAll(tablaPersonas.getItems());

                personas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

                columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombreSolicitante"));
                columnaTiempo.setCellValueFactory(new PropertyValueFactory<>("demoraPago"));

                tab.setContent(personas);

                tab.setText("Personas - Tiempo que demorarán en pagar");

                tabPaneObject.getTabs().add(tab);

            }
        }
        else if(texto.equals("Empresas - Tiempo que demorarán en pagar")) {

            Tab existente = tabPaneObject.getTabs()
                                            .stream()
                                            .filter(t -> "Empresas - Tiempo que demorarán en pagar".equals(t.getText()))
                                            .findFirst()
                                            .orElse(null);
            
            if(existente == null){

                Tab tab = new Tab();

                TableView<Empresa> empresas = new TableView<>();
                TableColumn<Empresa, String> columnaNombre = new TableColumn<>("Nombre");
                TableColumn<Empresa, Integer> columnaTiempo = new TableColumn<>("Tiempo que demorarán en pagar(Días)");

                empresas.getColumns().addAll(columnaNombre, columnaTiempo);
                empresas.getItems().setAll(tablaEmpresas.getItems());

                empresas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

                columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombreSolicitante"));
                columnaTiempo.setCellValueFactory(new PropertyValueFactory<>("demoraPago"));

                tab.setContent(empresas);

                tab.setText("Empresas - Tiempo que demorarán en pagar");

                tabPaneObject.getTabs().add(tab);

            }
        }

    }

    private void manejarAccionBotonesTiempo(MenuItem menuItem){

        menuItem.setOnAction(event -> {

            demoraPago(menuItem.getText());
        });

    }

    private boolean tieneCamposValidos(Empresa e){

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

    private boolean tieneCamposValidos(Persona p){

        return p.getNombreSolicitante() != null && !p.getNombreSolicitante().isEmpty()
            && p.getValorCredito() != null
            && p.getDireccionSolicitante() != null && !p.getDireccionSolicitante().isEmpty()
            && p.getCI() != null
            && p.getPersonasQueSustenta() >= 1
            && p.getSalarioNucleo() > 0;
    }

    private Map<String, String> nombresColumnasEmpresa() {
        Map<String, String> nombresColumnas = new HashMap<>();

        nombresColumnas.put("Nombre", "nombreSolicitante");
        nombresColumnas.put("Valor del Crédito", "valorCredito");
        nombresColumnas.put("Dirección", "direccionSolicitante");
        nombresColumnas.put("Director", "nombreDirector");
        nombresColumnas.put("Dirección del Director", "direccionDirector");
        nombresColumnas.put("Ganancia Promedio Anual", "gananciaPromedioAnual");
        nombresColumnas.put("Cantidad de Trabajadores", "cantidadTrabajadores");
        nombresColumnas.put("Ministerio", "ministerio");
        nombresColumnas.put("Código", "codigo");

        return nombresColumnas;
    }

    private Map<String, String> nombresColumnasPersona() {
        Map<String, String> nombresColumnas = new HashMap<>();

        nombresColumnas.put("Nombre", "nombreSolicitante");
        nombresColumnas.put("Valor del Crédito", "valorCredito");
        nombresColumnas.put("Dirección", "direccionSolicitante");
        nombresColumnas.put("CI", "CI");
        nombresColumnas.put("Salario del Núcleo Familiar", "salarioNucleo");
        nombresColumnas.put("Personas que Sustenta", "personasQueSustenta");

        return nombresColumnas;
    }

    private Map<String, String> nombresColumnasSolicitud(){

        Map<String, String> nombresColumnas = new HashMap<>();

        nombresColumnas.put("Tipo", "tipoSolicitud");
        nombresColumnas.put("Estado", "estadoSolicitud");
        nombresColumnas.put("A nombre de", "nombreSolicitante");
        nombresColumnas.put("Mensualidad", "mensualidad");

        return nombresColumnas;
    }

    private void manejarSolicitudEmpresa(){

        Empresa seleccionada = tablaEmpresas.getSelectionModel().getSelectedItem();

        procesarSolicitud( 

            seleccionada,
            SolicitudEmpresarial::new,
            sucursalBancaria.controladorSolicitudEmpresarial
        );
    }

    private void manejarSolicitudPersona(){

        Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();

        procesarSolicitud(

            seleccionada,
            SolicitudPersonal::new,
            sucursalBancaria.controladorSolicitudPersonal
        );
    }

    private <T extends Solicitante> void procesarSolicitud(T solicitante, Function<T, SolicitudCredito<T>> creadorSolicitud, ControladorSolicitud<T> controlador) {

        if (solicitante == null) return;

        boolean existe = controlador.existeSolicitud(solicitante);

        if (!existe) {
            SolicitudCredito<T> nueva = creadorSolicitud.apply(solicitante);
            controlador.listarSolicitudes().add(nueva);
            tablaSolicitudes.getItems().add(nueva);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Información");
        alert.setHeaderText("Advertencia");
        alert.setContentText("Este solicitante ya tiene una solicitud activa. ¿Desea sobreescribirla?");

        ButtonType confirm = new ButtonType("Sobreescribir", ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirm, cancel);

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent() && resultado.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {

            SolicitudCredito<T> nueva = creadorSolicitud.apply(solicitante);
            controlador.editarSolicitud(nueva);

            tablaSolicitudes.getItems().setAll(controlador.listarSolicitudes());
        }
    }

    private <T> Field buscarCampoEnJerarquia(Class<T> clazz, String nombreCampo) {
        Class<?> claseActual = clazz;
        while (claseActual != null) {
            try {
                Field campo = claseActual.getDeclaredField(nombreCampo);
                campo.setAccessible(true);
                return campo;
            } catch (NoSuchFieldException e) {
                claseActual = claseActual.getSuperclass();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> void configurarColumna(TableColumn<?, ?> columna, Field campo, String nombreCampo) {
        try {
            if (campo.getType() == String.class) {
                TableColumn<T, String> c = (TableColumn<T, String>) columna;
                configurarColumnaEditable(c, nombreCampo, campo, TextFieldTableCell.forTableColumn());
            } else if (campo.getType() == Double.class || campo.getType() == double.class) {
                TableColumn<T, Double> c = (TableColumn<T, Double>) columna;
                configurarColumnaEditable(c, nombreCampo, campo, TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
            } else if (campo.getType() == Integer.class || campo.getType() == int.class) {
                TableColumn<T, Integer> c = (TableColumn<T, Integer>) columna;
                configurarColumnaEditable(c, nombreCampo, campo, TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            } else if (campo.getType() == Long.class || campo.getType() == long.class) {
                TableColumn<T, Long> c = (TableColumn<T, Long>) columna;
                configurarColumnaEditable(c, nombreCampo, campo, TextFieldTableCell.forTableColumn(new LongStringConverter()));
            }
        } catch (ClassCastException e) {
            System.out.println("Error de tipo en la columna: " + columna.getText());
        }
    }   

    private <T, U> void configurarColumnaEditable(
        TableColumn<T, U> columna,
        String nombreCampo,
        Field campo,
        Callback<TableColumn<T, U>, TableCell<T, U>> cellFactory) {

        columna.setCellValueFactory(new PropertyValueFactory<>(nombreCampo));
        columna.setCellFactory(cellFactory);
        columna.setOnEditCommit(e -> {
            try {
                campo.set(e.getRowValue(), e.getNewValue());
            } catch (IllegalAccessException ex) {
                System.out.println("Error al asignar valor: " + ex.getMessage());
            }
        });
    }

    private <T extends Solicitante> void procesarSolicitantes(
        List<T> seleccionados,
        Supplier<List<T>> listar,
        Consumer<T> crear,
        Consumer<T> editar,
        Predicate<T> validar,
        String tipo) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText("Error de campos inválidos");

        for (T solicitante : seleccionados) {
            try {
                if (!validar.test(solicitante)) {
                    alert.setContentText("Existen campos con valores no válidos.");
                    alert.showAndWait();
                    continue;
                }

                boolean existe = listar.get().contains(solicitante);

                if (existe) {
                    
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Confirmar edición");
                    confirm.setHeaderText("Este solicitante ya existe");
                    confirm.setContentText("¿Desea sobrescribir los cambios?");

                    ButtonType sobrescribir = new ButtonType("Sobrescribir");
                    ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                    confirm.getButtonTypes().setAll(sobrescribir, cancelar);

                    Optional<ButtonType> resultado = confirm.showAndWait();

                    if (resultado.isEmpty() || resultado.get() == cancelar) {
                        continue;
                    }
                    editar.accept(solicitante);

                } else {
                    crear.accept(solicitante);
                }

            } catch (Exception e) {
                int fila = obtenerFila(solicitante, tipo);
                alert.setContentText(e.getMessage() + " (" + tipo + " en la fila: " + fila + ")");
                alert.showAndWait();
            }
        }

        if("Empresa".equalsIgnoreCase(tipo)) tablaEmpresas.refresh();
        else tablaPersonas.refresh();
    }

    private int obtenerFila(Solicitante solicitante, String tipo) {
    if ("empresa".equalsIgnoreCase(tipo)) {
        return tablaEmpresas.getItems().indexOf(solicitante) + 1;
    } else {
        return tablaPersonas.getItems().indexOf(solicitante) + 1;
    }
}
}