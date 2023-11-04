import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import edu.eci.aygo.twitter.core.CoreLambdaHandler;

import java.util.Map;

public class CoreTweetsApiTest {

    static CoreLambdaHandler handler = new CoreLambdaHandler();

    public static void main(String[] args) {
        APIGatewayProxyResponseEvent response = handler.handleRequest(new CoreTweetsApiTest().getTweetByIdRequest(), null);
        System.out.println(response.getStatusCode() + " -> " + response.getBody());
    }


    private APIGatewayProxyRequestEvent getRequest(){
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("GET");
        request.setResource("/tweets");
        request.setHeaders(Map.of("Authorization", "Basic "));
        return request;
    }

    private APIGatewayProxyRequestEvent postRequest(){
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("POST");
        request.setResource("/tweets");
        request.setHeaders(Map.of("Authorization", "Basic "));
        request.setBody("{\"message\":\"Hola Mundo\",\"mediaUrl\": \"https://www.google.com\"}");
        return request;
    }

    private APIGatewayProxyRequestEvent getTweetByIdRequest(){
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("GET");
        request.setResource("/tweets/{id}");
        request.setPath("/tweets/6064653462080734766");
        request.setHeaders(Map.of("Authorization", "Basic "));
        request.setPathParameters(Map.of("id", "3123123"));
        return request;
    }

    private APIGatewayProxyRequestEvent updateTweetRequest(){
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("PUT");
        request.setResource("/tweets/{id}");
        request.setPath("/tweets/5754665257539487905");
        request.setPathParameters(Map.of("id", "5754665257539487905"));
        request.setHeaders(Map.of("Authorization", "Basic "));
        request.setBody("{\"tweetId\": 5754665257539487905,\"message\":\"Hola Mundo 2 siuuuu\",\"mediaUrl\": \"https://www.google2god.com\",\"authorId\": \"marquitos\"}");
        return request;
    }

    private APIGatewayProxyRequestEvent deleteTweetRequest(){
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("DELETE");
        request.setResource("/tweets/{id}");
        request.setPath("/tweets/8");
        request.setPathParameters(Map.of("id", "443945922625949031"));
        request.setHeaders(Map.of("Authorization", "Basic "));
        return request;
    }


}
