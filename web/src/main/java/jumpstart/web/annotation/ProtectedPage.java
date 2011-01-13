// Based on http://wiki.apache.org/tapestry/Tapestry5HowToControlAccess
// When you apply this @ProtectedPage annotation to any page class that you want 

package jumpstart.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the class is a "protected page", one that must not be accessible by users that are not logged in.
 * This annotation is applied to a Tapestry page class. The protection is provided by {@link PageProtectionFilter}. 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProtectedPage {
}
