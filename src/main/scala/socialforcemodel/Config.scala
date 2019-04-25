package socialforcemodel

object Config {
 val initAgentsNumber: Int = 30
 val agentRadius: Double = 8
 val agentSize: Double = agentRadius * 2
 val personalSpaceRadius: Double = agentRadius * 6
 val interactionForceWeight: Double = 2.5
 val personalSpaceCenterShift: Double = personalSpaceRadius * 0.65
 val fixMoveRepeat: Int = 6
 val selfStopAngle: Double = 30
 val wallForceRadius: Double = personalSpaceRadius * 0.3
}
