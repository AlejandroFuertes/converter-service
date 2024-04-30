package com.capacitacion.converter.service;

import com.capacitacion.converter.dto.FlexibleRequest;

import java.util.Map;

public interface ConverterService {

    Map<String, Object> convert(Map<String, Object> json);
}
