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

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDTO {
    // 제목
    @Schema(example = "공지 제목")
    private String title;

    // 내용
    @Schema(example = "공지 내용")
    private String content;

    // 공지 시작일시
    @Schema(example = "공지 시작일시(yyyy-MM-dd HH:mm:ss)")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startDt;

    // 공지 종료일시
    @Schema(example = "공지 종료일시(yyyy-MM-dd HH:mm:ss)")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endDt;

    // 첨부파일 uri 목록
    ArrayList<NoticeAttachDTO> noticeUriList;

    public Notice toEntity() throws InvalidNoticeTimeException {
       // LocalDateTime startDtLdt = LocalDateTime.parse(this.startDt);
        //LocalDateTime endDtLdt = LocalDateTime.parse(this.endDt);

        if (startDt.isAfter(endDt)) {
            throw new InvalidNoticeTimeException("공지 시작일시가 종료일시보다 늦을 수는 없습니다.");
        }
        if (startDt.isEqual(endDt)) {
            throw new InvalidNoticeTimeException("공지 시작일시와 종료일시가 같을 수는 없습니다.");
        }
        if (startDt.isBefore(LocalDateTime.now())) {
            throw new InvalidNoticeTimeException("공지 시작일시가 현재 시간보다 빠를 수는 없습니다.");
        }

        Notice noticeEntity = Notice.builder()
                .title(this.title)
                .content(this.content)
                .startDt(this.startDt)
                .endDt(this.endDt)
                .views(0)
                .deleteYn("N")
                .build();

        ArrayList<NoticeAttach> noticeAttachList = new ArrayList<NoticeAttach>();
        for (NoticeAttachDTO noticeAttachDTO : this.noticeUriList) {
            noticeAttachList.add(NoticeAttach.builder().notice(noticeEntity).attachUri(noticeAttachDTO.getAttachUri()).deleteYn("N").build());
        }
        noticeEntity.setNoticeAttach(noticeAttachList);
        return noticeEntity;
    }
}