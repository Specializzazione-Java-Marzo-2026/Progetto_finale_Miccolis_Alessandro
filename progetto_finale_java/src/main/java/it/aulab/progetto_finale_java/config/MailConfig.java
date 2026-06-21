package it.aulab.progetto_finale_java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import jakarta.mail.internet.MimeMessage;

@Configuration
public class MailConfig {


@Bean
public JavaMailSender javaMailSender() {
    return new JavaMailSenderImpl() {

        @Override
        public void send(SimpleMailMessage simpleMessage) {
            System.out.println("=== EMAIL SIMULATA ===");
            System.out.println("TO: " + String.join(", ", simpleMessage.getTo() != null ? simpleMessage.getTo() : new String[]{}));
            System.out.println("SUBJECT: " + simpleMessage.getSubject());
            System.out.println("TEXT: " + simpleMessage.getText());
            System.out.println("======================");
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) {
            for (SimpleMailMessage message : simpleMessages) {
                send(message);
            }
        }

        @Override
        public void send(MimeMessage mimeMessage) {
            System.out.println("=== EMAIL MIME SIMULATA ===");
            System.out.println("Messaggio MIME intercettato in locale.");
            System.out.println("===========================");
        }

        @Override
        public void send(MimeMessage... mimeMessages) {
            for (MimeMessage mimeMessage : mimeMessages) {
                send(mimeMessage);
            }
        }

        @Override
        public void send(MimeMessagePreparator mimeMessagePreparator) {
            System.out.println("=== EMAIL PREPARATOR SIMULATA ===");
            System.out.println("MimeMessagePreparator intercettato in locale.");
            System.out.println("=================================");
        }

        @Override
        public void send(MimeMessagePreparator... mimeMessagePreparators) {
            for (MimeMessagePreparator preparator : mimeMessagePreparators) {
                send(preparator);
            }
        }
    };
}


}
