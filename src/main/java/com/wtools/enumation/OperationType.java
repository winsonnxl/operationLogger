package com.wtools.enumation;

public enum OperationType {
    SELECT("查询"),
    INSERT("插入"),
    UPDATE("更新"),
    DELETE("删除");

    private final String value;

    public String getValue() {
        return value;
    }

    OperationType(String value) {
        this.value = value;
    }
}
