package com.email_service.email_service.services;

import com.email_service.email_service.config.RabbitMQConfig;
import com.email_service.email_service.models.EmailEvent;
import com.email_service.email_service.models.OrderToPdfDTO;
import com.email_service.email_service.models.ProductRecord;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_EMAIL)
    public void sendRegistrationEmail(EmailEvent emailEvent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailEvent.getTo());
        message.setSubject(emailEvent.getSubject());
        message.setText(emailEvent.getBody());
        mailSender.send(message);
        System.out.println("Correo enviado a: " + emailEvent.getTo());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PDF)
    public void sendPdfOrderEmail (OrderToPdfDTO orderDTO) throws MessagingException {
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            generatePdfContent(contentStream, orderDTO, page);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
            document.close();
            System.out.println("se creo el pdf");


            sendEmail(orderDTO.getUserMail(),pdfBytes,"document.pdf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generatePdfContent(PDPageContentStream contentStream, OrderToPdfDTO orderDTO, PDPage page) throws IOException {
        PDType1Font font = PDType1Font.HELVETICA_BOLD;
        contentStream.setFont(font, 14);
        contentStream.beginText();
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(64, 750); // Posición inicial (x, y)
        contentStream.showText("Order ID: "+orderDTO.getOrderId());
        contentStream.newLine();
        for (ProductRecord item : orderDTO.getnewProductList()){
            contentStream.showText("Product ID: " + item.id() + " name: " + item.name() + " description: " + item.description()+" price: "+ item.price()+  " Quantity: "+ item.quantity());
            contentStream.newLine();
        }
        contentStream.endText();
        contentStream.close();
    }

    private void sendEmail(String to, byte[] pdfBytes, String fileName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Your Order Confirmation");
        helper.setText("Dear Customer,\n\nPlease find your order details attached.", false);

        helper.addAttachment(fileName, () -> new java.io.ByteArrayInputStream(pdfBytes));

        mailSender.send(message);
    }
}
