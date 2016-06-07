import io.luan.exp4j.util.ObjectComparator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luangm on 16/6/7.
 */
public class CompareTest {
    @Test
    public void testObjs() {

        ABC x = new ABC();
        x.x = 100;
        x.list = new ArrayList<>();
        x.list.add(new XYZ("ABC"));
        List<String> xxx = new ArrayList<>();
        xxx.add("EFG123");
        xxx.add("EFG234");
        xxx.add("EFG345");
        x.list.get(0).map.put("DD", xxx);


        x.list.add(new XYZ("ABCe"));
        ABC y = new ABC();
        y.x = 100;
        y.list = new ArrayList<>();
        y.list.add(new XYZ("ABC"));
        List<String> yyy = new ArrayList<>();
        yyy.add("EFG123");
        yyy.add("EFG234");
        y.list.get(0).map.put("DD", yyy);
        y.list.add(new XYZ("ABCf"));

        Map<String, String> diff = ObjectComparator.compare(x, y);

        System.out.println(diff);
    }

    public class ABC {
        int x;

        List<XYZ> list;
    }

    public class XYZ {
        String zz;

        Map<String, List<String>> map;

        public XYZ(String zz) {
            this.zz = zz;
            map = new HashMap<>();
        }
    }
}
