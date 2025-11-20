package com.nimblix.driverdashboard.driverdashboard.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admins")
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", updatable = false, nullable = false)
	private String id;  // Auto-generated UUID, stored as VARCHAR

	 @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	    private User user;
	  
	    @Column(nullable = false)
	    private String name;

	    @Column(name = "employee_id", nullable = false)
	    private String employeeId;

	    @Column(columnDefinition = "json")
	    private String permissions; // could parse JSON if needed

	    private String phone;

    // One Admin supervises many Drivers (non_teaching_staff)
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<Driver> drivers;
}
