package notice.controller;

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

import java.nio.charset.StandardCharsets;

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
        requestBody.put("startDt", "2023-12-01 00:00:00");
        requestBody.put("endDt", "2023-12-02 00:00:00");
        JSONArray noticeUriList = new JSONArray();
        JSONObject noticeAttach = new JSONObject();
        noticeAttach.put("attachUri", "test_uri1");
        noticeUriList.put(noticeAttach);
        requestBody.put("noticeUriList", noticeUriList);
    }

    @Test
    public void readNoticeTest() throws Exception {
        // 공지사항 작성
        String requestBodyStr = requestBody.toString();
        String uri = "/notice";
        ResultActions resultActions = sendMockRequest(uri, "POST", testToken, requestBodyStr);

        MvcResult mvcResult = resultActions.andReturn();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        // 방금 작성한 공지사항의 noticeCd를 추출
        JSONObject jsonObject = new JSONObject(content);
        String noticeCd = jsonObject.getJSONObject("result").getString("noticeCd");

        // 응답결과가 200으로 반환되는지 확인
        mockMvc.perform(
            MockMvcRequestBuilders
                .get(uri)
                .param("noticeCd", noticeCd)
        )
        .andExpect(status().isOk());
    }

    @Test
    public void createNoticeTest() throws Exception {
        // 공지사항 작성
        String requestBodyStr = requestBody.toString();
        String uri = "/notice";
        ResultActions resultActions = sendMockRequest(uri, "POST", testToken, requestBodyStr);

        // 응답결과가 200으로 반환되는지 확인
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void updateNoticeTest() throws Exception {
        // 공지사항 작성
        String requestBodyStr = requestBody.toString();
        String uri = "/notice";
        ResultActions resultActions = sendMockRequest(uri, "POST", testToken, requestBodyStr);

        MvcResult mvcResult = resultActions.andReturn();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        // 방금 작성한 공지사항의 noticeCd를 추출
        JSONObject jsonObject = new JSONObject(content);
        String noticeCd = jsonObject.getJSONObject("result").getString("noticeCd");

        // 해당 noticeCd를 통해 공지사항을 수정
        requestBody.put("noticeCd", noticeCd);
        requestBody.put("title", "test_notice2");
        resultActions = sendMockRequest(uri, "PUT", testToken, requestBody.toString());

        // 응답결과가 200으로 반환되는지 확인
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void deleteNoticeTest() throws Exception {
        // 공지사항 작성
        String requestBodyStr = requestBody.toString();
        String uri = "/notice";
        ResultActions resultActions = sendMockRequest(uri, "POST", testToken, requestBodyStr);

        MvcResult mvcResult = resultActions.andReturn();
        String content = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        // 방금 작성한 공지사항의 noticeCd를 추출
        JSONObject jsonObject = new JSONObject(content);
        String noticeCd = jsonObject.getJSONObject("result").getString("noticeCd");

        // 응답결과가 200으로 반환되는지 확인
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete(uri)
                .header("Authorization", testToken)
                .param("noticeCd", noticeCd)
        )
        .andExpect(status().isOk());

        // 이후 다시 조회하였을 때, 응답결과가 404로 반환되는지 확인
        mockMvc.perform(
            MockMvcRequestBuilders
                .get(uri)
                .param("noticeCd", noticeCd)
        )
        .andExpect(status().isNotFound());
    }
}
