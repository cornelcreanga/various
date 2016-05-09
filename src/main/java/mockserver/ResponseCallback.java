package mockserver;

import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpResponse.notFoundResponse;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.ACCEPTED_202;

public class ResponseCallback implements ExpectationCallback {

    private static HttpResponse httpResponse = response()
            .withStatusCode(ACCEPTED_202.code())
            .withHeaders(
                    header("x-callback", "test_callback_header"),
                    header("Content-Length", "{result: 'ok'}".getBytes().length),
                    header("Connection", "keep-alive")
            )
            .withBody("{result: 'ok'}");

    public HttpResponse handle(HttpRequest httpRequest) {
        String body = httpRequest.getBodyAsString();
        if (!body.contains("invalid")){
            return httpResponse;
        } else {
            return notFoundResponse();
        }
    }
}
