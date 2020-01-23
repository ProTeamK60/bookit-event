package se.knowit.bookitevent.kafka.producer;

public interface KafkaProducerService<K,V> {
    void sendMessage(final String topic, final K key, final V dto);
}
