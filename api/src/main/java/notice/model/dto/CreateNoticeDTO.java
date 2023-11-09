package notice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import notice.model.entity.Notice;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CreateNoticeDTO extends NoticeDTO{
    @Schema(example = "작성자 코드")
    private String writerUserCd;
    @Schema(example = "작성자")
    private String writer;
    public Notice toEntity() {
        Notice notice = super.toEntity();
        notice.setInsertUserCd(this.writerUserCd);
        notice.setWriter(this.writer);
        return notice;
    }
}
