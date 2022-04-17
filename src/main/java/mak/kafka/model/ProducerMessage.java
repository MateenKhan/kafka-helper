package mak.kafka.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProducerMessage {
    String topic, key, message;
    long offset, timestamp;
    int partition;

}
