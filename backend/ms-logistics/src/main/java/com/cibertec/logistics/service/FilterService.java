package com.cibertec.logistics.service;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class FilterService {

    public <T> List<T> filter(List<T> rows, Map<String, String> filters, Class<T> clazz) {
        if (filters == null || filters.isEmpty()) {
            return rows;
        }

        return rows.stream().filter(row -> matches(row, filters, clazz)).toList();
    }

    private <T> boolean matches(T row, Map<String, String> filters, Class<T> clazz) {
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            Object current = readField(clazz, row, entry.getKey());
            if (current == null || !String.valueOf(current).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    private <T> Object readField(Class<T> clazz, T row, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(row);
        } catch (Exception e) {
            return null;
        }
    }
}
