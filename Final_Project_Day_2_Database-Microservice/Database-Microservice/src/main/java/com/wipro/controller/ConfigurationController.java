package com.wipro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.model.Configuration;
import com.wipro.service.ConfigurationService;

@RestController
@RequestMapping("/api/configurations")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;

    // http://localhost:8088/api/config/add
//       {
//            "deviceId": "CISCO-ROUTER-2",
//                "configData": "id-user pass-123",
//                "version": 1
//        }
    @PostMapping(value = "/add")
    public ResponseEntity<Configuration> addConfiguration(final @RequestBody Configuration configuration) {
        final Configuration savedConfiguration = configurationService.addConfig(configuration);
        return new ResponseEntity<>(savedConfiguration, HttpStatus.CREATED);
    }
    //  http://localhost:8088/api/config/getAll
    @GetMapping("/getAll")
    public ResponseEntity<List<Configuration>> getAllConfiguration() {
        final List<Configuration> configurations = configurationService.getAll();
        return new ResponseEntity<>(configurations, HttpStatus.OK);
    }

    // http://localhost:8088/api/config/update/1
//    {
//        "deviceId": "TP-LINK-ROUTER-1",
//            "configData": "id-USER1 pass-12345678"
//    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Configuration> updateConfiguration(final @RequestBody Configuration configuration,
                                                             final @PathVariable Long id) {
        final Configuration updatedConfiguration = configurationService.updateConfiguration(configuration, id);
        return new ResponseEntity<>(updatedConfiguration, HttpStatus.OK);
    }

    //  http://localhost:8088/api/config/4
    @GetMapping("/{configId}")
    public ResponseEntity<Configuration> getConfiguration(final @PathVariable Long configId) {
        final Configuration configuration = configurationService.getConfiguration(configId);
        return new ResponseEntity<>(configuration, HttpStatus.OK);
    }
    //  http://localhost:8088/api/config/2
    @DeleteMapping("/{configId}")
    public ResponseEntity<Void> deleteConfiguration(final @PathVariable Long configId) {
        configurationService.deleteConfig(configId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
