package edu.eci.aygo.twitter.users;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.aygo.twitter.core.domain.Tweet;
import edu.eci.aygo.twitter.exceptions.ResourceNotFoundException;
import edu.eci.aygo.twitter.exceptions.UnauthorizedException;
import edu.eci.aygo.twitter.users.domain.User;
import edu.eci.aygo.twitter.users.domain.UserCreds;
import edu.eci.aygo.twitter.users.services.UserTwitterServices;
import edu.eci.aygo.twitter.users.services.impl.UserTwitterServicesImpl;
import edu.eci.aygo.twitter.utils.ApiGateway;
import edu.eci.aygo.twitter.utils.ApiRouter;
import edu.eci.aygo.twitter.utils.AuthorizationUtils;
import edu.eci.aygo.twitter.utils.ObjectMapperSingleton;

public class UsersLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    UserTwitterServices userTwitterServices = new UserTwitterServicesImpl();
    private ObjectMapper mapper = ObjectMapperSingleton.getMapper();
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        ApiGateway router= new ApiGateway(this);
        try {
            return (APIGatewayProxyResponseEvent) router.handleRequest(event,event.getResource(), event.getHttpMethod());
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
            return new APIGatewayProxyResponseEvent().withBody("Error Handling Request ").withStatusCode(500);
        }
    }

    @ApiRouter(Route = "/users")
    public APIGatewayProxyResponseEvent GetUsers(APIGatewayProxyRequestEvent evento) throws Exception {
        try {
            return new APIGatewayProxyResponseEvent().withBody(mapper.writeValueAsString(userTwitterServices.getUsers())).withStatusCode(200);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new APIGatewayProxyResponseEvent().withBody("Error Getting User").withStatusCode(500);
        }
    }
    @ApiRouter(Route = "/users/{username}")
    public APIGatewayProxyResponseEvent GetUserById(APIGatewayProxyRequestEvent evento){
        try{
            System.out.println(evento.getBody());
            User u = userTwitterServices.getUser(evento.getPathParameters().get("username"));
            return new APIGatewayProxyResponseEvent().withBody(mapper.writeValueAsString(u)).withStatusCode(200);
        }
        catch (ResourceNotFoundException e){
            return new APIGatewayProxyResponseEvent().withBody("User not found").withStatusCode(404);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new APIGatewayProxyResponseEvent().withBody("Error Retrieving User").withStatusCode(400);
        }
    }

    @ApiRouter(Route = "/users", Method = "POST")
    public APIGatewayProxyResponseEvent CreateUser(APIGatewayProxyRequestEvent evento){
        try{
            System.out.println(evento.getBody());
            User u = userTwitterServices.createUser(mapper.readValue(evento.getBody(), UserCreds.class));
            return new APIGatewayProxyResponseEvent().withBody(mapper.writeValueAsString(u)).withStatusCode(200);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new APIGatewayProxyResponseEvent().withBody("Error Creating User").withStatusCode(500);
        }
    }

    @ApiRouter(Route = "/users/{username}", Method = "DELETE")
    public APIGatewayProxyResponseEvent DeleteUser(APIGatewayProxyRequestEvent evento){
        try{
            System.out.println(evento.getBody());
            String user = AuthorizationUtils.GetUserFromBasicToken(evento.getHeaders().get("Authorization"));
            String status = userTwitterServices.deleteUser(evento.getPathParameters().get("username"), user);
            return new APIGatewayProxyResponseEvent().withBody(mapper.writeValueAsString(status)).withStatusCode(200);
        }
        catch (ResourceNotFoundException e){
            return new APIGatewayProxyResponseEvent().withBody("User not found").withStatusCode(404);
        }
        catch (UnauthorizedException e){
            return new APIGatewayProxyResponseEvent().withBody("You can't delete this user").withStatusCode(401);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new APIGatewayProxyResponseEvent().withBody("Error Deleting User").withStatusCode(500);
        }
    }
}
