package com.stefanolupo.ndngame.libgdx.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.stefanolupo.ndngame.libgdx.components.*;

public interface HasComponentMappers {

    ComponentMapper<AnimationComponent> ANIMATION_MAPPER = ComponentMapper.getFor(AnimationComponent.class);
    ComponentMapper<BodyComponent> BODY_MAPPER = ComponentMapper.getFor(BodyComponent.class);
    ComponentMapper<CollisionComponent> COLLISION_MAPPER = ComponentMapper.getFor(CollisionComponent.class);
    ComponentMapper<PlayerComponent> PLAYER_MAPPER = ComponentMapper.getFor(PlayerComponent.class);
    ComponentMapper<StateComponent> STATE_MAPPER = ComponentMapper.getFor(StateComponent.class);
    ComponentMapper<TextureComponent> TEXTURE_MAPPER = ComponentMapper.getFor(TextureComponent.class);
    ComponentMapper<TransformComponent> TRANSFORM_MAPPER = ComponentMapper.getFor(TransformComponent.class);
    ComponentMapper<TypeComponent> TYPE_MAPPER = ComponentMapper.getFor(TypeComponent.class);
}