package socialforcemodel

import org.scalatest.FunSuite
import socialforcemodel.agents.AgentsManager
import socialforcemodel.path.SimplePathStrategy

class AgentsManagerTest extends FunSuite {

  test("testInit") {
    val manager = new AgentsManager(new SimplePathStrategy())
    manager.init()

    assertResult(15)(manager.agents.size)
    assertResult(20)(manager.agents.head.desiredSpeed)
  }

  test("testAgents") {

  }

}
