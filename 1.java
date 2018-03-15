//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sankuai.qa.testCaseBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Capabilities {
    public static final String PLATFORM_NAME = "platformName";
    public static final String ANDROID = "Android";
    public static final String IOS = "iOS";
    private static final String PROTERTIES_FILE_NAME = "app.properties";
    private static ThreadLocal<Properties> tl = new ThreadLocal();
    private static String[] capasKeys = new String[]{"automationName", "platformName", "platformVersion", "deviceName", "app", "browserName", "newCommandTimeout", "language", "locale", "udid", "orientation", "autoWebview", "noReset", "fullReset", "appActivity", "appPackage", "appWaitActivity", "appWaitPackage", "deviceReadyTimeout", "androidCoverage", "enablePerformanceLogging", "androidDeviceReadyTimeout", "androidInstallTimeout", "adbPort", "androidDeviceSocket", "avd", "avdLaunchTimeout", "avdReadyTimeout", "avdArgs", "useKeystore", "keystorePath", "keystorePassword", "keyAlias", "keyPassword", "chromedriverExecutable", "autoWebviewTimeout", "intentAction", "intentCategory", "intentFlags", "optionalIntentArguments", "dontStopAppOnReset", "unicodeKeyboard", "resetKeyboard", "noSign", "ignoreUnimportantViews", "disableAndroidWatchers", "chromeOptions", "recreateChromeDriverSessions", "nativeWebScreenshot", "androidScreenshotPath", "calendarFormat", "bundleId", "udid", "launchTimeout", "locationServicesEnabled", "locationServicesAuthorized", "autoAcceptAlerts", "autoDismissAlerts", "nativeInstrumentsLib", "nativeWebTap", "safariInitialUrl", "safariAllowPopups", "safariIgnoreFraudWarning", "safariOpenLinksInBackground", "keepKeyChains", "localizableStringsDir", "processArguments", "interKeyDelay", "showIOSLog", "sendKeyStrategy", "screenshotWaitTimeout", "waitForAppScript", "webviewConnectRetries", "appName", "xcodeConfigFile", "keychainPath", "keychainPassword", "scaleFactor", "usePrebuiltWDA", "wdaConnectionTimeout", "wdaLaunchTimeout", "useNewWDA", "preventWDAAttachments"};
    private static Set<String> capas;

    public Capabilities() {
    }

    public static DesiredCapabilities getCapabilities(String capaName) {
        Properties properties = getProperties(capaName);
        HashMap map = new HashMap(properties);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        map.entrySet().stream().filter((entry) -> {
            return capas.contains(entry.getKey());
        }).forEach((entry) -> {
            capabilities.setCapability((String)entry.getKey(), (String)entry.getValue());
        });
        return capabilities;
    }

    public static DesiredCapabilities getCapabilities() {
        return getCapabilities((String)null);
    }

    public static String getUrl(String capaName) {
        Properties properties = getProperties(capaName);
        return String.format("http://127.0.0.1:%s/wd/hub", new Object[]{properties.getProperty("port")});
    }

    public static String getUrl() {
        return getUrl((String)null);
    }

    public static Properties getProperties(String capaName) {
        Properties properties = (Properties)tl.get();
        if(capaName == null || "".equals(capaName)) {
            capaName = "app.properties";
        }

        if(properties == null) {
            properties = new Properties();

            try {
                properties.load(new FileInputStream(capaName));
            } catch (IOException var3) {
                var3.printStackTrace();
            }

            tl.set(properties);
        }

        return properties;
    }

    static {
        capas = new HashSet(Arrays.asList(capasKeys));
    }
}
