package LAB1;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.time.LocalDateTime;

    public class ProcessedRequest {
        private int id ;
        private String reference_id ;
        private LocalDateTime processed_at;

        public ProcessedRequest(int id, String reference_id, LocalDateTime processed_at) {
            this.id = id;
            this.reference_id = reference_id;
            this.processed_at = processed_at;
        }

        public ProcessedRequest() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getReference_id() {
            return reference_id;
        }

        public void setReference_id(String reference_id) {
            this.reference_id = reference_id;
        }

        public LocalDateTime getProcessed_at() {
            return processed_at;
        }

        public void setProcessed_at(LocalDateTime processed_at) {
            this.processed_at = processed_at;
        }

        @Override
        public String toString() {
            return "ProcessedRequest{" +
                    "id=" + id +
                    ", reference_id='" + reference_id + '\'' +
                    ", processed_at=" + processed_at +
                    '}';
        }
    }


