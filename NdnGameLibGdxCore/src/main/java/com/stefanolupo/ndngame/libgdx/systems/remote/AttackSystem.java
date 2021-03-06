package com.stefanolupo.ndngame.libgdx.systems.remote;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.google.inject.Inject;
import com.stefanolupo.ndngame.libgdx.components.AttackComponent;
import com.stefanolupo.ndngame.libgdx.components.StateComponent;
import com.stefanolupo.ndngame.libgdx.systems.HasComponentMappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttackSystem extends IteratingSystem implements HasComponentMappers {

    private static final Logger LOG = LoggerFactory.getLogger(AttackSystem.class);

    @Inject
    public AttackSystem(){
        super(Family.all(AttackComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        // TODO: When / how will these attack components get cleaned up?
        AttackComponent attackComponent = ATTACK_MAPPER.get(entity);
        StateComponent stateComponent = STATE_MAPPER.get(entity);

//        stateComponent.updateAttackState(AttackState.fromAttackType(attackComponent.getAttack().getType()), deltaTime);
        LOG.info("Got attack: {}", attackComponent.getAttack());
    }
}
