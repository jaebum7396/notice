package notice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import notice.model.entity.Notice;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ReadNoticeResponseDTO{
    @Schema(example = "제목")
    private String title;
    @Schema(example = "내용")
    private String content;
    @Schema(example = "등록일시")
    private LocalDateTime insertDt;
    @Schema(example = "조회수")
    private int views;
    @Schema(example = "작성자")
    private String writer;
}
