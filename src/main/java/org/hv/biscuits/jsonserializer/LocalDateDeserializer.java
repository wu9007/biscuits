package org.hv.biscuits.jsonserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 时区矫正
 *
 * @author wujianchuan
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    private static final String ZULU = "Z";

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();
        if (text.endsWith(ZULU)) {
            return LocalDateTime.ofInstant(Instant.parse(text), ZoneId.systemDefault()).toLocalDate();
        } else {
            return LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE).toLocalDate();
        }
    }
}
