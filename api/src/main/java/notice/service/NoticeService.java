package notice.service;

import common.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import notice.common.Constants.Constants;
import notice.common.exception.NotFoundException;
import notice.model.dto.NoticeAttachDTO;
import notice.model.dto.NoticeDTO;
import notice.model.dto.UpdateNoticeDTO;
import notice.model.entity.Notice;
import notice.model.entity.NoticeAttach;
import notice.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {
    @Autowired
    CommonUtils commonUtils;
    @Autowired
    NoticeRepository noticeRepository;

    public void deleteAll() {
        noticeRepository.deleteAll();
    }

    public Notice readNotice(String noticeCd) throws Exception {
        Notice notice =  getNotice(noticeCd);
        // 조회수 증가
        notice.setViews(notice.getViews() + 1);
        noticeRepository.save(notice);

        return notice;
    }

    public Map<String, Object> getNoticeAll() {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        List<Notice> notices = noticeRepository.findAll();
        resultMap.put("notices", notices);
        return resultMap;
    }

    public Notice getNotice(String noticeCd) throws Exception {
        return noticeRepository.findByNoticeCd(noticeCd)
                .orElseThrow(() -> new NotFoundException(Constants.NOTICE_NOT_FOUND));
    }

    public String createNotice(NoticeDTO noticeDTO) throws Exception {

        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Notice noticeEntity = noticeDTO.toEntity();
        noticeRepository.save(noticeEntity);

        return noticeEntity.getNoticeCd();
    }

    public Map<String, Object> updateNotice(UpdateNoticeDTO noticeDTO) throws Exception {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

        Notice notice = noticeRepository.findByNoticeCd(((UpdateNoticeDTO) noticeDTO).getNoticeCd()).orElseThrow(
                () -> new NotFoundException(Constants.NOTICE_NOT_FOUND));

        boolean isUpdated = false;
        if (!notice.getTitle().equals(noticeDTO.getTitle())) {
            notice.setTitle(noticeDTO.getTitle());
            isUpdated = true;
        }
        if (!notice.getContent().equals(noticeDTO.getContent())) {
            notice.setContent(noticeDTO.getContent());
            isUpdated = true;
        }
        if (!notice.getStartDt().equals(noticeDTO.getStartDt())) {
            notice.setStartDt(noticeDTO.getStartDt());
            isUpdated = true;
        }
        if (!notice.getEndDt().equals(noticeDTO.getEndDt())) {
            notice.setEndDt(noticeDTO.getEndDt());
            isUpdated = true;
        }

        //기존 첨부파일 중 삭제된 것이 있을 경우 삭제처리
        List<NoticeAttach> deletedNoticeAttachList = notice.getNoticeAttachs()
                .stream()
                .filter(noticeAttach -> noticeDTO.getNoticeUriList().stream().noneMatch(noticeAttachDTO -> noticeAttachDTO.getAttachUri().equals(noticeAttach.getAttachUri())))
                .toList();

        if (!deletedNoticeAttachList.isEmpty()) {
            isUpdated = true;
            deletedNoticeAttachList.forEach(noticeAttach -> {
                noticeAttach.setDeleteYn("Y");
                noticeAttach.setDeleteUserCd(noticeDTO.getUpdateUserCd());
                noticeAttach.setDeleteDt(LocalDateTime.now());
            });
        }

        // 추가된 것이 있으면 추가
        List<NoticeAttachDTO> addedNoticeAttachList = noticeDTO.getNoticeUriList()
                .stream()
                .filter(noticeAttachDTO -> notice.getNoticeAttachs().stream().noneMatch(noticeAttach -> noticeAttach.getAttachUri().equals(noticeAttachDTO.getAttachUri())))
                .toList();

        if(!addedNoticeAttachList.isEmpty()){
            isUpdated = true;
            addedNoticeAttachList.forEach(noticeAttachDTO -> {
                NoticeAttach newNoticeAttach = new NoticeAttach();
                newNoticeAttach.setAttachUri(noticeAttachDTO.getAttachUri());
                newNoticeAttach.setNotice(notice);
                newNoticeAttach.setInsertUserCd(noticeDTO.getUpdateUserCd());
                newNoticeAttach.setDeleteYn("N");
                notice.getNoticeAttachs().add(newNoticeAttach);
            });
        }

        if (isUpdated) {
            notice.setUpdateUserCd(noticeDTO.getUpdateUserCd());
            noticeRepository.save(notice);
            resultMap.put("noticeCd", notice.getNoticeCd());
        }
        resultMap.put("isUpdated", isUpdated);
        return resultMap;
    }

    public void deleteNotice(String deleteUserCd, String noticeCd) throws Exception {
        // 삭제할 타겟 조회
        Notice notice =  getNotice(noticeCd);

        // 삭제
        notice.setDeleteYn("Y");
        notice.setDeleteDt(LocalDateTime.now());
        notice.setDeleteUserCd(deleteUserCd);

        noticeRepository.save(notice);
    }
}