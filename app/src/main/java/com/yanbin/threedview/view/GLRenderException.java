package com.yanbin.threedview.view;


public class GLRenderException extends RuntimeException {

    public GLRenderException(String detailMessage) {
        super(detailMessage);
    }

    public GLRenderException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public GLRenderException(Throwable throwable) {
        super(throwable);
    }
}
