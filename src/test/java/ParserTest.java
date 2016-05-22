import io.luan.exp4j.Expression;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ParserTest {

    @Test
    public void parseGreatThan() {

        Expression exp = Expression.parse("(x > 3) ? (x>4 ? x*2 : x*10) : x + 2");
        Map<String, Object> input = new HashMap<String, Object>(10);
        input.put("x", 3.512345);

        System.out.println(exp);

        Expression result = exp.evaluate(input);

        System.out.println(result);

    }

    @Test
    public void testNumbers() {
        Expression exp = Expression.parse("x / 10");
        Map<String, Object> input = new HashMap<String, Object>(10);
        input.put("x", 100);
        System.out.println(exp);

        Expression result = exp.evaluate(input);
        System.out.println(result);

    }

}
