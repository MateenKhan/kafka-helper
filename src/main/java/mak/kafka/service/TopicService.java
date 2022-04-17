package mak.kafka.service;

import lombok.AllArgsConstructor;
import mak.kafka.model.ConsumeDetails;
import mak.kafka.model.ProducerMessage;
import mak.kafka.model.Topic;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TopicService {

    Admin admin;
    KafkaConsumer consumer;

    KafkaProducer producer;

    public Set<String> getTopics() throws ExecutionException, InterruptedException {
        Set<String> result = admin.listTopics().names().get();
        return result;
    }

    public List<Integer> getPartitions(String topic) {
        List<PartitionInfo> partitions = consumer.partitionsFor(topic);
        return partitions.stream().map(PartitionInfo::partition).sorted().collect(Collectors.toList());
    }

    public Map<String, List<Integer>> getTopicPartitions() throws ExecutionException, InterruptedException {
        Set<String> topics = getTopics();
        Map<String, List<Integer>> result = new HashMap<>();
        topics.stream().forEach(topic -> result.put(topic, getPartitions(topic)));
        return result;
    }

    public boolean createTopic(Topic topic) throws ExecutionException, InterruptedException {
        CreateTopicsResult result = admin.createTopics(Collections.singleton(new NewTopic(topic.getName(), topic.getPartitions(), topic.getReplicationFactor())));
        KafkaFuture<Void> future = result.values().get(topic.getName());
        future.get();
        return true;
    }

    public ProducerMessage publishMessage(ProducerMessage message) throws ExecutionException, InterruptedException {
        Future<RecordMetadata> result = producer.send(new ProducerRecord(message.getTopic(), message.getKey(), message.getMessage()));
        RecordMetadata recordMetadata = result.get();
        message.setOffset(recordMetadata.offset());
        message.setPartition(recordMetadata.partition());
        message.setTopic(recordMetadata.topic());
        message.setTimestamp(recordMetadata.timestamp());
        return message;
    }

    public List<Object> consumeLatestMessages(ConsumeDetails details) {
        List<Object> result = new ArrayList<>();
        consumer.subscribe(Collections.singleton(details.getTopicName()));
        return getObjects(details, result);
    }

    public List<Object> consumeMessages(ConsumeDetails details) {
        List<Object> result = new ArrayList<>();
        String topic = details.getTopicName();
        TopicPartition partition = new TopicPartition(topic, details.getPartition());
        Collection<TopicPartition> partitions = Collections.singleton(partition);
        consumer.assign(partitions);
        consumer.seekToEnd(partitions);
        long endPosition = consumer.position(partition);
        long recentMessagesStartPosition = endPosition - details.getMaxMessagesToRetrieve();
        consumer.seek(partition, recentMessagesStartPosition);
        return getObjects(details, result);
    }

    public List<Object> consumeLatestMessagesFromPartition(ConsumeDetails details) {
        List<Object> result = new ArrayList<>();
        Collection<TopicPartition> partitions = Collections.singleton(new TopicPartition(details.getTopicName(), details.getPartition()));
        consumer.beginningOffsets(partitions);
        consumer.subscribe(Collections.singleton(details.getTopicName()));
        return getObjects(details, result);
    }

    private List<Object> getObjects(ConsumeDetails details, List<Object> result) {
        ConsumerRecords consumerRecords = consumer.poll(Duration.ofSeconds(details.getPollDurationInSeconds()));
        Iterator<ConsumerRecord> itr = consumerRecords.iterator();
        while (itr.hasNext()) {
            ConsumerRecord record = itr.next();
            result.add(record.value());
        }
        consumer.unsubscribe();
        return result;
    }
}
