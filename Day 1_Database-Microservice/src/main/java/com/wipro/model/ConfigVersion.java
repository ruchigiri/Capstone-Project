package com.wipro.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "config_versions")
public class ConfigVersion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "config_id")
	private Configuration configuration;

	@Column(name = "device_id", nullable = false)
	private String deviceId;

	@Column(name = "config_data", nullable = false)
	private String configData;

	@Column(name = "version", nullable = false)
	private int version;
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	// Getters and setters
}
