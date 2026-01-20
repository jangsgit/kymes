package com.dae.kdmes.controller.analysis.service;

import java.util.List;
import java.util.Map;
///26.01.20 14시
public interface MesAnalysisService {
    List<Map<String, Object>> getRecentProcessDefectStats();
    String analyzeWithGpt(String prompt, List<Map<String, Object>> data) throws InterruptedException;
    Map<String, Object> analyzeWithGptStructured(String prompt, List<Map<String, Object>> data) throws InterruptedException;
}
