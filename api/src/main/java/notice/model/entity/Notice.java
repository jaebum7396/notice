package notice.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import notice.model.entity.common.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity(name = "NOTICE")
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column( name = "NOTICE_CD")
    private String noticeCd;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENTS")
    private String contents;

    @Column(name = "START_DT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDt;

    @Column(name = "END_DT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endDt;

    @Column(name = "VIEWS")
    private Integer views;

    @Column(name = "WRITER")
    private String writer;

    @OneToMany(mappedBy = "notice", fetch = FetchType.EAGER, cascade = CascadeType.ALL) @Builder.Default
	private List<NoticeAttach> noticeAttachs = new ArrayList<>();
    
    public void addNoticeAttach(NoticeAttach noticeAttach) {
    	this.noticeAttachs.add(noticeAttach);
    }
    
    public void setNoticeAttach(List<NoticeAttach> noticeAttachs) {
        this.noticeAttachs = noticeAttachs;
        noticeAttachs.forEach(o -> o.setNotice(this));
    }
}