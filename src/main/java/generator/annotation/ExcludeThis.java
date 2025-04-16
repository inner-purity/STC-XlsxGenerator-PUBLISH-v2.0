package generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author inner-purity
 * @version 1.0
 * @description 手动排除此方法，不会为该方法生成测试用例
 * @date 2024/11/22 14:39
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExcludeThis {
}
