package com.airguardnet.device.infrastructure.web;

import com.airguardnet.common.web.ApiResponse;
import com.airguardnet.device.application.usecase.IngestReadingCommand;
import com.airguardnet.device.application.usecase.IngestReadingResult;
import com.airguardnet.device.application.usecase.IngestReadingUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/readings")
public class ReadingController {
    private final IngestReadingUseCase ingestReadingUseCase;

    public ReadingController(IngestReadingUseCase ingestReadingUseCase) {
        this.ingestReadingUseCase = ingestReadingUseCase;
    }

    @PostMapping("/ingest")
    public ResponseEntity<ApiResponse<IngestResponse>> ingest(@Valid @RequestBody ReadingIngestRequest request) {
        IngestReadingCommand command = new IngestReadingCommand();
        command.setDeviceUid(request.getDeviceUid());
        command.setPm1(request.getPm1());
        command.setPm25(request.getPm25());
        command.setPm10(request.getPm10());
        command.setBatteryLevel(request.getBatteryLevel());
        command.setTimestamp(request.getTimestamp());
        IngestReadingResult result = ingestReadingUseCase.ingest(command);
        IngestResponse response = new IngestResponse(result.getReadingId(), result.getDeviceId(), result.getRiskIndex(), result.getAirQualityPercent(), result.getDeviceStatus(), result.getCreatedAlert());
        return ResponseEntity.status(201).body(ApiResponse.success(response));
    }
}
