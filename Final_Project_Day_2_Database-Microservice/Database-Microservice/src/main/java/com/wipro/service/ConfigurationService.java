package com.wipro.service;

import java.util.List;
import com.wipro.model.Configuration;

public interface ConfigurationService {
	Configuration addConfig(Configuration configuration);

    List<Configuration> getAll();

    Configuration updateConfiguration(Configuration configuration, Long id);

    Configuration getConfiguration(Long deviceId);
    void deleteConfig(Long deviceId);
}
