package notice.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import common.configuration.LocalDateTimeDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import notice.common.exception.InvalidNoticeTimeException;
import notice.model.entity.Notice;
import notice.model.entity.NoticeAttach;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * 공지사항 정보를 담는 DTO 클래스.
 * Notice 엔티티와의 데이터 전달을 담당한다.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {

    // 공지사항 제목
    @Schema(example = "공지 제목")
    private String title;

    // 공지사항 내용
    @Schema(example = "공지 내용")
    private String content;

    // 공지사항 시작일시
    @Schema(example = "공지 시작일시(yyyy-MM-dd HH:mm:ss)")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDt;

    // 공지사항 종료일시
    @Schema(example = "공지 종료일시(yyyy-MM-dd HH:mm:ss)")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endDt;

    // 첨부파일 URI 목록
    ArrayList<NoticeAttachDTO> noticeUriList;

    /**
     * DTO를 Notice 엔티티로 변환하는 메서드.
     * @return 변환된 Notice 엔티티
     * @throws InvalidNoticeTimeException 유효하지 않은 공지사항 시간 정보일 경우 발생하는 예외
     */
    public Notice toEntity() throws InvalidNoticeTimeException {
        // 입력된 시간 정보의 유효성을 검사
        if (startDt.isAfter(endDt)) {
            throw new InvalidNoticeTimeException("공지 시작일시가 종료일시보다 늦을 수는 없습니다.");
        }
        if (startDt.isEqual(endDt)) {
            throw new InvalidNoticeTimeException("공지 시작일시와 종료일시가 같을 수는 없습니다.");
        }
        if (startDt.isBefore(LocalDateTime.now())) {
            throw new InvalidNoticeTimeException("공지 시작일시가 현재 시간보다 빠를 수는 없습니다.");
        }

        // Notice 엔티티를 빌더를 사용하여 생성
        Notice noticeEntity = Notice.builder()
                .title(this.title)
                .content(this.content)
                .startDt(this.startDt)
                .endDt(this.endDt)
                .views(0)
                .deleteYn("N")
                .build();

        // NoticeAttachDTO 목록을 이용하여 NoticeAttach 엔티티 목록 생성
        ArrayList<NoticeAttach> noticeAttachList = new ArrayList<>();
        for (NoticeAttachDTO noticeAttachDTO : this.noticeUriList) {
            noticeAttachList.add(NoticeAttach.builder().notice(noticeEntity).attachUri(noticeAttachDTO.getAttachUri()).deleteYn("N").build());
        }
        noticeEntity.setNoticeAttach(noticeAttachList);

        // 생성된 Notice 엔티티 반환
        return noticeEntity;
    }
}
