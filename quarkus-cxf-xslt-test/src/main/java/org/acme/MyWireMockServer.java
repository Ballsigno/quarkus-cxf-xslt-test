package org.acme;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
@UnlessBuildProfile("prod") // use dev and test
public class MyWireMockServer {

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "text/xml";

    private WireMockServer wireMockServer = new WireMockServer(options()
            .port(8099)
            .usingFilesUnderClasspath("/wiremock"));

    void onStart(@Observes StartupEvent ev) {
        wireMockServer.start();

        wireMockServer.stubFor(
                post("/cxf/dummy")
                        .withHeader(CONTENT_TYPE, containing(CONTENT_TYPE_VALUE))
                        .withRequestBody(matching(".*<ns2:hello.*"))
                        .willReturn(ok()
                                .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                                .withBodyFile("TestResponse.xml")));
    }

    void onStop(@Observes ShutdownEvent ev) {
        wireMockServer.shutdown();
    }
}
