package pro.tremblay.ehcachequestions.bugs;

import org.terracotta.context.WeakIdentityHashMap;

import java.util.concurrent.Callable;

/**
 * @author Henri Tremblay
 */
public class WeakTest {

    private String field = "allo";

    public static void main(String[] args) {
        WeakIdentityHashMap<Object, Object> map = new WeakIdentityHashMap<>();
        foo(map);
        System.gc();
        System.gc();
        System.gc();
//        System.out.println(map.entrySet());
    }

    public WeakTest(WeakIdentityHashMap<Object, Object> map) {
        map.putIfAbsent(this, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return field;
            }
        });
    }

    private static void foo(WeakIdentityHashMap<Object, Object> map) {
        System.out.println(new WeakTest(map));
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof WeakTest)) {
            return false;
        }
        return this.hashCode() == obj.hashCode();
    }
}
