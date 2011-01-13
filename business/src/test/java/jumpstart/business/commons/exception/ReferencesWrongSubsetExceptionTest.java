package jumpstart.business.commons.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import jumpstart.business.BaseTest;
import jumpstart.business.domain.workout.Building;
import jumpstart.business.domain.workout.Room;

import org.junit.Test;

public class ReferencesWrongSubsetExceptionTest extends BaseTest {

	@Test
	public void test_ReferencesWrongSubsetException_is_thrown_on_referencing_a_part_of_wrong_whole()
			throws BusinessException {

		// Create 2 buildings and add a room to the first building.

		Building bigBuilding = new Building("Big");
		bigBuilding = _workoutService.createBuilding(bigBuilding);

		Building littleBuilding = new Building("Little");
		littleBuilding = _workoutService.createBuilding(littleBuilding);

		Room room1 = new Room("West1", bigBuilding);
		room1 = _workoutService.addRoom(room1, false);

		// Try to set the main room of the 2nd building to be a room from the 1st building - expect a
		// ReferencesWrongSubsetException.

		littleBuilding.setMainRoom(room1);
		try {
			_workoutService.changeBuilding(littleBuilding);
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);
			assertEquals(s.getMessage(), ReferencesWrongSubsetException.class, s.getClass());
			assertEquals("Main room must be from Building \"" + littleBuilding.getId() + "\", but \"" + room1.getId()
					+ "\" is from Building \"" + bigBuilding.getId() + "\".", s.getLocalizedMessage());
		}

	}

	@Test
	public void test_ReferencesWrongSubsetException_is_thrown_on_referencing_a_part_of_wrong_whole_while_detached()
			throws BusinessException {

		// Create 2 buildings and add a room to the first building.

		Building bigBuilding = new Building("Big");
		bigBuilding = _workoutService.createBuilding(bigBuilding);

		Building littleBuilding = new Building("Little");
		littleBuilding = _workoutService.createBuilding(littleBuilding);

		Room room1 = new Room("West1", bigBuilding);
		room1 = _workoutService.addRoom(room1, false);

		// Try to set the main room of the 2nd building to be a room from the 1st building - expect a
		// ReferencesWrongSubsetException.

		littleBuilding.setMainRoom(room1);
		try {
			littleBuilding.validate();
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);
			assertEquals(s.getMessage(), ReferencesWrongSubsetException.class, s.getClass());
			assertEquals("Main room must be from Building \"" + littleBuilding.getId() + "\", but \"" + room1.getId()
					+ "\" is from Building \"" + bigBuilding.getId() + "\".", s.getLocalizedMessage());
		}

	}

}
