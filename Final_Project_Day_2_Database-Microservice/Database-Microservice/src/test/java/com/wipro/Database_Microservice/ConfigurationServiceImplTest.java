package com.wipro.Database_Microservice;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.wipro.model.ConfigVersion;
import com.wipro.model.Configuration;
import com.wipro.repository.ConfigVersionRepository;
import com.wipro.repository.ConfigurationRepository;
import com.wipro.service.ConfigurationServiceImpl;

class ConfigurationServiceImplTest {

    @Mock
    private ConfigurationRepository configurationRepository;

    @Mock
    private ConfigVersionRepository configVersionRepository;

    @InjectMocks
    private ConfigurationServiceImpl configurationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addConfig() {
        Configuration configuration = new Configuration();
        configuration.setConfigData("Sample Data");
        when(configurationRepository.save(configuration)).thenReturn(configuration);
        Configuration result = configurationService.addConfig(configuration);
        assertNotNull(result);
        assertEquals("Sample Data", result.getConfigData());
        verify(configurationRepository, times(1)).save(configuration);
    }

    @Test
    void getAll() {
        Configuration config1 = new Configuration();
        Configuration config2 = new Configuration();
        when(configurationRepository.findAll()).thenReturn(Arrays.asList(config1, config2));
        List<Configuration> result = configurationService.getAll();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(configurationRepository, times(1)).findAll();
    }

    @Test
    void updateConfiguration() {
        Long id = 1L;
        Configuration existingConfiguration = new Configuration();
        existingConfiguration.setId(id);
        existingConfiguration.setConfigData("Old Data");
        existingConfiguration.setDeviceId("Device123");
        existingConfiguration.setVersion(1);
        existingConfiguration.setCreatedAt(LocalDateTime.now());
        Configuration newConfiguration = new Configuration();
        newConfiguration.setConfigData("New Data");
        newConfiguration.setDeviceId("Device123");
        when(configurationRepository.findById(id)).thenReturn(Optional.of(existingConfiguration));
        when(configurationRepository.save(any(Configuration.class))).thenReturn(newConfiguration);
        when(configVersionRepository.save(any(ConfigVersion.class))).thenReturn(new ConfigVersion());
        Configuration result = configurationService.updateConfiguration(newConfiguration, id);
        assertNotNull(result);
        assertEquals("New Data", result.getConfigData());
        verify(configurationRepository, times(1)).findById(id);
        verify(configVersionRepository, times(1)).save(any(ConfigVersion.class));
        verify(configurationRepository, times(1)).save(any(Configuration.class));
    }

    @Test
    void updateConfiguration_NotFound() {
        Long id = 1L;
        Configuration newConfiguration = new Configuration();
        newConfiguration.setConfigData("New Data");
        newConfiguration.setDeviceId("Device123");
        when(configurationRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            configurationService.updateConfiguration(newConfiguration, id);
        });
        assertEquals("Configuration not found for this id : " + id, exception.getMessage());
        verify(configurationRepository, times(1)).findById(id);
        verify(configVersionRepository, never()).save(any(ConfigVersion.class));
        verify(configurationRepository, never()).save(any(Configuration.class));
    }


    @Test
    void getConfigurations() {
        Configuration config1 = new Configuration();
        when(configurationRepository.findById(anyLong())).thenReturn(Optional.of(config1));
        Configuration result = configurationService.getConfiguration(2L);
        assertNotNull(result);
        verify(configurationRepository, times(1)).findById(2L);
    }

    @Test
    void deleteConfig() {
        Long configId = 3L;
        Configuration config1 = new Configuration();
        when(configurationRepository.findById(anyLong())).thenReturn(Optional.of(config1));
        configurationService.deleteConfig(configId);
        verify(configVersionRepository, times(1)).deleteByConfigurationIds(configId);
    }

    @Test
    void deleteConfig_ErrorTest() {
        Configuration config1 = new Configuration();
        when(configurationRepository.findById(anyLong())).thenReturn(Optional.of(config1));
        doThrow(RuntimeException.class).when(configurationRepository).delete(any());
        assertThrows(RuntimeException.class, () -> configurationService.deleteConfig(3L));
        verify(configurationRepository, times(1)).findById(3L);
    }

    @Test
    void deleteConfig_ErrorTest2() {
        when(configurationRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> configurationService.deleteConfig(3L));
        verify(configurationRepository, times(1)).findById(3L);
    }
}
