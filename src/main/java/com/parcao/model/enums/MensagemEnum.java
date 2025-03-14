package com.parcao.model.enums;

public enum MensagemEnum {
    INEXISTENTE("INEXISTENTE"),
    USUARIO_JA_EXISTE("USUARIO JÁ EXISTE"),
    USUARIO_NAO_EXISTE("USUARIO NÃO EXISTE"),
    SUCESSO("SUCESSO"),
    PASSWORD_INCORRETO("PASSWORD INCORRETO");

    private final String mensagem;

    MensagemEnum(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
