package com.github.lfeagan.wheat;

import org.slf4j.Logger;

import java.util.function.BiFunction;

public final class CloseableUtils {

    public static <C extends AutoCloseable> void closeWithoutException(C closeable) {
        closeWithoutException(closeable, null);
    }

    public static <C extends AutoCloseable> void closeWithoutException(C closeable, Logger logger) {
        closeWithoutException(closeable, logger, (c,t) -> {
            if (c == null) {
                return "Attempt to close null object";
            } else {
                return "Exception thrown while closing";
            }
        });
    }

    public static <C extends AutoCloseable> void closeWithoutException(C closeable, Logger logger, BiFunction<C,Throwable,String> messageBuilder) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable throwable) {
                if (logger != null) {
                    final String message = messageBuilder.apply(closeable, throwable);
                    logger.error(message, throwable);
                } else {
                    // do nothing
                }
            }
        } else {
            if (logger != null) {
                final String message = messageBuilder.apply(closeable, null);
                logger.warn(message);
            } else {
                // do nothing
            }
        }
    }

}
