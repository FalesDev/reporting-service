package co.com.pragma.sqs.sender.adapter;

import co.com.pragma.model.dailyreport.DailyReport;
import co.com.pragma.model.dailyreport.gateways.DailyReportGateway;
import co.com.pragma.sqs.sender.SQSSender;
import co.com.pragma.sqs.sender.factory.SqsMessageFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DailyReportAdapter implements DailyReportGateway {

    private final SQSSender sqsSender;
    private final SqsMessageFactory messageFactory;

    @Value("${queue.names.notifications}")
    private String notificationQueue;

    @Override
    public Mono<Void> sendDailyReport(DailyReport payload) {
        var attributes = Map.of(
                "eventType", "DAILY_APPROVED_APPLICATION_REPORT"
        );

        return sqsSender.send(
                notificationQueue,
                messageFactory.toJson(payload),
                messageFactory.buildAttributes(attributes)
        ).then();
    }
}
