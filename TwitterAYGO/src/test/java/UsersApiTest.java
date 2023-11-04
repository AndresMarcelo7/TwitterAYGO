import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import edu.eci.aygo.twitter.core.CoreLambdaHandler;
import edu.eci.aygo.twitter.users.UsersLambdaHandler;
import edu.eci.aygo.twitter.users.domain.User;

import java.util.Map;

public class UsersApiTest {
    static UsersLambdaHandler handler = new UsersLambdaHandler();

    public static void main(String[] args) {
        APIGatewayProxyResponseEvent response = handler.handleRequest(new UsersApiTest().deleteUser(), null);
        System.out.println(response.getStatusCode() + " -> " + response.getBody());
    }

    public  APIGatewayProxyRequestEvent getRequest(){
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("GET");
        request.setResource("/users");
        request.setHeaders(Map.of("Authorization", "Basic YWRtaW46YWRtaW4="));
        return request;
    }

    public APIGatewayProxyRequestEvent postRequest() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("POST");
        request.setResource("/users");
        request.setBody("{\"userName\": \"anfemaru\",\"email\": \"hola@mundo.com\",\"password\": \"123456\"}");
        return request;
    }

    public APIGatewayProxyRequestEvent getUserByIdRequest() {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("GET");
        request.setResource("/users/{username}");
        request.setPath("/users/anfemaru");
        request.setHeaders(Map.of("Authorization", "Basic YWRtaW46YWRtaW4="));
        request.setPathParameters(Map.of("username", "anfemara"));
        return request;
    }

    public APIGatewayProxyRequestEvent deleteUser(){
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setHttpMethod("DELETE");
        request.setResource("/users/{username}");
        request.setPath("/users/anfemaru");
        request.setPathParameters(Map.of("username", "lolazos"));
        request.setHeaders(Map.of("Authorization", "Basic YW5mZW1hcnU6MTIzNDU2"));
        return request;
    }


}
