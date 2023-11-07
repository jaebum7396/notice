package notice.controller;

import common.model.Response;
import common.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notice.model.dto.CreateNoticeRequest;
import notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "NoticeController")
@Tag(name = "NoticeController", description = "공지사항 서비스 제공")
@RestController
@RequiredArgsConstructor
public class NoticeController {
    @Autowired
    CommonUtils commonUtils;
    @Autowired
    NoticeService noticeService;
    @PostMapping(value = "/notice")
    @Operation(summary="공지사항 생성 엔드포인트", description="주문 생성 API")
    public ResponseEntity<Response> createOrder(HttpServletRequest request, @RequestBody CreateNoticeRequest createNoticeRequest) throws Exception {
        return commonUtils.okResponsePackaging(noticeService.createNotice(request, createNoticeRequest));
    }
}
