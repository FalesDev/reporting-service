package co.com.pragma.model.report.gateways;

import co.com.pragma.model.report.Report;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReportRepository {
    Mono<Report> save(Report report);
    Mono<Report> findById(String id);
}
