package org.example.ping.lock;

import org.example.ping.constant.FileConstants;
import org.example.ping.log.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;


/**
 * @author Michael
 * @version 2024/6/23
 */
public class RateLimitLock {

    static {
        final File lockFileDir = FileConstants.LOCK_FILE_DIR;
        if (!lockFileDir.exists()) {
            lockFileDir.mkdir();
        }

        final Consumer<File> consumer = file -> {
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        consumer.accept(FileConstants.LOCK_FILE_1);
        consumer.accept(FileConstants.LOCK_FILE_2);
    }

    private RateLimitLock() {

    }

    public static void tryLock(Consumer<FileLock> successConsumer, Consumer<Exception> failConsumer) {
        try {
            try {
                final FileOutputStream fileOutputStream1 = new FileOutputStream(FileConstants.LOCK_FILE_1);
                final FileChannel fileChannel1 = fileOutputStream1.getChannel();
                final FileLock tryLock1 = fileChannel1.tryLock();
                if (Objects.nonNull(tryLock1)) {
                    successConsumer.accept(tryLock1);
                    return;
                }
            } catch (OverlappingFileLockException ex) {
                Logger.log("其他线程已经获取文件锁了");
            }

            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(FileConstants.LOCK_FILE_2);
                final FileChannel fileChannel2 = fileOutputStream2.getChannel();
                final FileLock tryLock2 = fileChannel2.tryLock();
                if (Objects.nonNull(tryLock2)) {
                    successConsumer.accept(tryLock2);
                    return;
                }
            } catch (OverlappingFileLockException ignore) {
                Logger.log("其他线程已经获取文件锁了");
            }
        } catch (IOException ex) {
            failConsumer.accept(ex);
        }

        failConsumer.accept(new NullPointerException());
    }


    public static void releaseLock(FileLock fileLock) {
        Optional.ofNullable(fileLock).ifPresent(lock -> {
            try {
                lock.release();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
