package com.agrihusac.SysInvent.utils;

public enum TypeMessage {
    DANGER(0),
    WARNING(1),
    INFO(2);

    private final Integer value;

    TypeMessage(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
