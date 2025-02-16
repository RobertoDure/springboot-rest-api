package pt.com.springboot.api.config;

/**
 * This class is used to store the transactionId in a ThreadLocal context holder.
 * ThreadLocal is thread-safe for a given thread because each thread gets its own
 * isolated copy of the transactionId. That said, if you start asynchronous tasks
 * or new threads, the ThreadLocal’s data does not automatically propagate to those
 * threads. In such cases, you would need to transfer the transactionId manually or
 * use an appropriate propagation mechanism.
 */
public class TransactionContextHolder {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void setTransactionId(String transactionId) {
        CONTEXT.set(transactionId);
    }

    public static String getTransactionId() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
