package com.loftschool.ozaharenko.loftcoin19.log;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import timber.log.Timber;

//Timber logger highly recommended to used to log outputs in console/tomcat
//is able to send long size messages with proper formatting
public class LoftTree extends Timber.DebugTree {

    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        final Thread thread = Thread.currentThread();
        super.log(priority, tag, String.format(Locale.US,
                "[%s] %s",
                thread.getName(),
                message
        ), t);
    }
}
