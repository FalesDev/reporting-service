package co.com.pragma.model.report;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReportApprovedCountMessage {
    private UUID applicationId;
    private Double amount;
    private String state;
}
