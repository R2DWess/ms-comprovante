package br.com.wzzy.mscomprovante.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.RawMessage;
import software.amazon.awssdk.services.ses.model.SendRawEmailRequest;

import java.io.File;
import java.nio.file.Files;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${aws.accessKeyId}")
    private String accessKey;

    @Value("${aws.secretAccessKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.ses.sender}")
    private String remetente;

    @Override
    public void enviarComprovante(String destinatario, String caminhoAnexo) {
        try {
            SesClient client = SesClient.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(accessKey, secretKey)
                    ))
                    .build();

            String subject = "Seu comprovante de compra";
            String bodyText = "Olá! Segue anexo o seu comprovante de compra.";

            File file = new File(caminhoAnexo);
            byte[] bytes = Files.readAllBytes(file.toPath());

            RawMessage rawMessage = RawMessage.builder()
                    .data(SesUtils.criarMensagemComAnexo(remetente, destinatario, subject, bodyText, file.getName(), bytes))
                    .build();

            SendRawEmailRequest request = SendRawEmailRequest.builder()
                    .rawMessage(rawMessage)
                    .build();

            client.sendRawEmail(request);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }
}
