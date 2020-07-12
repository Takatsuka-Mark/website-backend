package com.takatsuka.web.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MathLogger {
    private static final StackWalker WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static Logger forCallingClass(){
        return LoggerFactory.getLogger(WALKER.getCallerClass());
    }
}
