package com.example.corona.coronavirus;

import models.LocationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.NumberFormat;
import java.util.List;

@Controller
public class CoronaVirusDataController {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStatus> stats = coronaVirusDataService.getAllStats();
        int totalReportedCases = stats.stream().mapToInt(stat -> stat.getLatestTotal()).sum();
        model.addAttribute("stats", stats);
        String totalCases = NumberFormat.getIntegerInstance().format(totalReportedCases);
        model.addAttribute("totalReportedCases", totalCases);

        return "home";//Returns to a file named home in templates
    }

}
