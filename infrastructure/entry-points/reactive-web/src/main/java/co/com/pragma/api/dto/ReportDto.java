package co.com.pragma.api.dto;

public record ReportDto (
        String id,
        String status,
        Long count,
        Double totalAmount,
        String updatedAt
){
}
