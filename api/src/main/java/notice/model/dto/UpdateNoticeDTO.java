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
public class UpdateNoticeDTO extends NoticeDTO{
    @Schema(example = "noticeCd", nullable = false)
    private String noticeCd;
    @Schema(example = "업데이트 유저 코드", hidden = true)
    private String updateUserCd;
    public Notice toEntity() throws InvalidNoticeTimeException {
        Notice notice = super.toEntity();
        notice.setNoticeCd(noticeCd);
        return notice;
    }
}
