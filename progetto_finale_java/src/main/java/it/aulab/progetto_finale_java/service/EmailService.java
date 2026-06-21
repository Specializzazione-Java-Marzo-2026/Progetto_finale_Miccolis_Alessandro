package it.aulab.progetto_finale_java.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
