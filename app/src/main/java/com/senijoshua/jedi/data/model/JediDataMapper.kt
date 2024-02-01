package com.senijoshua.jedi.data.model

import com.senijoshua.jedi.data.local.JediEntity
import com.senijoshua.jedi.data.remote.NetworkJedi

/**
 * Extension functions for converting data models to types relevant for the architectural
 * layers at which they are used. There are three model types:
 *
 * Jedi: model exposed to and used in architectural layers external to the data layer.
 * i.e. the presentation layer. Obtained with `toExternalModel()`.
 *
 * NetworkJedi: Model internal to the data layer that represents a Jedi retrieved
 * from the remote service.
 *
 * JediEntity: internal data layer model that represents a Jedi stored in the Room DB.
 * Obtained with `toLocal()`.
 */

/**
 * Converts the network model to the local representation for persistence in the DB.
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
 * Converts the local model to the model relevant for layers external to the data layer.
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
