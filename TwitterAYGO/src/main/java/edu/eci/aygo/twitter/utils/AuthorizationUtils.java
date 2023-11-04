package edu.eci.aygo.twitter.utils;

import java.util.Base64;

public class AuthorizationUtils {

    public static String GetUserFromBasicToken(String token) throws Exception {
        String[] parts = token.split(" ");
        if (parts.length == 2 && parts[0].equalsIgnoreCase("Basic")) {
            // Decode the Basic Auth value
            String base64Credentials = parts[1];
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));

            // Split the credentials and get the first part (element at position 1)
            String[] authParts = credentials.split(":");
            return authParts[0];
        }
        throw new Exception("Invalid Token");
    }
}
