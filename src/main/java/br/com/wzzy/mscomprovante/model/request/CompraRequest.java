package br.com.wzzy.mscomprovante.model.request;

import br.com.wzzy.mscomprovante.model.dto.ProdutoDTO;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class CompraRequest {
    private String emailCliente;
    private List<ProdutoDTO> produtos = new ArrayList<>();
}
