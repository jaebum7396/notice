package notice.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 공지사항의 첨부 파일 정보를 나타내는 DTO 클래스.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class NoticeAttachDTO {
    // 첨부 파일의 URI
    String attachUri;
}
