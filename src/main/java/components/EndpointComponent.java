package components;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
@AllArgsConstructor
public class EndpointComponent {

    private Map<String, String> endpointConfiguationMap;


    @PostConstruct
    public  void loadEndpointConfigurations() throws IOException {
        this.endpointConfiguationMap = new HashMap<>();
        var endpointProperties = new Properties();
//        log.info("Loading Endpoint paths from endpoint.properties");
        endpointProperties.load(EndpointComponent.class.getClassLoader().getResourceAsStream("endpoint.properties"));
        endpointProperties.forEach((k, v)->endpointConfiguationMap.put(k.toString(), v.toString()));
 //       log.info(endpointProperties.toString());
    }

    public Map<String, String> getEndpointConfiguationMap(){

        return this.endpointConfiguationMap;
    }

}
