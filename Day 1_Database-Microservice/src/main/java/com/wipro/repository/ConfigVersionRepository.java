package com.wipro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wipro.model.ConfigVersion;

public interface ConfigVersionRepository extends JpaRepository<ConfigVersion, Long> {
 List<ConfigVersion> findByDeviceId(String deviceId);
}