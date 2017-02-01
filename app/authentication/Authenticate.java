package authentication;

import play.mvc.With;

import java.lang.annotation.*;

/**
 * Created by rudy on 30/01/17.
 */
@With(ActionAuthentication.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface Authenticate {
    Class[] value() default {};
}
