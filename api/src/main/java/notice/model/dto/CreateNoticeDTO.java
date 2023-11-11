package notice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import notice.common.exception.InvalidNoticeTimeException;
import notice.model.entity.Notice;

/**
 * 새로운 공지사항을 생성할 때 사용하는 DTO 클래스.
 * NoticeDTO를 확장하며, 공지사항 작성에 필요한 정보를 담고 있다.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class CreateNoticeDTO extends NoticeDTO {

    // 작성자 코드
    @Schema(example = "작성자 코드", hidden = true)
    private String writerUserCd;

    // 작성자
    @Schema(example = "작성자", hidden = true)
    private String writer;

    /**
     * DTO를 Notice 엔티티로 변환하는 메서드.
     * @return 변환된 Notice 엔티티
     * @throws InvalidNoticeTimeException 유효하지 않은 공지사항 시간 정보일 경우 발생하는 예외
     */
    public Notice toEntity() throws InvalidNoticeTimeException {
        // 상위 클래스의 toEntity 메서드를 호출하여 공통 필드를 설정
        Notice notice = super.toEntity();

        // 작성자 코드와 작성자 정보 설정
        notice.setInsertUserCd(this.writerUserCd);
        notice.setWriter(this.writer);

        // 변환된 Notice 엔티티 반환
        return notice;
    }
}
