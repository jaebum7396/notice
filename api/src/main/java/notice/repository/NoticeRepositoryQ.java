package notice.repository;

import notice.model.entity.Notice;
import java.util.List;

public interface NoticeRepositoryQ {
    List<Notice> findByNoticeCd(String noticeCd);
}
