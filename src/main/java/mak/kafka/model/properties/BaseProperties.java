package mak.kafka.model.properties;

import lombok.Data;

@Data
public class BaseProperties {
    protected String successLog, failureLog, inputLog, prettyPrintLog;
    protected boolean prettyPrint, loggingEnabled;
}
