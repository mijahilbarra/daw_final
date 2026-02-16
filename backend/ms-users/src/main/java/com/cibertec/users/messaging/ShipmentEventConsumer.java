package com.cibertec.users.messaging;

import com.cibertec.users.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ShipmentEventConsumer {

    @RabbitListener(queues = RabbitConfig.USERS_EVENTS_QUEUE)
    public void consume(Map<String, Object> payload) {
        System.out.println("[ms-users] Evento recibido: " + payload);
    }
}
