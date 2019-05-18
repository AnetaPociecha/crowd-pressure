package simulation

object Config {
 val initAgentsNumber: Int = 0
 val agentRadius: Double = 4
 val agentSize: Double = agentRadius * 2
 val personalSpaceRadius: Double = agentRadius * 10
 val personalSpaceCenterShift: Double = personalSpaceRadius * 0.5
 val fixMoveRepeat: Int = 12
 val selfStopAngle: Double = 17
 val wallForceRadius: Double = personalSpaceRadius * 0.3
 val circleSelectorRadius: Double = personalSpaceRadius * 0.5
 val minAgentSpeed: Double = 12
 val agentSpeedRange: Int = 1
 val canvasWidth: Int = 800
 val canvasHeight: Int = 600
 val hexGridCellSize: Int = 50
 val density: Int = 9
}
