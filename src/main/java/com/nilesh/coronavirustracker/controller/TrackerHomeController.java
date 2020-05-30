package com.nilesh.coronavirustracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.nilesh.coronavirustracker.model.LocationStats;
import com.nilesh.coronavirustracker.service.CoronaVirusDataService;

@Controller
public class TrackerHomeController {
	
	@Autowired
	private CoronaVirusDataService coronaVirusDataService;

	@GetMapping("/")
	public String home(Model model) {
		
		List<LocationStats> allStats = coronaVirusDataService.getAllStats();
		
		int totalCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
		
		model.addAttribute("locationStats", allStats);
		model.addAttribute("totalCases", totalCases);
		return "home";
	}
}
