package br.com.wzzy.mscomprovante.service;

public interface EmailService {
    void enviarComprovante(String destinatario, String caminhoAnexo);
}
