package scenemodel

import simulation.utils.Vector2D

case class Wall(edges: Set[Edge],
                start: Vector2D,
                stop: Vector2D) {}
