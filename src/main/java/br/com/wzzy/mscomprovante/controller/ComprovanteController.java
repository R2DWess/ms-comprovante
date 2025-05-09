package br.com.wzzy.mscomprovante.controller;

import br.com.wzzy.mscomprovante.model.request.CompraRequest;
import br.com.wzzy.mscomprovante.service.ComprovanteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/comprovantes")
public class ComprovanteController {

    private final ComprovanteService comprovanteService;

    public ComprovanteController(ComprovanteService comprovanteService) {
        this.comprovanteService = comprovanteService;
    }

    @PostMapping
    public ResponseEntity<String> gerarComprovante(@RequestBody CompraRequest compra) {
        String path = comprovanteService.gerar(compra);
        return ResponseEntity.ok("Comprovante gerado em: " + path);
    }


    @GetMapping
    public ResponseEntity<?> listarComprovantes() {
        return ResponseEntity.ok(comprovanteService.listarComprovantes());
    }
}
