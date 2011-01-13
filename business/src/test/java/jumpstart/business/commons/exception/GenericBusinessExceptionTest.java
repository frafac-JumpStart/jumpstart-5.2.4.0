package jumpstart.business.commons.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import jumpstart.business.BaseTest;
import jumpstart.business.domain.workout.Teacher;

import org.junit.Test;

public class GenericBusinessExceptionTest extends BaseTest {

	@Test
	public void test_GenericBusinessException_is_thrown_sometimes() throws BusinessException {

		// Try making a teacher whose name is "silly" - expect a GenericBusinessException.

		Teacher teacher = new Teacher("silly", null, null);
		try {
			_workoutService.createTeacher(teacher, false);
			fail("Should not reach here because name is invalid.");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);
			assertEquals(s.getMessage(), GenericBusinessException.class, s.getClass());
			assertEquals("Name must not be \"silly\".", s.getLocalizedMessage());
		}

	}

}
