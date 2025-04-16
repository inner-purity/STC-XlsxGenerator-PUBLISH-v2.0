package generator.constant;

/**
 * @author inner-purity
 * @version 1.0
 * @description 错误常量
 * @date 2024/11/22 12:56
 */
public class ExceptionConstant {
    public static final String START_REMIND =
            "========================= 【STC-XlsxGenerator】 正常启动 =======================";
    public static final String NULL_REFERENCE_EXCEPTION =
            "\n【检测不到引用-ERROR】：请确认输入的.java测试文件引用是否正确!" +
                    "\n【方法】：你应该右键你的测试文件，在弹出框中点击“复制路径/引用”，之后选择“复制引用”" +
                    "\n以下是源错误信息供参考：\n";
    public static final String PROPERTIES_LOAD_FAILURE =
            "\n【generator.properties配置文件加载失败-ERROR】：请检查generator.properties配置文件是否正确！" +
                    "以下是源错误信息供参考：\n";
    public static final String XSSF_WORKBOOK_CREATE_FAILURE =
            "\n【XSSFWorkbook工作簿创建失败-ERROR】：" +
                    "\n1.请检查模板xlsx文件路径是否正确！可以尝试复制文件的绝对路径." +
                    "\n2.检查文件后缀是否为xlsx,请更改文件后缀为xlsx后重试." +
                    "\n以下是源错误信息供参考：\n";

    public static final String XSSF_WORKBOOK_WRITE_FAILURE =
            "\n【XSSFWorkbook工作簿写入失败-ERROR】：" +
                    "\n1.请检查模板xlsx文件路径是否正确！可以尝试复制文件的绝对路径." +
                    "\n2.检查文件后缀是否为xlsx,如果为xls旧版Excel会出错，请更改文件后缀为xlsx后重试." +
                    "\n以下是源错误信息供参考：\n";
    public static final String END_REMIND =
            "========================= Excel文件生成完毕 =======================\n" +
                    "\n你必须检查是否完全去除了相关注解: @CaseProperties 和引入语句 import ... 以免影响比赛成绩！\n" +
                    "\n【重要提醒-INFO】：每次重新进行自动化测试时，生成的截图文件名会有所不同，" +
                    "需要重新运行该程序来保证文件截图名为最新时间戳!\n" +
                    "\n【重要提醒-INFO】：在提交你最终的 java 文件时，需要去除文件中所有与此程序相关的 注解 和 import引入！\n" +
                    "\n【重要提醒-INFO】：在提交你最终的 java 文件时，确保你的WebDriver文件路径改回到官方源码提供的路径！\n";

    public static final String END_REMIND_TITLE =
            "=========================\n" +
                    "【STC-XlsxGenerator】 运行结束 , 上滑查看运行日志( 含 [代码检查] 及 [重要提醒] ) ！\n" +
                    "=======================";

    public static final String SCREENSHOT_FOLDER_READ_FAILURE =
            "【文件检查-WARN】: 本次生成的模板未成功注入截图文件名，有以下几种可能：" +
                    "\n【WARN】1.检查你的截图文件夹名称是否为：screenshots。" +
                    "\n【WARN】2.请确认是否已经生成了截图，生成后再使用此插件生成Excel" +
                    "\n【WARN】为保证生成的Excel文件完整性，请稍后确认";
}
