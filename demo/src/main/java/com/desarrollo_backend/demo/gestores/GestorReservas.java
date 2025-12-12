package com.desarrollo_backend.demo.gestores;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desarrollo_backend.demo.exceptions.ReservaNotFoundException;
import com.desarrollo_backend.demo.modelo.habitacion.*;

import com.desarrollo_backend.demo.repository.HabitacionRepository;
import com.desarrollo_backend.demo.repository.HistorialEstadoHabitacionRepository;
import com.desarrollo_backend.demo.repository.ReservaRepository;

@Service
public class GestorReservas {

    @Autowired
    private HistorialEstadoHabitacionRepository historialRepo;

    @Autowired
    private HabitacionRepository habitacionRepo;

    @Autowired
    private ReservaRepository reservaRepo;

    public List<Map<String, Object>> buscarDisponibilidad(String tipoString, String desdeStr, String hastaStr) {
        List<Map<String, Object>> listaResultado = new ArrayList<>();

        TipoHabitacion tipoEnum = normalizarTipo(tipoString);
        if (tipoEnum == null)
            return listaResultado;

        Date desde = parsearFechaFront(desdeStr);
        Date hasta = parsearFechaFront(hastaStr);

        if (desde == null || hasta == null || desde.after(hasta))
            return listaResultado;

        List<Date> rango = generarRangoFechas(desde, hasta);

        for (Date fecha : rango) {
            boolean sd1 = verificarLibre(1, tipoEnum, fecha);
            boolean sd2 = verificarLibre(2, tipoEnum, fecha);

            Map<String, Object> fila = new HashMap<>();
            fila.put("fecha", new SimpleDateFormat("dd/MM/yyyy").format(fecha));
            fila.put("sd1", sd1);
            fila.put("sd2", sd2);

            listaResultado.add(fila);
        }
        return listaResultado;
    }

    @Transactional
    public String crearReserva(String tipoStr, int numeroHab, String fechaInicioStr, String fechaFinStr) {
        try {
            Date fechaInicio = parsearFechaFront(fechaInicioStr);
            Date fechaFin = parsearFechaFront(fechaFinStr);

            if (fechaInicio == null || fechaFin == null)
                return "Error: Fechas inválidas.";

            TipoHabitacion tipoEnum = normalizarTipo(tipoStr);
            if (tipoEnum == null)
                return "Error: Tipo de habitación inválido";

            List<Date> diasSolicitados = generarRangoFechas(fechaInicio, fechaFin);
            for (Date dia : diasSolicitados) {
                if (!verificarLibre(numeroHab, tipoEnum, dia)) {
                    String diaOcupado = new SimpleDateFormat("dd/MM/yyyy").format(dia);
                    return "Error: Habitación ocupada el día " + diaOcupado;
                }
            }

            Habitacion habitacionRef = habitacionRepo.findByIdNumeroAndIdTipo(numeroHab, tipoEnum);

            if (habitacionRef == null) {
                return "Error: La habitación no existe (Revise número y tipo)";
            }

            HistorialEstadoHabitacion nuevo = new HistorialEstadoHabitacion(
                    habitacionRef,
                    "14:00",
                    fechaInicio,
                    "10:00",
                    fechaFin,
                    EstadoHabitacion.Reservada);

            historialRepo.save(nuevo);

            //faltan los datos del q reserva, hacer reservas para más de una habitacion
            List<Habitacion> auxList = new ArrayList<>();
            auxList.add(habitacionRef);
            Reserva nuevaReserva = new Reserva("","","",fechaInicio,"14:00",fechaFin,"10:00",auxList);
            reservaRepo.save(nuevaReserva);

            return "¡Reserva Exitosa!";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error en el servidor: " + e.getMessage();
        }
    }

   public List<Reserva> consultarReservas(String nombre, String apellido) 
        throws ReservaNotFoundException{

        if(apellido == null || apellido.isBlank())
            throw new ReservaNotFoundException("ingrese apellido");
        
        List<Reserva> reservas = new ArrayList<>();

        if(nombre == null || nombre.isBlank()) reservas = reservaRepo.findByApellido(apellido);
        else reservas = reservaRepo.findByApellidoAndNombre(apellido,nombre);

        if(reservas.isEmpty()) 
            throw new ReservaNotFoundException("no hay reservas a nombre de " + apellido
                                                + ", " + nombre);

        return reservas;
    }

    //retorno rebotadas
    public List<Reserva> eliminarReservas(List<Reserva> reservas) {
            
        List<Reserva> rebotadas = new ArrayList<>();
        
        for(Reserva r : reservas){
            if(!eliminarReserva(r)){
                rebotadas.add(r);
            }
        }

        return rebotadas;
}
    
    //true para eliminacion exitosa, aca podría poner excepciones tambien, ya me gustó xddd ce
    public boolean eliminarReserva(Reserva reserva){

        if(reserva == null) return false;

        //verifico q esté todo bien para q no se rompa
        Reserva aux = reservaRepo.findById(reserva.getId()).orElse(null);

        if(aux != null){
            reservaRepo.delete(aux);
            return true;
        }
        return false;
    }

    private TipoHabitacion normalizarTipo(String t) {
        try {
            if (t == null)
                return null;
            switch (t.trim()) {
                case "Individual estándar":
                    return TipoHabitacion.IE;
                case "Doble estándar":
                    return TipoHabitacion.DE;
                case "Doble superior":
                    return TipoHabitacion.DS;
                case "Superior family plan":
                    return TipoHabitacion.SFP;
                case "Suite doble":
                    return TipoHabitacion.SD;
                default:
                    return TipoHabitacion.valueOf(t.toUpperCase());
            }
        } catch (Exception e) {
            return null;
        }
    }

    private Date parsearFechaFront(String f) {
        try {
            if (f.contains("-"))
                return new SimpleDateFormat("yyyy-MM-dd").parse(f);
            return new SimpleDateFormat("dd/MM/yyyy").parse(f);
        } catch (Exception e) {
            return null;
        }
    }

    private List<Date> generarRangoFechas(Date d1, Date d2) {
        List<Date> lista = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d1);
        while (!cal.getTime().after(d2)) {
            lista.add(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return lista;
    }

    private boolean verificarLibre(int numero, TipoHabitacion tipo, Date fecha) {
        List<HistorialEstadoHabitacion> historial = historialRepo.findByHabitacion(numero, tipo);
        for (HistorialEstadoHabitacion h : historial) {
            if (!fecha.before(h.getFechaInicio()) && !fecha.after(h.getFechaFin())) {
                if (h.getEstado() == EstadoHabitacion.Ocupada || h.getEstado() == EstadoHabitacion.Reservada) {
                    return false;
                }
            }
        }
        return true;
    }
}