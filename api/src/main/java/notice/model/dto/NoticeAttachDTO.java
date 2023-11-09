package notice.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import notice.model.entity.Notice;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class NoticeAttachDTO{
    String attachUri;
}
