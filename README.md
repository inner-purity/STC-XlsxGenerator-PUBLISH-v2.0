# STC-XlsxGenerator-PUBLISH-v2.0
 This is an Excel report automatic generation plug-in developed for China | National College Student Software Testing Competition to improve the efficiency of filling out test report forms.

---

 这是一个Excel自动生成插件，目的是让我们的测试报告表格书写和代码完美结合，而不必要在写完代码后再去书写测试报告。因为在写完测试用例之后，我们立即可以通过方便的注解来书写我们对指定测试用例的描述，在思维上变得更加连贯，书写效率上也有一定提高。总体使用起来丝滑连贯。
 
### 使用方法
使用起来很简单，以下方式你任选其一：
 - 1. 直接下载整个代码包，随后在here_to_write_your_test中书写你的测试类。随后即可使用插件提供的全部注解。在书写完你的代码之后，运行 GeneratorApplication.java ，几秒钟后，你将会在控制台看到插件运行产生的详细的日志输出。随后，一张测试报告表格就在你的项目文件夹下生成了。
 - 2. 下载本代码包到本地，在本项目文件夹内运行:
      
   ```
   mvn clean install
   ```
   
   随后在你的比赛项目文件的pom.xml文件中，导入如下配置：
   
   ```
   <groupId>com.innerpurity</groupId>
   <artifactId>STC-XlsxGenerator</artifactId>
   <version>1.2</version>
   ```
   
   随后即可使用插件提供的全部注解。在书写完你的代码之后，在你的项目根目录，也就是src下的com.xxx顶级目录下，创建一个 GeneratorApplication.java文件，复制以下内容：
   
   ```
   /**
    * STC-XlsxGenerator 生成器主启动类，点击此处运行程序
    */
   public class GeneratorApplication {
       //运行此代码以启动生成器
       public static void main(String[] args) {
           STCXslxGenerator.start();
       }
   }
   ```

   运行它！几秒钟后，你将会在控制台看到插件运行产生的详细的日志输出。随后，一张测试报告表格就在你的项目文件夹下生成了。

### 重点提示
1. 因为每次比赛Excel的字段列名称可能都不太一样，所以此插件不能保证每次比赛都生成正确的表格，仅做参考使用。你也可以根据本场比赛全新的表格规则来改造源码，以达到你的目的。
2. 本插件仅作为本人兴趣开发的项目，不对任何人的比赛成绩和比赛结果生成做担保，一切因使用本插件产生的后果都应由使用者自行承担，本人概不负责
3. 有兴趣的同学也可以对此源码做改造优化，有问题随时在Issues区反馈，我看到了有空就会及时更新。
