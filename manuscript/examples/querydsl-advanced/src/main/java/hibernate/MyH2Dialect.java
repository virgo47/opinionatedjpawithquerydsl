package hibernate;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.DoubleType;

public class MyH2Dialect extends H2Dialect {
  public MyH2Dialect() {
    super();
    registerFunction("random",
      new StandardSQLFunction("random", DoubleType.INSTANCE));
  }
}
