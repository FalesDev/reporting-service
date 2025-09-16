package co.com.pragma.usecase.updateapprovedcount;

import co.com.pragma.model.gateways.CustomLogger;
import co.com.pragma.model.report.Report;
import co.com.pragma.model.report.ReportApprovedCountMessage;
import co.com.pragma.model.report.gateways.ReportRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RequiredArgsConstructor
public class UpdateApprovedCountUseCase {

    private final ReportRepository reportRepository;
    private final CustomLogger logger;

    private static final String TOTAL_APPROVED_ID = "total_approved_requests";

    public Mono<Void> incrementApprovedRequestsCount(ReportApprovedCountMessage message) {

        if (!"Approved".equalsIgnoreCase(message.getState())) {
            logger.trace("Message with ApplicationId {} ignored. State = {}", message.getApplicationId(), message.getState());
            return Mono.empty();
        }

        return reportRepository.findById(TOTAL_APPROVED_ID)
                .flatMap(report -> {
                    logger.trace("Counter found. Current value: {}. Incrementing...", report.getCount());

                    Long currentCount = report.getCount() == null ? 0L : report.getCount();
                    Double currentTotalAmount = report.getTotalAmount() == null ? 0.0 : report.getTotalAmount();

                    report.setCount(currentCount + 1);
                    report.setTotalAmount(currentTotalAmount + message.getAmount());
                    report.setUpdatedAt(Instant.now());

                    return reportRepository.save(report);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    logger.trace("Counter not found. Creating new record with value 1.");
                    Report newReport = Report.builder()
                            .id(TOTAL_APPROVED_ID)
                            .status(message.getState())
                            .count(1L)
                            .totalAmount(message.getAmount())
                            .updatedAt(Instant.now())
                            .build();
                    return reportRepository.save(newReport);
                }))
                .doOnSuccess(report -> logger.trace("Counter updated successfully. New value: {}", report.getCount()))
                .then();
    }
}
