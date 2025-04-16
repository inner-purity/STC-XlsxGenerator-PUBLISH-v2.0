//package here_to_write_your_test;
//
//
//import org.apache.commons.io.FileUtils;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.interactions.Actions;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//
//public class Test12306 {
//    private WebDriver driver;
//
//    @BeforeEach
//    public void setup() {
//        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--remote-allow-origins=*");
//        driver = new ChromeDriver(chromeOptions);
//        driver.get("https://www.12306.cn/index/index.html");
//        driver.manage().window().maximize();
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//    }
//
//    // test-code-start
//
//    //    需求1（R001）: 用户进入单程车票查询页面，输入有效出发地、目的地和出发日后，系统应能正确显示符合条件的车次列表。（用例数不超过10条）
////    输入数据要求：
////            - 出发地：仅考虑以下2个城市（北京、上海）；
////            - 目的地：仅考虑以下2个城市（广州、成都）；
////            - 出发日：根据预售期确定有效等价类并进行边界值分析；
//    @ParameterizedTest
//    @CsvSource({
//            "北京,广州,2024-10-26,12306_R001_001.png",
//            "北京,广州,2024-11-09,12306_R001_002.png",
//            "北京,广州,2024-11-06,12306_R001_003.png",
//            "上海,成都,2024-10-26,12306_R001_004.png",
//            "上海,成都,2024-11-09,12306_R001_005.png",
//            "上海,成都,2024-11-06,12306_R001_006.png",
//    })
//    @CaseDesc({
//            "本日车票的单程车票查询",
//            "本日车票的单程车票查询，不包含当日的票，不包含当日的票。",
//            "本日车票的单程车票查询，不包含当日的票，包含当日的票。",
//            "本日车票的单程车票查询，包含当日的票，不包含当日的票。",
//            "本日车票的单程车票查询，包含当日的票，包含当日的票。",
//            "本日车票的单程车票查询，不包含当日的票，不包含当日的票，包含当日的票"
//    })
//    @ModuleName("单程车票查询模块")
//    @PreCondition("成功打开12306，\n" +
//            "进入单程票查询模块")
//    @ExecStep("1、在【出发地输入框】中输入出发地名称；\n" +
//            "2、在【到达地输入框】中输入到达地名称；\n" +
//            "3、在【出发日期输入框】中输入出发日期；\n" +
//            "4、点击【查询按钮】。")
//    public void test_12306_R001(String from, String to, String fromTime, String screenShot) throws InterruptedException {
//        driver.switchTo().window(driver.getWindowHandles().toArray()[3].toString());
//
//        Actions actions = new Actions(driver);
//        Thread.sleep(1000);
//        actions.clickAndHold(driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/a"))).perform();
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/div/div[1]/ul/li[1]/a")).click();
//
//        WebElement fromStationText = driver.findElement(By.id("fromStationText"));
//        fromStationText.click();
//        fromStationText.clear();
//        fromStationText.sendKeys(from);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + from + "']")).click();
//
//        WebElement toStationText = driver.findElement(By.id("toStationText"));
//        toStationText.click();
//        toStationText.clear();
//        toStationText.sendKeys(to);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + to + "']")).click();
//
//        WebElement fromDateText = driver.findElement(By.id("train_date"));
//        fromDateText.clear();
//        fromDateText.sendKeys(fromTime + Keys.ENTER);
//
//        driver.findElement(By.xpath("/html/body/div[2]/div[7]/div[9]/form/div[3]/div/a")).click();
//        Thread.sleep(1000);
//        takeScreenshot(screenShot);
//    }
//
//    //- 需求2（R002）: 用户进入单程车票查询页面，用户输入合法出发地、合法目的地以及无效出发日时，系统应显示相应的错误提示信息。（用例数不超过3条）
////    输入数据要求：
////            - 出发地：输入任意合法出发地；
////            - 目的地：输入任意合法目的地；
////            - 出发日：根据预售期确定无效等价类并进行边界值分析；
//    @ParameterizedTest
//    @CsvSource({
//            "北京,广州,2024-1-1,12306_R002_001.png",
//            "北京,广州,2025-1-1,12306_R002_002.png",
//            "北京,广州,9999-13-13,12306_R002_003.png",
//    })
//    @ModuleName("单程车票查询模块")
//    @PreCondition("成功打开12306，\n" +
//            "进入单程票查询模块")
//    @ExecStep("1、在【出发地输入框】中输入出发地名称；\n" +
//            "2、在【到达地输入框】中输入到达地名称；\n" +
//            "3、在【出发日期输入框】中输入出发日期；\n" +
//            "4、点击【查询按钮】。")
//    public void test_12306_R002(String from, String to, String fromTime, String screenShot) throws InterruptedException {
//        Actions actions = new Actions(driver);
//        Thread.sleep(1000);
//        actions.clickAndHold(driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/a"))).perform();
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/div/div[1]/ul/li[1]/a")).click();
//
//        WebElement fromStationText = driver.findElement(By.id("fromStationText"));
//        fromStationText.click();
//        fromStationText.clear();
//        fromStationText.sendKeys(from);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + from + "']")).click();
//
//        WebElement toStationText = driver.findElement(By.id("toStationText"));
//        toStationText.click();
//        toStationText.clear();
//        toStationText.sendKeys(to);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + to + "']")).click();
//
//        WebElement fromDateText = driver.findElement(By.id("train_date"));
//        fromDateText.clear();
//        fromDateText.sendKeys(fromTime + Keys.ENTER);
//
//        driver.findElement(By.xpath("/html/body/div[2]/div[7]/div[9]/form/div[3]/div/a")).click();
//        Thread.sleep(1000);
//        takeScreenshot(screenShot);
//    }
//
//    //    -需求3（R003）:用户进入单程车票查询页面，出发地输入北京、目的地输入上海和出发日期输入2024-10-30，系统显示符合条件的车次列表，
////    在此基础上对查询结果进行筛选，筛选应能根据用户选择的车次类型、出发车站、到达车站和车次席别正确筛选结果。
////    通过鼠标点击的方式依次对车次类型、出发车站、到达车站和车次席别选中第一个可筛选项并取消选中的方式来验证。
//
//    @ParameterizedTest
//    @CsvSource({
//            "北京,上海,2024-10-30",
//    })
//    @ModuleName("单程车票查询模块")
//    @PreCondition("成功打开12306，\n" +
//            "进入单程票查询模块")
//    @ExecStep("1、在【出发地输入框】中输入出发地名称；\n" +
//            "2、在【到达地输入框】中输入到达地名称；\n" +
//            "3、在【出发日期输入框】中输入出发日期；\n" +
//            "4、点击【查询按钮】。")
//    public void test_12306_R003(String from, String to) throws InterruptedException {
//        Actions actions = new Actions(driver);
//        Thread.sleep(1000);
//        actions.clickAndHold(driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/a"))).perform();
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/div/div[1]/ul/li[1]/a")).click();
//
//        WebElement fromStationText = driver.findElement(By.id("fromStationText"));
//        fromStationText.click();
//        fromStationText.clear();
//        fromStationText.sendKeys(from);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + from + "']")).click();
//
//        WebElement toStationText = driver.findElement(By.id("toStationText"));
//        toStationText.click();
//        toStationText.clear();
//        toStationText.sendKeys(to);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + to + "']")).click();
//
//
//        WebElement fromDateText = driver.findElement(By.id("train_date"));
//        fromDateText.clear();
//        fromDateText.sendKeys(Keys.BACK_SPACE + "2024-10-30" + Keys.ENTER);
//
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("/html/body/div[2]/div[7]/div[9]/form/div[3]/div/a")).click();
//
//        driver.findElement(By.xpath("//label[text()='GC-高铁/城际']")).click();
//        Thread.sleep(1000);
//        takeScreenshot("12306_R003_001.png");
//        driver.findElement(By.xpath("//label[text()='GC-高铁/城际']")).click();
//
//        driver.findElement(By.xpath("//label[text()='北京南']")).click();
//        Thread.sleep(1000);
//        takeScreenshot("12306_R003_002.png");
//        driver.findElement(By.xpath("//label[text()='北京南']")).click();
//
//        driver.findElement(By.xpath("//label[text()='上海虹桥']")).click();
//        Thread.sleep(1000);
//        takeScreenshot("12306_R003_003.png");
//        driver.findElement(By.xpath("//label[text()='上海虹桥']")).click();
//
//        driver.findElement(By.xpath("//label[text()='商务座']")).click();
//        Thread.sleep(1000);
//        takeScreenshot("12306_R003_004.png");
//        driver.findElement(By.xpath("//label[text()='商务座']")).click();
//
//    }
//
//    //    - 需求4（R004）：用户进入往返车票查询页面，输入有效出发地、目的地、出发日、返程日后，系统应能正确显示符合条件的车次列表。（用例数不超过4条）
////            - 出发地：输入任意合法出发地；
////            - 目的地：输入任意合法目的地；
////            - 出发日：输入2024-11-01；
////            - 返程日：根据可选择的返程日确定有效等价类并进行边界值分析；
//    @ParameterizedTest
//    @CsvSource({
//            "北京,上海,2024-11-01,2024-11-01,12306_R004_001.png",
//            "北京,上海,2024-11-01,2024-11-02,12306_R004_002.png",
//            "北京,上海,2024-11-01,2024-11-08,12306_R004_002.png",
//            "北京,上海,2024-11-01,2024-11-09,12306_R004_003.png",
//    })
//    @ModuleName("往返票查询模块")
//    @PreCondition("成功打开12306，\n" +
//            "进入往返票查询模块")
//    @ExecStep("1、在【出发地输入框】中输入出发地名称；\n" +
//            "2、在【到达地输入框】中输入到达地名称；\n" +
//            "3、在【出发日期输入框】中输入出发日期；\n" +
//            "4、点击【查询按钮】。")
//    public void test_12306_R004(String from, String to, String fromTime, String backTime, String screenShot) throws InterruptedException {
//        Actions actions = new Actions(driver);
//        Thread.sleep(1000);
//        actions.clickAndHold(driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/a"))).perform();
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/div/div[1]/ul/li[2]/a")).click();
//
//        WebElement fromStationText = driver.findElement(By.id("fromStationText"));
//        fromStationText.click();
//        fromStationText.clear();
//        fromStationText.sendKeys(from);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + from + "']")).click();
//
//        WebElement toStationText = driver.findElement(By.id("toStationText"));
//        toStationText.click();
//        toStationText.clear();
//        toStationText.sendKeys(to);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + to + "']")).click();
//
//        WebElement fromDateText = driver.findElement(By.id("train_date"));
//        fromDateText.clear();
//        fromDateText.sendKeys(Keys.BACK_SPACE + fromTime + Keys.ENTER);
//
//        WebElement backDateText = driver.findElement(By.id("back_train_date"));
//        backDateText.clear();
//        backDateText.sendKeys(Keys.BACK_SPACE + backTime + Keys.ENTER);
//
//        driver.findElement(By.xpath("/html/body/div[2]/div[7]/div[9]/form/div[3]/div/a")).click();
//        Thread.sleep(1000);
//        takeScreenshot(screenShot);
//    }
//
//
//    //    - 需求5（R005）：用户进入往返车票查询页面，用户输入有效的信息（出发地、目的地、出发日）和无效的返程日，系统应显示相应的错误提示信息。（用例数不超过3条）
////            - 出发地：输入任意合法出发地；
////            - 目的地：输入任意合法目的地；
////            - 出发日：输入2024-11-02；
////            - 返程日：根据可选择的返程日确定无效等价类并进行边界值分析；
//    @ParameterizedTest
//    @CsvSource({
//            "北京,上海,2024-11-02,2024-10-30,12306_R005_001.png",
//            "北京,上海,2024-11-02,2024-11-20,12306_R005_002.png",
//            "北京,上海,2024-11-02,9999-13-13,12306_R005_003.png",
//    })
//    @ModuleName("往返票查询模块")
//    @PreCondition("成功打开12306，\n" +
//            "进入往返票查询模块")
//    @ExecStep("1、在【出发地输入框】中输入出发地名称；\n" +
//            "2、在【到达地输入框】中输入到达地名称；\n" +
//            "3、在【出发日期输入框】中输入出发日期；\n" +
//            "4、点击【查询按钮】。")
//    public void test_12306_R005(String from, String to, String fromTime, String backTime, String screenShot) throws InterruptedException {
//        Actions actions = new Actions(driver);
//        Thread.sleep(1000);
//        actions.clickAndHold(driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/a"))).perform();
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/div/div[1]/ul/li[2]/a")).click();
//
//        WebElement fromStationText = driver.findElement(By.id("fromStationText"));
//        fromStationText.click();
//        fromStationText.clear();
//        fromStationText.sendKeys(from);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + from + "']")).click();
//
//        WebElement toStationText = driver.findElement(By.id("toStationText"));
//        toStationText.click();
//        toStationText.clear();
//        toStationText.sendKeys(to);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + to + "']")).click();
//
//        WebElement fromDateText = driver.findElement(By.id("train_date"));
//        fromDateText.clear();
//        fromDateText.sendKeys(Keys.BACK_SPACE + fromTime + Keys.ENTER);
//
//        WebElement backDateText = driver.findElement(By.id("back_train_date"));
//        backDateText.clear();
//        backDateText.sendKeys(Keys.BACK_SPACE + backTime + Keys.ENTER);
//
//        driver.findElement(By.xpath("/html/body/div[2]/div[7]/div[9]/form/div[3]/div/a")).click();
//        Thread.sleep(1000);
//        takeScreenshot(screenShot);
//    }
//
//    //    - 需求6（R006）: 用户进入往返车票查询页面，使用车站选择控件的方式选择出发地（FGHIJ->福州南），
////    同样使用车站选择控件选择目的地（热门->厦门），出发日输入2024-10-30，返程日输入2024-11-06，选择学生票进行查询。
////    查询出结果后，在Tab中日期选择11-03周日，然后车次类型选择最后一个复选框，出发车站和到达车站选择全部，同时最后勾选显示折扣车次，截图查询结果。
//    @Test
//    public void test_12306_R006() throws InterruptedException {
//        Actions actions = new Actions(driver);
//        Thread.sleep(1000);
//        actions.clickAndHold(driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/a"))).perform();
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/div/div[1]/ul/li[2]/a")).click();
//
//        WebElement fromStationText = driver.findElement(By.id("fromStationText"));
//        fromStationText.click();
//        driver.findElement(By.xpath("/html/body/div[2]/div[10]/div/div/div[2]/div/div/ul[1]/li[3]")).click();
//        driver.findElement(By.xpath("/html/body/div[2]/div[10]/div/div/div[2]/div/div/div[3]/ul[1]/li[4]")).click();
//
//        WebElement toStationText = driver.findElement(By.id("toStationText"));
//        toStationText.click();
//        driver.findElement(By.xpath("/html/body/div[2]/div[10]/div/div/div[2]/div/div/ul[2]/li[33]")).click();
//
//        WebElement fromDateText = driver.findElement(By.id("train_date"));
//        fromDateText.clear();
//        fromDateText.sendKeys(Keys.BACK_SPACE + "2024-10-30" + Keys.ENTER);
//
//        WebElement backDateText = driver.findElement(By.id("back_train_date"));
//        backDateText.clear();
//        backDateText.sendKeys(Keys.BACK_SPACE + "2024-11-06" + Keys.ENTER);
//
//        // 选择学生票
//        driver.findElement(By.xpath("/html/body/div[2]/div[7]/div[9]/form/div[3]/ul/li[2]/input")).click();
//
//        // 进行查询
//        driver.findElement(By.xpath("/html/body/div[2]/div[7]/div[9]/form/div[3]/div/a")).click();
//        Thread.sleep(1000);
//
//        // 在Tab中日期选择11-03周日
//        driver.findElement(By.xpath("/html/body/div[3]/div[7]/div[10]/div[1]/ul/li[9]/span[1]")).click();
//        Thread.sleep(1000);
//
//        // 车次类型选择最后一个复选框
//        driver.findElement(By.xpath("/html/body/div[3]/div[7]/div[10]/div[2]/div[2]/div[2]/ul/li[8]/input")).click();
//
//        // 出发车站和到达车站选择全部
//        driver.findElement(By.xpath("/html/body/div[3]/div[7]/div[10]/div[2]/div[3]/div[2]/span")).click();
//        driver.findElement(By.xpath("/html/body/div[3]/div[7]/div[10]/div[2]/div[4]/div[2]/span")).click();
//
//        // 同时最后勾选显示折扣车次
//        driver.findElement(By.xpath("/html/body/div[3]/div[7]/div[11]/span/input[1]")).click();
//        Thread.sleep(1000);
//        // 截图查询结果
//        takeScreenshot("12306_R006_001.png");
//    }
//
//    //    需求7（R007）: 用户进入中转车票查询页面，输入有效出发地、目的地和乘车日期，同时不指定换乘站后进行查询，
////    系统应能正确显示符合条件的中转换乘车次列表。（用例数不超过10条）
////            - 出发地：仅考虑以下3个城市（哈尔滨、长春、安吉）；
////            - 目的地：仅考虑以下3个城市（济南、西安、拉萨）；
////            - 出发日：比赛当天日期的后一天；
//    @ParameterizedTest
//    @CsvSource({
//            "哈尔滨,济南,2024-10-27,12306_R007_001.png",
//            "长春,西安,2024-10-27,12306_R007_002.png",
//            "安吉,拉萨,2024-10-27,12306_R007_003.png",
//            "济南,哈尔滨,2024-10-27,12306_R007_001.png",
//            "西安,长春,2024-10-27,12306_R007_002.png",
//            "拉萨,安吉,2024-10-27,12306_R007_003.png"
//    })
//    public void test_12306_R007(String from, String to, String fromTime, String screenShot) throws InterruptedException {
//        Actions actions = new Actions(driver);
//        Thread.sleep(1000);
//        actions.clickAndHold(driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/a"))).perform();
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/div/div[1]/ul/li[3]/a")).click();
//
//        WebElement fromStationText = driver.findElement(By.id("fromStationText"));
//        fromStationText.click();
//        fromStationText.clear();
//        fromStationText.sendKeys(from);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + from + "']")).click();
//
//        WebElement toStationText = driver.findElement(By.id("toStationText"));
//        toStationText.click();
//        toStationText.clear();
//        toStationText.sendKeys(to);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + to + "']")).click();
//
//        WebElement fromDateText = driver.findElement(By.id("train_start_date"));
//        fromDateText.clear();
//        fromDateText.sendKeys(Keys.BACK_SPACE + fromTime + Keys.ENTER);
//
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/a")).click();
//        Thread.sleep(1000);
//        takeScreenshot(screenShot);
//    }
//
//    //    - 需求8（R008）: 用户进入中转车票查询页面，输入有效出发地、目的地和乘车日期后和无效换乘站，系统应显示相应的错误提示信息。（用例数不超过4条）
////            - 出发地：输入任意合法出发地；
////            - 目的地：输入任意合法目的地；
////            - 出发日：比赛当天日期的后一天；
////            - 换乘站：（为空、特殊字符、无效的换乘站）;
//    @ParameterizedTest
//    @CsvSource({
//            "哈尔滨,济南,2024-10-27,,12306_R008_001.png",
//            "哈尔滨,济南,2024-10-27,{}{}{},12306_R008_002.png",
//            "哈尔滨,济南,2024-10-27,万象,12306_R008_003.png",
//    })
//    public void test_12306_R008(String from, String to, String fromTime, String station, String screenShot) throws InterruptedException {
//        Actions actions = new Actions(driver);
//        Thread.sleep(1000);
//        actions.clickAndHold(driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/a"))).perform();
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/div/div[1]/ul/li[3]/a")).click();
//
//        WebElement fromStationText = driver.findElement(By.id("fromStationText"));
//        fromStationText.click();
//        fromStationText.clear();
//        fromStationText.sendKeys(from);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + from + "']")).click();
//
//        WebElement toStationText = driver.findElement(By.id("toStationText"));
//        toStationText.click();
//        toStationText.clear();
//        toStationText.sendKeys(to);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + to + "']")).click();
//
//        WebElement fromDateText = driver.findElement(By.id("train_start_date"));
//        fromDateText.clear();
//        fromDateText.sendKeys(Keys.BACK_SPACE + fromTime + Keys.ENTER);
//
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div/ul/li[4]/span/input")).click();
//
//        WebElement changeStationText = driver.findElement(By.id("changeStationText"));
//        changeStationText.click();
//        changeStationText.clear();
//        changeStationText.sendKeys(station + Keys.ENTER);
//
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/a")).click();
//        Thread.sleep(1000);
//        takeScreenshot(screenShot);
//    }
//
////    - 需求9（R009） ：用户进入中转车票查询页面，输入有效出发地、目的地和乘车日期后，
////    勾选指定换乘站并输入的换乘站点，系统应能显示通过指定换乘站的中转方案。（用例数不超过4条）
////            - 出发地：仅考虑以下1个城市（哈尔滨）；
////            - 目的地：仅考虑以下1个城市（南京）；
////            - 出发日：比赛当天日期的后一天；
////            - 换乘站：仅考虑以下3个城市（济南、安阳、周口）；
//
//    @ParameterizedTest
//    @CsvSource({
//            "哈尔滨,南京,2024-10-27,济南,12306_R009_001.png",
//            "哈尔滨,南京,2024-10-27,安阳,12306_R009_002.png",
//            "哈尔滨,南京,2024-10-27,周口,12306_R009_003.png",
//    })
//    public void test_12306_R009(String from, String to, String fromTime, String station, String screenShot) throws InterruptedException {
//        Actions actions = new Actions(driver);
//        Thread.sleep(1000);
//        actions.clickAndHold(driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/a"))).perform();
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/div/div[1]/ul/li[3]/a")).click();
//
//        WebElement fromStationText = driver.findElement(By.id("fromStationText"));
//        fromStationText.click();
//        fromStationText.clear();
//        fromStationText.sendKeys(from);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + from + "']")).click();
//
//        WebElement toStationText = driver.findElement(By.id("toStationText"));
//        toStationText.click();
//        toStationText.clear();
//        toStationText.sendKeys(to);
//        driver.findElement(By.xpath("//span[@class='ralign' and text()='" + to + "']")).click();
//
//        WebElement fromDateText = driver.findElement(By.id("train_start_date"));
//        fromDateText.clear();
//        fromDateText.sendKeys(Keys.BACK_SPACE + fromTime + Keys.ENTER);
//
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div/ul/li[4]/span/input")).click();
//
//        WebElement changeStationText = driver.findElement(By.id("changeStationText"));
//        changeStationText.click();
//        changeStationText.clear();
//        changeStationText.sendKeys(station + Keys.ENTER);
//
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/div/div[1]/a")).click();
//        Thread.sleep(1000);
//        takeScreenshot(screenShot);
//    }
//
//
//    //- 需求10（R010）: 用户进入计次·定期车票查询页面，输入有效出发地和目的地后，
//    // 系统应能正确显示符合条件的计次票和定期票列表。（用例数不超过5条，选手自由发挥）
//    @Test
//    public void test_12306_R010() throws InterruptedException {
//        Actions actions = new Actions(driver);
//        Thread.sleep(1000);
//        actions.clickAndHold(driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/a"))).perform();
//        Thread.sleep(1000);
//        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[2]/ul/li[2]/div/div[1]/ul/li[4]/a")).click();
//        Thread.sleep(60000);
//
//    }
//
//    @Test
//    public void test_12306_R011() throws InterruptedException {
//
//    }
//
//    // test-code-end
//
//    @AfterEach
//    public void teardown() {
//        this.driver.quit();
//    }
//
//    private void takeScreenshot(String fileName) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmssSSS");
//        String timestamp = dateFormat.format(new Date());
//        String timestampedFileName = timestamp + "_" + fileName;
//        File screenshotsDir = new File("screenshots");
//        if (!screenshotsDir.exists()) {
//            screenshotsDir.mkdirs();
//        }
//        String screenshotFilePath = screenshotsDir.getAbsolutePath() + File.separator + timestampedFileName;
//        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//        try {
//            FileUtils.copyFile(screenshotFile, new File(screenshotFilePath));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
