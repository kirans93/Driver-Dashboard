package com.nimblix.driverdashboard.driverdashboard.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "announcements")
public class Announcement {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
	@JsonProperty("id")
	private String id;

	@Column(name = "title", nullable = false)
	@JsonProperty("title")
	private String title;

	@Column(name = "description", nullable = false)
	@JsonProperty("description")
	private String description;  //content


	@Column(name = "type", nullable = false)
	@JsonProperty("type")
	private String type;

	@Column(name = "priority", nullable = false)
	@JsonProperty("priority")
	private String priority = "normal";

	@Column(name = "target_class")
	@JsonProperty("target_class")
	private String targetClass;

	@Column(name = "created_at")
	@JsonProperty("created_at")
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Driver> drivers;


}
