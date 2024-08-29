package us.dot.its.jpo.ode.s3.depositor.threads;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ServiceManager {
   private ThreadFactory threadFactory;

   public ServiceManager(ThreadFactory tf) {
      this.threadFactory = tf;
   }

   public void submit(Runnable run) {
      Executors.newSingleThreadExecutor(threadFactory).submit(run);
   }

}
