package config;

import okhttp3.OkHttpClient;

/**
 * A singleton class that provides a single instance of OkHttpClient for making HTTP requests.
 * This implementation ensures thread safety by using a synchronized block to prevent multiple instances
 * from being created in a multithreaded environment. It is considered good practice to reuse one instance
 * of an OkHttpClient.
 */
public class OkHttpClientSingleton {

    private static OkHttpClient instance;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private OkHttpClientSingleton() {

    }

    /**
     * Returns the single instance of OkHttpClient.
     * If the instance is already available, it is returned.
     * Otherwise, it is run through the synchronized keyword to prevent multiple instances
     *
     * @return the single instance of OkHttpClient
     */
    public static OkHttpClient getInstance() {
        if (instance == null) {
            // Synchronized block: Ensures thread safety, preventing multiple instances
            // from being created in a multithreaded environment.
            synchronized (OkHttpClientSingleton.class) {
                if (instance == null) {
                    instance = new OkHttpClient.Builder()
                            .build();
                }
            }
        }
        return instance;
    }

}
