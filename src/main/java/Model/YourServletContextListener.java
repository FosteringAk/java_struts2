package Model;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class YourServletContextListener implements ServletContextListener {

   @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Initialize the tracer when the servlet context is initialized
        OpenTracingInitializer.initializeGlobalTracer("Structs2-Login");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Clean up resources when the servlet context is destroyed
        // For example, you might want to unregister the tracer here
    }
}