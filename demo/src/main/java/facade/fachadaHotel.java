package facade;

import java.util.List;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desarrollo_backend.demo.dtos.EstadiaDTO;
import com.desarrollo_backend.demo.dtos.HabitacionDTO;
import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.gestores.*;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.factura.Factura;
import com.desarrollo_backend.demo.modelo.estadias.Estadia;

public class fachadaHotel {

    @Autowired
    private GestorReservas gestorReservas;

    @Autowired
    private GestorHuesped gestorHuespedes;

    @Autowired
    private GestorContable gestorContable;

    public List<Huesped> obtenerHuespedesParaFacturacion(EstadiaDTO estadiaDTO, HabitacionDTO habitacionDTO) {
        // Delegamos al gestor pasándole los datos primitivos necesarios
        Reserva reserva = gestorReservas.consultarReservas(
            habitacionDTO.getNumero(),
            habitacionDTO.getTipo(),
            estadiaDTO.getFechaFin()
        );

         List<Huesped> huespedes =
            gestorHuespedes.buscarPorReservas(reserva);

        //List<HuespedDTO> dtos = huespedes.stream()
        //.map(huesped -> new HuespedDTO(huesped)) // Usas el constructor que ya tienes en HuespedDTO
        //.collect(Collectors.toList());
        //esto va en el controller, la fachada no debe devolver dtos.
        return huespedes;
    }

    //public Factura generarFactura(HuespedDTO huesped, String CUIT, EstadiaDTO estadia) {
        //Huesped entidad = null;
        //if(huesped.getNroDocumento() != null){
            //List<Huesped> entidades = gestorHuespedes.buscarHuespedes(huesped); NICO TIENE QUE ARREGLAR ESTO
            //NUNCA debería entrar acá porque el huésped fue seleccionado antes
            //if (entidades.isEmpty()) {
            //    throw new RuntimeException("No existe el huésped");
            //}
            //entidad = entidades.get(0);
        //}
        //Estadia entidadEstadia = new Estadia(estadia); HACER EL CONSTRUCTOR EN ESTADIA DTO->ESTADIA
        //Estadia entidadEstadia = new Estadia();
        //Factura factura = gestorContable.generarFacturaParaHuesped(entidad, CUIT, entidadEstadia);
        
        //return factura;
        
    //}
}
