package ru.yan0kom.clinic.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControllerTestBase {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper json;
    @Value("${jwt.client-id}")
    protected String clientId;
    @Value("${jwt.client-secret}")
    protected String clientSecret;

    protected  <T> T fromMvcResult(MvcResult mvcResult, Class<T> valueType) throws Exception {
        return json.readValue(mvcResult.getResponse().getContentAsString(), valueType);
    }

    protected <T> T fromMvcResult(MvcResult mvcResult, TypeReference<T> typeRef) throws Exception {
        return json.readValue(mvcResult.getResponse().getContentAsString(), typeRef);
    }

    protected <T> T fromRequest(MockHttpServletRequestBuilder requestBuilder, Class<T> valueType) throws Exception {
        return fromMvcResult(mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn(), valueType);
    }

    protected <T> T fromRequest(MockHttpServletRequestBuilder requestBuilder, String token, Class<T> valueType) throws Exception {
        return fromMvcResult(mvcPerform(requestBuilder, token).andExpect(status().isOk()).andReturn(), valueType);
    }

    protected <T> T fromRequest(MockHttpServletRequestBuilder requestBuilder, TypeReference<T> typeRef) throws Exception {
        return fromMvcResult(mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn(), typeRef);
    }

    protected MockHttpServletRequestBuilder setBody(MockHttpServletRequestBuilder requestBuilder, Object body)
            throws JsonProcessingException {
        return requestBuilder.contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(json.writeValueAsString(body))
                .accept("application/json;charset=UTF-8");
    }

    protected MockHttpServletRequestBuilder addToken(MockHttpServletRequestBuilder requestBuilder, String token) {
        return requestBuilder.header("Authorization", String.format("Bearer %s", token));
    }

    protected ResultActions mvcPerform(MockHttpServletRequestBuilder requestBuilder, String token) throws Exception {
        return mvc.perform(addToken(requestBuilder, token));
    }
}
