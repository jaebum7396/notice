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

    /**
     * 모든 공지사항을 삭제하는 메서드.
     * 주의: 해당 메서드를 호출하면 모든 공지사항이 영구적으로 삭제됩니다.
     */
    public void deleteAll() {
        // noticeRepository를 통해 모든 공지사항을 삭제
        noticeRepository.deleteAll();
    }

    /**
     * 주어진 noticeCd에 해당하는 공지사항을 물리적으로 삭제하는 메서드.
     * @param noticeCd 삭제할 공지사항의 고유 코드
     * 주의: 해당 메서드를 호출하면 공지사항이 영구적으로 삭제됩니다.
     */
    public void physicalDelete(String noticeCd) {
        // noticeRepository를 통해 주어진 noticeCd에 해당하는 공지사항을 물리적으로 삭제
        noticeRepository.deleteById(noticeCd);
    }

    /**
     * 주어진 noticeCd에 해당하는 공지사항을 조회하고 조회수를 증가시킨 후 반환하는 메서드.
     * @param noticeCd 조회할 공지사항의 고유 코드
     * @return 조회된 공지사항
     * @throws Exception 공지사항 조회 중 발생한 예외
     */
    public Notice readNotice(String noticeCd) throws Exception {
        // 주어진 noticeCd에 해당하는 공지사항을 가져옴
        Notice notice = getNotice(noticeCd);

        // 조회수 증가
        notice.setViews(notice.getViews() + 1);

        // 변경된 공지사항을 저장
        noticeRepository.save(notice);

        // 조회된 공지사항 반환
        return notice;
    }

    /**
     * 주어진 noticeCd에 해당하는 공지사항을 조회하는 메서드.
     * @param noticeCd 조회할 공지사항의 고유 코드
     * @return 조회된 공지사항
     * @throws NotFoundException 조회된 공지사항이 없을 경우 발생하는 예외
     */
    public Notice getNotice(String noticeCd) throws NotFoundException {
        // noticeCd를 이용하여 공지사항을 조회하고 Optional로 감싸 반환
        return noticeRepository.findByNoticeCd(noticeCd)
                // 조회된 값이 없으면 NotFoundException 발생
                .orElseThrow(() -> new NotFoundException(Constants.NOTICE_NOT_FOUND));
    }

    /**
     * 주어진 NoticeDTO를 이용하여 새로운 공지사항을 생성하고 생성된 공지사항의 코드를 반환하는 메서드.
     * @param noticeDTO 생성할 공지사항의 정보를 담은 DTO
     * @return 생성된 공지사항의 코드
     * @throws Exception 공지사항 생성 중 발생한 예외
     */
    public String createNotice(NoticeDTO noticeDTO) throws Exception {
        // NoticeDTO를 Notice 엔티티로 변환
        Notice noticeEntity = noticeDTO.toEntity();

        // 공지사항을 저장
        noticeRepository.save(noticeEntity);

        // 생성된 공지사항의 코드를 반환
        return noticeEntity.getNoticeCd();
    }

    /**
     * 주어진 UpdateNoticeDTO를 이용하여 공지사항을 업데이트하는 메서드.
     * @param noticeDTO 업데이트할 공지사항 정보를 담은 DTO
     * @return 업데이트 결과 및 업데이트된 공지사항의 코드를 포함한 Map
     * @throws NotFoundException 공지사항이나 공지사항 코드가 존재하지 않을 경우 발생하는 예외
     */
    public Map<String, Object> updateNotice(UpdateNoticeDTO noticeDTO) throws NotFoundException {
        Map<String, Object> resultMap = new LinkedHashMap<>();

        // 주어진 noticeDTO에 noticeCd가 없으면 Exception 발생
        if (noticeDTO.getNoticeCd() == null) {
            throw new NotFoundException(Constants.NOTICE_CD_NOT_FOUND);
        }

        // 주어진 noticeCd에 해당하는 공지사항을 가져오거나 없으면 Exception 발생
        Notice notice = noticeRepository.findByNoticeCd(noticeDTO.getNoticeCd())
                .orElseThrow(() -> new NotFoundException(Constants.NOTICE_NOT_FOUND));

        // 업데이트 여부를 나타내는 플래그
        boolean isUpdated = false;

        // 제목이 변경되었으면 업데이트하고 플래그를 설정
        if (!notice.getTitle().equals(noticeDTO.getTitle())) {
            notice.setTitle(noticeDTO.getTitle());
            isUpdated = true;
        }

        // 내용이 변경되었으면 업데이트하고 플래그를 설정
        if (!notice.getContent().equals(noticeDTO.getContent())) {
            notice.setContent(noticeDTO.getContent());
            isUpdated = true;
        }

        // 시작일이 변경되었으면 업데이트하고 플래그를 설정
        if (!notice.getStartDt().equals(noticeDTO.getStartDt())) {
            notice.setStartDt(noticeDTO.getStartDt());
            isUpdated = true;
        }

        // 종료일이 변경되었으면 업데이트하고 플래그를 설정
        if (!notice.getEndDt().equals(noticeDTO.getEndDt())) {
            notice.setEndDt(noticeDTO.getEndDt());
            isUpdated = true;
        }

        // 기존 첨부파일 중 삭제된 것이 있으면 삭제 처리하고 플래그를 설정
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

        // 추가된 첨부파일이 있으면 추가하고 플래그를 설정
        List<NoticeAttachDTO> addedNoticeAttachList = noticeDTO.getNoticeUriList()
                .stream()
                .filter(noticeAttachDTO -> notice.getNoticeAttachs().stream().noneMatch(noticeAttach -> noticeAttach.getAttachUri().equals(noticeAttachDTO.getAttachUri())))
                .toList();

        if (!addedNoticeAttachList.isEmpty()) {
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

        // 업데이트가 발생했다면 업데이트된 공지사항을 저장하고 결과 Map에 추가
        if (isUpdated) {
            notice.setUpdateUserCd(noticeDTO.getUpdateUserCd());
            noticeRepository.save(notice);
            resultMap.put("noticeCd", notice.getNoticeCd());
        }

        // 결과 Map에 업데이트 여부와 함께 반환
        resultMap.put("isUpdated", isUpdated);
        return resultMap;
    }

    /**
     * 주어진 noticeCd에 해당하는 공지사항을 삭제하는 메서드.
     * @param deleteUserCd 삭제한 사용자의 코드
     * @param noticeCd 삭제할 공지사항의 코드
     * @throws NotFoundException 공지사항이나 공지사항 코드가 존재하지 않을 경우 발생하는 예외
     */
    public void deleteNotice(String deleteUserCd, String noticeCd) throws NotFoundException {
        // 삭제할 타겟 조회
        Notice notice = getNotice(noticeCd);

        // 공지사항을 삭제 처리
        notice.setDeleteYn("Y");
        notice.setDeleteDt(LocalDateTime.now());
        notice.setDeleteUserCd(deleteUserCd);

        // 변경된 공지사항을 저장
        noticeRepository.save(notice);
    }
}