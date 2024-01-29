package jaeger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

@Aspect
public class SpanAdd {

    public final Tracer trace;

    public SpanAdd(Tracer trace) {
        this.trace = GlobalTracer.get();
    }

    @Pointcut(value = "@within(jaeger.CustomSpanned)")
    private void spanClass() {

    }

    @Pointcut(value = "execution(* *(..))")
    private void spanMethod() {

    }

    @Around(value = "spanClass() && spanMethod()")
    private Object addSpan(ProceedingJoinPoint point) throws Throwable {

        //log.info("Span Start");
        MethodSignature signature = (MethodSignature) point.getSignature();
        Span span = trace.buildSpan(point.getSignature().getDeclaringType().getName() + "." + signature.getName())
                .start();
        trace.activateSpan(span);
        Object next = point.proceed();
        span.finish();
       // log.info("Span Finished");

        return next;
    }

}
