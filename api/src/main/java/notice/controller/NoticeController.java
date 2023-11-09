package notice.controller;

import common.model.Response;
import common.utils.CommonUtils;
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
@Tag(name = "NoticeController", description = "공지사항 서비스 제공")
@RestController
@RequiredArgsConstructor
public class NoticeController {
    @Autowired
    CommonUtils commonUtils;
    @Autowired
    NoticeService noticeService;

    @GetMapping(value = "/notice-all")
    @Operation(summary="모든 공지사항 조회 엔드포인트", description="모든 공지사항 조회 API")
    public ResponseEntity<Response> getNoticeAll() throws Exception {
        return commonUtils.okResponsePackaging(noticeService.getNoticeAll());
    }
    @GetMapping(value = "/notice")
    @Operation(summary="공지사항 조회 엔드포인트", description="공지사항 조회 API")
    public ResponseEntity<Response> getNotice(HttpServletRequest request, @RequestParam("noticeCd") String noticeCd) throws Exception {
        return commonUtils.okResponsePackaging(noticeService.getNotice(request, noticeCd));
    }
    @PostMapping(value = "/notice")
    @Operation(summary="공지사항 작성 엔드포인트", description="공지사항 작성 API")
    public ResponseEntity<Response> createNotice(HttpServletRequest request, @RequestBody CreateNoticeRequest createNoticeRequest) throws Exception {
        return commonUtils.okResponsePackaging(noticeService.createNotice(request, createNoticeRequest));
    }
}
