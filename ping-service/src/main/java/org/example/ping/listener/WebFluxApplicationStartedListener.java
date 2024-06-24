package org.example.ping.listener;

import org.example.ping.constant.FileConstants;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author Michael
 * @version 2024/6/23
 */
@Component
public class WebFluxApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        final File workspaceDir = FileConstants.WORKSPACE_DIR;
        if (!workspaceDir.exists()) {
            workspaceDir.mkdir();
        }

        final ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        final String port = applicationContext.getEnvironment().getProperty("server.port");
        FileConstants.setLogFile(port);
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
