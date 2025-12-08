package com.desarrollo_backend.demo.gestores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desarrollo_backend.demo.dtos.PersonaFisicaDTO;

import com.desarrollo_backend.demo.modelo.responsablePago.*;
import com.desarrollo_backend.demo.modelo.huesped.Huesped;

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
}
