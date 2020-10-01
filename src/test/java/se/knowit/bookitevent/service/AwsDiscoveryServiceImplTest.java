package se.knowit.bookitevent.service;

import com.amazonaws.services.servicediscovery.AWSServiceDiscovery;
import com.amazonaws.services.servicediscovery.model.DiscoverInstancesResult;
import com.amazonaws.services.servicediscovery.model.HttpInstanceSummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.knowit.bookitevent.servicediscovery.AwsDiscoveryServiceImpl;
import se.knowit.bookitevent.servicediscovery.DiscoveryServiceResult;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AwsDiscoveryServiceImplTest {

    private final static String NAMESPACENAME = "namespaceName";
    private final static String SERVICENAME = "serviceName";
    private final static String AWS_INSTANCE_IPV4 = "AWS_INSTANCE_IPV4";
    private final static String AWS_INSTANCE_PORT = "AWS_INSTANCE_PORT";

    @Mock
    AWSServiceDiscovery serviceDiscoverClient;

    @InjectMocks
    private AwsDiscoveryServiceImpl awsDiscoveryService;

    @Test
    void discoverInstancesTest() {
        DiscoverInstancesResult discoveryResult = new DiscoverInstancesResult();
        Map<String, String> attributes = new LinkedHashMap<>();
        attributes.put(AWS_INSTANCE_IPV4, AWS_INSTANCE_IPV4);
        attributes.put(AWS_INSTANCE_PORT, AWS_INSTANCE_PORT);
        HttpInstanceSummary instanceSummaries = new HttpInstanceSummary();
        instanceSummaries.setAttributes(attributes);
        discoveryResult.setInstances(Collections.singleton(instanceSummaries));
        when(serviceDiscoverClient.discoverInstances(any())).thenReturn(discoveryResult);
        DiscoveryServiceResult discoveryServiceResult = awsDiscoveryService.discoverInstances(NAMESPACENAME,
                SERVICENAME);
        assertEquals(discoveryServiceResult.getInstances().get(0).getInstanceIpv4(), AWS_INSTANCE_IPV4);
        assertEquals(discoveryServiceResult.getInstances().get(0).getInstancePort(), AWS_INSTANCE_PORT);
        assertEquals(discoveryServiceResult.getInstances().get(0).getRegion(), null);
    }
}
