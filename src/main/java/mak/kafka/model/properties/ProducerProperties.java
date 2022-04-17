package mak.kafka.model.properties;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Data
@Component
@ConfigurationProperties(prefix = "kafka.producer")
public class ProducerProperties extends BaseProperties {

    public HashMap<String, Object> configMap;

    private String topic;

    @Value("${kafka.isProducer}")
    private boolean isProducer;

    public void setConfigMap(HashMap<String, Object> configMap) {
        this.configMap = configMap;
    }


}
