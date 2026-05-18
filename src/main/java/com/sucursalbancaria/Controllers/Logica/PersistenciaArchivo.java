package com.sucursalbancaria.Controllers.Logica;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import com.sucursalbancaria.Models.Solicitantes.Empresa;
import com.sucursalbancaria.Models.Solicitantes.Persona;
import com.sucursalbancaria.Models.Solicitudes.SolicitudCredito;

public class PersistenciaArchivo {
    
    private static final String CARPETA_DATOS = "Datos";
    private static final String ARCHIVO_EMPRESAS = CARPETA_DATOS + "/empresas.dat";
    private static final String ARCHIVO_PERSONAS = CARPETA_DATOS + "/personas.dat";
    private static final String ARCHIVO_SOLICITUDES_EMPRESAS = CARPETA_DATOS + "/solicitudes_empresas.dat";
    private static final String ARCHIVO_SOLICITUDES_PERSONAS = CARPETA_DATOS + "/solicitudes_personas.dat";
    
    public static void crearCarpetaSiNoExiste() {
        File carpeta = new File(CARPETA_DATOS);
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
    }
    
    public static void guardarTodo(ControladorEmpresas controladorEmpresas,
                                   ControladorPersonas controladorPersonas,
                                   ControladorSolicitudEmpresarial controladorSolicitudEmpresarial,
                                   ControladorSolicitudPersonal controladorSolicitudPersonal) throws IOException {
        crearCarpetaSiNoExiste();
        guardarEmpresas(controladorEmpresas.mapaEmpresas);
        guardarPersonas(controladorPersonas.mapaPersonas);
        guardarSolicitudesEmpresas(controladorSolicitudEmpresarial.listaSolicitudesEmpresas);
        guardarSolicitudesPersonas(controladorSolicitudPersonal.listaSolicitudesPersonas);
    }
    
    private static void guardarEmpresas(Map<Long, Empresa> mapaEmpresas) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_EMPRESAS))) {
            oos.writeObject(mapaEmpresas);
        }
    }
    
    private static void guardarPersonas(Map<String, Persona> mapaPersonas) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PERSONAS))) {
            oos.writeObject(mapaPersonas);
        }
    }
    
    private static void guardarSolicitudesEmpresas(java.util.List<SolicitudCredito<Empresa>> lista) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_SOLICITUDES_EMPRESAS))) {
            oos.writeObject(lista);
        }
    }
    
    private static void guardarSolicitudesPersonas(java.util.List<SolicitudCredito<Persona>> lista) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_SOLICITUDES_PERSONAS))) {
            oos.writeObject(lista);
        }
    }

    public static void cargarTodo(ControladorEmpresas controladorEmpresas,
                                  ControladorPersonas controladorPersonas,
                                  ControladorSolicitudEmpresarial controladorSolicitudEmpresarial,
                                  ControladorSolicitudPersonal controladorSolicitudPersonal) throws IOException, ClassNotFoundException {
        
        controladorEmpresas.mapaEmpresas = cargarEmpresas();
        controladorPersonas.mapaPersonas = cargarPersonas();
        controladorSolicitudEmpresarial.listaSolicitudesEmpresas = cargarSolicitudesEmpresas();
        controladorSolicitudPersonal.listaSolicitudesPersonas = cargarSolicitudesPersonas();
    }
    
    @SuppressWarnings("unchecked")
    private static Map<Long, Empresa> cargarEmpresas() throws IOException, ClassNotFoundException {
        File archivo = new File(ARCHIVO_EMPRESAS);
        if (!archivo.exists()) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Map<Long, Empresa>) ois.readObject();
        }
    }
    
    @SuppressWarnings("unchecked")
    private static Map<String, Persona> cargarPersonas() throws IOException, ClassNotFoundException {
        File archivo = new File(ARCHIVO_PERSONAS);
        if (!archivo.exists()) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Map<String, Persona>) ois.readObject();
        }
    }
    
    @SuppressWarnings("unchecked")
    private static java.util.List<SolicitudCredito<Empresa>> cargarSolicitudesEmpresas() throws IOException, ClassNotFoundException {
        File archivo = new File(ARCHIVO_SOLICITUDES_EMPRESAS);
        if (!archivo.exists()) return new java.util.ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (java.util.List<SolicitudCredito<Empresa>>) ois.readObject();
        }
    }
    
    @SuppressWarnings("unchecked")
    private static java.util.List<SolicitudCredito<Persona>> cargarSolicitudesPersonas() throws IOException, ClassNotFoundException {
        File archivo = new File(ARCHIVO_SOLICITUDES_PERSONAS);
        if (!archivo.exists()) return new java.util.ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (java.util.List<SolicitudCredito<Persona>>) ois.readObject();
        }
    }
    
    public static boolean existenDatosGuardados() {
        return new File(ARCHIVO_EMPRESAS).exists() || new File(ARCHIVO_PERSONAS).exists();
    }
}