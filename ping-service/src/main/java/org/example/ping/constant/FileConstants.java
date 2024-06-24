package org.example.ping.constant;

import java.io.File;

/**
 * 文件类常量
 *
 * @author Michael
 * @version 2024/6/23
 */
public class FileConstants {

    private FileConstants() {

    }

    public static final String USER_HOME_DIR = System.getProperty("user.home");

    public static final File WORKSPACE_DIR = new File(USER_HOME_DIR, ".code-challenge");

    public static final File LOCK_FILE_DIR = new File(WORKSPACE_DIR, "lock");
    public static final File LOCK_FILE_1 = new File(LOCK_FILE_DIR, "lock1");
    public static final File LOCK_FILE_2 = new File(LOCK_FILE_DIR, "lock2");

    public static final File LOG_FILE_DIR = new File(WORKSPACE_DIR, "log");

    public static File logFile;

    public static void setLogFile(String port) {
        logFile = new File(LOG_FILE_DIR, "ping-" + port + ".log");
    }

    public static File getLogFile() {
        return logFile;
    }
}
