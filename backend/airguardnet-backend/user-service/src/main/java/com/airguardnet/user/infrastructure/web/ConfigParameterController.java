package com.airguardnet.user.infrastructure.web;

import com.airguardnet.common.web.ApiResponse;
import com.airguardnet.user.application.usecase.ListConfigParametersUseCase;
import com.airguardnet.user.application.usecase.UpsertConfigParameterUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/config-parameters")
public class ConfigParameterController {

    private final ListConfigParametersUseCase listConfigParametersUseCase;
    private final UpsertConfigParameterUseCase upsertConfigParameterUseCase;

    public ConfigParameterController(ListConfigParametersUseCase listConfigParametersUseCase,
                                     UpsertConfigParameterUseCase upsertConfigParameterUseCase) {
        this.listConfigParametersUseCase = listConfigParametersUseCase;
        this.upsertConfigParameterUseCase = upsertConfigParameterUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ConfigParameterDTO>>> listAll() {
        List<ConfigParameterDTO> parameters = listConfigParametersUseCase.execute()
                .stream()
                .map(ConfigParameterDTO::fromDomain)
                .collect(Collectors.toList());

        ApiResponse<List<ConfigParameterDTO>> response = ApiResponse.success(parameters);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{key}")
    public ResponseEntity<ApiResponse<ConfigParameterDTO>> upsert(@PathVariable String key,
                                                                   @RequestBody ConfigParameterRequest request) {
        ConfigParameterDTO dto = ConfigParameterDTO.fromDomain(
                upsertConfigParameterUseCase.execute(key, request.getValue())
        );

        ApiResponse<ConfigParameterDTO> response = ApiResponse.success(dto);
        return ResponseEntity.ok(response);
    }
}
