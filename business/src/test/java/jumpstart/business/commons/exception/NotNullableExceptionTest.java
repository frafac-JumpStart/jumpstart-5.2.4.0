package jumpstart.business.commons.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import jumpstart.business.BaseTest;
import jumpstart.business.domain.workout.Student;
import jumpstart.business.domain.workout.Teacher;

import org.junit.Test;

public class NotNullableExceptionTest extends BaseTest {

	@Test
	public void test_NotNullableException_is_thrown_on_referencing_nonpersisted_entity() throws BusinessException {

		// First make a building with a room and a teacher that references the room.

		Teacher teacher = new Teacher("T1", null, null);
		Student student = new Student("Jack", teacher);

		// Try to create the student - expect a NotNullableException
		// because the teacher has not been persisted so is null in the eyes of the business layer.

		try {
			student = _workoutService.createStudent(student, false);
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);
			assertEquals(s.getMessage(), NotNullableException.class, s.getClass());
			assertEquals("Student must have a Teacher.", s.getLocalizedMessage());
		}

	}

}
