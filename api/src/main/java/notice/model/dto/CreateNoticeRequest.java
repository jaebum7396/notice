package notice.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
    @ApiModelProperty(example = "공지 제목")
    private String title;

    // 내용
    @ApiModelProperty(example = "공지 내용")
    private String contents;

    // 공지 시작일시
    @ApiModelProperty(example = "공지 시작일시")
    private LocalDateTime startDt;

    // 공지 종료일시
    @ApiModelProperty(example = "공지 종료일시")
    private LocalDateTime endDt;

    // 첨부파일 uri 목록
    ArrayList<String> noticeUriList;
}