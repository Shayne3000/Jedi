package com.senijoshua.jedi.data.model

import com.senijoshua.jedi.data.local.JediEntity
import com.senijoshua.jedi.data.remote.NetworkJedi

/**
 * Extension functions for converting the network model to the DB model and from the DB model
 * to the model relevant for layers external to the data layer.
 */

/**
 * Converts the network model to the local model for persistence in the DB.
 */
fun NetworkJedi.toLocal() = JediEntity(
    name = name,
    gender = gender,
    height = height,
    mass = mass,
    hairColor = hairColor,
    skinColor = skinColor,
    eyeColor = eyeColor
)

fun List<NetworkJedi>.toLocal() = map(NetworkJedi::toLocal)

/**
 * Converts the local model to the model used in layers external to the data layer.
 */
fun JediEntity.toExternalModel() = Jedi(
    id = id,
    name = name,
    gender = gender,
    height = height,
    mass = mass,
    hairColor = hairColor,
    skinColor = skinColor
)

fun List<JediEntity>.toExternalModel() = map(JediEntity::toExternalModel)
