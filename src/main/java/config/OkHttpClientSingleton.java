package config;

import okhttp3.OkHttpClient;

public class OkHttpClientSingleton {

    private static OkHttpClient instance;

    // Private to prevent instantiation from outside class
    private OkHttpClientSingleton() {

    }

    public static OkHttpClient getInstance() {
        if (instance == null) {
            // Synchronized block: Ensures thread safety, preventing multiple instances
            // from being created in a multi-threaded environment.
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
