package com.capacitacion.converter.service.impl;

import com.capacitacion.converter.dto.FlexibleRequest;
import com.capacitacion.converter.service.ConverterService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ConverterServiceImpl implements ConverterService {


    //problema:
    //hacer un metodo que reciba un objeto json y devuelva otro json
    //con las fechas cambiadas a la zona horaria marcada (en esta caso lo cambie a GMT-3)

    //json puede venir de diferente estructura.

    @Override
    public Map<String, Object> convert(Map<String, Object> json) {

        for (Map.Entry<String, Object> entry : json.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                // Si el valor se trata de un String, intento analizar la fecha parseandola.
                if(isParseable((String) value)) {
                    //Si se puede parsear es una fecha.
                    String dateConverted = convertUtcToHourZone((String) value);
                    json.put(key, dateConverted);
                }
            } else if (value instanceof List) {

                System.out.println("Campo: " + key + " (Lista):");
                List<?> list = (List<?>) value;
                for (Object listItem : list) {
                    if (listItem instanceof Map) {
                        /*
                        * Ingresa a este if cuando uno de los campos de la lista es un json, como por
                        * ejemplo cuando tiene esta estructura:
                        * "objects": [
                        *   {
                        *     "key": "value"
                        *   }
                        * ]
                         */
                        convert((Map<String, Object>) listItem);
                    } else {
                        System.out.println("Valor: " + listItem);
                    }
                }
            } else if (value instanceof Map) {

                /* Ingresa a este if cuando se trata de un json que tienen la estructura de
                * {
                *   "key": {
                *     "key2": "value"
                *   }
                * }
                * */

                System.out.println("Campo: " + key + " (Objeto anidado):");
                convert((Map<String, Object>) value);
            } else {
                System.out.println("Campo: " + key + ", Valor: " + value);
            }
        }

        return json;
    }

    private String convertUtcToHourZone(String utcTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime utcLocalDateTime = LocalDateTime.parse(utcTimeString, formatter);

        // funciona pero tenemos que pasar un integer con el - o +
        //ZoneOffset gmtMinus3Offset = ZoneOffset.ofHours(-3);

        // este dato podemos obtener de base de datos, y en caso de almacenar GMT-3 podriamos obtener el -3 igualmente
        // usando replace("GMT", "")
        ZoneOffset gmtMinus3Offset = ZoneOffset.of("-3");

        // Convertir a GMT-3 (Argentina) para prueba
        Instant instant = utcLocalDateTime.toInstant(ZoneOffset.UTC);
        LocalDateTime gmtMinus3LocalDateTime = instant.atOffset(gmtMinus3Offset).toLocalDateTime();

        // Formatear en el formato deseado
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return gmtMinus3LocalDateTime.format(outputFormatter);
    }

    private boolean isParseable(String value) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date dateValue = dateFormat.parse(value);
            System.out.println("Valor (formateado): " + dateValue);
            return true;
        } catch (ParseException e) {
            // Si no se puede analizar como fecha, simplemente imprime el valor original y no hago mas nada con
            // el campo
            System.out.println("Valor: " + value);
            return false;
        }
    }
}
