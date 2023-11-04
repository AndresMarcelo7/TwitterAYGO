# TwitterAYGO

## Objetivo

Diseñar e implementar una arquitectura orientada a microservicios que simule el funcionamiento de Twitter usando recursos cloud serverless como API Gateway, Lambda y Dynamo.

## Diseño de la solución

La solución se basa en una arquitectura orientada a microservicios, donde cada uno de ellos se encarga de una funcionalidad específica. En este caso, se han desarrollado 2 microservicios:

- **[TwitterCoreService](TwitterAYGO%2Fsrc%2Fmain%2Fjava%2Fedu%2Feci%2Faygo%2Ftwitter%2Fcore)**: encargado de gestionar los tweets.
- **[TwitterUserService](TwitterAYGO%2Fsrc%2Fmain%2Fjava%2Fedu%2Feci%2Faygo%2Ftwitter%2Fusers)**: encargado de gestionar los usuarios.

Estos microservicios tienen en comun el uso de una logica encargada de crear un cliente dynamo [clients](TwitterAYGO%2Fsrc%2Fmain%2Fjava%2Fedu%2Feci%2Faygo%2Ftwitter%2Finfrastructure%2Fclients) y tambien el uso de [utilidades](TwitterAYGO%2Fsrc%2Fmain%2Fjava%2Fedu%2Feci%2Faygo%2Ftwitter%2Futils) que permiten el uso de anotaciones para facilitar el tratamiento de las peticiones ([ApiRouter.java](TwitterAYGO%2Fsrc%2Fmain%2Fjava%2Fedu%2Feci%2Faygo%2Ftwitter%2Futils%2FApiRouter.java) y [ApiGateway.java](TwitterAYGO%2Fsrc%2Fmain%2Fjava%2Fedu%2Feci%2Faygo%2Ftwitter%2Futils%2FApiGateway.java))
tambien ofrecen utilidades para el tratamiento de objetos json [ObjectMapperSingleton.java](TwitterAYGO%2Fsrc%2Fmain%2Fjava%2Fedu%2Feci%2Faygo%2Ftwitter%2Futils%2FObjectMapperSingleton.java) y herramientas de autenticacion [AuthorizationUtils.java](TwitterAYGO%2Fsrc%2Fmain%2Fjava%2Fedu%2Feci%2Faygo%2Ftwitter%2Futils%2FAuthorizationUtils.java)

El diseño de la solución (Clases definidas y su interacción) se puede ver en la siguiente imagen:

![TwitterArchitecture-Classes.png](img%2FTwitterArchitecture-Classes.png)

En la grafica anterior podemos evidenciar la identificación y definicion de los objetos que pertenecen al dominio del problema de cada uno de los servicios.


**Como herramienta adicional**, se ha desarrollado un **microservicio** para gestionar la autenticación de los usuarios que va directamente enlazado a la funcionalidad de Lambda Authorizer de API Gateway.
- [TwitterLambdaAuthorizer](LambdaAuthorizer)

## Diseño de metodos API y Recursos

Para abarcar una funcionalidad basica de twitter se definieron los siguientes recursos y metodos:
![TwitterArchitecture-API Methods.png](img%2FTwitterArchitecture-API%20Methods.png)

Cada metodo cumple con su objetivo principal y se encarga de gestionar los recursos (Tweets/Usuarios) de la siguiente manera:

- **GET:** Obtiene la informacion de un recurso especifico
- **POST:** Crea un nuevo recurso
- **PUT:** Actualiza un recurso existente
- **DELETE:** Elimina un recurso existente

## Arquitectura de la solución

La arquitectura de la solución se basa en el uso de servicios cloud serverless de AWS, como lo son API Gateway, Lambda y DynamoDB. La arquitectura se puede ver en la siguiente imagen:

![TwitterArchitecture-Architecture.png](img%2FTwitterArchitecture-Architecture.png)

Aqui podemos observar el uso de API Gateway como puerta de enlace para el acceso a los servicios, el uso de una lambda en Python para la gestion de la autenticacion de los usuarios y el uso de lambdas en Java para la gestion de los recursos (Tweets/Usuarios). Tambien podemos observar que los datos capturados y almacenados se encuentran en tablas de DynamoDB (Users, Tweets y UserCreds).
Esta arquitectura es muy util para el desarrollo de aplicaciones que requieran de una escalabilidad rapida y que no requieran de un alto costo de mantenimiento.

## DEMO (VIDEOS)

La demostracion de creación de Usuarios y Tweets usando POSTMAN se puede ver en el siguiente video: [TwitterAYGO API Demo](https://youtu.be/dN4GF-Z2pzo)

Tambien, la evidencia de la infrastructura desplegada y su uso se puede ver este video: [TwitterAYGO API AWS_Infra](https://youtu.be/jAp-ngXNk00)



