package notice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import notice.common.exception.InvalidNoticeTimeException;
import notice.model.entity.Notice;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CreateNoticeDTO extends NoticeDTO{
    @Schema(example = "작성자 코드", hidden = true)
    private String writerUserCd;
    @Schema(example = "작성자", hidden = true)
    private String writer;
    public Notice toEntity() throws InvalidNoticeTimeException {
        Notice notice = super.toEntity();
        notice.setInsertUserCd(this.writerUserCd);
        notice.setWriter(this.writer);
        return notice;
    }
}
