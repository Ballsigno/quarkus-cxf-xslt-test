package org.acme;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.service.DummyWebService;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.feature.transform.XSLTInInterceptor;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.phase.Phase;

import io.quarkiverse.cxf.annotation.CXFClient;
import io.quarkus.logging.Log;
import io.quarkus.runtime.configuration.ProfileManager;

/**
 * This is a reproducer project, so I just add code in controller...
 */
@Path("/test")
public class GreetingResource {

    @Inject
    @CXFClient
    DummyWebService dummyWebServiceClient;

    /**
     * attempt using xslt (failed in native mode!)
     */
    @Path("hello1")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test1() {
        Log.info("attempting to set the xslt...");

        // get my client
        Client client = ClientProxy.getClient(dummyWebServiceClient);

        // create XSLT InInterceptor (from ~/resources/test.xsl)
        XSLTInInterceptor inInterceptor = new XSLTInInterceptor(Phase.RECEIVE, null, null, "test.xsl"); // failed(native)

        // add to my client settings
        client.getInInterceptors().add(inInterceptor);

        if ("prod".equals(ProfileManager.getActiveProfile())) {
            Log.info("in native image, just return string. don't call wiremock.");
            return "hello";
        }

        Log.info("call wiremock.");
        return dummyWebServiceClient.hello();
    }

    /**
     * attempt reading a xslt file (success!)
     */
    @Path("hello2")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test2() {
        Log.info("attempting to get the file...");
        InputStream testStream = ClassLoaderUtils.getResourceAsStream("test.xsl", this.getClass());

        if (Objects.nonNull(testStream)) {
            try {
                Log.infov("got the file successfully: {0}", testStream.available());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if ("prod".equals(ProfileManager.getActiveProfile())) {
            Log.info("in native image, just return string. don't call wiremock.");
            return "hello";
        }

        Log.info("call wiremock.");
        return dummyWebServiceClient.hello();
    }
}