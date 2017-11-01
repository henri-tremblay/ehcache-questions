package pro.tremblay.ehcachequestions.experience;

import org.terracotta.context.WeakIdentityHashMap;

/**
 * @author Henri Tremblay
 */
public class Weak {

    public static void main(String[] args) {
        WeakIdentityHashMap<String, byte[]> map = new WeakIdentityHashMap<>();
        for(int i = 0; i < Integer.MAX_VALUE; i++) {
            map.putIfAbsent(""+i, new byte[1024]);
//            if(i % 100 == 0) {
//                System.out.println(map..size());
//            }
        }
    }
}
