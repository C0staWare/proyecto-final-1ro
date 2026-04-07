package com.sucursalbancaria.Controllers.ControlVistas;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.sucursalbancaria.App;
import com.sucursalbancaria.Controllers.Logica.SucursalBancaria;
import com.sucursalbancaria.Models.Solicitantes.Empresa;
import com.sucursalbancaria.Models.Solicitantes.Persona;
import com.sucursalbancaria.Models.Solicitantes.Solicitante;
import com.sucursalbancaria.Models.Solicitudes.SolicitudCredito;
import com.sucursalbancaria.Models.Solicitudes.SolicitudEmpresarial;
import com.sucursalbancaria.Models.Solicitudes.SolicitudPersonal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

public class MainController {
    
    SucursalBancaria sucursalBancaria = new SucursalBancaria();

    @FXML
    TabPane tabPaneObject;

    @FXML
    MenuItem cambiarEstilo;

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

        if(clazz == SolicitudEmpresarial.class || clazz == SolicitudPersonal.class){

            tabla.getColumns().forEach(columna -> {

                String nombreCampo = nombresColumnas.get(columna.getText());
                columna.setCellValueFactory(new PropertyValueFactory<>(nombreCampo));

            });
        }
        else{
            tabla.getColumns().forEach(columna -> {
                String nombreCampo = nombresColumnas.get(columna.getText());

                Field campoActual = null;

                Class<T> claseActual = clazz;

                while (claseActual != null) {
                    
                    try{
                        campoActual = claseActual.getDeclaredField(nombreCampo);
                        campoActual.setAccessible(true);
                        break;
                    }
                    catch(NoSuchFieldException ex){

                        claseActual = (Class<T>)claseActual.getSuperclass();
                    }
                }

                final Field campo = campoActual;

                try{

                    if(campo.getType() == String.class){

                        @SuppressWarnings("unchecked")
                        TableColumn<T, String> c = (TableColumn<T, String>) columna;
                        
                        c.setCellValueFactory(new PropertyValueFactory<>(nombreCampo));
                        c.setCellFactory(TextFieldTableCell.forTableColumn());
                        c.setOnEditCommit(e -> {
                            try{
                                campo.set(e.getRowValue(), e.getNewValue());
                            }
                            catch(IllegalAccessException ill){
                                System.out.println(ill.getMessage());
                            }
                            
                        });
                    }
                    else if(campo.getType() == Double.class || campo.getType() == double.class){

                        @SuppressWarnings("unchecked")
                        TableColumn<T, Double> c = (TableColumn<T, Double>) columna;

                        c.setCellValueFactory(new PropertyValueFactory<>(nombreCampo));
                        c.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
                        c.setOnEditCommit(e -> {
                            try{

                                campo.set(e.getRowValue(), e.getNewValue());

                            }catch(IllegalAccessException ill){

                                System.out.println(ill.getMessage());
                            }
                        });
                    }
                    else if(campo.getType() == Integer.class || campo.getType() == int.class){

                        @SuppressWarnings("unchecked")
                        TableColumn<T, Integer> c = (TableColumn<T, Integer>) columna;

                        c.setCellValueFactory(new PropertyValueFactory<>(nombreCampo));
                        c.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
                        c.setOnEditCommit(e -> {
                            try{
                                campo.set(e.getRowValue(), e.getNewValue());
                            }catch(IllegalAccessException ill){

                                System.out.println(ill.getMessage());
                            }
                        });

                    }
                    else if(campo.getType() == Long.class || campo.getType() == long.class){

                        @SuppressWarnings("unchecked")
                        TableColumn<T, Long> c = (TableColumn<T, Long>) columna;

                        c.setCellValueFactory(new PropertyValueFactory<>(nombreCampo));
                        c.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
                        c.setOnEditCommit(e -> {

                            try{

                                campo.set(e.getRowValue(), e.getNewValue());

                            }catch(IllegalAccessException ill){

                                System.out.println(ill.getMessage());
                            }
                        });
                    }

                }catch(ClassCastException e){

                    System.out.println("Error de tipo en la columna: "+columna.getText());
                }
            });
        }
    }

    @FXML
    public void guardarSolicitante() {

        ObservableList<Solicitante> seleccionados;

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText("Error de campos inválidos");

        if(tabPaneObject.getSelectionModel().getSelectedItem().getText().equals("Empresas")){

            seleccionados = FXCollections.observableArrayList(tablaEmpresas.getSelectionModel().getSelectedItems());

            seleccionados.forEach(empresa -> {
                try{

                    if(tieneCamposValidos((Empresa)empresa)) {
                        
                        sucursalBancaria.controladorEmpresas.crearEmpresa((Empresa)empresa);
                    }
                    else{

                        alert.setContentText("Existen campos con valores no válidos.");
                        alert.showAndWait();
                    }
                }
                catch(Exception e){

                    alert.setContentText(e.getMessage() + " (empresa en la fila: " + (tablaEmpresas.getItems().indexOf(empresa)+1) + ")");
                    alert.showAndWait();
                }

            });


        }
        else{

            seleccionados = FXCollections.observableArrayList(tablaPersonas.getSelectionModel().getSelectedItems());

            seleccionados.forEach(persona -> {
                try{

                    if(tieneCamposValidos((Persona) persona)){

                        sucursalBancaria.controladorPersonas.agregarPersona((Persona)persona);

                    }
                    else{

                        alert.setContentText("Existen campos con valores no válidos.");
                        alert.showAndWait();
                    }
                        
                }
                catch(Exception e){

                    alert.setContentText(e.getMessage() + " (Persona en la fila: "+ (tablaPersonas.getItems().indexOf(persona)+1) + ")");
                    alert.showAndWait();
                    
                }
                
            });

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



        }
        else if(tabSeleccionada.getText().equals("Personas")){

            ObservableList<Persona> seleccionadas = FXCollections.observableArrayList(tablaPersonas.getSelectionModel().getSelectedItems());

            tablaPersonas.getItems().removeAll(seleccionadas);

        }

        else{

        }
    }

    @FXML
    public void agregarSolicitud(){

        Tab seleccionado = tabPaneObject.getSelectionModel().getSelectedItem();

        if(seleccionado.getText().equals("Empresas")){

            Empresa seleccionada = tablaEmpresas.getSelectionModel().getSelectedItem();
            if (seleccionada != null){ 

                SolicitudEmpresarial solicitud = new SolicitudEmpresarial(seleccionada);
                tablaSolicitudes.getItems().add(solicitud);

            }
        }
        else if(seleccionado.getText().equals("Personas")){

            Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
            if(seleccionada != null){

                SolicitudPersonal solicitud = new SolicitudPersonal(seleccionada);
                tablaSolicitudes.getItems().add(solicitud);

            }
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
    //UTILIDAD

    public void manejarAccionBotonesTiempo(MenuItem menuItem){

        menuItem.setOnAction(event -> {

            demoraPago(menuItem.getText());
        });

    }

    public boolean tieneCamposValidos(Empresa e){

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

    public boolean tieneCamposValidos(Persona p){

        return p.getNombreSolicitante() != null && !p.getNombreSolicitante().isEmpty()
            && p.getValorCredito() != null
            && p.getDireccionSolicitante() != null && !p.getDireccionSolicitante().isEmpty()
            && p.getCI() != null
            && p.getPersonasQueSustenta() > 0
            && p.getSalarioNucleo() > 0;
    }

    private static boolean estiloCambiado = false;

    public void cambiarCSS(){

        if(estiloCambiado){

            App.scene.getStylesheets().clear();
            App.scene.getStylesheets().add(getClass().getResource("/Styles/MainStyle.css").toExternalForm());
            estiloCambiado = false;
        }
        else{
            
            App.scene.getStylesheets().clear();
            App.scene.getStylesheets().add(getClass().getResource("/Styles/EstiloAlternativo.css").toExternalForm());
            estiloCambiado = true;

        }
        
    }

    public Map<String, String> nombresColumnasEmpresa() {
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

    public Map<String, String> nombresColumnasPersona() {
        Map<String, String> nombresColumnas = new HashMap<>();

        nombresColumnas.put("Nombre", "nombreSolicitante");
        nombresColumnas.put("Valor del Crédito", "valorCredito");
        nombresColumnas.put("Dirección", "direccionSolicitante");
        nombresColumnas.put("CI", "CI");
        nombresColumnas.put("Salario del Núcleo Familiar", "salarioNucleo");
        nombresColumnas.put("Personas que Sustenta", "personasQueSustenta");

        return nombresColumnas;
    }

    public Map<String, String> nombresColumnasSolicitud(){

        Map<String, String> nombresColumnas = new HashMap<>();

        nombresColumnas.put("Tipo", "tipoSolicitud");
        nombresColumnas.put("Estado", "estadoSolicitud");
        nombresColumnas.put("A nombre de", "nombreSolicitante");
        nombresColumnas.put("Mensualidad", "mensualidad");

        return nombresColumnas;
    }
}
