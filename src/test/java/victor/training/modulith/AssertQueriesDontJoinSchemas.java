package victor.training.modulith;

import com.p6spy.engine.common.PreparedStatementInformation;
import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.event.JdbcEventListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class AssertQueriesDontJoinSchemas extends JdbcEventListener {

  @Override
  public void onBeforeExecute(PreparedStatementInformation statementInformation) {
    checkSql(statementInformation.getSql());
  }

  @Override
  public void onBeforeExecute(StatementInformation statementInformation, String sql) {
    checkSql(statementInformation.getSql());
  }

  @Override
  public void onBeforeExecuteQuery(PreparedStatementInformation statementInformation) {
    checkSql(statementInformation.getSql());
  }

  @Override
  public void onBeforeExecuteQuery(StatementInformation statementInformation, String sql) {
    checkSql(statementInformation.getSql());
  }

  @SneakyThrows
  public void checkSql(String sql) {
    Set<String> tables;
    try {
      Statement stmt = CCJSqlParserUtil.parse(sql);
      TablesNamesFinder<Void> finder = new TablesNamesFinder<>();
      tables = finder.getTables(stmt);
    } catch (Exception e) {
      log.warn("Could not parse SQL: " + e);
      tables = Set.of();
    }
    log.info("{} tables involved: {} in query: {}", tables.size(), tables, sql);
    var tablesPerSchema = tables.stream()
        .filter(t -> t.contains("."))
        .filter(t -> !t.toLowerCase().endsWith("_view")) // views are ok to join
        .collect(Collectors.groupingBy(
            t -> t.split("\\.")[0],
            mapping(t -> t.split("\\.")[1], toList())));
    if (tablesPerSchema.size() > 1) {
      String message = "Query uses tables of different schemas: " + tablesPerSchema + "\n Illegal as per ADR-012" + sql;
//      log.error(message);
      throw new AssertionError(message);
    }
  }
}
