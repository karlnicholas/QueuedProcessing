package org.example.queuedprocessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Component
public class ProcessingRequestListener {

    private static final Logger logger = Logger.getLogger(ProcessingRequestListener.class.getName());

    private final SubmitToQueueService submitToQueueService;

    @Autowired
    public ProcessingRequestListener(SubmitToQueueService submitToQueueService) {
        this.submitToQueueService = submitToQueueService;
    }

    @JmsListener(destination = "processing-requests-queue", concurrency = "4")
    public void receiveAndProcessRequest(ProcessingRequest request) {
        try {
            // Submit the request to the SubmitToQueueService and wait for the response
            CompletableFuture<String> future = submitToQueueService.submitItem(request);
            String result = future.get(); // Blocking until the result is available

            // Log the result
            logger.info("Processed request with key: " + request.getKey() + ", result: " + result);
        } catch (Exception e) {
            logger.severe("Failed to process request with key: " + request.getKey() + ", error: " + e.getMessage());
        }
    }
}
