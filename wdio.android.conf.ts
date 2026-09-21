/// <reference types="@wdio/types" />

import { config as baseConfig } from "./wdio.conf.js";

export const config: WebdriverIO.Config = {
  ...baseConfig,

  // Override capabilities for Android
  capabilities: [
    {
      // capabilities for local Appium native app tests on Android Emulator
      platformName: "Android",
      "appium:deviceName": "Pixel 9 Pro",
      "appium:platformVersion": "36",
      "appium:automationName": "UiAutomator2",
      "appium:udid": "emulator-5554",
      "appium:app": "/Users/roychow/IdeaProjects/EFSG_auto/src/main/resources/com.emperorfs.ebltrading.android_uat-0.0.303-0909.apk.zip",
      "appium:appPackage": "com.emperorfs.ebltrading.android",
      "appium:appActivity": ".MainActivity",
      "appium:newCommandTimeout": 600,
      "appium:noReset": false,
    },
  ],
};
