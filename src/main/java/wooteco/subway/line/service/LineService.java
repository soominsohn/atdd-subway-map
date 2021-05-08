package wooteco.subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.LineCreateRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.LineUpdateRequest;
import wooteco.subway.line.exception.LineIllegalArgumentException;
import wooteco.subway.station.dao.StationDao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDao lineDao;
    private final StationDao stationDao;

    public LineService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public LineResponse save(LineCreateRequest lineCreateRequest) {
        validateDuplicateName(lineCreateRequest.getName());
        validateAllStationsIsExist(lineCreateRequest);
        validateIfDownStationIsEqualToUpStation(lineCreateRequest);

        Line line = Line.of(lineCreateRequest);
        Line savedLine = lineDao.save(line);

        return LineResponse.from(savedLine);
    }

    private void validateDuplicateName(String lineName) {
        if (lineDao.findByName(lineName).isPresent()) {
            throw new LineIllegalArgumentException("같은 이름의 노선이 있습니다;");
        }
    }

    private void validateAllStationsIsExist(LineCreateRequest lineCreateRequest) {
        stationDao.findById(lineCreateRequest.getDownStationId())
                .orElseThrow(() -> new LineIllegalArgumentException("입력하신 하행역이 존재하지 않습니다."));
        stationDao.findById(lineCreateRequest.getUpStationId())
                .orElseThrow(() -> new LineIllegalArgumentException("입력하신 상행역이 존재하지 않습니다."));
    }

    private void validateIfDownStationIsEqualToUpStation(LineCreateRequest lineCreateRequest) {
        if (lineCreateRequest.isSameStations()) {
            throw new LineIllegalArgumentException("상행과 하행 종점은 같을 수 없습니다.");
        }
    }

    public List<LineResponse> findAll() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse find(Long id) {
        Line line = lineDao.findById(id)
                .orElseThrow(() -> new LineIllegalArgumentException("해당하는 노선이 존재하지 않습니다."));

        return LineResponse.from(line);
    }

    public void delete(Long id) {
        lineDao.findById(id)
                .orElseThrow(() -> new LineIllegalArgumentException("삭제하려는 노선이 존재하지 않습니다"));
        lineDao.delete(id);
    }

    public void update(Long id, LineUpdateRequest lineUpdateRequest) {
        lineDao.findById(id)
                .orElseThrow(() -> new LineIllegalArgumentException("수정하려는 노선이 존재하지 않습니다"));
        validateDuplicateNameExceptMyself(id, lineUpdateRequest.getName());
        Line line = Line.of(id, lineUpdateRequest);
        lineDao.update(line);
    }

    private void validateDuplicateNameExceptMyself(Long id, String lineName) {
        Optional<Line> lineByName = lineDao.findByName(lineName);
        if (lineByName.isPresent() && !lineByName.get().getId().equals(id)) {
            throw new LineIllegalArgumentException("같은 이름의 노선이 있습니다;");
        }
    }
}