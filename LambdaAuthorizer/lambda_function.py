import base64
import boto3
import json

dynamodb = boto3.client('dynamodb')
dynamodb_table_name = 'TwitterUserCreds'

def lambda_handler(event, context):
    authorization_header = event.get('headers', {}).get('Authorization', '')

    if not authorization_header.startswith('Basic '):
        return generate_policy('anonymous', 'Deny', event['methodArn'])
    credentials = base64.b64decode(authorization_header[6:]).decode("utf-8")
    username, password = credentials.split(":")
    response = dynamodb.get_item(
        TableName=dynamodb_table_name,
        Key={'Username': {'S': username}}
    )

    if 'Item' in response:
        stored_password = response['Item'].get('Password', {}).get('S', '')
        if stored_password == password:
            policy = generate_policy(username, 'Allow', event['methodArn'])
            return policy
    policy = generate_policy('anonymous', 'Deny', event['methodArn'])
    return policy

def generate_policy(principalId, effect, resource):
    auth_response = {
        'principalId': principalId,
        'policyDocument': {
            'Version': '2012-10-17',
            'Statement': [
                {
                    'Action': 'execute-api:Invoke',
                    'Effect': effect,
                    'Resource': resource,
                }
            ]
        }
    }
    return auth_response
