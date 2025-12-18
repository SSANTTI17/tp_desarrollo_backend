package com.desarrollo_backend.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.gestores.GestorHuesped;
import com.desarrollo_backend.demo.mappers.HuespedMapper;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.HuespedPK;
import com.desarrollo_backend.demo.modelo.huesped.TipoDoc;
import com.desarrollo_backend.demo.repository.HuespedRepository;

@SpringBootTest
@Transactional
public class GestorHuespedTest {

    @Autowired
    private GestorHuesped gestorHuesped;

    @Autowired
    private HuespedRepository huespedRepository;

    @Autowired
    private HuespedMapper huespedMapper; // 2. Inyectamos el Mapper

    @Test
    public void testDarDeAltaHuesped_Exito() {
        HuespedDTO dto = crearDTO("Laura", "Gomez", "87654321");

        Huesped creado = gestorHuesped.darDeAltaHuesped(dto);

        assertNotNull(creado);
        assertEquals("Laura", creado.getNombre());
        assertTrue(huespedRepository.existsById(new HuespedPK(TipoDoc.DNI, "87654321")));
    }

    @Test
    public void testBuscarHuespedes_Exito() {
        guardarHuesped("Carlos", "Lopez", "11111111");
        guardarHuesped("Ana", "Lopez", "22222222");
        guardarHuesped("Pedro", "Perez", "33333333");

        HuespedDTO filtro = new HuespedDTO();
        filtro.setApellido("Lop");

        List<Huesped> resultados = gestorHuesped.buscarHuespedes(filtro);

        assertEquals(2, resultados.size(), "Debería encontrar 2 huéspedes con apellido Lopez");
    }

    @Test
    public void testBuscarHuespedes_PorDocumento_Exito() {
        // GIVEN
        guardarHuesped("Buscado", "Doc", "11122233");
        guardarHuesped("Otro", "Doc", "99988877");

        // WHEN: Filtramos específicamente por ese DNI
        HuespedDTO filtro = new HuespedDTO();
        filtro.setTipo_documento(TipoDoc.DNI);
        filtro.setNroDocumento("11122233");

        List<Huesped> resultados = gestorHuesped.buscarHuespedes(filtro);

        // THEN
        assertEquals(1, resultados.size());
        assertEquals("Buscado", resultados.get(0).getNombre());
    }

    @Test
    public void testHuespedIsAlojado_Exito() {
        Huesped h = new Huesped("Juan", "Alojado", TipoDoc.DNI, "99999999", new Date(), "Arg", "mail", "123", "Ocup",
                true, "Dir", false);
        huespedRepository.save(h);

        // CORRECCIÓN 1: Usamos el mapper en lugar del constructor new HuespedDTO(h)
        HuespedDTO dto = huespedMapper.toDto(h);

        boolean estaAlojado = gestorHuesped.huespedIsAlojado(dto);

        assertTrue(estaAlojado, "El huésped debería figurar como alojado");
    }

    @Test
    public void testEliminarHuesped_Exito() {
        String dni = "55555555";
        guardarHuesped("Eliminar", "Me", dni);

        HuespedDTO dtoEliminar = new HuespedDTO();
        dtoEliminar.setTipo_documento(TipoDoc.DNI);
        dtoEliminar.setNroDocumento(dni);

        gestorHuesped.eliminarHuesped(dtoEliminar);

        assertFalse(huespedRepository.existsById(new HuespedPK(TipoDoc.DNI, dni)),
                "El huésped no debería existir en BD");
    }

    @Test
    public void testModificarHuesped_SinCambioPK_Exito() {
        String dni = "12312312";
        guardarHuesped("Original", "Apellido", dni);

        HuespedDTO dtoModificado = crearDTO("Modificado", "NuevoApellido", dni);
        dtoModificado.setEmail("nuevo@email.com");

        HuespedPK pk = new HuespedPK(TipoDoc.DNI, dni);

        gestorHuesped.modificarHuesped(dtoModificado, pk, false);

        Huesped enBD = huespedRepository.findById(pk).orElse(null);
        assertNotNull(enBD);
        assertEquals("Modificado", enBD.getNombre());
        assertEquals("nuevo@email.com", enBD.getEmail());

        // CORRECCIÓN 2: Usamos getBorradoLogico() (generado por Lombok) en lugar de
        // getBorrado()
        assertFalse(enBD.getBorradoLogico());
    }

    @Test
    public void testModificarHuesped_ConCambioPK_Exito() {
        String dniViejo = "11111111";
        String dniNuevo = "99999999";
        guardarHuesped("Cambio", "PK", dniViejo);

        HuespedDTO dtoNuevo = crearDTO("Cambio", "PK", dniNuevo);
        HuespedPK pkAnterior = new HuespedPK(TipoDoc.DNI, dniViejo);

        gestorHuesped.modificarHuesped(dtoNuevo, pkAnterior, true);

        // Verificamos viejo
        Huesped viejo = huespedRepository.findById(pkAnterior).orElse(null);
        assertNotNull(viejo);
        // CORRECCIÓN 2: getBorradoLogico()
        assertTrue(viejo.getBorradoLogico(), "El registro anterior debe tener borrado lógico");

        // Verificamos nuevo
        Huesped nuevo = huespedRepository.findById(new HuespedPK(TipoDoc.DNI, dniNuevo)).orElse(null);
        assertNotNull(nuevo);
        // CORRECCIÓN 2: getBorradoLogico()
        assertFalse(nuevo.getBorradoLogico(), "El nuevo registro debe estar activo");
    }

    @Test
    public void testObtenerHuespedPorId_Exito() {
        String dni = "77777777";
        guardarHuesped("Buscado", "PorID", dni);
        HuespedPK id = new HuespedPK(TipoDoc.DNI, dni);

        Huesped resultado = gestorHuesped.obtenerHuespedPorId(id);

        assertNotNull(resultado);
        assertEquals("Buscado", resultado.getNombre());
    }

    @Test
    public void testDarDeAltaHuesped_Null_RetornaNull() {
        Huesped resultado = gestorHuesped.darDeAltaHuesped(null);
        assertNull(resultado);
    }

    @Test
    public void testHuespedIsAlojado_RetornaFalse() {
        // GIVEN: Creamos un huésped explícitamente NO alojado (false)
        // Usamos el constructor directo para asegurar el estado inicial
        Huesped h = new Huesped("Pedro", "Libre", TipoDoc.DNI, "55667788", new Date(), "Arg", "mail", "123", "Ocup", 
                false, "Dir", false); // El anteúltimo parámetro es 'alojado' -> false
        huespedRepository.save(h);

        HuespedDTO dto = huespedMapper.toDto(h);

        // WHEN
        boolean estaAlojado = gestorHuesped.huespedIsAlojado(dto);

        // THEN
        assertFalse(estaAlojado, "El huésped no debería figurar como alojado");
    }

    @Test
    public void testModificarHuesped_NoExiste_NoHaceNada() {
        String dniInexistente = "00000000";
        HuespedDTO dto = crearDTO("Fantasma", "Ghost", dniInexistente);
        HuespedPK pk = new HuespedPK(TipoDoc.DNI, dniInexistente);

        gestorHuesped.modificarHuesped(dto, pk, false);

        assertFalse(huespedRepository.existsById(pk));
    }

    // --- Métodos Auxiliares ---

    private void guardarHuesped(String nombre, String apellido, String dni) {
        // Usamos el constructor manual que agregamos a la entidad para compatibilidad
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