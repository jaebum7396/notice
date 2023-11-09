package notice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import notice.model.entity.Notice;
import notice.model.entity.NoticeAttach;

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
    @Schema(example = "공지 시작일시")
    private LocalDateTime startDt;

    // 공지 종료일시
    @Schema(example = "공지 종료일시")
    private LocalDateTime endDt;

    // 첨부파일 uri 목록
    ArrayList<NoticeAttachDTO> noticeUriList;

    public Notice toEntity() {
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