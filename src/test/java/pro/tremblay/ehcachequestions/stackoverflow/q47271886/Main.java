package pro.tremblay.ehcachequestions.stackoverflow.q47271886;

/**
 * @author Henri Tremblay
 */
public class Main {

  public static void main(String[] args) throws InterruptedException {
    Thread thread = new Thread("Weblogic thread") {
//      private ThreadLocal<ByteWrapper> data = ThreadLocal.withInitial(() -> new ByteWrapper());

      @Override
      public void run() {
        ByteWrapper b = new ByteWrapper();
        synchronized(this) {
          try {
            wait();
          }
          catch(InterruptedException e) {
          }
        }
      }
    };

    thread.start();
    thread.join();

  }
}
