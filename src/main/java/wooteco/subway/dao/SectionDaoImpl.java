package wooteco.subway.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;

@Repository
public class SectionDaoImpl implements SectionDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public SectionDaoImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    private static final RowMapper<Section> ACTOR_ROW_MAPPER = (resultSet, rowNum) -> new Section(
            resultSet.getLong("id"),
            resultSet.getLong("line_id"),
            resultSet.getLong("up_station_id"),
            resultSet.getLong("down_station_id"),
            resultSet.getInt("distance")
    );

    @Override
    public Section save(Section section) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(section);
        Long id = insertActor.executeAndReturnKey(parameters).longValue();
        return new Section(
                id,
                section.getLineId(),
                section.getUpStationId(),
                section.getDownStationId(),
                section.getDistance());
    }

    @Override
    public Sections findByLineId(Long lineId) {
        String sql = "select * from section where line_id = :lineId";
        SqlParameterSource namedParameter = new MapSqlParameterSource("lineId", lineId);
        return new Sections(namedParameterJdbcTemplate.query(sql, namedParameter, ACTOR_ROW_MAPPER));
    }

    @Override
    public int update(Section section) {
        String sql = "update section set up_station_id = :upStationId, down_station_id = :downStationId, distance = :distance where id = :id";
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(section);
        return namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public int deleteById(Long id) {
        String sql = "delete from section where id = :id";
        SqlParameterSource namedParameter = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.update(sql, namedParameter);
    }

    @Override
    public int deleteByIds(List<Long> ids) {
        String sql = "delete from section where id in (:ids)";
        SqlParameterSource namedParameter = new MapSqlParameterSource("ids", ids);
        return namedParameterJdbcTemplate.update(sql, namedParameter);
    }
}
