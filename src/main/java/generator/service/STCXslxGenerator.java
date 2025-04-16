package generator.service;

import cn.hutool.core.util.StrUtil;
import com.google.gson.internal.LinkedHashTreeMap;
import generator.annotation.CaseProperties;
import generator.annotation.ExcludeThis;
import generator.constant.ExceptionConstant;
import generator.constant.TitleIndexConstant;
import generator.po.TestCase;
import generator.utils.PropertiesUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.xssf.usermodel.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * STC-XlsxGenerator
 *
 * @author inner-purity
 * @version 1.0
 * @date 2024/11/15
 * @description STC-XlsxGenerator 生成器主功能类
 */
public class STCXslxGenerator {
    public static void start() {
        //程序运行时长计时——start
        long startMillis = System.currentTimeMillis();
        System.out.println(ExceptionConstant.START_REMIND);


        // 读取generator.properties配置文件
        PropertiesUtils.load("generator.properties");
        // 获取测试类
        Class<?> testClass;
        try {
            testClass = Class.forName(PropertiesUtils.properties.get("java_test_file_path"));
        } catch (Exception e) {
            throw new RuntimeException(ExceptionConstant.NULL_REFERENCE_EXCEPTION + e);
        }
        // 获取Excel工作簿
        XSSFWorkbook workbook;
        try {
            // 获取模板文件路径
            Path path = Paths.get(PropertiesUtils.properties.get("template_xlsx_path"));
            workbook = new XSSFWorkbook(Files.newInputStream(path));
        } catch (Exception e) {
            throw new RuntimeException(
                    ExceptionConstant.XSSF_WORKBOOK_CREATE_FAILURE + e);
        }

        //获取第一张表单，一般是第一张
        XSSFSheet sheet = workbook.getSheetAt(0);
        //获取表头信息
        XSSFRow titleRow = sheet.getRow(0);
        //将各索引值匹配记录到常量类中，方便后续使用
        ConvertIndexToConstant(titleRow);
        // 设置表格边框样式
        XSSFCellStyle cellBorderThinStyle = workbook.createCellStyle();
        cellBorderThinStyle.setBorderTop(BorderStyle.THIN);
        cellBorderThinStyle.setBorderBottom(BorderStyle.THIN);
        cellBorderThinStyle.setBorderLeft(BorderStyle.THIN);
        cellBorderThinStyle.setBorderRight(BorderStyle.THIN);

        //获取目标类中所有的方法
        Method[] methods = testClass.getDeclaredMethods();
        //存储测试方法
        List<Method> testMethods = new LinkedList<>();
        //筛选测试方法，排除无关方法
        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                testMethods.add(method);
            }
        }
        //对筛选结果按测试编号尾号顺序排序
        testMethods.sort((o1, o2) -> {
            String[] o1_str = o1.getName().split("_");
            Integer o1_int = Integer.valueOf(o1_str[o1_str.length - 1].substring(1));
            String[] o2_str = o2.getName().split("_");
            Integer o2_int = Integer.valueOf(o2_str[o2_str.length - 1].substring(1));
            return o1_int - o2_int;
        });
        // 存储测试用例
        // 存储格式为： <需求编号, 测试用例对象>
        Map<String, List<TestCase>> testCaseMap = new LinkedHashTreeMap<>();

        for (Method testMethod : testMethods) {
            //如果不是手动排除的方法，且拥有@CaseProperties注解，则进行解析
            if (!testMethod.isAnnotationPresent(ExcludeThis.class) &&
                    testMethod.isAnnotationPresent(CaseProperties.class)) {
                //测试用例编号列表
                List<TestCase> testCaseList = new LinkedList<>();
                //原始测试方法名切割数组
                // 如：[test, 12306, R001]
                String[] originalNameArray = testMethod.getName().split("_");
                // 解析@CaseProperties注解
                CaseProperties caseProperties = testMethod.getAnnotation(CaseProperties.class);
                //非参数化测试方法
                if (!testMethod.isAnnotationPresent(ParameterizedTest.class) ||
                        !testMethod.isAnnotationPresent(CsvSource.class)) {
                    //截取需求编号
                    String testRequireNum = originalNameArray[originalNameArray.length - 1];
                    // 拼接测试用例编号： test_12306_..._R001 -> 12306_..._R001_001
                    StringBuilder testCaseNumBuilder = new StringBuilder();
                    for (int i = 1; i < originalNameArray.length; i++) {
                        testCaseNumBuilder.append(originalNameArray[i]).append("_");
                    }
                    String testCaseNum = testCaseNumBuilder
                            .append(StrUtil.fillBefore("1", '0', 3))
                            .toString();
                    //初始化测试用例模板对象
                    TestCase testCase = new TestCase();
                    // 获取需求编号
                    testCase.setRequireNum(testRequireNum);
                    // 获取测试用例编号
                    testCase.setCaseNum(testCaseNum);
                    // 获取模块名称
                    testCase.setModuleName(caseProperties.moduleName());
                    // 获取用例说明
                    testCase.setCaseDescription(caseProperties.caseDescription()[0]);
                    // 获取前置条件
                    testCase.setPreCondition(caseProperties.preCondition());
                    // 获取执行步骤
                    testCase.setExecuteStep(caseProperties.executeStep());
                    // 获取输入数据
                    testCase.setInputData(caseProperties.inputData()[0]);
                    // 获取预期结果
                    testCase.setExpectResult(caseProperties.expectResult()[0]);
                    // 获取实际结果
                    testCase.setActualResult(caseProperties.actualResult()[0]);
                    testCaseList.add(testCase);
                    testCaseMap.put(testRequireNum, testCaseList);
                    // 参数化测试方法
                } else {
                    CsvSource csvSource = testMethod.getAnnotation(CsvSource.class);
                    String[] csvArgs = csvSource.value();
                    //截取需求编号
                    String testRequireNum = originalNameArray[originalNameArray.length - 1];
                    for (int i = 0; i < csvArgs.length; i++) {
                        // 初始化对象容器
                        TestCase testCase = new TestCase();
                        // 拼接测试用例编号： test_12306_..._R001 -> [12306_..._R001_001, 12306_..._R001_002,...]
                        StringBuilder testCaseNumBuilder = new StringBuilder();
                        for (int j = 1; j < originalNameArray.length; j++) {
                            testCaseNumBuilder.append(originalNameArray[j]).append("_");
                        }
                        String testCaseNum = testCaseNumBuilder
                                .append(StrUtil.fillBefore(String.valueOf(i + 1), '0', 3))

                                .toString();
                        // 获取需求编号
                        testCase.setRequireNum(testRequireNum);
                        // 获取测试用例编号
                        testCase.setCaseNum(testCaseNum);
                        // 解析模板，替换指定模板字符串，默认${}
                        // 截取本次要替换的 CSV 索引列
                        String[] values = csvArgs[i].split(String.valueOf(csvSource.delimiter()));
                        // 读取配置文件中指定的占位符字符串
                        String templateReplaceStr = PropertiesUtils.properties.get("template_replace_string");

                        // 完成String类型属性中占位符的替换
                        // 获取模块名称
                        testCase.setModuleName(replaceTemplate(caseProperties.moduleName(), values, templateReplaceStr));
                        // 获取前置条件
                        testCase.setPreCondition(replaceTemplate(caseProperties.preCondition(), values, templateReplaceStr));
                        // 获取执行步骤
                        testCase.setExecuteStep(replaceTemplate(caseProperties.executeStep(), values, templateReplaceStr));
                        // 完成String[]类型属性中占位符的替换
                        // 获取用例说明
                        if (caseProperties.caseDescription().length > 0) {
                            if (i == 0 &&
                                    caseProperties.caseDescription().length != 1 &&
                                    csvArgs.length != caseProperties.caseDescription().length) {
                                System.out.println("【代码检查-WARN】：检测到你的方法： " + testMethod.getName() +
                                        " 是参数化测试方法，但 [caseDescription] 条数和@CsvSource参数数量不匹配, 如果不是有意为之请检查代码.\n" +
                                        "（CSV参数数量：" + csvArgs.length +
                                        " ，用例说明数量：" + caseProperties.caseDescription().length + ")\n");
                            }
                            if (caseProperties.caseDescription().length == 1) {
                                testCase.setCaseDescription(replaceTemplate(caseProperties.caseDescription()[0], values, templateReplaceStr));
                            } else {
                                if (i < caseProperties.caseDescription().length) {
                                    testCase.setCaseDescription(replaceTemplate(caseProperties.caseDescription()[i], values, templateReplaceStr));
                                }
                            }
                        }
                        // 获取输入数据
                        if (caseProperties.inputData().length > 0) {
                            if (i == 0 &&
                                    caseProperties.inputData().length != 1 &&
                                    (csvArgs.length != caseProperties.inputData().length)) {
                                System.out.println("【代码检查-WARN】：检测到你的方法： " + testMethod.getName() +
                                        " 是参数化测试方法，但 [inputData] 条数和@CsvSource参数数量不匹配, 如果不是有意为之请检查代码.\n" +
                                        "（CSV参数数量：" + csvArgs.length +
                                        " ，输入数据数量：" + caseProperties.inputData().length + ")\n");
                            }
                            if (caseProperties.inputData().length == 1) {
                                testCase.setInputData(replaceTemplate(caseProperties.inputData()[0], values, templateReplaceStr));
                            } else {
                                if (i < caseProperties.inputData().length) {
                                    testCase.setInputData(replaceTemplate(caseProperties.inputData()[i], values, templateReplaceStr));
                                }
                            }
                        }
                        // 获取预期结果
                        if (caseProperties.expectResult().length > 0) {
                            if (i == 0 &&
                                    caseProperties.expectResult().length != 1 &&
                                    (csvArgs.length != caseProperties.expectResult().length)) {
                                System.out.println("【代码检查-WARN】：检测到你的方法： " + testMethod.getName() +
                                        " 是参数化测试方法，但 [expectResult] 条数和@CsvSource参数数量不匹配, 如果不是有意为之请检查代码.\n" +
                                        "（CSV参数数量：" + csvArgs.length +
                                        " ，输入数据数量：" + caseProperties.expectResult().length + ")\n");
                            }
                            if (caseProperties.expectResult().length == 1) {
                                testCase.setExpectResult(replaceTemplate(caseProperties.expectResult()[0], values, templateReplaceStr));
                            } else {
                                if (i < caseProperties.expectResult().length) {
                                    testCase.setExpectResult(replaceTemplate(caseProperties.expectResult()[i], values, templateReplaceStr));
                                }
                            }
                        }
                        // 获取实际结果
                        if (caseProperties.actualResult().length > 0) {
                            if (i == 0 &&
                                    caseProperties.actualResult().length != 1 &&
                                    (csvArgs.length != caseProperties.actualResult().length)) {
                                System.out.println("【代码检查-WARN】：检测到你的方法： " + testMethod.getName() +
                                        " 是参数化测试方法，但 [actualResult] 条数和@CsvSource参数数量不匹配, 如果不是有意为之请检查代码.\n" +
                                        "（CSV参数数量：" + csvArgs.length +
                                        " ，输入数据数量：" + caseProperties.actualResult().length + ")\n");
                            }
                            if (caseProperties.actualResult().length == 1) {
                                testCase.setActualResult(replaceTemplate(caseProperties.actualResult()[0], values, templateReplaceStr));
                            } else {
                                if (i < caseProperties.actualResult().length) {
                                    testCase.setActualResult(replaceTemplate(caseProperties.actualResult()[i], values, templateReplaceStr));
                                }
                            }
                        }
                        testCaseList.add(testCase);
                    }
                    testCaseMap.put(testRequireNum, testCaseList);
                }
            } else {
                if (!testMethod.isAnnotationPresent(ExcludeThis.class))
                    System.out.println("【代码检查-WARN】:" + testMethod.getName() +
                            "你可能遗漏了该方法，为保证生成Excel文件的完整性，请稍后确认。" +
                            "如果你不需要为此方法生成测试用例，可以在方法上加 @ExcludeThis 来排除该方法。\n");
            }
        }

        //获取截图文件名列表
        List<String> screenshotFileNameList = getScreenshotFileNameList();
        // 遍历 testCaseMap, 为其中用例赋值截图名称
        if (!screenshotFileNameList.isEmpty()) {
            testCaseMap.forEach((k, v) -> {
                v.forEach(testCase -> {
                    // 获取用例编号
                    String testCaseNum = testCase.getCaseNum();
                    String[] strings = testCaseNum.split("_");

                    // 获取截图文件名后缀
                    String endsPattern = strings[strings.length - 2] + "_" + strings[strings.length - 1] + ".png";

                    // 遍历截图文件名列表，查找匹配项
                    screenshotFileNameList.forEach(s -> {
                        // 如果截图文件名匹配，则设置截图文件名
                        if (s.endsWith(endsPattern)) {
                            testCase.setScreenShotFileName(s);  // 设置截图文件名
                        }
                    });
                });
            });
        }else {
                System.out.println(ExceptionConstant.SCREENSHOT_FOLDER_READ_FAILURE);
        }

        // 遍历用例集合，写入到excel中
        writeDataToExcel(sheet, cellBorderThinStyle, testCaseMap);

        try {
            Path newExcelPath = Paths.get(
                    PropertiesUtils.properties.get("generate_xlsx_path") +
                            "STCXslxGenerator" + "_" + System.currentTimeMillis() + ".xlsx");
            workbook.write(Files.newOutputStream(newExcelPath));
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException(
                    ExceptionConstant.XSSF_WORKBOOK_WRITE_FAILURE + e);
        }

        System.out.println(ExceptionConstant.END_REMIND);
        System.out.println(ExceptionConstant.END_REMIND_TITLE);
        //程序运行时长计时——end
        long endMillis = System.currentTimeMillis();
        System.out.println((endMillis - startMillis) / 1000 > 0
                ? "耗时：" + (endMillis - startMillis) / 1000 + "秒"
                : "耗时：" + (endMillis - startMillis) + "毫秒");
    }

    /**
     * 写入数据到excel中
     *
     * @param sheet           工作簿
     * @param cellBorderStyle 边框样式
     * @param testCaseMap     用例集合
     */
    private static void writeDataToExcel(XSSFSheet sheet, XSSFCellStyle cellBorderStyle, Map<String, List<TestCase>> testCaseMap) {
        AtomicInteger totalRow = new AtomicInteger(1);
        testCaseMap.forEach((testRequireNum, testCaseList) -> {
            for (TestCase testCase : testCaseList) {
                XSSFRow curRow = sheet.createRow(totalRow.getAndAdd(1));

                XSSFCell testCaseNumCell = curRow.createCell(TitleIndexConstant.testCaseNumIdx);
                testCaseNumCell.setCellValue(testCase.getCaseNum());
                testCaseNumCell.setCellStyle(cellBorderStyle);

                XSSFCell testModuleNameCell = curRow.createCell(TitleIndexConstant.testModuleNameIdx);
                testModuleNameCell.setCellValue(testCase.getModuleName());
                testModuleNameCell.setCellStyle(cellBorderStyle);

                XSSFCell testRequireCell = curRow.createCell(TitleIndexConstant.testRequireNumIdx);
                testRequireCell.setCellValue(testRequireNum);
                testRequireCell.setCellStyle(cellBorderStyle);

                XSSFCell caseDescCell = curRow.createCell(TitleIndexConstant.caseDescIdx);
                caseDescCell.setCellValue(testCase.getCaseDescription());
                caseDescCell.setCellStyle(cellBorderStyle);

                XSSFCell preconditionsCell = curRow.createCell(TitleIndexConstant.preconditionsIdx);
                preconditionsCell.setCellValue(testCase.getPreCondition());
                preconditionsCell.setCellStyle(cellBorderStyle);

                XSSFCell execStepsCell = curRow.createCell(TitleIndexConstant.execStepsIdx);
                execStepsCell.setCellValue(testCase.getExecuteStep());
                execStepsCell.setCellStyle(cellBorderStyle);

                XSSFCell inputDataCell = curRow.createCell(TitleIndexConstant.inputDataIdx);
                inputDataCell.setCellValue(testCase.getInputData());
                inputDataCell.setCellStyle(cellBorderStyle);

                XSSFCell expectedResultCell = curRow.createCell(TitleIndexConstant.expectResultIdx);
                expectedResultCell.setCellValue(testCase.getExpectResult());
                expectedResultCell.setCellStyle(cellBorderStyle);

                XSSFCell actualResultCell = curRow.createCell(TitleIndexConstant.actualResultIdx);
                actualResultCell.setCellValue(testCase.getActualResult());
                actualResultCell.setCellStyle(cellBorderStyle);

                XSSFCell screenShotFileNameCell = curRow.createCell(TitleIndexConstant.screenShotFileNameIdx);
                screenShotFileNameCell.setCellValue(testCase.getScreenShotFileName());
                screenShotFileNameCell.setCellStyle(cellBorderStyle);
            }
        });
    }

    /**
     * 将各索引值匹配记录到常量类中，方便后续使用
     *
     * @param titleRow 表头行
     */
    private static void ConvertIndexToConstant(XSSFRow titleRow) {
        for (int i = 0; i < titleRow.getLastCellNum(); i++) {
            if ("测试用例编号".equals(titleRow.getCell(i).getStringCellValue())) {
                TitleIndexConstant.testCaseNumIdx = i;
            }
            if ("模块名称".equals(titleRow.getCell(i).getStringCellValue())) {
                TitleIndexConstant.testModuleNameIdx = i;
            }
            if ("需求编号".equals(titleRow.getCell(i).getStringCellValue())) {
                TitleIndexConstant.testRequireNumIdx = i;
            }
            if ("用例说明".equals(titleRow.getCell(i).getStringCellValue())) {
                TitleIndexConstant.caseDescIdx = i;
            }
            if ("前置条件".equals(titleRow.getCell(i).getStringCellValue())) {
                TitleIndexConstant.preconditionsIdx = i;
            }
            if ("执行步骤".equals(titleRow.getCell(i).getStringCellValue())) {
                TitleIndexConstant.execStepsIdx = i;
            }
            if ("输入数据".equals(titleRow.getCell(i).getStringCellValue())) {
                TitleIndexConstant.inputDataIdx = i;
            }
            if ("预期结果".equals(titleRow.getCell(i).getStringCellValue())) {
                TitleIndexConstant.expectResultIdx = i;
            }
            if ("实际结果".equals(titleRow.getCell(i).getStringCellValue())) {
                TitleIndexConstant.actualResultIdx = i;
            }
            if ("截图文件名".equals(titleRow.getCell(i).getStringCellValue())) {
                TitleIndexConstant.screenShotFileNameIdx = i;
            }
        }
    }

    /**
     * 获取所有截图文件名列表
     *
     * @return 截图文件名列表
     */
    private static List<String> getScreenshotFileNameList() {
        // 设置screenshots文件夹路径
        Path folderPath = Paths.get("screenshots");  // 假设 "screenshots" 文件夹在项目根目录下
        String globalPattern = "*.{png,jpg,jpeg}";
        // 获取所有图片文件并存储文件名
        List<String> imageFileNames = new LinkedList<>();
        try {
            imageFileNames = getFileNameList(folderPath, globalPattern);
        } catch (IOException ignored) {
        }
        return imageFileNames;
    }

    /**
     * 获取指定文件夹下的文件名列表
     *
     * @param folderPath    文件夹路径
     * @param globalPattern 全局文件名匹配模式
     * @return 文件名列表
     * @throws IOException IO异常
     */
    private static List<String> getFileNameList(Path folderPath, String globalPattern) throws IOException {
        List<String> fileNames = new ArrayList<>();
        // 使用Files.newDirectoryStream遍历文件夹内容
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, globalPattern)) {
            // 遍历流中的每个文件，获取文件名并添加到列表中
            for (Path entry : stream) {
                // 获取文件名并转换为字符串
                fileNames.add(entry.getFileName().toString());
            }
        } // 可以根据需要调整文件扩展名
        return fileNames;
    }

    /**
     * 替换占位符
     *
     * @param template
     * @param values
     * @param templateReplaceStr
     * @return
     */
    private static String replaceTemplate(String template, String[] values, String templateReplaceStr) {
        Pattern pattern = Pattern.compile(Pattern.quote(templateReplaceStr));
        for (String value : values) {
            Matcher matcher = pattern.matcher(template);
            if (matcher.find()) {
                template = matcher.replaceFirst(Matcher.quoteReplacement(value));
            }
        }
        return template;
    }
}
