package com.csye6225.assignment3.metrics;

import com.timgroup.statsd.NoOpStatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsClient {

    @Value("${publish.metrics}")
    private boolean publishMetrics;
    @Value("${metrics.server.hostname}")
    private String metricsHost;
    @Value("${metrics.server.port}")
    private int metricsPort;

    @Bean
    public StatsDClient statsDClient() {
        if(publishMetrics) {
            return new NonBlockingStatsDClient("csye6225", metricsHost, metricsPort);
        }
        return new NoOpStatsDClient();
    }
}
