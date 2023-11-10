package notice.service;

import notice.common.Constants.Constants;
import notice.common.exception.NotFoundException;
import notice.model.dto.CreateNoticeDTO;
import notice.model.dto.NoticeAttachDTO;
import notice.model.dto.UpdateNoticeDTO;
import notice.model.entity.Notice;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class NoticeServiceTest {

    @Autowired
    NoticeService noticeService;
    CreateNoticeDTO createNoticeDTO = null;
    NoticeAttachDTO noticeAttachDTO = null;

    @BeforeEach
    void beforeEach() {
        // 현재 날짜와 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        // 오늘 밤 12시 설정
        LocalDateTime todayStart = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime nextDayStart = todayStart.plusDays(1);
        LocalDateTime nextNextDayStart = nextDayStart.plusDays(1);
        // 포맷터를 사용하여 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String todayStr = todayStart.format(formatter);
        createNoticeDTO = new CreateNoticeDTO();
        createNoticeDTO.setTitle("글 제목");
        createNoticeDTO.setContent("글 내용");
        createNoticeDTO.setStartDt(nextDayStart); //내일부터
        createNoticeDTO.setEndDt(nextNextDayStart); //다다음날까지
        createNoticeDTO.setNoticeUriList(new ArrayList<>());
        noticeAttachDTO = new NoticeAttachDTO();
        noticeAttachDTO.setAttachUri("테스트 URI");
        createNoticeDTO.getNoticeUriList().add(noticeAttachDTO);
        createNoticeDTO.setWriterUserCd("TEST_USER_CD");
        createNoticeDTO.setWriter("작성자1");
    }

    @AfterEach
    void afterEach() {
        // 테스트가 끝날 때 마다 글 전체 삭제
        noticeService.deleteAll();
    }

    @Test
    @DisplayName("공지사항 조회 테스트")
    void readNoticeTest() throws Exception {
        // 공지사항을 작성한다.
        String noticeCd = noticeService.createNotice(createNoticeDTO);

        // 작성된 공지사항의 noticeCd를 통해 읽기전 공지사항을 가져온다.
        Notice notice = noticeService.getNotice(noticeCd);
        // 읽기 전 views
        int preReadViews = notice.getViews();

        // 작성된 공지사항을 읽어오고. 읽는 동시에 view +1 처리
        notice = noticeService.readNotice(noticeCd);
        // 읽음 후 views
        int postReadViews = notice.getViews();

        //검증 절차 - 읽기 전 views와 읽은 후 views가 1 차이가 나는지 확인
        assertThat(preReadViews + 1).isEqualTo(postReadViews);
    }

    @Test
    @DisplayName("공지사항 작성 테스트")
    void createNoticeTest() throws Exception {
        // 공지사항을 작성한다.
        String noticeCd = noticeService.createNotice(createNoticeDTO);

        // 작성된 공지사항의 noticeCd를 통해 notice를 가져와 검증한다.
        Notice notice = noticeService.getNotice(noticeCd);

        //검증 절차
        assertThat(createNoticeDTO.getTitle()).isEqualTo(notice.getTitle());
        assertThat(createNoticeDTO.getContent()).isEqualTo(notice.getContent());
        assertThat(createNoticeDTO.getStartDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .isEqualTo(notice.getStartDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(createNoticeDTO.getEndDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .isEqualTo(notice.getEndDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(createNoticeDTO.getWriterUserCd()).isEqualTo(notice.getInsertUserCd());
        assertThat(createNoticeDTO.getWriter()).isEqualTo(notice.getWriter());
        assertThat(createNoticeDTO.getNoticeUriList().get(0).getAttachUri())
                .isEqualTo(notice.getNoticeAttachs().get(0).getAttachUri());
    }

    @Test
    @DisplayName("공지사항 수정 테스트")
    void updateNoticeTest() throws Exception {
        // 공지사항을 작성한다.
        String noticeCd = noticeService.createNotice(createNoticeDTO);

        UpdateNoticeDTO updateNoticeDTO = new UpdateNoticeDTO();

        updateNoticeDTO.setNoticeCd(noticeCd);
        updateNoticeDTO.setTitle("글 제목2");
        updateNoticeDTO.setContent("글 내용2");
        updateNoticeDTO.setStartDt(LocalDateTime.now().plusDays(1));
        updateNoticeDTO.setEndDt(LocalDateTime.now().plusDays(2));
        updateNoticeDTO.setNoticeUriList(new ArrayList<>());
        noticeAttachDTO = new NoticeAttachDTO();
        noticeAttachDTO.setAttachUri("테스트 URI2");
        updateNoticeDTO.getNoticeUriList().add(noticeAttachDTO);

        // 공지사항을 수정한다.
        Map<String, Object> resultMap = noticeService.updateNotice(updateNoticeDTO);
        // 수정 되었는지 체크
        assertThat(resultMap.get("isUpdated")).isEqualTo(true);

        // 공지사항의 noticeCd를 통해 값이 올바르게 수정되었는지 검증한다.
        Notice notice = noticeService.getNotice(noticeCd);

        //검증 절차
        assertThat(updateNoticeDTO.getTitle()).isEqualTo(notice.getTitle());
        assertThat(updateNoticeDTO.getContent()).isEqualTo(notice.getContent());
        assertThat(updateNoticeDTO.getStartDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .isEqualTo(notice.getStartDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(updateNoticeDTO.getEndDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .isEqualTo(notice.getEndDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        assertThat(updateNoticeDTO.getUpdateUserCd()).isEqualTo(notice.getUpdateUserCd());
        assertThat(updateNoticeDTO.getNoticeUriList().get(0).getAttachUri())
                .isEqualTo(notice.getNoticeAttachs().get(0).getAttachUri());
    }

    @Test
    @DisplayName("공지사항 삭제 테스트")
    void deleteNoticeTest() throws Exception {
        // 공지사항을 작성한다.
        String noticeCd = noticeService.createNotice(createNoticeDTO);

        // 작성된 공지사항의 noticeCd를 통해 공지사항을 가져온다.
        Notice notice = noticeService.getNotice(noticeCd);

        // 공지사항을 삭제한다.
        noticeService.deleteNotice("관리자", noticeCd);

        //검증 절차 - 삭제된 공지사항을 조회하여 예상한 예외인 NotFoundException이 발생하는지 확인
        assertThatThrownBy(() -> noticeService.getNotice(noticeCd))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(Constants.NOTICE_NOT_FOUND);
    }
}