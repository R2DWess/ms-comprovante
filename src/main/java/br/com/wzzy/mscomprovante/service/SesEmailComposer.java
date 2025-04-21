package br.com.wzzy.mscomprovante.service;

import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.ses.model.RawMessage;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Properties;

@Service
public class SesEmailComposer {

    public static RawMessage compor(String destinatario, String anexoBase64, String nomeArquivo) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);

        MimeMessage message = new MimeMessage(session);
        message.setSubject("Seu comprovante de compra");
        message.setFrom(new InternetAddress("seu-email-verificado@dominio.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));

        MimeBodyPart texto = new MimeBodyPart();
        texto.setText("Segue em anexo o comprovante da sua compra.");

        MimeBodyPart anexo = new MimeBodyPart();
        anexo.setFileName(nomeArquivo);
        anexo.setContent(Base64.getDecoder().decode(anexoBase64), "application/pdf");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(texto);
        multipart.addBodyPart(anexo);

        message.setContent(multipart);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message.writeTo(outputStream);
        return RawMessage.builder()
                .data(SdkBytes.fromByteArray(outputStream.toByteArray()))
                .build();
    }
}