package br.com.wzzy.mscomprovante.service;

import br.com.wzzy.mscomprovante.model.request.CompraRequest;

public interface ComprovanteService {
    String gerar(CompraRequest compra);
}
