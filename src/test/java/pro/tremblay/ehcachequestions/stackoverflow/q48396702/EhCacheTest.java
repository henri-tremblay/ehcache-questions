package pro.tremblay.ehcachequestions.stackoverflow.q48396702;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.pool.sizeof.UnsafeSizeOf;

public class EhCacheTest {

  private List<Double> testList = new ArrayList<Double>();

  public static void main(String[] args) {
    EhCacheTest test = new EhCacheTest();
    UnsafeSizeOf unsafeSizeOf = new UnsafeSizeOf();
    for(int i=0;i<1000;i++) {
      test.addItem(1.0);
      System.out.println(unsafeSizeOf.deepSizeOf(1000, false, test).getCalculated());
    }
  }

  public void addItem(double a) {
    testList.add(a);
  }
}
