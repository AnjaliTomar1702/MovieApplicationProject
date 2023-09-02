package com.niit.pushnotification.service;

import com.niit.pushnotification.dto.NotificationDTO;
import com.niit.pushnotification.dto.NotificationDeliveredDTO;
import com.niit.pushnotification.dto.UserSessionDTO;
import com.niit.pushnotification.exception.InvalidCredentialsException;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PushNotificationServiceImpl implements PushNotificationService{

    private final JavaMailSender javaMailSender;
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directNotificationExchange;
    private final DirectExchange directUserEmailExchange;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    @RabbitListener(queues = "notification-consume-queue", ackMode = "MANUAL")
    public void consumeNotification(NotificationDTO notificationDTO, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag, @Header("delivery-date") String deliveryDate) throws Exception{
        try {
            if (LocalDateTime.parse(deliveryDate).isBefore(LocalDateTime.now())) {
                this.sendEmail(notificationDTO);
                channel.basicAck(tag, false);
                this.rabbitTemplate.convertAndSend(directNotificationExchange.getName()
                        , "notification-delivered-routing"
                        , new NotificationDeliveredDTO(notificationDTO.getEmailId(), notificationDTO.getMovie()));
            } else {
                this.rabbitTemplate.convertAndSend(this.directNotificationExchange.getName(), "notification-consume-routing", notificationDTO, message -> {
                    message.getMessageProperties().setDelay(432000000);     //Delay of 12 hours
                    message.getMessageProperties().setHeader("delivery-date", deliveryDate);
                    return message;
                });
            }
        }
        catch (Exception e){
            channel.basicReject(tag, true);
            throw e;
        }
    }

    @Override
    @RabbitListener(queues = "user-forgot-password-queue", ackMode = "AUTO")
    public void sendUserForgotEmail(UserSessionDTO userSessionDTO){
        if(userSessionDTO.getEmailId() == null)
            throw new InvalidCredentialsException();
        if(userSessionDTO.getSessionId() == null)
            throw new InvalidCredentialsException();
        this.sendEmail(userSessionDTO);
    }


    private void sendEmail(NotificationDTO notificationDTO){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(this.sender);
        simpleMailMessage.setTo(notificationDTO.getEmailId());
        simpleMailMessage.setText(notificationDTO.getMovie().toString());
        simpleMailMessage.setSubject("Movie release notification");
        javaMailSender.send(simpleMailMessage);
    }

    private void sendEmail(UserSessionDTO userSessionDTO){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(this.sender);
        simpleMailMessage.setTo(userSessionDTO.getEmailId());
        simpleMailMessage.setText("http://34.83.1.21/#/reset-password?session=" + userSessionDTO.getSessionId());
        simpleMailMessage.setSubject("Password reset link for Movie App");
        javaMailSender.send(simpleMailMessage);
    }
}
