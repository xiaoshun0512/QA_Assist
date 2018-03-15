//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sankuai.qa.testCaseBase;

import com.sankuai.qa.testCaseBase.Capabilities;
import com.sankuai.qa.testCaseBase.log.Logger;
import com.sankuai.qa.testCaseBase.utils.FileUtil;
import com.sankuai.qa.testCaseBase.utils.ScreenShootUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class TestCase {
    public static final String CASE_START_MARK = "[APPIUM_CASE_START]";
    public static final String CASE_END_MARK = "[APPIUM_CASE_END]";
    private static final String IOS_CRASH_TMPE_DIR = "./result/iOSCrashTemp/";
    private static final String IOS_CRASH_ALL_Name = "./test-output/crash";
    private static final String IOS_CRASH_DIR = "./result/iOSCrash/";
    private static final String CRASH_MARK_FILE = "./result/crashMark.info";
    private static final String CLASS_NAME = "className";
    private static final String METHOD_NAME = "methodName";
    private static final String CONSOLE = "Console";
    private static final String SUITE_END_TAG = "error or warning";
    public static AppiumDriver<WebElement> driver;

    public TestCase() {
    }

    public static void initDriver(String capFileName) {
        DesiredCapabilities capabilities = Capabilities.getCapabilities(capFileName);
        int tryCnt = 3;
        Logger.info("start to init driver. tryCnt = " + tryCnt);

        while(tryCnt-- > 0) {
            Logger.info("tryCnt = " + tryCnt);
            closeDriver();

            try {
                if("iOS".equals(capabilities.getCapability("platformName"))) {
                    Logger.info("creating iOS driver");
                    driver = new IOSDriver(new URL(Capabilities.getUrl()), capabilities);
                } else {
                    Logger.info("creating Android driver");
                    driver = new AndroidDriver(new URL(Capabilities.getUrl()), capabilities);
                }

                Logger.info("session id :" + driver.getSessionId());
                if(driver.getSessionId() != null) {
                    TimeUnit.SECONDS.sleep(5L);
                    break;
                }
            } catch (Exception var5) {
                Logger.error(var5.getMessage(), var5);
            }

            try {
                TimeUnit.SECONDS.sleep(70L);
            } catch (InterruptedException var4) {
                Logger.error(var4.getMessage(), var4);
            }
        }

        if(driver != null && driver.getSessionId() != null) {
            Logger.info("建立会话连接成功");
        } else {
            Logger.info("建立会话连接失败");
        }

    }

    public static void initDriver() {
        initDriver((String)null);
    }

    public static void closeDriver() {
        try {
            if(driver != null) {
                Logger.info("try to close driver");
                driver.quit();
                Logger.info("after close driver, sessionId = " + driver.getSessionId());
            }
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }

    public void shootScreenIfFailed(ITestResult testResult) {
        this.shootScreenIfFailed(testResult, (String)null);
    }

    public void shootScreenIfFailed(ITestResult testResult, String filename) {
        if(!testResult.isSuccess()) {
            String className = testResult.getInstanceName();
            String dir = className + "/" + testResult.getMethod().getMethodName() + "/";
            ScreenShootUtil.shootScreen(driver, dir, filename);
        }

    }

    public boolean shoot() {
        return ScreenShootUtil.shootScreen(driver);
    }

    public boolean shoot(boolean condition) {
        return condition && ScreenShootUtil.shootScreen(driver);
    }

    public boolean shoot(String fileName) {
        return ScreenShootUtil.shootScreen(driver, fileName);
    }

    public boolean shoot(boolean condition, String fileName) {
        return condition && ScreenShootUtil.shootScreen(driver, fileName);
    }

    public void shootForUi(String pageName) {
        ScreenShootUtil.shootScreenForUI(driver, pageName);
    }

    public void getSysLog(ITestResult testResult) {
        try {
            Logger.getSyslog(driver, testResult);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void getCrashLog(ITestResult testResult) {
        try {
            Logger.getCrashlog(driver, testResult);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    @BeforeMethod(
        alwaysRun = true
    )
    public void printCaseStartTag() {
        System.out.println("[APPIUM_CASE_START]");
    }

    @AfterMethod(
        alwaysRun = true
    )
    public void printCaseEndTag(ITestResult iTestResult) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("className", iTestResult.getInstanceName());
        jsonObject.put("methodName", iTestResult.getMethod().getMethodName());
        System.out.println("[APPIUM_CASE_END]" + jsonObject.toString());
    }

    @AfterMethod(
        alwaysRun = true
    )
    public void getIOSCrashFile(ITestResult iTestResult) {
        Properties prop = Capabilities.getProperties((String)null);
        if("iOS".equals(prop.getProperty("platformName"))) {
            String className = iTestResult.getInstanceName();
            String methodName = iTestResult.getMethod().getMethodName();
            File crashDir = new File("./result/iOSCrash/", String.format("%s/%s", new Object[]{className, methodName}));
            if(!crashDir.exists()) {
                crashDir.mkdirs();
            }

            File iOSCrashTemp = (new File("./result/iOSCrashTemp/")).getAbsoluteFile();
            if(!iOSCrashTemp.exists()) {
                iOSCrashTemp.mkdirs();
            }

            File iOSCrashName = new File("./test-output/crash" + System.currentTimeMillis() + ".txt");
            File iOSCrashNameDir = iOSCrashName.getParentFile();
            if(!iOSCrashNameDir.exists()) {
                iOSCrashNameDir.mkdirs();
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String cmd = String.format("idevicecrashreport -u %s -e %s", new Object[]{prop.getProperty("udid"), iOSCrashTemp.getAbsolutePath()});

            try {
                Process e = Runtime.getRuntime().exec(cmd);
                e.waitFor();
                File[] allCrash = iOSCrashTemp.listFiles();
                FileWriter fileWriter = null;

                File[] iOSCrashFiles;
                int var16;
                try {
                    fileWriter = new FileWriter(iOSCrashName);
                    if(allCrash != null) {
                        iOSCrashFiles = allCrash;
                        int var15 = allCrash.length;

                        for(var16 = 0; var16 < var15; ++var16) {
                            File crash = iOSCrashFiles[var16];
                            fileWriter.write(crash.getName() + format.format(new Date(crash.lastModified())));
                            fileWriter.write(System.lineSeparator());
                        }
                    }
                } catch (Exception var27) {
                    var27.printStackTrace();
                } finally {
                    if(fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (Exception var26) {
                            var26.printStackTrace();
                        }
                    }

                }

                iOSCrashFiles = iOSCrashTemp.listFiles((pathname) -> {
                    String filename = pathname.getName();
                    return filename.startsWith(prop.getProperty("applicationName"));
                });
                File[] var30 = iOSCrashFiles;
                var16 = iOSCrashFiles.length;

                for(int var31 = 0; var31 < var16; ++var31) {
                    File file = var30[var31];
                    File outFile = new File(crashDir, file.getName());
                    Files.copy(file.toPath(), outFile.toPath(), new CopyOption[0]);
                }

                FileUtil.deleteDir(iOSCrashTemp);
            } catch (Exception var29) {
                var29.printStackTrace();
            }

        }
    }

    @BeforeSuite(
        alwaysRun = true
    )
    public void clearIOSCrash() {
        Properties prop = Capabilities.getProperties((String)null);
        if("iOS".equals(prop.getProperty("platformName"))) {
            File iOSCrashTemp = (new File("./result/iOSCrashTemp/")).getAbsoluteFile();
            if(!iOSCrashTemp.exists()) {
                iOSCrashTemp.mkdirs();
            }

            String cmd = String.format("idevicecrashreport -u %s -e %s", new Object[]{prop.getProperty("udid"), iOSCrashTemp.getAbsolutePath()});

            try {
                Process e = Runtime.getRuntime().exec(cmd);
                e.waitFor();
                FileUtil.deleteDir(iOSCrashTemp);
            } catch (IOException var5) {
                var5.printStackTrace();
            } catch (InterruptedException var6) {
                var6.printStackTrace();
            }

        }
    }

    @AfterSuite(
        alwaysRun = true
    )
    public void printSuiteEndTag() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("className", "Console");
        jsonObject.put("methodName", "error or warning");
        System.out.println("[APPIUM_CASE_END]" + jsonObject.toString());
    }

    @AfterSuite(
        alwaysRun = true
    )
    public void markCrash() {
        Set set = this.findCrash();
        File file = new File("./result/crashMark.info");
        FileWriter writer = null;

        try {
            writer = new FileWriter(file);
            Iterator e = set.iterator();

            while(e.hasNext()) {
                String s = (String)e.next();
                writer.write(s);
                writer.write(System.lineSeparator());
            }
        } catch (IOException var14) {
            var14.printStackTrace();
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException var13) {
                    var13.printStackTrace();
                }
            }

        }

    }

    private Set<String> findCrash() {
        HashSet set = new HashSet();
        List fileList = this.getAllFile(new File("./result/iOSCrash/"));
        Iterator var3 = fileList.iterator();

        while(var3.hasNext()) {
            File file = (File)var3.next();
            String className = file.getParentFile().getParentFile().getName();
            String methodName = file.getParentFile().getName();
            set.add(className + "." + methodName);
        }

        return set;
    }

    private List<File> getAllFile(File file) {
        ArrayList list = new ArrayList();
        if(!file.exists()) {
            return list;
        } else {
            if(file.isFile()) {
                list.add(file);
            } else if(file.isDirectory()) {
                File[] files = file.listFiles();
                if(files == null) {
                    return list;
                }

                File[] var4 = files;
                int var5 = files.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    File subFile = var4[var6];
                    list.addAll(this.getAllFile(subFile));
                }
            }

            return list;
        }
    }
}
