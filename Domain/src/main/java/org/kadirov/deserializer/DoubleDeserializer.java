package org.kadirov.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import java.io.IOException;

public class DoubleDeserializer extends JsonDeserializer<Double> {

    public DoubleDeserializer() {
    }

    public Double deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return Double.parseDouble(jsonParser.getText());
    }
}
