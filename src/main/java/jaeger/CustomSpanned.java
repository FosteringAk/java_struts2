package jaeger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.opentelemetry.api.trace.SpanKind;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CustomSpanned {


    SpanKind kind() default SpanKind.INTERNAL;

}

