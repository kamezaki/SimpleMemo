package biz.info_cloud.simplememo.domain.exception;

public class DomainException extends Exception {
    public DomainException() {
    }

    public DomainException(String detailMessage) {
        super(detailMessage);
    }

    public DomainException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DomainException(Throwable throwable) {
        super(throwable);
    }
}
