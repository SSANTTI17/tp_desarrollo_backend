package com.desarrollo_backend.demo.gestores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desarrollo_backend.demo.dtos.HuespedDTO;
import com.desarrollo_backend.demo.dtos.PersonaFisicaDTO;

import com.desarrollo_backend.demo.modelo.responsablePago.*;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;
import com.desarrollo_backend.demo.modelo.huesped.HuespedPK;
import com.desarrollo_backend.demo.repository.PersonaFisicaRepository;
import com.desarrollo_backend.demo.repository.PersonaJuridicaRepository;
import com.desarrollo_backend.demo.repository.ResponsablePagoRepository;

@Service
public class GestorContable {

    @Autowired
    private PersonaFisicaRepository personaFisicaRepository;
    @Autowired
    private PersonaJuridicaRepository personaJuridicaRepository;
    @Autowired
    private ResponsablePagoRepository responsablePagoRepository;
    @Autowired
    private GestorHuesped gestorHuesped;

    public ResponsablePago registrarResponsable(ResponsablePago responsable) {

        ResponsablePago respp;
        if (responsable instanceof PersonaFisica fisica)
            respp = personaFisicaRepository.save(fisica);
        else if (responsable instanceof PersonaJuridica juridica)
            respp = personaJuridicaRepository.save(juridica);
        else
            return null;

        responsablePagoRepository.save(new ResponsablePago(respp.getCUIT()));

        return respp;
    }

    public void registrarPersonaFisica(PersonaFisicaDTO pfDto, Huesped huespedReferencia) {
        if (pfDto == null || pfDto.getCUIT() == null)
            return;

        String cuitLimpio = pfDto.getCUIT().replaceAll("[^0-9]", "");
        String cuitFormateado = cuitLimpio;
        if (cuitLimpio.length() == 11) {
            cuitFormateado = cuitLimpio.substring(0, 2) + "-" +
                    cuitLimpio.substring(2, 10) + "-" +
                    cuitLimpio.substring(10, 11);
        }

        PersonaFisica nuevaPF = new PersonaFisica(
                pfDto.getPosicionIVA(),
                cuitFormateado,
                huespedReferencia);

        personaFisicaRepository.save(nuevaPF);
    }

    public void modificarHuesped(HuespedDTO dto){
        
        // 1. Recuperamos el Huésped (necesario para vincular la PersonaFisica)
        HuespedPK idHuesped = new HuespedPK(dto.getTipo_documento(), dto.getNroDocumento());
        Huesped huesped = gestorHuesped.obtenerHuespedPorId(idHuesped);

        // 2. Buscamos si ya tiene una PersonaFisica asignada
        PersonaFisica pfExistente = personaFisicaRepository.findByRefHuesped(huesped).orElse(null);

        String nuevoCuit = dto.getCUIT();
        String nuevaPosicionIVA = dto.getPosicionIVA(); 

        if (pfExistente == null) {
            // CASO A: El huésped no tenía datos fiscales previos. Creamos uno nuevo.
            PersonaFisica nuevaPF = new PersonaFisica(nuevaPosicionIVA, nuevoCuit, huesped);
            personaFisicaRepository.save(nuevaPF);
        
        } else {
            // CASO B: Ya tiene datos. Verificamos si cambió el CUIT.
            if (!pfExistente.getCUIT().equals(nuevoCuit)) {
                
                // Como el CUIT es ID, no podemos hacer setCUIT. 
                // Debemos borrar el registro viejo y crear uno nuevo.
                personaFisicaRepository.delete(pfExistente);
                
                // Forzamos el flush para que el DELETE se ejecute en la BD antes del INSERT
                // Esto evita errores de clave duplicada si hubiera conflictos raros
                personaFisicaRepository.flush(); 

                PersonaFisica nuevaPF = new PersonaFisica(nuevaPosicionIVA, nuevoCuit, huesped);
                personaFisicaRepository.save(nuevaPF);

            } else {
                // --- EL CUIT ES EL MISMO (Simple) ---
                // Solo actualizamos la posición del IVA
                pfExistente.setPosicionIVA(nuevaPosicionIVA);
                personaFisicaRepository.save(pfExistente);
            }
        }

    }
}
