package com.airguardnet.user.infrastructure.web;

import com.airguardnet.common.web.ApiResponse;
import com.airguardnet.user.application.usecase.ListPlanFeaturesUseCase;
import com.airguardnet.user.application.usecase.ListPlansUseCase;
import com.airguardnet.user.application.usecase.ListUsersUseCase;
import com.airguardnet.user.domain.model.Plan;
import com.airguardnet.user.domain.model.PlanFeature;
import com.airguardnet.user.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final ListUsersUseCase listUsersUseCase;
    private final ListPlansUseCase listPlansUseCase;
    private final ListPlanFeaturesUseCase listPlanFeaturesUseCase;

    public UserController(ListUsersUseCase listUsersUseCase, ListPlansUseCase listPlansUseCase, ListPlanFeaturesUseCase listPlanFeaturesUseCase) {
        this.listUsersUseCase = listUsersUseCase;
        this.listPlansUseCase = listPlansUseCase;
        this.listPlanFeaturesUseCase = listPlanFeaturesUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> listUsers() {
        List<User> users = listUsersUseCase.listAll();
        return ResponseEntity.ok(ApiResponse.success(users.stream().map(UserDTO::fromDomain).toList()));
    }

    @GetMapping("/plans")
    public ResponseEntity<ApiResponse<List<PlanDTO>>> listPlans() {
        List<Plan> plans = listPlansUseCase.execute();
        return ResponseEntity.ok(ApiResponse.success(plans.stream().map(PlanDTO::fromDomain).toList()));
    }

    @GetMapping("/plans/{planId}/features")
    public ResponseEntity<ApiResponse<List<PlanFeatureDTO>>> listPlanFeatures(@PathVariable Long planId) {
        List<PlanFeature> features = listPlanFeaturesUseCase.execute(planId);
        return ResponseEntity.ok(ApiResponse.success(features.stream().map(PlanFeatureDTO::fromDomain).toList()));
    }
}
