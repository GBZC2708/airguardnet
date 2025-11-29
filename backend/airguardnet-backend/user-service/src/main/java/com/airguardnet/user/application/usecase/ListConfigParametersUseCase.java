package com.airguardnet.user.application.usecase;

import com.airguardnet.user.domain.model.ConfigParameter;
import com.airguardnet.user.domain.repository.ConfigParameterRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListConfigParametersUseCase {

    private final ConfigParameterRepositoryPort configParameterRepositoryPort;

    public List<ConfigParameter> execute() {
        return configParameterRepositoryPort.findAll();
    }
}
