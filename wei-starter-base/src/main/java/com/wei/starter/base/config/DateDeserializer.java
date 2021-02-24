package com.wei.starter.base.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.Date;

public class DateDeserializer extends StdDeserializer<Date> {
    private DateTimeFormatter dateTimeFormatter;

    public DateDeserializer(DateTimeFormatter dateTimeFormatter) {
        super(Date.class);
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.hasToken(JsonToken.VALUE_STRING)) {
            String str = p.getText().trim();
            if (str.length() == 0) {
                return (Date) getEmptyValue(ctxt);
            }
            try {
                return dateTimeFormatter.parseDateTime(str).toDate();
            } catch (Exception e) {
                return (Date) ctxt.handleWeirdStringValue(handledType(), str,
                        "expected format \"%s\"", dateTimeFormatter.toString());
            }
        }
        return null;
    }
}
