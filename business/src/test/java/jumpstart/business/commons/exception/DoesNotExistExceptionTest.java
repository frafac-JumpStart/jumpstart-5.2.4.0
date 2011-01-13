package jumpstart.business.commons.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import jumpstart.business.BaseTest;

import org.junit.Test;

public class DoesNotExistExceptionTest extends BaseTest {

	@Test
	public void test_DoesNotExistException_is_thrown_on_find_by_wrong_String_id() {

		// Try to find an entity that doesn't exist - expect a DoesNotExistException.

		try {
			_workoutService.findStringThing("garbage");
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);
			assertEquals(s.getMessage(), DoesNotExistException.class, s.getClass());
			assertEquals("StringThing \"garbage\" does not exist.", e.getLocalizedMessage());
		}

	}

	@Test
	public void test_DoesNotExistException_is_thrown_on_find_by_wrong_Long_id() {

		// Try to find an entity that doesn't exist - expect a DoesNotExistException.

		try {
			_workoutService.findBuilding(new Long(346746437));
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);
			assertEquals(s.getMessage(), DoesNotExistException.class, s.getClass());
			assertEquals("Building \"346,746,437\" does not exist.", s.getLocalizedMessage());
		}

	}

	// @Test
	// public void test_DoesNotExistException_is_thrown_on_find_shallowish_by_wrong_String_id() {
	//
	// try {
	// _dBTestSvc.findStringThingShallowish("garbage");
	// fail("Should not reach here");
	// }
	// catch (Exception e) {
	// BusinessException s = _interpreter.interpretAsBusinessException(e);
	// assertEquals(s.getMessage(), DoesNotExistException.class, s.getClass());
	// assertEquals("Teacher \"garbage\" does not exist.", e.getLocalizedMessage());
	// }
	//
	// }

	@Test
	public void test_DoesNotExistException_is_thrown_on_find_shallowish_by_wrong_Long_id() {

		// Try to find an entity that doesn't exist, using a "shallowish" method, ie. one that join fetches its
		// immediate ManyToOne references - expect a DoesNotExistException.

		try {
			_workoutService.findTeacherShallowish(new Long(346746437));
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);
			assertEquals(s.getMessage(), DoesNotExistException.class, s.getClass());
			assertEquals("Teacher \"346,746,437\" does not exist.", s.getLocalizedMessage());
		}

	}

}
