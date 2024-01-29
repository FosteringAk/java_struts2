package Model;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import java.io.IOException;

@WebFilter("/*")
public class RequestTracingFilter implements Filter {



    private Tracer tracer;

 
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code here
        tracer = GlobalTracer.get();

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String path = httpRequest.getRequestURI();

        // Start a span for the incoming HTTP request
        Span span = GlobalTracer.get().buildSpan("HTTP Request").start();

        try (Scope scope = GlobalTracer.get().activateSpan(span)) {
            // Tag the span with relevant information
            span.setTag("http.method", httpRequest.getMethod());
            span.setTag("http.url", path);

            // Proceed with the request
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            // Close the span
            span.finish();
        }
    }

    @Override
    public void destroy() {
        // Cleanup code here
    }
}
