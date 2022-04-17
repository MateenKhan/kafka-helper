package mak.kafka.model.properties;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Data
@Component("consumerProperties")
@ConfigurationProperties(prefix = "kafka.consumer")
public class ConsumerProperties extends BaseProperties {

    public HashMap<String, Object> configMap;

    String topics;

    @Value("${kafka.isConsumer}")
    private boolean isConsumer;

    @Value("${kafka.isOffsetListener}")
    private boolean isOffsetListener;

    public String getGroupId() {
        return configMap != null && configMap.get("group.id") != null ? configMap.get("group.id").toString() : "";
    }

}
