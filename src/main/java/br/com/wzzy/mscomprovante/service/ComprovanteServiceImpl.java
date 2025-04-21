package br.com.wzzy.mscomprovante.service;

import br.com.wzzy.mscomprovante.model.dto.ProdutoDTO;
import br.com.wzzy.mscomprovante.model.request.CompraRequest;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ComprovanteServiceImpl implements ComprovanteService{

    private EmailService emailService;

    @Autowired
    public ComprovanteServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    public String gerar(CompraRequest compra) {
        try {
            if (compra.getProdutos() == null || compra.getProdutos().isEmpty()) {
                throw new IllegalArgumentException("A lista de produtos está vazia ou nula.");
            }

            String nomeArquivo = "comprovante-" + System.currentTimeMillis() + ".pdf";
            String caminho = "comprovantes/" + nomeArquivo;
            File file = new File("comprovantes");
            if (!file.exists()) file.mkdirs();

            PdfWriter writer = new PdfWriter(new FileOutputStream(caminho));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Comprovante de Compra"));
            document.add(new Paragraph("Cliente: " + compra.getEmailCliente()));
            document.add(new Paragraph("Data: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

            float[] colWidths = {300F, 100F};
            Table table = new Table(colWidths);
            table.addCell("Produto");
            table.addCell("Preço (R$)");

            for (ProdutoDTO p : compra.getProdutos()) {
                table.addCell(p.getTitle());
                table.addCell(String.format("%.2f", p.getPrice()));
            }

            document.add(table);
            document.close();

            emailService.enviarComprovante(compra.getEmailCliente(), caminho);

            return caminho;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}