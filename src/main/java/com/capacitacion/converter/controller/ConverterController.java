package com.capacitacion.converter.controller;

import com.capacitacion.converter.dto.FlexibleRequest;
import com.capacitacion.converter.service.ConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/converter")
public class ConverterController {

    private final ConverterService converterService;

    @Autowired
    public ConverterController(ConverterService converterService) {
        this.converterService = converterService;
    }

    @PostMapping(value = "/tojson")
    public ResponseEntity<Map<String, Object>> convert(@RequestBody Map<String, Object> json) {

        Map<String, Object> response = new HashMap<>();

        response.put("message", "DTO procesado exitosamente");
        response = converterService.convert(json);
        return ResponseEntity.ok(response);
    }
}
