package pt.com.springboot.api.error.details;

public class BadRequestDetails extends ErrorDetails {

    public static final class Builder {
        private String title;
        private int status;
        private String detail;
        private String timestamp;
        private String transactionId;

        private Builder() {
        }

        public static BadRequestDetails.Builder newBuilder() {
            return new BadRequestDetails.Builder();
        }

        public BadRequestDetails.Builder title(String title) {
            this.title = title;
            return this;
        }

        public BadRequestDetails.Builder status(int status) {
            this.status = status;
            return this;
        }

        public BadRequestDetails.Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public BadRequestDetails.Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public BadRequestDetails.Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public BadRequestDetails build() {
            BadRequestDetails badRequestDetails = new BadRequestDetails();
            badRequestDetails.setTitle(title);
            badRequestDetails.setStatus(status);
            badRequestDetails.setDetail(detail);
            badRequestDetails.setTimestamp(timestamp);
            badRequestDetails.setTransactionId(transactionId);
            return badRequestDetails;
        }
    }
}
