package com.cibertec.logistics.messaging;

import com.cibertec.logistics.config.RabbitConfig;
import com.cibertec.logistics.entity.Shipment;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ShipmentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ShipmentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCreated(Shipment shipment) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.CREATED_KEY,
                Map.of(
                        "event", "shipment.created",
                        "shipmentId", shipment.getId(),
                        "transportId", shipment.getShipmentTransportId(),
                        "statusId", shipment.getShipmentStatusId(),
                        "date", String.valueOf(shipment.getShipmentDate())
                )
        );
    }

    public void publishStatusChanged(Shipment shipment) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.STATUS_CHANGED_KEY,
                Map.of(
                        "event", "shipment.status.changed",
                        "shipmentId", shipment.getId(),
                        "statusId", shipment.getShipmentStatusId(),
                        "transportId", shipment.getShipmentTransportId()
                )
        );
    }
}
