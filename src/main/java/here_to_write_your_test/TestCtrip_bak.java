package here_to_write_your_test;

import generator.annotation.*;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestCtrip_bak {
    private WebDriver driver;


    @BeforeEach
    public void setup() {
        //提交最终代码脚本时，请将驱动路径换回官方路径"C:\\Users\\86153\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe"
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(chromeOptions);
        driver.get("https://www.ctrip.com");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    // test-code-start


    @ParameterizedTest
    @CsvSource({
            "北京,成都,ctrip_R001_001.png",
            "北京,广州,ctrip_R001_002.png",
            "上海,成都,ctrip_R001_003.png",
            "上海,广州,ctrip_R001_004.png",
    })
    @CaseProperties(
            moduleName = "单程车票查询",
            caseDescription = {
                    "输入合法出发地目的地和出发日期",
                    "输入合法出发地目的地和出发日期",
                    "输入合法出发地目的地和出发日期",
                    "输入合法出发地目的地和出发日期"},
            preCondition = "成功打开携程网，\n" +
                    "用户进入单程车票查询页面",
            executeStep =
                    "输入有效出发城市、到达城市\n" +
                            "勾选【只搜高铁动车】选择框\n" +
                            "选择出发日期为2024-12-20。\n" +
                            "点击搜索进行查询\n",
            inputData = "出发地：${}\n" +
                    "目的地：${}\n",
            expectResult = "给出从${}到${}的出行方案",
            actualResult = "页面成功加载出从${}到${}的出行方案")
    public void test_Ctrip_R001(String from, String to, String screenShot) throws InterruptedException {

        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[1]/div")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div[1]/div/div[3]/button/span[2]")).click();

        WebElement fromStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[1]/input"));
        fromStation.click();
        fromStation.clear();
        fromStation.sendKeys(from);
        WebElement toStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        toStation.click();
        toStation.clear();
        toStation.sendKeys(to);
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[2]/ul[2]/li[20]")).click();

        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/button/text()")).click();

        Thread.sleep(1000);
        takeScreenshot(screenShot);
    }

    @ParameterizedTest
    @CsvSource({
            "${},ctrip_R002_001.png",
            "未知城市,ctrip_R002_002.png",
    })
    @CaseProperties(
            moduleName = "单程车票查询",
            caseDescription = {
                    "输入非法出发地，测试系统稳定性",
                    "输入非法出发地，测试系统稳定性",
                    "输入非法出发地，测试系统稳定性"},
            preCondition = "成功打开携程网，\n" +
                    "用户进入单程车票查询页面",
            executeStep = "输入无效出发城市\n" +
                    "勾选【只搜高铁动车】选择框\n" +
                    "选择出发日期为2024-12-20。\n" +
                    "点击搜索进行查询\n",
            inputData = "出发地：${}\n",
            expectResult = "显示错误提示信息",
            actualResult = {
                    "显示错误 ${} 提示信息",
                    "显示错误 ${} 提示信息",
                    "显示错误 ${} 提示信息",
            })
    public void test_Ctrip_R002(String from, String screenShot) throws InterruptedException {
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div[1]/div/div[3]/button/span[2]")).click();
        WebElement fromStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        fromStation.click();
        fromStation.clear();
        fromStation.sendKeys(from);

        Thread.sleep(1000);
        takeScreenshot(screenShot);
    }

    @CaseProperties(
            moduleName = "单程车票查询",
            caseDescription = {
                    "测试系统筛选功能1",
                    "测试系统筛选功能2",
                    "测试系统筛选功能3"},
            preCondition = "成功打开携程网，\n" +
                    "用户进入单程车票查询页面",
            executeStep = " 用户进入单程车票查询页面，出发城市输入北京、到达城市输入上海和出发日期选择2024-11-17，点击搜索进行查询，\n" +
                    "系统应能显示符合条件的车次列表，在此基础上对查询结果进行筛选。\n" +
                    "通过鼠标点击的方式依次对仅有票车次、车型选中第一个复选框，出发时间选中第二个复选框，并按照价格由高到低排序",
            inputData = "出发地：${}\n",
            expectResult = "显示错误提示信息",
            actualResult = {
                    "显示错误提示信息",
                    "显示错误 ${} 提示信息",
                    "显示错误提示信息",
            })
    @Test
    public void test_Ctrip_R003() throws InterruptedException {
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div[1]/div/div[3]/button/span[2]")).click();
        WebElement fromStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        fromStation.click();
        fromStation.clear();
        fromStation.sendKeys("北京");
        WebElement toStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        toStation.click();
        toStation.clear();
        toStation.sendKeys("上海");

        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[1]/strong[2]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]/ul[2]/li[22]")).click();

        WebElement searchButton = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/button"));
        searchButton.click();

        Thread.sleep(1000);

        //仅有票车次
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[1]/ul/li/strong")).click();
        //车型选中第一个复选框
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[2]/ul/li[2]/strong")).click();
        //出发时间选中第二个复选框
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[3]/ul/li[2]")).click();
        //并按照价格由高到低排序(点两次)
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[1]/div[1]/ul[2]/li[3]/div")).click();
        Thread.sleep(500);
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[1]/div[1]/ul[2]/li[3]/div")).click();

        takeScreenshot("test_Ctrip_R003_001");
    }

    @Test
    public void test_Ctrip_R004() throws InterruptedException {
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div[1]/div/div[3]/button/span[2]")).click();

        WebElement fromStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        fromStation.click();
        fromStation.clear();

        fromStation.sendKeys("南京");
        WebElement toStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        toStation.click();
        toStation.clear();
        toStation.sendKeys("广州");

        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[1]/strong[2]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]/ul[2]/li[22]")).click();

        WebElement searchButton = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/button"));
        searchButton.click();

        Thread.sleep(1000);

        //展开筛选列表
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[3]"));
        //通过鼠标点击的方式依次对仅有票车次、车型、出发时间、到达时间、坐席、出发车站、到达车站选中第一个可筛选项_7个筛选项
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[1]/ul/li/strong")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[2]/ul/li[1]/strong")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[3]/ul/li[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[4]/ul/li[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[5]/ul/li[1]/strong")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[6]/ul/li[1]/strong")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[7]/ul/li[1]/strong")).click();
        //并依次点击重置按钮取消选中_6个重置
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[2]/div/a")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[3]/div/a")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[4]/div/a")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[5]/div/a")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[6]/div/a")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[2]/div[7]/div/a")).click();
        //最后收起列表
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div[2]/div[1]/div[3]")).click();

        takeScreenshot("test_Ctrip_R004_001");
    }

    @ParameterizedTest
    @CsvSource({
            "济南,西安,ctrip_R005_001.png",
            "济南,杭州,ctrip_R005_002.png",
            "天津,西安,ctrip_R005_003.png",
            "天津,杭州,ctrip_R005_004.png",
    })
    @CaseProperties(
            moduleName = "单程车票查询",
            caseDescription = {
                    "测试系统筛选功能1",
                    "测试系统筛选功能2",
                    "测试系统筛选功能3"},
            preCondition = "成功打开携程网，\n" +
                    "用户进入单程车票查询页面",
            executeStep = " 用户进入单程车票查询页面，出发城市输入北京、到达城市输入上海和出发日期选择2024-11-17，点击搜索进行查询，\n" +
                    "系统应能显示符合条件的车次列表，在此基础上对查询结果进行筛选。\n" +
                    "通过鼠标点击的方式依次对仅有票车次、车型选中第一个复选框，出发时间选中第二个复选框，并按照价格由高到低排序",
            inputData = "出发地：${}\n" +
                    "目的地：${}\n",
            expectResult = "显示错误提示信息",
            actualResult = {
                    "显示错误提示信息",
                    "显示错误 ${} 提示信息",
                    "显示错误提示信息",
            })
    public void test_Ctrip_R005(String from, String to, String screenShot) throws InterruptedException {
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div[1]/div/div[3]/button/span[2]")).click();
        //通过点击【往返】按钮进入往返车票查询页面
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[1]/div/ul/li[2]/button")).click();
        //输入有效出发城市、到达城市，选择有效的出发日期、返回日期
        WebElement fromStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        fromStation.click();
        fromStation.clear();

        fromStation.sendKeys(from);
        WebElement toStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        toStation.click();
        toStation.clear();

        toStation.sendKeys(to);
        //选择有效出发日期、返回日期
        WebElement fromDate = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[1]"));
        fromDate.click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]/ul[2]/li[22]/strong")).click();

        WebElement toDate = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[2]"));
        toDate.click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]/ul[2]/li[25]/strong")).click();

        //点击搜索进行查询
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/button"));
        searchButton.click();
        Thread.sleep(1500);

        //在【去程】Tab中，通过鼠标点击的方式依次对车型、坐席、出发车站选中第一个复选框.出发时间、到达时间选中第二个复选框
        // 展开全部
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/div")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[1]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[4]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[5]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[2]/div[2]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[3]/div[2]")).click();

        Thread.sleep(1000);
        takeScreenshot(screenShot);
    }

    @ParameterizedTest
    @CsvSource({
            "兰州,郑州,ctrip_R006_001.png",
            "兰州,厦门,ctrip_R006_002.png",
            "徐州,郑州,ctrip_R006_003.png",
            "徐州,厦门,ctrip_R006_004.png",
    })
    public void test_Ctrip_R006(String from, String to, String screenShot) throws InterruptedException {
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div[1]/div/div[3]/button/span[2]")).click();
        //通过点击【往返】按钮进入往返车票查询页面
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[1]/div/ul/li[2]/button")).click();
        //输入有效出发城市、到达城市
        WebElement fromStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        fromStation.click();
        fromStation.clear();

        fromStation.sendKeys(from);
        WebElement toStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        toStation.click();
        toStation.clear();

        toStation.sendKeys(to);
        //选择有效出发日期、返回日期
        WebElement fromDate = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[1]"));
        fromDate.click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]/ul[2]/li[22]/strong")).click();

        WebElement toDate = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[2]"));
        toDate.click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]/ul[2]/li[25]/strong")).click();

        //点击搜索进行查询
        WebElement searchButton = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/button"));
        searchButton.click();
        Thread.sleep(1500);

        //在【返程】Tab中，通过鼠标点击的方式依次对车型、坐席、出发车站选中第一个复选框.出发时间、到达时间选中第二个复选框
        // 展开全部
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[3]/div[2]/div[3]/div")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[1]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[3]/div[2]/div[3]/ul/li[4]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[2]/div[2]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[3]/div[2]")).click();

        Thread.sleep(1000);
        takeScreenshot(screenShot);
    }

    @ParameterizedTest
    @CsvSource({
            "12306,海南,ctrip_R007_001.png",
            "#$%,海南,ctrip_R007_002.png",
            "全国大学生软件测试大赛,海南,ctrip_R007_003.png",
    })
    public void test_Ctrip_R007(String from, String to, String screenShot) throws InterruptedException {
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div[1]/div/div[3]/button/span[2]")).click();
        //用户通过点击【添加返程】按钮进入往返车票查询页面
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[2]")).click();

        WebElement fromStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[1]/input"));
        fromStation.click();
        fromStation.clear();

        fromStation.sendKeys(from);
        WebElement toStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        toStation.click();
        toStation.clear();

        toStation.sendKeys(to);
        //选择有效出发日期、返回日期
        WebElement fromDate = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[1]"));
        fromDate.click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]/ul[2]/li[23]/strong")).click();

        WebElement toDate = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[2]"));
        toDate.click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]/ul[2]/li[25]/strong")).click();

        WebElement searchButton = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/button"));
        searchButton.click();

        Thread.sleep(1000);
        takeScreenshot(screenShot);
    }

    @Test
    public void test_Ctrip_R008(String from, String to, String screenShot) throws InterruptedException {
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div[1]/div/div[3]/button/span[2]")).click();
        //通过点击【往返】按钮进入往返车票查询页面
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[1]/div/ul/li[2]/button")).click();

        //使用车站选择控件的方式选择出发城市（NPQRS->上海）
        WebElement fromStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[1]/input"));
        fromStation.click();
        fromStation.clear();

        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[2]/div/div[2]/ul[1]/li[5]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[2]/div/div[2]/ul[6]/li[6]")).click();

        //使用车站选择控件选择到达城市（热门选择->北京）
        WebElement toStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        toStation.click();
        toStation.clear();

        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[2]/div/div[2]/ul[1]/li[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[2]/div/div[2]/ul[2]/li[1]")).click();

        //选择有效出发日期、返回日期
        WebElement fromDate = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[1]"));
        fromDate.click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]/ul[2]/li[23]/strong")).click();

        WebElement toDate = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[2]"));
        toDate.click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]/ul[2]/li[25]/strong")).click();

        WebElement searchButton = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/button"));
        searchButton.click();
        Thread.sleep(1000);

//        在【去程】Tab中日期选择11-19周二
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/ul[1]/li[3]")).click();

//        然后出发时间全部勾选，到达时间、坐席、出发车站、到达车站选中第一个复选框
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[2]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[2]/div[2]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[2]/div[3]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[2]/div[4]")).click();
        //展开全部
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/div")).click();
        Thread.sleep(1500);
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[3]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[4]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[5]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/div[3]/ul/li[6]/div[1]")).click();

//        同时在【返程】Tab中，坐席、出发车站、到达车站选中第一个复选框
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[3]/div[2]/div[3]/div")).click();
        Thread.sleep(1500);
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[3]/div[2]/div[3]/ul/li[4]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[3]/div[2]/div[3]/ul/li[5]/div[1]")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[3]/div[2]/div[3]/ul/li[6]/div[1]")).click();

        Thread.sleep(1000);
        takeScreenshot(screenShot);
    }

    @ParameterizedTest
    @CsvSource({
            "哈尔滨,济南,ctrip_R009_001.png",
            "长春,西安,ctrip_R009_002.png",
            "安吉,拉萨,ctrip_R009_003.png",
            "哈尔滨,西安,ctrip_R009_004.png",
            "长春,拉萨,ctrip_R009_005.png",
            "安吉,济南,ctrip_R009_006.png",
    })
    public void test_Ctrip_R009(String from, String to, String screenShot) throws InterruptedException {
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div[1]/div/div[3]/button/span[2]")).click();
        //进入中转车票查询页面
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[1]/div/ul/li[3]")).click();

        WebElement fromStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[1]/input"));
        fromStation.click();
        fromStation.clear();

        fromStation.sendKeys(from);
        WebElement toStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        toStation.click();
        toStation.clear();

        toStation.sendKeys(to);
//        - 出发日期：比赛当天日期的后一天；
        WebElement dateTime = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[1]"));
        dateTime.click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]/ul[2]/li[22]/strong")).click();

        WebElement searchButton = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/button"));
        searchButton.click();

        Thread.sleep(2000);
        takeScreenshot(screenShot);
    }

    @ParameterizedTest
    @CsvSource({
            "哈尔滨,济南,123,ctrip_R010_001.png",
            "长春,西安,???,ctrip_R010_002.png",
            "安吉,拉萨,未知城市,ctrip_R010_003.png",
    })
    public void test_Ctrip_R010(String from, String to, String middle, String screenShot) throws InterruptedException {
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div[1]/div/div[3]/button/span[2]")).click();
        //进入中转车票查询页面
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[1]/div/ul/li[3]")).click();

        WebElement fromStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[1]/input"));
        fromStation.click();
        fromStation.clear();

        fromStation.sendKeys(from);
        WebElement toStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        toStation.click();
        toStation.clear();

        toStation.sendKeys(to);
        WebElement middleStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[2]/form/input"));
        middleStation.clear();
        middleStation.click();
        middleStation.sendKeys(middle);

        WebElement dateTime = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[1]"));
        dateTime.click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]/ul[2]/li[22]/strong")).click();

        WebElement searchButton = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/button"));
        searchButton.click();

        Thread.sleep(2000);
        takeScreenshot(screenShot);
    }

    @ParameterizedTest
    @CsvSource({
            "哈尔滨,南京,济南,ctrip_R011_001.png",
            "哈尔滨,南京,安阳,ctrip_R011_002.png",
            "哈尔滨,南京,周口,ctrip_R011_003.png",
    })
    public void test_Ctrip_R011(String from, String to, String middle, String screenShot) throws InterruptedException {

        driver.findElement(By.xpath("/html/body/div[1]/div/div/div/div[2]/div/div[1]/div/div[3]/button/span[2]")).click();
        //进入中转车票查询页面
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[1]/div/ul/li[3]")).click();

        WebElement fromStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[1]/input"));
        fromStation.click();
        fromStation.clear();

        fromStation.sendKeys(from);
        WebElement toStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[3]/div/form[2]/input"));
        toStation.click();
        toStation.clear();

        toStation.sendKeys(to);
        WebElement middleStation = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[2]/form/input"));
        middleStation.clear();
        middleStation.click();
        middleStation.sendKeys(middle);

//        - 出发日期：比赛当天日期的后两天；
        WebElement dateTime = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[4]/div[1]/div[1]"));
        dateTime.click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/div[1]/div/div[2]/div[1]/ul[2]/li[23]/strong")).click();

        WebElement searchButton = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div/div[2]/div[1]/div[2]/button"));
        searchButton.click();
        Thread.sleep(2000);

        //对查询结果按照运行时长进行降序排序_点两次
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/ul[2]/li[2]/div")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[1]/div[2]/ul[2]/li[2]/div")).click();

        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[3]/div[2]/ul[2]/li[2]/div")).click();
        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div/div[3]/div/div[3]/div[2]/ul[2]/li[2]/div")).click();

        Thread.sleep(1000);
        takeScreenshot(screenShot);
    }

    // test-code-end


    @AfterEach
    public void teardown() {
        this.driver.quit();
    }


    private void takeScreenshot(String fileName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmssddSSS");
        String timestamp = dateFormat.format(new Date());
        String timestampedFileName = timestamp + "_" + fileName;
        File screenshotsDir = new File("screenshots");
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdirs();
        }
        String screenshotFilePath = screenshotsDir.getAbsolutePath() + File.separator + timestampedFileName;
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshotFile, new File(screenshotFilePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
