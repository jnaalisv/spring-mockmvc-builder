package org.jnaalisv.test.springframework;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MockMvcResponseBuilder {

    private ObjectMapper objectMapper;
    private ResultActions resultActions;

    public MockMvcResponseBuilder(ObjectMapper objectMapper, ResultActions resultActions) {
        this.objectMapper = objectMapper;
        this.resultActions = resultActions;
    }

    public MockMvcResponseBuilder expect200() {
        return expectStatus(HttpStatus.OK);
    }

    public MockMvcResponseBuilder expect201() {
        return expectStatus(HttpStatus.CREATED);
    }

    public MockMvcResponseBuilder expect400() {
        return expectStatus(HttpStatus.BAD_REQUEST);
    }

    public MockMvcResponseBuilder expect401() {
        return expectStatus(HttpStatus.UNAUTHORIZED);
    }

    public MockMvcResponseBuilder expect403() {
        return expectStatus(HttpStatus.FORBIDDEN);
    }

    public MockMvcResponseBuilder expect404() {
        return expectStatus(HttpStatus.NOT_FOUND);
    }

    public MockMvcResponseBuilder expect409() {
        return expectStatus(HttpStatus.CONFLICT);
    }

    private MockMvcResponseBuilder expectStatus(HttpStatus httpStatus) {
        try {
            resultActions.andExpect(status().is(httpStatus.value()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public MockMvcResponseBuilder expectHeader(String name, String value) {
        try {
            resultActions.andExpect(header().string(name, new StringMatcher(value)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public <T> T returnResponseBody() {
        try {
            return objectMapper.readValue(responseBody(), new TypeReference<T>() { });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T responseBodyAs(Class<T> responseType) {
        try {
            return objectMapper.readValue(responseBody(), responseType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> responseBodyAsListOf(Class<T> listItemType) {
        JavaType responseType = objectMapper.getTypeFactory().constructCollectionType(List.class, listItemType);
        try {
            return objectMapper.readValue(responseBody(), responseType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String responseBody() {
        try {
            return resultActions.andReturn().getResponse().getContentAsString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
