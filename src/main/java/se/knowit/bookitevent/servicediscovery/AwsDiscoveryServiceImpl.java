package se.knowit.bookitevent.servicediscovery;

import com.amazonaws.services.servicediscovery.AWSServiceDiscovery;
import com.amazonaws.services.servicediscovery.AWSServiceDiscoveryClientBuilder;
import com.amazonaws.services.servicediscovery.model.DiscoverInstancesRequest;
import com.amazonaws.services.servicediscovery.model.DiscoverInstancesResult;
import com.amazonaws.services.servicediscovery.model.HttpInstanceSummary;
import org.springframework.beans.factory.annotation.Value;

public class AwsDiscoveryServiceImpl implements DiscoveryService {

    private final AWSServiceDiscovery service;

    @Value("${discover.healthstatus.filter}")
    private String healthStatus;

    public AwsDiscoveryServiceImpl() {
        service = AWSServiceDiscoveryClientBuilder.defaultClient();
    }

    @Override
    public String discoverInstance(String serviceName) {
        DiscoverInstancesResult result =
                service.discoverInstances(
                        new DiscoverInstancesRequest()
                                .withServiceName(serviceName)
                                .withHealthStatus(healthStatus));
        String address = null;
        if(!result.getInstances().isEmpty()) {
            HttpInstanceSummary instanceSummary = result.getInstances().get(0);
            address = instanceSummary.getAttributes().get("AWS_INSTANCE_IPV4");
        }
        return address;
    }
}
