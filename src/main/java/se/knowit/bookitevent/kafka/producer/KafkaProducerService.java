package se.knowit.bookitevent.kafka.producer;

public interface KafkaProducerService<T> {
    void sendMessage(final String topic, final T dto);
}
