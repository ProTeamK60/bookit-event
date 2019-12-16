package se.knowit.bookitevent.servicediscovery;

import org.springframework.core.env.Environment;

public class LocalDiscoveryServiceImpl implements DiscoveryService {

    private Environment env;
    private final static String PREFIX = "discovery.service.";

    public LocalDiscoveryServiceImpl(Environment env) {
        this.env = env;
    }

    @Override
    public String discoverInstance(String serviceName) {
        return env.getProperty(PREFIX + serviceName);
    }
}
