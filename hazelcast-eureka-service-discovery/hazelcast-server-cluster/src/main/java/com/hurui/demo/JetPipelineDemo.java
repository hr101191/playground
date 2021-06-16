package com.hurui.demo;

import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.function.ComparatorEx;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Util;
import com.hazelcast.jet.aggregate.AggregateOperations;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.config.ProcessingGuarantee;
import com.hazelcast.jet.datamodel.WindowResult;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.WindowDefinition;
import com.hazelcast.jet.pipeline.test.TestSources;

@RestController
public class JetPipelineDemo {


    public static final int TOP = 10;
    private static final String RESULTS = "top10_results";
    
	@Autowired
	private JetInstance jetInstance;
	
	public JetPipelineDemo() {
		initPipelines();
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
    
    @GetMapping("/test")
    public void test() {
    	Pipeline p = buildPipeline();
    	JobConfig config = new JobConfig();
        config.setName("hello-world");
        config.setProcessingGuarantee(ProcessingGuarantee.EXACTLY_ONCE);
        jetInstance.newJobIfAbsent(p, config).join();
    }
}
