package xyz.mcutils.backend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@AllArgsConstructor @Getter
public class Endpoint {

    /**
     * The endpoint.
     */
    private final String endpoint;

    /**
     * The statuses that indicate that the endpoint is online.
     */
    private final List<HttpStatusCode> allowedStatuses;
}
