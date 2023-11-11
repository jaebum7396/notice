package notice.controller;

import common.model.Response;
import common.utils.CommonUtils;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notice.model.dto.CreateNoticeDTO;
import notice.model.dto.ReadNoticeResponseDTO;
import notice.model.dto.UpdateNoticeDTO;
import notice.model.entity.Notice;
import notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Tag(name = "NoticeController", description = "공지사항 서비스 제공")
@RestController
@RequiredArgsConstructor
public class NoticeController {
    @Autowired
    CommonUtils commonUtils;
    @Autowired
    NoticeService noticeService;
    @GetMapping(value = "/notice")
    @Operation(summary="공지사항 조회 엔드포인트", description="공지사항 조회 API")
    public ResponseEntity<Response> readNotice(HttpServletRequest request, @RequestParam("noticeCd") String noticeCd) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        // 공지사항 서비스를 통해 공지사항 정보 조회
        Notice notice = noticeService.readNotice(noticeCd);

        // 조회된 공지사항 정보를 DTO로 변환
        ReadNoticeResponseDTO noticeResponseDTO = ReadNoticeResponseDTO.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .insertDt(notice.getInsertDt())
                .views(notice.getViews())
                .writer(notice.getWriter())
                .build();
        resultMap.put("notice", noticeResponseDTO);

        // 공통 유틸리티를 사용하여 응답 패키징 후 반환
        return commonUtils.okResponsePackaging(resultMap);
    }
    @PostMapping(value = "/notice")
    @Operation(summary="공지사항 작성 엔드포인트", description="공지사항 작성 API")
    public ResponseEntity<Response> createNotice(HttpServletRequest request, @RequestBody CreateNoticeDTO noticeDTO) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        // 사용자 클레임을 가져오고 사용자 코드를 추출하여 DTO에 설정
        Claims claim = commonUtils.getClaims(request);
        noticeDTO.setWriterUserCd(claim.getSubject());
        noticeDTO.setWriter(claim.get("userNm", String.class));

        // 공지사항 작성 서비스를 통해 공지사항을 생성하고 생성된 코드를 결과 Map에 추가
        resultMap.put("noticeCd", noticeService.createNotice(noticeDTO));
        resultMap.put("message", "공지사항이 작성되었습니다.");

        // 공통 유틸리티를 사용하여 응답 패키징 후 반환
        return commonUtils.okResponsePackaging(resultMap);
    }
    @PutMapping(value = "/notice")
    @Operation(summary="공지사항 수정 엔드포인트", description="공지사항 수정 API")
    public ResponseEntity<Response> updateNotice(HttpServletRequest request, @RequestBody UpdateNoticeDTO noticeDTO) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        // 사용자 클레임을 가져오고 사용자 코드를 추출하여 DTO에 설정
        Claims claim = commonUtils.getClaims(request);
        String userCd = claim.getSubject();
        noticeDTO.setUpdateUserCd(userCd);

        // 공지사항 수정 서비스를 호출하여 결과를 받아옴
        Map<String, Object> serviceLayerResult = noticeService.updateNotice(noticeDTO);

        if (serviceLayerResult.get("isUpdated").equals(true)) {
            resultMap.put("message", "공지사항이 수정되었습니다.");
            resultMap.put("noticeCd", serviceLayerResult.get("noticeCd"));
        } else {
            resultMap.put("message", "변경된 내용이 없습니다.");
        }
        // 공통 유틸리티를 사용하여 응답 패키징 후 반환
        return commonUtils.okResponsePackaging(resultMap);
    }

    @DeleteMapping(value = "/notice")
    @Operation(summary="공지사항 삭제 엔드포인트", description="공지사항 삭제 API")
    public ResponseEntity<Response> deleteNotice(HttpServletRequest request, @RequestParam("noticeCd") String noticeCd) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        // 사용자 클레임을 가져오고 사용자 코드 추출
        Claims claim = commonUtils.getClaims(request);
        String userCd = claim.getSubject();

        // 공지사항 삭제 서비스 호출
        noticeService.deleteNotice(userCd, noticeCd);
        resultMap.put("message", "공지사항이 삭제되었습니다.");

        // 공통 유틸리티를 사용하여 응답 패키징 후 반환
        return commonUtils.okResponsePackaging(resultMap);
    }

}