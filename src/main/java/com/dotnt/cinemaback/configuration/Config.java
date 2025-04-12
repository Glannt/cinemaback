package com.dotnt.cinemaback.configuration;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

//@Configuration
public class Config {
//    @Bean
//    public AuditorAware<String> auditorProvider() {
//        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
//                .map(Authentication::getName)
//                .orElse("SYSTEM");
//    }

//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
//        return builder -> {
//            builder.simpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//        };
//    }
}
