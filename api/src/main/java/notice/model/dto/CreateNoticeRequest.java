package notice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import notice.model.entity.Notice;
import notice.model.entity.NoticeAttach;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CreateNoticeRequest {
    // 제목
    @Schema(example = "공지 제목")
    private String title;

    // 내용
    @Schema(example = "공지 내용")
    private String contents;

    // 공지 시작일시
    @Schema(example = "공지 시작일시")
    private LocalDateTime startDt;

    // 공지 종료일시
    @Schema(example = "공지 종료일시")
    private LocalDateTime endDt;

    // 첨부파일 uri 목록
    ArrayList<String> noticeUriList;

    public Notice toEntity() {
        Notice noticeEntity = Notice.builder()
                .title(this.title)
                .contents(this.contents)
                .startDt(this.startDt)
                .endDt(this.endDt)
                .views(0)
                .build();

        ArrayList<NoticeAttach> noticeAttachList = new ArrayList<NoticeAttach>();
        for (String uri : this.noticeUriList) {
            noticeAttachList.add(NoticeAttach.builder().notice(noticeEntity).attachUri(uri).build());
        }
        noticeEntity.setNoticeAttach(noticeAttachList);
        return noticeEntity;
    }
}