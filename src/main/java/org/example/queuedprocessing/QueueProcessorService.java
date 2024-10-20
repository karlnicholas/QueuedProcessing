package org.example.queuedprocessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class QueueProcessorService {

    private final SubmitToQueueService submitToQueueService;
    private volatile boolean running = true;

    @Autowired
    public QueueProcessorService(SubmitToQueueService submitToQueueService) {
        this.submitToQueueService = submitToQueueService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        new Thread(this::processItems).start();
    }

    public void processItems() {
        try {
            while (running) {
                // Take the first item (blocks until at least one is available)
                ProcessingRequest initialRequest = submitToQueueService.getRequestQueue().take();

                // Collect any additional available items from the queue
                List<ProcessingRequest> requests = new ArrayList<>();
                requests.add(initialRequest);
                submitToQueueService.getRequestQueue().drainTo(requests); // Drains all available items into the list

                // Process all collected items at once
                processItem(requests);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }

    private String processItem(List<ProcessingRequest> requests) {
        // Simulate processing all requests
        for (ProcessingRequest request : requests) {
            String result = "Processed: " + request.getData();
            request.getFuture().complete(result); // Notify the async method that processing is complete
        }
        return "Batch of " + requests.size() + " requests processed";
    }

    @EventListener(ContextClosedEvent.class)
    public void onApplicationShutdown() {
        running = false; // Set running to false to stop the loop
    }
}

