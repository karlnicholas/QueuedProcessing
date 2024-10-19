package org.example.queuedprocessing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup {

    private final SyncProcessor syncProcessor;

    @Autowired
    public ApplicationStartup(SyncProcessor syncProcessor) {
        this.syncProcessor = syncProcessor;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        new Thread(syncProcessor::processItems).start();
    }
}
