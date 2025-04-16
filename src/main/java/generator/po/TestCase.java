package generator.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * STC-XlsxGenerator
 * @author inner-purity
 * @version 1.0
 * @date  2024/11/15
 * @description 测试用例对象模型
 */
@Data
@NoArgsConstructor
public class TestCase {
    // 测试用例编号 
    private String caseNum;
    // 模块名称 
    private String moduleName;
    // 需求编号 
    private String requireNum;
    // 用例说明 
    private String caseDescription;
    // 前置条件 
    private String preCondition;
    // 执行步骤 
    private String executeStep;
    // 输入数据 
    private String inputData;
    // 预期结果 
    private String expectResult;
    // 实际结果 
    private String actualResult;
    // 截图文件名 
    private String screenShotFileName;
}
