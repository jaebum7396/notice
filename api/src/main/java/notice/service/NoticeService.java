package notice.service;

import common.utils.CommonUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import notice.model.dto.CreateNoticeRequest;
import notice.model.entity.Notice;
import notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    @Autowired
    CommonUtils commonUtils;
    @Autowired
    NoticeRepository noticeRepository;

    public Map<String, Object> getNoticeAll() {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        List<Notice> notices = noticeRepository.findAll();
        resultMap.put("notices", notices);
        return resultMap;
    }

    public Map<String, Object> getNotice(HttpServletRequest request, String noticeCd) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        List<Notice> notices = noticeRepository.findByNoticeCd(noticeCd);
        resultMap.put("notices", notices);
        return resultMap;
    }

    public Map<String, Object> createNotice(HttpServletRequest request, CreateNoticeRequest createNoticeRequest) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        // 사용자 클레임을 가져오고 사용자 코드를 추출
        Claims claim = commonUtils.getClaims(request);
        String userCd = claim.getSubject();
        String userNm = claim.get("userNm", String.class);

        Notice noticeEntity = createNoticeRequest.toEntity();
        noticeEntity.setInsertUserCd(userCd);
        noticeEntity.setWriter(userNm); //작성자 이름 세팅
        noticeRepository.save(noticeEntity);

        resultMap.put("notice", noticeEntity);

        return resultMap;
    }
}