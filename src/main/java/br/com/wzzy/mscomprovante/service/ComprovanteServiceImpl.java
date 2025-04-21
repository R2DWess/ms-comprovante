package br.com.wzzy.mscomprovante.service;

import br.com.wzzy.mscomprovante.model.dto.ProdutoDTO;
import br.com.wzzy.mscomprovante.model.request.CompraRequest;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.pdf.PdfDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ComprovanteServiceImpl implements ComprovanteService{

    private EmailService emailService;
    private S3Client s3Client;

    private final String bucketName = "wzzy-comprovantes";

    @Autowired
    public ComprovanteServiceImpl(EmailService emailService, S3Client s3Client) {
        this.emailService = emailService;
        this.s3Client = s3Client;
    }

    public String gerar(CompraRequest compra) {
        try {
            if (compra.getProdutos() == null || compra.getProdutos().isEmpty()) {
                throw new IllegalArgumentException("A lista de produtos está vazia ou nula.");
            }

            String nomeArquivo = "comprovante-" + UUID.randomUUID() + ".pdf";
            String caminho = "comprovantes/" + nomeArquivo;
            File file = new File(caminho);
            file.getParentFile().mkdirs();

            PdfWriter writer = new PdfWriter(new FileOutputStream(file));
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

            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(nomeArquivo)
                            .contentType("application/pdf")
                            .metadata(Map.of(
                                    "cliente-email", compra.getEmailCliente(),
                                    "data-compra", LocalDateTime.now().toString()
                            ))
                            .build(),
                    Paths.get(caminho));

            emailService.enviarComprovante(compra.getEmailCliente(), caminho);

            return "s3://" + bucketName + "/" + nomeArquivo;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    @Override
    public List<String> listarComprovantes() {
        ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response response = s3Client.listObjectsV2(request);

        return response.contents().stream()
                .map(s3Object -> "s3://" + bucketName + "/" + s3Object.key())
                .collect(Collectors.toList());
    }
}
