package notice.service;

import common.utils.CommonUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import notice.model.dto.CreateNoticeRequest;
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

    public Map<String, Object> createNotice(HttpServletRequest request, CreateNoticeRequest createNoticeRequest) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        // 사용자 클레임을 가져오고 사용자 코드를 추출
        Claims claim = commonUtils.getClaims(request);
        String userCd = claim.getSubject();
    }
}