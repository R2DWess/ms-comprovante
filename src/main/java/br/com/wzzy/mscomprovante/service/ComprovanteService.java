package br.com.wzzy.mscomprovante.service;

import br.com.wzzy.mscomprovante.model.request.CompraRequest;

import java.util.List;

public interface ComprovanteService {
    String gerar(CompraRequest compra);

    List<String> listarComprovantes();
}
