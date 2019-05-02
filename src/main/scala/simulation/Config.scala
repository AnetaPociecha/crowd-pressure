package simulation

object Config {
 val initAgentsNumber: Int = 5
 val agentRadius: Double = 4
 val agentSize: Double = agentRadius * 2
 val personalSpaceRadius: Double = agentRadius * 10
 val interactionForceWeight: Double = 2.5
 val personalSpaceCenterShift: Double = personalSpaceRadius * 0.7
 val fixMoveRepeat: Int = 12
 val selfStopAngle: Double = 35
 val wallForceRadius: Double = personalSpaceRadius * 0.3
 val circleSelectorRadius: Double = personalSpaceRadius * 0.3
 val minAgentSpeed: Double = 13
 val agentSpeedRange: Int = 2
 val canvasWidth: Int = 800
 val canvasHeight: Int = 600
 val hexGridCellSize: Int = 40
 val density: Int = 15
}
