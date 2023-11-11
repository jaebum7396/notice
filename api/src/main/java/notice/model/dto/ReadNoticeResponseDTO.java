package notice.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import common.configuration.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 공지사항 조회 응답을 담는 DTO 클래스.
 * ReadNoticeResponseDTO는 공지사항을 조회한 결과를 클라이언트에 응답할 때 사용된다.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ReadNoticeResponseDTO {

    // 공지사항 제목
    @Schema(example = "제목")
    private String title;

    // 공지사항 내용
    @Schema(example = "내용")
    private String content;

    // 공지사항 등록일시
    @Schema(example = "등록일시")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime insertDt;

    // 공지사항 조회수
    @Schema(example = "조회수")
    private int views;

    // 공지사항 작성자
    @Schema(example = "작성자")
    private String writer;
}
