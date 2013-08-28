/**
 * ClassName: AppRuntimeException
 * CopyRight: OpenSoft
 * Date: 12-12-24
 * Version: 1.0
 */
package com.opensoft.common.exception;

/**
 * Description : 统一运行时异常
 * User : 康维
 */
public class AppRuntimeException extends RuntimeException {
    public AppRuntimeException() {
        super();
    }

    public AppRuntimeException(String message) {
        super(message);
    }

    public AppRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppRuntimeException(Throwable cause) {
        super(cause);
    }
}
