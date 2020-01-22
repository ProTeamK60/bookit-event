package se.knowit.bookitevent.kafka.producer;


public interface KafkaProducerService<T> {
    void sendMessage(final T dto);
}
