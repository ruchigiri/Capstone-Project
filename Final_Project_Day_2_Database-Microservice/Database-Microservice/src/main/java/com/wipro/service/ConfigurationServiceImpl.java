package com.wipro.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.model.ConfigVersion;
import com.wipro.model.Configuration;
import com.wipro.repository.ConfigVersionRepository;
import com.wipro.repository.ConfigurationRepository;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {


    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private ConfigVersionRepository configVersionRepository;

    @Override
    public Configuration addConfig(final Configuration configuration) {
        return configurationRepository.save(configuration);

    }

    @Override
    public List<Configuration> getAll() {
        return configurationRepository.findAll();
    }

    @Override
    public Configuration updateConfiguration(final Configuration configuration, final Long id) {
        //  finding old configuration using id
        final Optional<Configuration> configurationOptional = configurationRepository.findById(id);

        if (configurationOptional.isPresent()) {
            final Configuration currentConfiguration = configurationOptional.get();
            //  Storing old configuration into ConfigVersion table
            ConfigVersion configVersion = new ConfigVersion();
            configVersion.setConfiguration(currentConfiguration);
            configVersion.setConfigData(currentConfiguration.getConfigData());
            configVersion.setDeviceId(currentConfiguration.getDeviceId());
            configVersion.setVersion(currentConfiguration.getVersion());
            configVersion.setCreatedAt(currentConfiguration.getCreatedAt());
            configVersionRepository.save(configVersion);

            // storing new configuration coming from controller into configuration table
            final Configuration newConfiguration = new Configuration();
            newConfiguration.setId(currentConfiguration.getId());
            newConfiguration.setConfigData(configuration.getConfigData());
            newConfiguration.setVersion(currentConfiguration.getVersion() + 1);
            newConfiguration.setDeviceId(configuration.getDeviceId());
            newConfiguration.setCreatedAt(LocalDateTime.now());
            return configurationRepository.save(newConfiguration);


        } else {
            throw new RuntimeException("Configuration not found for this id : " + id);
        }
    }

    @Override
    public Configuration getConfiguration(final Long configId) {
        return configurationRepository
            .findById(configId)
            .orElseThrow(() -> new RuntimeException("Configuration not found with id : " + configId));
    }

    @Override
    public void deleteConfig(final Long configId) {

        final Optional<Configuration> configurationListOptional = configurationRepository.findById(configId);
        if (configurationListOptional.isPresent()) {
            final Configuration configuration = configurationListOptional.get();
            try {
                configVersionRepository.deleteByConfigurationIds(configId);
                configurationRepository.delete(configuration);
            } catch (Exception ex) {
                throw new RuntimeException("Error Occurred while deletion :: " + ex.getMessage());
            }
        } else {
            throw new RuntimeException("Configuration not found for this id : " + configId);
        }

    }

}
