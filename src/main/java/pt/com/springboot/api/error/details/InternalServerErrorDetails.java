package pt.com.springboot.api.error.details;

import java.time.LocalDateTime;

public class InternalServerErrorDetails extends ErrorDetails {

    public static final class Builder {
        private String title;
        private int status;
        private String detail;
        private String timestamp;
        private String transactionId;

        private Builder() {
        }

        public static InternalServerErrorDetails.Builder newBuilder() {
            return new InternalServerErrorDetails.Builder();
        }

        public InternalServerErrorDetails.Builder title(String title) {
            this.title = title;
            return this;
        }

        public InternalServerErrorDetails.Builder status(int status) {
            this.status = status;
            return this;
        }

        public InternalServerErrorDetails.Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public InternalServerErrorDetails.Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public InternalServerErrorDetails.Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public InternalServerErrorDetails build() {
            InternalServerErrorDetails internalServerErrorDetails = new InternalServerErrorDetails();
            internalServerErrorDetails.setTitle(title);
            internalServerErrorDetails.setStatus(status);
            internalServerErrorDetails.setDetail(detail);
            internalServerErrorDetails.setTimestamp(timestamp);
            internalServerErrorDetails.setTransactionId(transactionId);
            return internalServerErrorDetails;
        }
    }
}
