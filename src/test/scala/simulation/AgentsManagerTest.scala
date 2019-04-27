package simulation

import org.scalatest.FunSuite
import simulation.agents.AgentsManager

class AgentsManagerTest extends FunSuite {

  test("testInit") {
    val manager = new AgentsManager()

    assertResult(15)(manager.agents.size)
    assertResult(20)(manager.agents.head.desiredSpeed)
  }

  test("testAgents") {

  }

}
