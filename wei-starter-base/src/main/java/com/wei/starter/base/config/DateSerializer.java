package com.wei.starter.base.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.Date;

public class DateSerializer extends StdSerializer<Date> {
    private DateTimeFormatter dateTimeFormatter;

    public DateSerializer(DateTimeFormatter dateTimeFormatter) {
        super(Date.class);
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(new DateTime(value).toString(dateTimeFormatter));
    }
}
