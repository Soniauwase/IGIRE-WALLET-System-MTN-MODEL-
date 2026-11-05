package LAB2;

import LAB1.ProcessedRequest;

import java.sql.SQLException;
import java.util.List;


public interface ProcessedRequestInterfaceDAO {
    void createProcessedRequestTable()throws SQLException;
    boolean saveProcessedRequest(String reference_id) throws SQLException;
    Object findProcessedRequestByReferenceId(String reference_id) throws SQLException;
    List<ProcessedRequest> displayAllProcessedRequests() throws SQLException;
    boolean updateProcessedRequest(String reference_id, String new_status) throws SQLException;
    boolean deleteProcessedRequest(String reference_id) throws SQLException;
    boolean existsByReferenceId(String reference_id) throws SQLException;
}
