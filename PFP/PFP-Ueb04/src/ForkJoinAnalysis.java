import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
public class ForkJoinAnalysis {

 private static volatile long temp;
 private static AtomicLong temp2 = new AtomicLong();
 private static Long temp3 = 0L;
 private static synchronized void syncAccess(long val) { temp3 += val; }

 public static void main(String[] args)
  throws InterruptedException, ExecutionException {
  ForkJoinPool pool = new ForkJoinPool(3);
  long[] operand = new long[] {5, 10, 15, 20, 25, 30, 35, 40, 45, 50 }; //
  ForkJoinTask<Long> who = new MysteriousTask(operand, 0, operand.length);
  System.out.println("Result: " + pool.submit(who).get());
 }

 protected static class MysteriousTask extends RecursiveTask<Long> {
  private long[] operand;
  private final int first;
  private final int second;
  protected MysteriousTask(long[] array, int first, int second) {
   this.operand = array;
   this.first = first;
   this.second = second;
  }

  @Override protected Long compute() {
   int value = second - first;
   if ( value < 2) return operand[first];
   int anotherValue = first + (value / 2);
   MysteriousTask a = new MysteriousTask(operand, first, anotherValue);
   MysteriousTask b = new MysteriousTask(operand, anotherValue, second);
   invokeAll(a, b); // teilt beide Tasks auf
   try {
    long res = a.get() + b.get();
    temp += res;
    temp2.addAndGet(res);
    syncAccess(res);
    return res;
   } catch (ExecutionException e) {
    e.printStackTrace();
   } catch (InterruptedException e) {
	   e.printStackTrace();
   }
   System.exit(-1); return 0L;
  }
 }
}