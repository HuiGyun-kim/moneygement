package com.room7.moneygement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.room7.moneygement.service.AnalyticsService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticsController {

	private final AnalyticsService analyticsService;
}