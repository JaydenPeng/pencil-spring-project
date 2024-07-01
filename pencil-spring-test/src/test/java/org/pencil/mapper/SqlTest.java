package org.pencil.mapper;

import org.junit.jupiter.api.Test;
import org.pencil.test.TestApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

/**
 * The type Sql test.
 *
 * @author pencil
 * @Date 24 /07/01
 */
@SpringBootTest(classes = TestApplication.class)
public class SqlTest {

    /**
     * Select by id.
     */
    @Test
    @Sql(scripts = "/sql/create_table.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "INSERT INTO user (id, name, age) VALUES (1, 'pencil', 20)", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM user WHERE id = 1", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void selectById() {
        // 这里就是正常的增删改查
    }
}
