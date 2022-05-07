package wooteco.subway.dao;

import java.util.List;
import wooteco.subway.domain.Line;

public interface LineDao {

    Line save(Line line);

    List<Line> findAll();

    int deleteById(Long id);

    Line findById(Long id);

    boolean exists(Line line);

    int update(Long id, Line updatingLine);
}
