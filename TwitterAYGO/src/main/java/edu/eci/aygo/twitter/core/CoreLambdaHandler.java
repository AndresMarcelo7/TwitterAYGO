package edu.eci.aygo.twitter.core;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.aygo.twitter.core.domain.Tweet;
import edu.eci.aygo.twitter.exceptions.ResourceNotFoundException;
import edu.eci.aygo.twitter.exceptions.UnauthorizedException;
import edu.eci.aygo.twitter.core.services.CoreTwitterServices;
import edu.eci.aygo.twitter.core.services.impl.CoreTwitterServicesImpl;
import edu.eci.aygo.twitter.utils.ApiGateway;
import edu.eci.aygo.twitter.utils.ApiRouter;
import edu.eci.aygo.twitter.utils.AuthorizationUtils;
import edu.eci.aygo.twitter.utils.ObjectMapperSingleton;

public class CoreLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private CoreTwitterServices coreTwitterServices = new CoreTwitterServicesImpl();
    private ObjectMapper mapper = ObjectMapperSingleton.getMapper();
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        ApiGateway router= new ApiGateway(this);
        try {
            return (APIGatewayProxyResponseEvent) router.handleRequest(event,event.getResource(), event.getHttpMethod());
        } catch (Exception e) {
            System.out.println("No se que paso");
            System.out.println(e.getMessage() + "\n");
        }
        return new APIGatewayProxyResponseEvent().withBody("Ni idea de que paso").withStatusCode(400);
    }

    @ApiRouter(Route = "/tweets")
    public APIGatewayProxyResponseEvent GetTweets(APIGatewayProxyRequestEvent evento){
        try {
            return new APIGatewayProxyResponseEvent().withBody(mapper.writeValueAsString(coreTwitterServices.GetAllTweets())).withStatusCode(200);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return new APIGatewayProxyResponseEvent().withBody("Error Deserializing Tweet").withStatusCode(400);
        }
    }
    @ApiRouter(Route = "/tweets/{id}")
    public APIGatewayProxyResponseEvent GetTweetById(APIGatewayProxyRequestEvent evento){
        try{
            Tweet t = coreTwitterServices.GetTweetById(Long.valueOf(evento.getPathParameters().get("id")));
            return new APIGatewayProxyResponseEvent().withBody(mapper.writeValueAsString(t)).withStatusCode(200);
        }
        catch (ResourceNotFoundException e){
            return new APIGatewayProxyResponseEvent().withBody("Tweet not found").withStatusCode(404);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new APIGatewayProxyResponseEvent().withBody("Error Retrieving Tweet").withStatusCode(400);
        }
    }
    @ApiRouter(Route = "/tweets", Method = "POST")
    public APIGatewayProxyResponseEvent PostTweet(APIGatewayProxyRequestEvent evento){
        System.out.println(evento.getBody());
        try {
            Tweet t = mapper.readValue(evento.getBody(), Tweet.class);
            t.AuthorId = AuthorizationUtils.GetUserFromBasicToken(evento.getHeaders().get("Authorization"));
            Tweet posted = coreTwitterServices.PostTweet(t);
            return new APIGatewayProxyResponseEvent().withBody(mapper.writeValueAsString(posted)).withStatusCode(200);
        } catch (JsonProcessingException e) {
            return new APIGatewayProxyResponseEvent().withBody("Error Deserializing Tweet").withStatusCode(400);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new APIGatewayProxyResponseEvent().withBody("Something Happened ").withStatusCode(500);
        }

    }

    @ApiRouter(Route = "/tweets/{id}", Method = "DELETE")
    public APIGatewayProxyResponseEvent DeleteTweet(APIGatewayProxyRequestEvent evento){
        try{
            String user= AuthorizationUtils.GetUserFromBasicToken(evento.getHeaders().get("Authorization"));
            coreTwitterServices.DeleteTweet(Long.valueOf(evento.getPathParameters().get("id")), user);
            return new APIGatewayProxyResponseEvent().withBody("Tweet deleted").withStatusCode(200);
        }
        catch (ResourceNotFoundException e){
            return new APIGatewayProxyResponseEvent().withBody("Tweet not found").withStatusCode(404);
        }
        catch (UnauthorizedException e){
            return new APIGatewayProxyResponseEvent().withBody("You can't delete this tweet").withStatusCode(401);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new APIGatewayProxyResponseEvent().withBody("Something happened").withStatusCode(400);
        }

    }
    @ApiRouter(Route = "/tweets/{id}", Method = "PUT")
    public APIGatewayProxyResponseEvent UpdateTweet(APIGatewayProxyRequestEvent evento){
        try {
            Tweet t = mapper.readValue(evento.getBody(), Tweet.class);
            t.TweetId = Long.valueOf(evento.getPathParameters().get("id"));
            String user= AuthorizationUtils.GetUserFromBasicToken(evento.getHeaders().get("Authorization"));
            coreTwitterServices.UpdateTweet(t, user);
            return new APIGatewayProxyResponseEvent().withBody("Tweet updated").withStatusCode(200);
        } catch (JsonProcessingException e) {
            return new APIGatewayProxyResponseEvent().withBody("Error Deserializing Tweet").withStatusCode(400);
        }
        catch (UnauthorizedException e){
            return new APIGatewayProxyResponseEvent().withBody("You can't update this tweet").withStatusCode(401);
        }
        catch (ResourceNotFoundException e){
            return new APIGatewayProxyResponseEvent().withBody("Tweet not found").withStatusCode(404);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return new APIGatewayProxyResponseEvent().withBody("Something Happened ").withStatusCode(500);
        }
    }
}