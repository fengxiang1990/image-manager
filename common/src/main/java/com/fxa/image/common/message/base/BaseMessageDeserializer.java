package com.fxa.image.common.message.base;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fxa.image.common.message.Content;
import com.fxa.image.common.message.TestMessage;

import java.io.IOException;

public class BaseMessageDeserializer<T> extends StdDeserializer<CustomMessage<T>> {

    protected BaseMessageDeserializer() {
        this(null);
    }

    public BaseMessageDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public CustomMessage<T> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        int dataType = node.get("dataType").asInt(); // 假设 JSON 中包含 dataType 属性
        CustomMessage baseMessage = new CustomMessage();

        switch (dataType) {
            case CustomMessage.DataType.TESTMSG:
                baseMessage.setData(jp.getCodec().treeToValue(node.get("data"), TestMessage.class));
                break;
            case CustomMessage.DataType.CONTENT:
                baseMessage.setData(jp.getCodec().treeToValue(node.get("data"), Content.class));
                break;
            case CustomMessage.DataType.STRING:
                baseMessage.setData(jp.getCodec().treeToValue(node.get("data"), String.class));
                break;
            case CustomMessage.DataType.INTEGER:
                baseMessage.setData(jp.getCodec().treeToValue(node.get("data"), Integer.class));
                break;
            case CustomMessage.DataType.LONG:
                baseMessage.setData(jp.getCodec().treeToValue(node.get("data"), Long.class));
                break;
            case CustomMessage.DataType.DOUBLE:
                baseMessage.setData(jp.getCodec().treeToValue(node.get("data"), Double.class));
                break;
            // 其他类型
            default:
                baseMessage = new CustomMessage<>();
        }

        baseMessage.setCode(node.get("code").asInt());
        baseMessage.setMsg(node.get("msg").asText());
        baseMessage.setDataType(dataType);
        return baseMessage;
    }
}