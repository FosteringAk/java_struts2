package Model;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerTracer;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentelemetry.api.trace.Tracer;
import io.opentracing.util.GlobalTracer;

public class OpenTracingInitializer implements ServletContextListener {   
     public static JaegerTracer initTracer(String service) {
        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv()
                                            .withType(ConstSampler.TYPE)
                                            .withParam(1);
        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv()
                                            .withLogSpans(true);

        Configuration config = new Configuration(service)
                                    .withSampler(samplerConfig)
                                    .withReporter(reporterConfig);

        return config.getTracer();
    }

  
    public static void initializeGlobalTracer(String serviceName) {
        JaegerTracer tracer = initTracer(serviceName);
        GlobalTracer.register(tracer);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        initializeGlobalTracer("Java_Struct2-Login");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Clean up resources if needed
    }
    

}
