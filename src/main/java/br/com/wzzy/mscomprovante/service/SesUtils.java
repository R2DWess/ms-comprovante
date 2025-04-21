package br.com.wzzy.mscomprovante.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import software.amazon.awssdk.core.SdkBytes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SesUtils {

    public static SdkBytes criarMensagemComAnexo(
            String from, String to, String subject, String bodyText, String fileName, byte[] attachmentBytes
    ) throws MessagingException, IOException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);

        MimeMessage message = new MimeMessage(session);
        message.setSubject(subject, "UTF-8");
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

        MimeBodyPart texto = new MimeBodyPart();
        texto.setContent(bodyText, "text/plain; charset=UTF-8");

        MimeBodyPart anexo = new MimeBodyPart();
        anexo.setFileName(fileName);
        anexo.setContent(attachmentBytes, "application/pdf");
        anexo.setHeader("Content-Transfer-Encoding", "base64");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(texto);
        multipart.addBodyPart(anexo);

        message.setContent(multipart);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        message.writeTo(outputStream);

        return software.amazon.awssdk.core.SdkBytes.fromByteArray(outputStream.toByteArray());
    }
}