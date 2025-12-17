package com.desarrollo_backend.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.gestores.GestorHuesped;
import com.desarrollo_backend.demo.modelo.habitacion.Habitacion;
import com.desarrollo_backend.demo.modelo.habitacion.Reserva;
import com.desarrollo_backend.demo.modelo.habitacion.TipoHabitacion;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.HuespedPK;
import com.desarrollo_backend.demo.modelo.huesped.TipoDoc;
import com.desarrollo_backend.demo.repository.HabitacionRepository;
import com.desarrollo_backend.demo.repository.HuespedRepository;
import com.desarrollo_backend.demo.repository.ReservaRepository;
import com.desarrollo_backend.demo.gestores.GestorReservas;

@SpringBootTest
@Transactional // Importante: Revierte cambios en BD al terminar cada test
public class GestorHuespedTest {

    @Autowired
    private GestorHuesped gestorHuesped;

    @Autowired
    private GestorReservas gestorReservas;

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    // 1. Test para darDeAltaHuesped (Ya lo tenías, incluido por completitud)
    @Test
    public void testDarDeAltaHuesped_Exito() {
        HuespedDTO dto = crearDTO("Laura", "Gomez", "87654321");

        Huesped creado = gestorHuesped.darDeAltaHuesped(dto);

        assertNotNull(creado);
        assertEquals("Laura", creado.getNombre());
        assertTrue(huespedRepository.existsById(new HuespedPK(TipoDoc.DNI, "87654321")));
    }

    // 2. Test para buscarHuespedes (Filtros)
    @Test
    public void testBuscarHuespedes_Exito() {
        // Arrange
        guardarHuesped("Carlos", "Lopez", "11111111");
        guardarHuesped("Ana", "Lopez", "22222222"); // Mismo apellido
        guardarHuesped("Pedro", "Perez", "33333333");

        HuespedDTO filtro = new HuespedDTO();
        filtro.setApellido("Lop"); // Búsqueda parcial por apellido

        // Act
        List<Huesped> resultados = gestorHuesped.buscarHuespedes(filtro);

        // Assert
        assertEquals(2, resultados.size(), "Debería encontrar 2 huéspedes con apellido Lopez");
    }

    // 3. Test para huespedIsAlojado
    @Test
    public void testHuespedIsAlojado_Exito() {
        // Arrange
        Huesped h = new Huesped("Juan", "Alojado", TipoDoc.DNI, "99999999", new Date(), "Arg", "mail", "123", "Ocup",
                true, "Dir", false);
        huespedRepository.save(h);

        HuespedDTO dto = new HuespedDTO(h); // Usamos el DTO para consultar

        // Act
        boolean estaAlojado = gestorHuesped.huespedIsAlojado(dto);

        // Assert
        assertTrue(estaAlojado, "El huésped debería figurar como alojado");
    }

    // 4. Test para eliminarHuesped
    @Test
    public void testEliminarHuesped_Exito() {
        // Arrange
        String dni = "55555555";
        guardarHuesped("Eliminar", "Me", dni);

        HuespedDTO dtoEliminar = new HuespedDTO();
        dtoEliminar.setTipo_documento(TipoDoc.DNI);
        dtoEliminar.setNroDocumento(dni);

        // Act
        gestorHuesped.eliminarHuesped(dtoEliminar);

        // Assert
        assertFalse(huespedRepository.existsById(new HuespedPK(TipoDoc.DNI, dni)),
                "El huésped no debería existir en BD");
    }

    // 5.a Test para modificarHuesped (Sin cambio de PK)
    @Test
    public void testModificarHuesped_SinCambioPK_Exito() {
        // Arrange
        String dni = "12312312";
        guardarHuesped("Original", "Apellido", dni);

        HuespedDTO dtoModificado = crearDTO("Modificado", "NuevoApellido", dni);
        dtoModificado.setEmail("nuevo@email.com");

        HuespedPK pk = new HuespedPK(TipoDoc.DNI, dni);

        // Act
        gestorHuesped.modificarHuesped(dtoModificado, pk, false);

        // Assert
        Huesped enBD = huespedRepository.findById(pk).orElse(null);
        assertNotNull(enBD);
        assertEquals("Modificado", enBD.getNombre());
        assertEquals("nuevo@email.com", enBD.getEmail());
        assertFalse(enBD.getBorrado()); // No debe estar borrado lógico
    }

    // 5.b Test para modificarHuesped (Con cambio de PK - Borrado lógico y creación)
    @Test
    public void testModificarHuesped_ConCambioPK_Exito() {
        // Arrange
        String dniViejo = "11111111";
        String dniNuevo = "99999999";
        guardarHuesped("Cambio", "PK", dniViejo);

        HuespedDTO dtoNuevo = crearDTO("Cambio", "PK", dniNuevo); // Mismos datos, nuevo DNI
        HuespedPK pkAnterior = new HuespedPK(TipoDoc.DNI, dniViejo);

        // Act
        gestorHuesped.modificarHuesped(dtoNuevo, pkAnterior, true);

        // Assert
        // 1. El viejo debe existir pero con borrado lógico true
        Huesped viejo = huespedRepository.findById(pkAnterior).orElse(null);
        assertNotNull(viejo);
        assertTrue(viejo.getBorrado(), "El registro anterior debe tener borrado lógico");

        // 2. El nuevo debe existir y estar activo
        Huesped nuevo = huespedRepository.findById(new HuespedPK(TipoDoc.DNI, dniNuevo)).orElse(null);
        assertNotNull(nuevo);
        assertFalse(nuevo.getBorrado(), "El nuevo registro debe estar activo");
    }

    // 6. Test para obtenerHuespedPorId
    @Test
    public void testObtenerHuespedPorId_Exito() {
        // Arrange
        String dni = "77777777";
        guardarHuesped("Buscado", "PorID", dni);
        HuespedPK id = new HuespedPK(TipoDoc.DNI, dni);

        // Act
        Huesped resultado = gestorHuesped.obtenerHuespedPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals("Buscado", resultado.getNombre());
    }

    // 8. Test para darDeAltaHuesped con NULL (Cubre el if(dto == null))
    @Test
    public void testDarDeAltaHuesped_Null_RetornaNull() {
        Huesped resultado = gestorHuesped.darDeAltaHuesped(null);
        assertNull(resultado, "Si el DTO es null, el método debería retornar null");
    }

    // 9. Test para modificarHuesped cuando NO EXISTE (Caso modificoPK = false)
    // Cubre el 'else' del if(existente != null)
    @Test
    public void testModificarHuesped_NoExiste_NoHaceNada() {
        // Arrange
        String dniInexistente = "00000000";
        HuespedDTO dto = crearDTO("Fantasma", "Ghost", dniInexistente);
        HuespedPK pk = new HuespedPK(TipoDoc.DNI, dniInexistente);

        // Act
        gestorHuesped.modificarHuesped(dto, pk, false);

        // Assert
        assertFalse(huespedRepository.existsById(pk), "No debería haber creado nada");
    }

    // --- Métodos Auxiliares ---

    private void guardarHuesped(String nombre, String apellido, String dni) {
        Huesped h = new Huesped(nombre, apellido, TipoDoc.DNI, dni, new Date(), "Arg", "test@mail.com", "123456",
                "Ocupacion", false, "Calle 1", false);
        huespedRepository.save(h);
    }

    private HuespedDTO crearDTO(String nombre, String apellido, String dni) {
        HuespedDTO dto = new HuespedDTO();
        dto.setNombre(nombre);
        dto.setApellido(apellido);
        dto.setTipo_documento(TipoDoc.DNI);
        dto.setNroDocumento(dni);
        dto.setNacionalidad("Arg");
        dto.setEmail("test@mail.com");
        dto.setTelefono("123");
        dto.setOcupacion("Ocup");
        dto.setAlojado(false);
        dto.setDireccion("Dir 123");
        dto.setFechaDeNacimiento(new Date());
        return dto;
    }
}
