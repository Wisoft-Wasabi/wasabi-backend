package io.wisoft.wasabi.domain.member;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

public class PartDeserializer extends JsonDeserializer<Part> {
    @Override
    public Part deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        final String value = ((TextNode) p.getCodec().readTree(p)).asText();
        return Part.fromString(value);
    }
}