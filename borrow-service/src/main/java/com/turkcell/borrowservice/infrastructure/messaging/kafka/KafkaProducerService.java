package com.turkcell.borrowservice.infrastructure.messaging.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String key, Object payload) {
        // Spring'in JsonSerializer'ı (YAML'de tanımlı) bu Object'i yakalar,
        // JSON'a çevirir ve __TypeId__ başlığını otomatik ekler.
        kafkaTemplate.send(topic, key, payload);
    }
}
