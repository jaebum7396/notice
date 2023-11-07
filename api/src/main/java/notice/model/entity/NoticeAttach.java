package notice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import notice.model.entity.common.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper=false)
@DynamicInsert
@DynamicUpdate
@Entity(name = "NOTICE_ATTACH")
public class NoticeAttach extends BaseEntity implements Serializable{
	@Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@Column( name = "NOTICE_ATTACH_CD")
    private String noticeAttachCd;

	@ManyToOne @JoinColumn(name = "NOTICE_CD")
	private Notice notice;

	@Column( name = "ATTACH_URI")
	private String attachUri;

	@JsonIgnore
	public void setNotice(Notice notice) {
        this.notice = notice;
    }
}