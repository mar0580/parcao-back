package com.parcao.models;

public enum EEmailDetails {
    ESTOQUE_BAIXO("Estoque baixo");

    private final String levelCode;

    EEmailDetails(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getEEmailDetails() {
        return this.levelCode;
    }
}


