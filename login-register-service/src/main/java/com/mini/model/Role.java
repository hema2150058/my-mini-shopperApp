package com.mini.model;

import java.util.Set;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {

    @Id
    private String roleName;
    private String roleDescription;
    @JsonIgnore
    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
	private Set<User> users;
    
    @Override
	public String toString() {
		return roleName;
	}

}
