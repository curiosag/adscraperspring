package app.server;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.cg.ads.ScanScheduler;
import org.cg.ads.SystemEntryGateway;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScanSchedulerIntegration {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    SystemEntryGateway entry;
    
    ScanScheduler s;
    
    // use the esoteric gatewayProxyFactory
    public ScanSchedulerIntegration (SystemEntryGateway entry){
    	this.entry = entry;
    }
    
    @Scheduled(fixedRate = 20000)
    public String reportCurrentTime() {
    	String id = "Running scan at " + dateFormat.format(new Date());
    	entry.trigger(id);
    	return id;
 
    }
}