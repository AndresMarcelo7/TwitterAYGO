package edu.eci.aygo.twitter.utils;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ApiGateway {

    private Map<String, Method> routeMap = new HashMap<>();

    public ApiGateway(Object o) {
        scanHandlers(o);
    }

    private void scanHandlers(Object o) {
        Class<?> apiGatewayClass = o.getClass();
        Method[] methods = apiGatewayClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ApiRouter.class)) {
                ApiRouter annotation = method.getAnnotation(ApiRouter.class);
                String routePath = annotation.Route();
                String routeMethod = annotation.Method();
                routeMap.put(String.format("%s_%s",routeMethod,routePath), method);
            }
        }
    }

    public Object handleRequest(APIGatewayProxyRequestEvent event, String routePath, String routeMethod) throws Exception {
        Method handlerMethod = routeMap.get(String.format("%s_%s",routeMethod,routePath));
        if (handlerMethod != null) {
            return handlerMethod.invoke(handlerMethod.getDeclaringClass().newInstance(), event);
        }
        throw new RuntimeException("Route not found: " + routePath);
    }

}
