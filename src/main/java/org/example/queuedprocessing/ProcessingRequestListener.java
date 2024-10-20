package org.example.queuedprocessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

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
        // Submit the request to the queue for processing
        submitToQueueService.submitItem(request);

        // Immediately log that the request was submitted
        logger.info("Submitted request with key: " + request.getKey());
    }
}
