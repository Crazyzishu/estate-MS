package org.zishu.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.math.BigDecimal;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .serializerByType(BigDecimal.class, new JsonSerializer<BigDecimal>() {
                    @Override
                    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException, IOException {
                        gen.writeNumber(value.doubleValue());
                    }
                });
    }
}
