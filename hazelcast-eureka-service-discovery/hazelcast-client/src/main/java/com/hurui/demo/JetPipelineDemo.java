package com.hurui.demo;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import com.hazelcast.collection.IQueue;
import com.hazelcast.config.ReliableTopicConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.function.ComparatorEx;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.aggregate.AggregateOperations;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.config.ProcessingGuarantee;
import com.hazelcast.jet.datamodel.WindowResult;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.WindowDefinition;
import com.hazelcast.jet.pipeline.test.TestSources;
import com.hazelcast.map.IMap;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.MessageListener;

@RestController
public class JetPipelineDemo implements ApplicationListener<ApplicationStartedEvent> {


    public static final int TOP = 10;
    private static final String RESULTS = "top10_results";
    private Map<Long, String> inMemoryMap;
    private AtomicInteger counter = new AtomicInteger();
    
	@Autowired
	private JetInstance jetInstance;
	
	@Autowired
	private HazelcastInstance hazelcastInstance;
	
	@Autowired
	private SampleMessageListener sampleMessageListener;
	
	@Autowired
	private SampleItemListener sampleItemListener;
	
	public JetPipelineDemo() {
		//init in memory map
		//inMemoryMap = hazelcastInstance.getMap("map");
		//hazelcastInstance.getQueue("queue");
		initPipelines();
	}

	//We will only initialize the hazelcast resources only when the Spring Context has started to prevent NPE
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		ITopic<String> topic = hazelcastInstance.getTopic("topic");
		topic.addMessageListener(sampleMessageListener);
		IQueue<String> queue = hazelcastInstance.getQueue("queue");
		queue.addItemListener(sampleItemListener, Boolean.TRUE);
	}
	
	private void initPipelines() {
        
       
	}
	
    private static Pipeline buildPipeline() {
        Pipeline p = Pipeline.create();
        p.readFrom(TestSources.itemStream(100, (ts, seq) -> ThreadLocalRandom.current().nextLong()))
                .withIngestionTimestamps()
                .window(WindowDefinition.tumbling(1000))
                .aggregate(AggregateOperations.topN(TOP, ComparatorEx.comparingLong(l -> l)))
                .map(WindowResult::result)
                .writeTo(Sinks.observable(RESULTS));
        return p;
    }
    
    @RequestMapping(path = "/map", method = {RequestMethod.GET, RequestMethod.PUT}, consumes = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> map(HttpMethod method) {
    	if(method.equals(HttpMethod.GET)) {
    		return new ResponseEntity<>(hazelcastInstance.getMap("map"), HttpStatus.OK);
    	} else {
    		hazelcastInstance.getMap("map").put(counter.getAndAdd(1), "This is a test message. Count: " + (counter.get() - 1));
    		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    	}
    }
    
    @GetMapping("/queue")
    public ResponseEntity<?> queue() {
    	hazelcastInstance.getQueue("queue").add("This is a test message");
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/topic")
    public ResponseEntity<?> topic() {
    	hazelcastInstance.getTopic("topic").publish("This is a test message");	
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/pipeline")
    public void test() {
    	inMemoryMap = hazelcastInstance.getMap("map");
    	hazelcastInstance.getQueue("queue");
    	Pipeline p = buildPipeline();
    	JobConfig config = new JobConfig();
        config.setName("hello-world");
        config.setProcessingGuarantee(ProcessingGuarantee.EXACTLY_ONCE);
        jetInstance.newJob(p).join();
        return;
    }

}
