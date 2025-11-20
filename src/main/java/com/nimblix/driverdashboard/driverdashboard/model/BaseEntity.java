package com.nimblix.driverdashboard.driverdashboard.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity {
    @PrePersist
    public void ensureId() {
        try {
            var idField = this.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Object val = idField.get(this);
            if (val == null) {
                idField.set(this, UUID.randomUUID().toString());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // ignore
        }
    }
}
