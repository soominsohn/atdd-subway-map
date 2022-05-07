package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.Line;
import wooteco.subway.exception.line.LineNotFoundException;

@JdbcTest
@Sql("classpath:lineDao.sql")
class LineDaoImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;
    private Line line;

    @BeforeEach
    void setUp() {
        lineDao = new LineDaoImpl(jdbcTemplate);
        line = lineDao.save(new Line("신분당선", "red"));
    }

    @DisplayName("이름값을 받아 해당 이름값을 가진 노선이 있는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"2호선, red, true", "신분당선, blue, true", "신분당선, red, true", "2호선, blue, false"})
    void exists(String name, String color, boolean expected) {
        boolean actual = lineDao.exists(new Line(name, color));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("id에 해당하는 노선의 정보를 가져온다.")
    @Test
    void findById() {
        Line actual = lineDao.findById(line.getId());

        assertThat(actual).isEqualTo(new Line(line.getId(), "신분당선", "red"));
    }

    @DisplayName("id에 해당하는 노선이 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void findById_exception() {
        assertThatThrownBy(() -> lineDao.findById(-1L))
                .isInstanceOf(LineNotFoundException.class);
    }

    @DisplayName("id에 해당하는 노선의 정보를 바꾼다.")
    @Test
    void update() {
        Line updatingLine = new Line("7호선", "darkGreen");
        lineDao.update(line.getId(), updatingLine);

        Line updated = lineDao.findById(line.getId());

        assertThat(updated).isEqualTo(new Line(line.getId(), "7호선", "darkGreen"));
    }

    @DisplayName("id에 해당하는 노선을 제거한다.")
    @Test
    void deleteById() {
        lineDao.deleteById(line.getId());

        assertThat(lineDao.findAll()).isEmpty();
    }
}