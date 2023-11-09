package notice.controller;

import jakarta.transaction.Transactional;
import notice.model.dto.CreateNoticeDTO;
import notice.model.dto.NoticeAttachDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NoticeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    final static String testToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJObSI6ImFkbWluIn0.ELfEy-gNZsxrwKCq5VAN2dQRFHCtOjiksszDMfU5ai4";
    JSONObject requestBody;

    // MockMvc를 사용하여 요청을 보내는 메서드
    public ResultActions sendMockRequest(String uri, String method, String token, String requestBody) throws Exception {
        return mockMvc.perform(
            MockMvcRequestBuilders.request(HttpMethod.valueOf(method), uri)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );
    }

    @BeforeEach
    void beforeEach() throws JSONException {
        requestBody = new JSONObject();
        requestBody.put("title", "test_notice1");
        requestBody.put("content", "test_notice 입니다.");
        requestBody.put("startDt", LocalDateTime.now());
        requestBody.put("endDt", LocalDateTime.now().plusDays(1));
        JSONArray noticeUriList = new JSONArray();
        JSONObject noticeAttach = new JSONObject();
        noticeAttach.put("attachUri", "test_uri1");
        noticeUriList.put(noticeAttach);
        requestBody.put("noticeUriList", noticeUriList);
    }

    @Test
    public void createNoticeTest() throws Exception {
        String requestBodyStr = requestBody.toString();
        String uri = "/notice";

        ResultActions resultActions = sendMockRequest(uri, "POST", testToken, requestBodyStr);
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void updateNoticeTest() throws Exception {
        String requestBodyStr = requestBody.toString();
        String uri = "/notice";
        ResultActions resultActions = sendMockRequest(uri, "POST", testToken, requestBodyStr);
        resultActions.andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        // content를 원하는 방식으로 처리
        System.out.println("Response Content: " + new String(content.getBytes("UTF-8"), "UTF-8"));
    }
}
