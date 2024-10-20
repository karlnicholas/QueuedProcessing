package org.example.queuedprocessing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessingRequestListenerTest {

    @Mock
    private SubmitToQueueService submitToQueueService;

    @InjectMocks
    private ProcessingRequestListener processingRequestListener;

    private final Logger logger = Logger.getLogger(ProcessingRequestListener.class.getName());

    @BeforeEach
    void setup() {
        // Any additional setup if needed
    }

    @Test
    void testReceiveAndProcessRequestSuccess() throws Exception {
        ProcessingRequest request = new ProcessingRequest("key1", "data1");
        CompletableFuture<String> future = new CompletableFuture<>();
        when(submitToQueueService.submitItem(request)).thenReturn(future);

        future.complete("Processed: data1");
        processingRequestListener.receiveAndProcessRequest(request);

        verify(submitToQueueService).submitItem(request);
    }

    @Test
    void testReceiveAndProcessRequestFailure() throws Exception {
        ProcessingRequest request = new ProcessingRequest("key2", "data2");
        CompletableFuture<String> future = new CompletableFuture<>();
        when(submitToQueueService.submitItem(request)).thenReturn(future);

        future.completeExceptionally(new RuntimeException("Processing failed"));
        processingRequestListener.receiveAndProcessRequest(request);

        verify(submitToQueueService).submitItem(request);
    }
}
