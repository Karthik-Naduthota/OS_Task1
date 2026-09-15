import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Product represents an item produced by the producer.
 */
class Product {

    private final int productId;
    private final String category;

    public Product(int productId, String category) {
        this.productId = productId;
        this.category = category;
    }

    public int getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return "Product-" + productId + " (" + category + ")";
    }
}


/*
 * Producer creates products and puts them into
 * the shared bounded queue.
 */
class ProductProducer implements Runnable {

    private final BlockingQueue<Product> queue;

    private final String[] categories = {
        "Electronics",
        "Books",
        "Clothing",
        "Grocery",
        "Accessories"
    };

    public ProductProducer(BlockingQueue<Product> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        try {

            for (int i = 1; i <= 15; i++) {

                String category =
                    categories[(i - 1) % categories.length];

                Product product =
                    new Product(i, category);

                /*
                 * put() automatically waits if the
                 * bounded queue is full.
                 */
                queue.put(product);

                System.out.println(
                    "Producer -> Added: "
                    + product
                    + " | Buffer size: "
                    + queue.size()
                );

                Thread.sleep(150);
            }

            /*
             * Special product used to tell the consumer
             * that production is completed.
             */
            queue.put(new Product(-1, "STOP"));

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                "Producer thread interrupted."
            );
        }
    }
}


/*
 * Consumer removes products from the shared queue
 * and processes them.
 */
class ProductConsumer implements Runnable {

    private final BlockingQueue<Product> queue;

    public ProductConsumer(BlockingQueue<Product> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        try {

            while (true) {

                /*
                 * take() automatically waits when
                 * the queue is empty.
                 */
                Product product = queue.take();

                /*
                 * Stop signal.
                 */
                if (product.getProductId() == -1) {
                    break;
                }

                System.out.println(
                    "Consumer <- Processing: "
                    + product
                    + " | Buffer size: "
                    + queue.size()
                );

                Thread.sleep(350);
            }

            System.out.println(
                "Consumer finished processing."
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                "Consumer thread interrupted."
            );
        }
    }
}


/*
 * Main class
 */
public class ProducerConsumerAlternative {

    public static void main(String[] args) {

        /*
         * Bounded buffer with capacity 4.
         */
        BlockingQueue<Product> buffer =
            new ArrayBlockingQueue<>(4);

        /*
         * Two worker threads are managed using
         * an ExecutorService.
         */
        ExecutorService executor =
            Executors.newFixedThreadPool(2);

        ProductProducer producer =
            new ProductProducer(buffer);

        ProductConsumer consumer =
            new ProductConsumer(buffer);

        System.out.println(
            "=========================================="
        );

        System.out.println(
            "      PRODUCER-CONSUMER SIMULATION"
        );

        System.out.println(
            "=========================================="
        );

        System.out.println(
            "Buffer capacity: 4"
        );

        System.out.println(
            "Products to produce: 15\n"
        );

        /*
         * Start producer and consumer.
         */
        executor.execute(producer);
        executor.execute(consumer);

        /*
         * No new tasks are accepted after this.
         */
        executor.shutdown();

        System.out.println(
            "\nProducer and Consumer threads started."
        );
    }
}
