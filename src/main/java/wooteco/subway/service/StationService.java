package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.station.DuplicatedStationException;
import wooteco.subway.exception.station.StationNotFoundException;

@Service
public class StationService {

    private static final int NONE = 0;

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public Station save(Station station) {
        if (stationDao.existsByName(station)) {
            throw new DuplicatedStationException();
        }
        return stationDao.save(station);
    }

    @Transactional
    public void deleteById(long id) {
        int executedRows = stationDao.deleteById(id);
        if (executedRows == NONE) {
            throw new StationNotFoundException();
        }
    }

    @Transactional(readOnly = true)
    public List<Station> findAll() {
        return stationDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Station> findStationByIds(List<Long> ids) {
        List<Station> stations = stationDao.findByIds(ids);
        List<Station> stationsWithIdOrder = new ArrayList<>();

        for (Long id : ids) {
            Optional<Station> station = stations.stream()
                    .filter(it -> it.isSameId(id))
                    .findFirst();

            station.ifPresent(stationsWithIdOrder::add);
        }
        return stationsWithIdOrder;
    }
}
