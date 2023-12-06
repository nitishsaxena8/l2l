package com.xperience.aem.xpbootstrap.core.component.Utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ComponentDataModelSerializer extends StdSerializer<ComponentData> {

    public ComponentDataModelSerializer() {
        this(null);
    }

    public ComponentDataModelSerializer(Class<ComponentData> t) {
        super(t);
    }

    @Override
    public void serialize(ComponentData componentData, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.setCodec(new ObjectMapper());
        jsonGenerator.writeObjectField(componentData.getId(), componentData);
        jsonGenerator.writeEndObject();
    }
}