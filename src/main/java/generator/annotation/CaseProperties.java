package generator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author inner-purity
 * @version 1.0
 * @description 测试用例属性注解
 * @date 2024/11/22 14:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CaseProperties {
    /**
     * 模块名称-必填项
     *
     * @return String
     */
    String moduleName();

    /**
     * 用例说明-必填项, 如果有多个用例说明，则用数组，与@CsvSource传入的数据量相等。
     * 如果整个模块都使用同一个用例说明则传字符串即可，程序会把这句话分配给模块下的每个测试用例
     *
     * @return String[]
     */
    String[] caseDescription();

    /**
     * 前置条件-必填项
     *
     * @return String
     */
    String preCondition();

    /**
     * 执行步骤-必填项
     *
     * @return String
     */
    String executeStep();

    /**
     * 输入数据-必填项, 输入数据与@CsvSource传入的数据量相等。
     * 如果只传一个值则整个模块都使用同一个输入数据，程序会把这句话分配给模块下的每个测试用例
     * @return String[]
     */
    String[] inputData();

    /**
     * 预期结果-必填项, 如果有多个预期结果，则用数组，与@CsvSource传入的数据量相等。
     * 如果整个模块都使用同一个预期结果则传字符串即可，程序会把这句话分配给模块下的每个测试用例
     *
     * @return String[]
     */
    String[] expectResult();

    /**
     * 实际结果-必填项, 如果有多个预期结果，则用数组，与@CsvSource传入的数据量相等。
     * 如果整个模块都使用同一个预期结果则传字符串即可，程序会把这句话分配给模块下的每个测试用例
     *
     * @return String[]
     */
    String[] actualResult();
}
