package mak.kafka.controller;


import lombok.AllArgsConstructor;
import mak.kafka.model.ConsumeDetails;
import mak.kafka.model.ProducerMessage;
import mak.kafka.model.Topic;
import mak.kafka.service.TopicService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@RequestMapping("/topics")
public class TopicController {

    TopicService topicService;

    @GetMapping("/partitions")
    public Map<String, List<Integer>> getTopicsWithPartitions() throws ExecutionException, InterruptedException {
        return topicService.getTopicPartitions();
    }

    @GetMapping()
    public Set<String> getTopics() throws ExecutionException, InterruptedException {
        return topicService.getTopics();
    }

    @GetMapping("/{topic}/partitions/")
    public List<Integer> getPartition(@PathVariable String topic) throws ExecutionException, InterruptedException {
        return topicService.getPartitions(topic);
    }

    @PostMapping()
    public boolean createTopics(@RequestBody Topic topic) throws ExecutionException, InterruptedException {
        return topicService.createTopic(topic);
    }

    @PostMapping("/publish")
    public ProducerMessage publishMessage(@RequestBody ProducerMessage message) throws ExecutionException, InterruptedException {
        return topicService.publishMessage(message);
    }


    @PostMapping("/poll")
    public List<Object> consume(@RequestBody ConsumeDetails details) {
        return topicService.consumeLatestMessages(details);
    }

    @PostMapping("/partition/poll")
    public List<Object> consumeLatestMessagesFromPartition(@RequestBody ConsumeDetails details) {
        return topicService.consumeLatestMessagesFromPartition(details);
    }

    @PostMapping("/partition/consume")
    public List<Object> consumeMessages(@RequestBody ConsumeDetails details) {
        return topicService.consumeMessages(details);
    }



}
