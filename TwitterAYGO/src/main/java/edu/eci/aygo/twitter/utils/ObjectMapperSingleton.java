package edu.eci.aygo.twitter.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperSingleton {
    private static ObjectMapper mapper;
    public ObjectMapperSingleton() {
    }

    public static ObjectMapper getMapper(){
        if (mapper == null){
            mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.registerModule(new JodaModule());
        }
        return mapper;
    }


}
