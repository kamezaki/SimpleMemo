package biz.info_cloud.simplememo.domain.exception;

public class ItemNotFoundException extends DomainException {
    public ItemNotFoundException() {
    }

    public ItemNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    public ItemNotFoundException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ItemNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
