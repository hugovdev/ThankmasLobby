package me.hugo.thankmaslobby.entities;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityProjectile;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomPearl extends EntityProjectile {
    public CustomPearl(@Nullable Entity shooter, @NotNull EntityType entityType) {
        super(shooter, entityType);
    }

    @Override
    public void onStuck() {
        remove();
        removePassenger(getShooter());
    }
}
