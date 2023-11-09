package notice.repository;

import notice.model.entity.Notice;
import java.util.List;
import java.util.Optional;

public interface NoticeRepositoryQ {
    Optional<Notice> findByNoticeCd(String noticeCd);
}
