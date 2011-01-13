package jumpstart.business.commons.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.NumberFormat;

import jumpstart.business.BaseTest;
import jumpstart.business.domain.workout.Department;
import jumpstart.business.domain.workout.Teacher;

import org.junit.Test;

public class CannotDeleteIsNotEmptyExceptionTest extends BaseTest {

	@Test
	public void test_CannotDeleteIsNotEmptyException_is_thrown_on_deleting_whole_that_has_parts_without_cascade()
			throws BusinessException {

		// First make a department with a teacher.

		Department department = new Department("Maths");
		Teacher teacher = new Teacher("T1", department, null);
		department.addTeacher(teacher);
		department = _workoutService.createDepartment(department);

		// Try to delete the department - expect a CannotDeleteIsNotEmptyException
		// because the relationship between Department and Teacher is aggregation, not composition.

		try {
			_workoutService.deleteDepartment(department);
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);
			assertEquals(s.getMessage(), CannotDeleteIsNotEmptyException.class, s.getClass());
			assertEquals("Cannot delete Department \"" + NumberFormat.getIntegerInstance().format(department.getId())
					+ "\" because it still contains Teacher.", s.getLocalizedMessage());
		}

	}

}
