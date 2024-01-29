package Model;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

@Aspect
public class TracingAspect {

    @Before("execution(* Controller.DeleteController.*(..))")
    public void beforeControllerMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String methodSignature = joinPoint.getSignature().toString();
        System.out.println("Before method execution: " + methodSignature);

        Tracer tracer = GlobalTracer.get();
        Span span = tracer.buildSpan(methodName).start();
        tracer.activateSpan(span);
    }

    @After("execution(* Controller.DeleteController.*(..))")
    public void afterControllerMethodExecution(JoinPoint joinPoint) {
        String methodSignature = joinPoint.getSignature().toString();
        System.out.println("After method execution: " + methodSignature);

        Tracer tracer = GlobalTracer.get();
        tracer.activeSpan().finish();
    }
}
