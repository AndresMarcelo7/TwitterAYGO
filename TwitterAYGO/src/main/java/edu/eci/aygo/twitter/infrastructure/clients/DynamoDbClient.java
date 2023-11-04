package edu.eci.aygo.twitter.infrastructure.clients;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

public class DynamoDbClient {
    private static DynamoDB dynamoDBClient;

    public DynamoDbClient() { }

    public static DynamoDB getClient() {
        // Initialize the DynamoDB client
        if (dynamoDBClient == null)
        {
            AmazonDynamoDB dynamo = AmazonDynamoDBClientBuilder.standard()
                    // Provide your AWS credentials and region here
                    .withRegion("us-east-1")
                    .build();
            dynamoDBClient = new DynamoDB(dynamo);
        }

        return dynamoDBClient;
    }

}