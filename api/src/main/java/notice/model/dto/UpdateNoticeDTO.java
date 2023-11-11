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
 * 공지사항 업데이트에 사용되는 DTO 클래스.
 * NoticeDTO를 확장하며, 업데이트에 필요한 정보를 담고 있다.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UpdateNoticeDTO extends NoticeDTO {

    // 업데이트할 공지사항의 코드
    @Schema(example = "noticeCd", nullable = false)
    private String noticeCd;

    // 업데이트를 수행한 사용자의 코드
    @Schema(example = "업데이트 유저 코드", hidden = true)
    private String updateUserCd;

    /**
     * DTO를 Notice 엔티티로 변환하는 메서드.
     * @return 변환된 Notice 엔티티
     * @throws InvalidNoticeTimeException 유효하지 않은 공지사항 시간 정보일 경우 발생하는 예외
     */
    public Notice toEntity() throws InvalidNoticeTimeException {
        // 상위 클래스의 toEntity 메서드를 호출하여 공통 필드를 설정
        Notice notice = super.toEntity();

        // 업데이트할 공지사항의 코드 설정
        notice.setNoticeCd(noticeCd);

        // 변환된 Notice 엔티티 반환
        return notice;
    }
}
