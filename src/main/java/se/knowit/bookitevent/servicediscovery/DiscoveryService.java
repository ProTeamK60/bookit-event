package se.knowit.bookitevent.servicediscovery;

public interface DiscoveryService {
    DiscoveryServiceResult discoverInstances(String namespaceName, String serviceName);
}
