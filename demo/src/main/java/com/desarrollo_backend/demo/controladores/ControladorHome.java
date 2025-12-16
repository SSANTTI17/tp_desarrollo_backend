package com.desarrollo_backend.demo.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/ocupar")
    public String vistaOcupar() {
        return "ocuparHabitacion";
    }
}
