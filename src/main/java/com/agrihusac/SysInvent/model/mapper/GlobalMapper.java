package com.agrihusac.SysInvent.model.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.springframework.stereotype.Component;

@Component
public class GlobalMapper {

    public <S, D> D map(S source, Class<D> destinationClass) {
        if (source == null) {
            return null;
        }

        try {
            D destination = destinationClass.getDeclaredConstructor().newInstance();
            for (Method method : source.getClass().getMethods()) {
                if (!isGetter(method)) {
                    continue;
                }

                String fieldName = getFieldName(method.getName());
                Field field = findField(destinationClass, fieldName);
                if (field == null) {
                    continue;
                }

                Object value = method.invoke(source);
                if (value == null || isAssignable(field.getType(), value.getClass())) {
                    field.setAccessible(true);
                    field.set(destination, value);
                }
            }
            return destination;
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo mapear el objeto", exception);
        }
    }

    private boolean isGetter(Method method) {
        return method.getParameterCount() == 0
                && method.getReturnType() != Void.TYPE
                && (method.getName().startsWith("get") || method.getName().startsWith("is"))
                && !method.getName().equals("getClass");
    }

    private String getFieldName(String methodName) {
        int prefixLength = methodName.startsWith("is") ? 2 : 3;
        String propertyName = methodName.substring(prefixLength);
        return Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
    }

    private Field findField(Class<?> type, String fieldName) {
        Class<?> currentType = type;
        while (currentType != null) {
            try {
                return currentType.getDeclaredField(fieldName);
            } catch (NoSuchFieldException exception) {
                currentType = currentType.getSuperclass();
            }
        }
        return null;
    }

    private boolean isAssignable(Class<?> targetType, Class<?> sourceType) {
        if (targetType.isAssignableFrom(sourceType)) {
            return true;
        }
        return (targetType == Integer.TYPE && sourceType == Integer.class)
                || (targetType == Boolean.TYPE && sourceType == Boolean.class);
    }
}
