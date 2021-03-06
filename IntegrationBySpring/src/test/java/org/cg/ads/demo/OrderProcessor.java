package org.cg.ads.demo;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component()
public class OrderProcessor {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(OrderProcessor.class);

	public String processOrder(String order) {
		logger.info("Processing order {}", order);
		
		if (isInvalidOrder(order)) {
			logger.info("Error while processing order [{}]", order);
			throw new RuntimeException(order);
		}
		
		return "confirmed";
	}
	
	private boolean isInvalidOrder(String order) {
		return order.contains("invalid");
	}
}