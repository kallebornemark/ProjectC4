package projectc4.c4.client;

import android.app.Application;

/**
 * Created by Erik on 2015-04-02.
 */
public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.
        initSingletons();
    }

    protected void initSingletons()
    {
        // Initialize the instance of MySingleton
        ClientController.initInstance();
    }

    public void customAppMethod()
    {
        // Custom application method
    }
}
