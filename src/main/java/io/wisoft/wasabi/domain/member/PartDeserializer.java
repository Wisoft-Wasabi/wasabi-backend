package io.wisoft.wasabi.domain.member;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;

public class PartDeserializer extends JsonDeserializer<Part> {

    @Override
    public Part deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        final String value = ((TextNode) parser.getCodec().readTree(parser)).asText();
        return Part.fromString(value);
    }
}
