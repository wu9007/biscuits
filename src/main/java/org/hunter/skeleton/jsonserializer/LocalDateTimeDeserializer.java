package org.hunter.skeleton.jsonserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author wujianchuan
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private static final String ZULU = "Z";

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();
        if (text.endsWith(ZULU)) {
            return LocalDateTime.ofInstant(Instant.parse(text), ZoneId.systemDefault());
        } else {
            return LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }
}
