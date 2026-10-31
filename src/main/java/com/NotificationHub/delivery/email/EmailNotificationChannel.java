package com.NotificationHub.delivery.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.NotificationHub.delivery.NotificationChannel;
import com.NotificationHub.notification.entity.NotificationEvent;
import com.NotificationHub.template.entity.NotificationTemplate;

@Component
@RequiredArgsConstructor
public class EmailNotificationChannel implements NotificationChannel {

    private final JavaMailSender mailSender;

    @Override
    public String getChannelType() {
        return "EMAIL";
    }

    @Override
    public void send(
            NotificationEvent event,
            NotificationTemplate template
    ) {

        String recipient = extractRecipient(event);

        String subject = render(
                template.getSubject(),
                event
        );

        String body = render(
                template.getBody(),
                event
        );

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    private String extractRecipient(NotificationEvent event) {

        Object email = event.getPayload().get("email");

        if (email == null) {
            throw new IllegalArgumentException(
                    "Email address missing from notification payload"
            );
        }

        return email.toString();
    }

    private String render(
            String template,
            NotificationEvent event
    ) {

        String result = template;

        for (var entry : event.getPayload().entrySet()) {

            String placeholder = "{{" + entry.getKey() + "}}";

            String value = String.valueOf(entry.getValue());

            result = result.replace(
                    placeholder,
                    value
            );
        }

        return result;
    }
}
