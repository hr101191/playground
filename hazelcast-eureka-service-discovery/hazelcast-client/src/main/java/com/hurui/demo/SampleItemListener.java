package com.hurui.demo;

import org.springframework.stereotype.Component;

import com.hazelcast.collection.ItemEvent;
import com.hazelcast.collection.ItemListener;

@Component
public class SampleItemListener implements ItemListener<String> {

	@Override
	public void itemAdded(ItemEvent<String> item) {
		System.out.println("Item added to queue: " + item.getItem());
	}

	@Override
	public void itemRemoved(ItemEvent<String> item) {
		System.out.println("Item removed from queue: " + item.getItem());
	}

}
