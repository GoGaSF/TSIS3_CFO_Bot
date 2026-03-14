package com.cfo.cfobot.controller;

import com.cfo.cfobot.dto.CalculationRequest;
import com.cfo.cfobot.dto.CalculationResponse;
import com.cfo.cfobot.dto.ChatResponse;
import com.cfo.cfobot.dto.HistoryItemResponse;
import com.cfo.cfobot.dto.RecommendationResponse;
import com.cfo.cfobot.dto.ScenarioResponse;
import com.cfo.cfobot.service.CalculationService;
import com.cfo.cfobot.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cfo")
@CrossOrigin(origins = "*")
public class CalculationController {

    private final CalculationService calculationService;
    private final HistoryService historyService;

    public CalculationController(CalculationService calculationService, HistoryService historyService) {
        this.calculationService = calculationService;
        this.historyService = historyService;
    }

    private void requireAuth(HttpSession session) {
        if (session.getAttribute("userEmail") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must log in first");
        }
    }

    @Operation(summary = "Calculate detailed cloud infrastructure cost")
    @PostMapping("/calculate")
    public CalculationResponse calculate(@RequestBody CalculationRequest request, HttpSession session) {
        requireAuth(session);
        return calculationService.calculate(request);
    }

    @Operation(summary = "Chatbot style cloud cost estimation")
    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody CalculationRequest request, HttpSession session) {
        requireAuth(session);
        return calculationService.chatCalculate(request);
    }

    @Operation(summary = "Get optimization recommendations for current estimate")
    @PostMapping("/recommendation")
    public RecommendationResponse recommendation(@RequestBody CalculationRequest request, HttpSession session) {
        requireAuth(session);
        return calculationService.getRecommendations(request);
    }

    @Operation(summary = "Get built-in scenario comparisons")
    @GetMapping("/scenarios")
    public List<ScenarioResponse> scenarios(HttpSession session) {
        requireAuth(session);
        return calculationService.getScenarios();
    }

    @Operation(summary = "Get pricing model")
    @GetMapping("/pricing")
    public Map<String, Object> pricing(HttpSession session) {
        requireAuth(session);
        return calculationService.getPricingModel();
    }

    @Operation(summary = "Get recent estimate history")
    @GetMapping("/history")
    public List<HistoryItemResponse> history(HttpSession session) {
        requireAuth(session);
        return historyService.getHistory();
    }

    @Operation(summary = "Health check endpoint")
    @GetMapping("/ping")
    public String ping() {
        return "CFO Bot backend is running";
    }
}