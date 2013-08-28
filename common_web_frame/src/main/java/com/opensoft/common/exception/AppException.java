/**
 * ClassName: AppException
 * CopyRight: OpenSoft
 * Date: 12-12-24
 * Version: 1.0
 */
package com.opensoft.common.exception;

/**
 * Description : 统一异常
 * User : 康维
 */
public class AppException extends Exception {
    public AppException() {
        super();
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }
}
