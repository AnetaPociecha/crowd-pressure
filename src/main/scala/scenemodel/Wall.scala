package scenemodel

import socialforcemodel.utils.Vector2D

case class Wall(edges: Set[Edge],
                start: Vector2D,
                stop: Vector2D) {}
