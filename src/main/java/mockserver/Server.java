package mockserver;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.mockserver.client.proxy.ProxyClient;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpClassCallback;
import org.mockserver.model.HttpRequest;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


public class Server {


    public static void main(String[] args) throws IOException {


        ClientAndServer mockServer = startClientAndServer(1080);

        MockServerClient serverClient = new MockServerClient("localhost", 1080);
        serverClient.when(
                        request()
                                .withMethod("POST")
                                .withPath("/login")
                                .withBody("{username: 'foo', password: 'bar'}")
                )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withCookie(
                                        "sessionId", "2By8LOhBmaW5nZXJwcmludCIlMDAzMW"
                                )
                                .withHeader(
                                        "Location", "https://www.mock-server.com"
                                )
                                .withBody("{result: 'ok'}")
                );



        serverClient.when(
                request()
                        .withMethod("POST")
                        .withPath("/login")
                        .withBody("{username: 'foo', password: 'invalid'}")
        ).callback(
                HttpClassCallback.callback()
                        .withCallbackClass("mockserver.ResponseCallback")
        );

        System.out.println(Request.Post("http://localhost:1080/login")
                .bodyString("{username: 'foo', password: 'bar'}", ContentType.APPLICATION_JSON)
                .execute().returnContent());

    }

}
