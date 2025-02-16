package pt.com.springboot.api.error.details;


import java.time.LocalDateTime;

public class ResourceNotFoundDetails extends ErrorDetails {

    public static final class Builder {
        private String title;
        private int status;
        private String detail;
        private String timestamp;
        private String transactionId;

        private Builder() {
        }

        public static ResourceNotFoundDetails.Builder newBuilder() {
            return new ResourceNotFoundDetails.Builder();
        }

        public ResourceNotFoundDetails.Builder title(String title) {
            this.title = title;
            return this;
        }

        public ResourceNotFoundDetails.Builder status(int status) {
            this.status = status;
            return this;
        }

        public ResourceNotFoundDetails.Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public ResourceNotFoundDetails.Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ResourceNotFoundDetails.Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public ResourceNotFoundDetails build() {
            ResourceNotFoundDetails resourceNotFoundDetails = new ResourceNotFoundDetails();
            resourceNotFoundDetails.setTitle(title);
            resourceNotFoundDetails.setStatus(status);
            resourceNotFoundDetails.setDetail(detail);
            resourceNotFoundDetails.setTimestamp(timestamp);
            resourceNotFoundDetails.setTransactionId(transactionId);
            return resourceNotFoundDetails;
        }
    }
}
