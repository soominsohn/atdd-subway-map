package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.line.DuplicatedLineException;
import wooteco.subway.exception.line.LineNotFoundException;

@Service
public class LineService {

    private static final int NONE = 0;

    private final LineDao lineDao;
    private final SectionService sectionService;
    private final StationService stationService;

    public LineService(LineDao lineDao, SectionService sectionService, StationService stationService) {
        this.lineDao = lineDao;
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    @Transactional
    public Line save(Line line, Section section) {
        if (lineDao.existsByNameOrColor(line)) {
            throw new DuplicatedLineException();
        }
        Line newLine = lineDao.save(line);
        Section newSection = new Section(newLine.getId(), section);

        sectionService.save(newSection);

        List<Long> stationIds = sectionService.findArrangedStationIdsByLineId(newLine.getId());
        List<Station> stations = stationService.findStationByIds(stationIds);

        return new Line(newLine, stations);
    }

    @Transactional(readOnly = true)
    public List<Line> findAll() {
        List<Line> lines = lineDao.findAll();
        List<Line> newLines = new ArrayList<>();

        for (Line line : lines) {
            List<Long> stationIds = sectionService.findArrangedStationIdsByLineId(line.getId());
            List<Station> stations = stationService.findStationByIds(stationIds);
            newLines.add(new Line(line, stations));
        }
        return new ArrayList<>(newLines);
    }

    @Transactional
    public void deleteById(Long id) {
        int executedRows = lineDao.deleteById(id);
        if (executedRows == NONE) {
            throw new LineNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long id) {
        try {
            Line line = lineDao.findById(id);

            List<Long> stationIds = sectionService.findArrangedStationIdsByLineId(line.getId());
            List<Station> stations = stationService.findStationByIds(stationIds);

            return new Line(line, stations);
        } catch (EmptyResultDataAccessException e) {
            throw new LineNotFoundException();
        }
    }

    @Transactional
    public void update(Line line) {
        int executedRows = lineDao.update(line);
        if (executedRows == NONE) {
            throw new LineNotFoundException();
        }
    }
}
