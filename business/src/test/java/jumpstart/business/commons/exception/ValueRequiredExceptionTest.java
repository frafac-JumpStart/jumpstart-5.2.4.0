package jumpstart.business.commons.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import jumpstart.business.BaseTest;
import jumpstart.business.domain.workout.Student;
import jumpstart.business.domain.workout.Teacher;

import org.junit.Test;

public class ValueRequiredExceptionTest extends BaseTest {

	@Test
	public void test_ValueRequiredException_is_thrown_when_creating_entity_with_required_string_empty() {

		// Try to make a teacher without a name - expect a ValueRequiredException.

		Teacher teacher1 = new Teacher();
		try {
			teacher1 = _workoutService.createTeacher(teacher1, false);
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);
			assertEquals(s.getMessage(), ValueRequiredException.class, s.getClass());
			assertEquals("You must enter a value for Name.", e.getLocalizedMessage());
		}

	}

	@Test
	public void test_ValueRequiredException_is_thrown_when_creating_entity_with_required_string_empty_while_detached() {

		// Try to make a teacher without a name - expect a ValueRequiredException.

		Teacher teacher1 = new Teacher();
		try {
			teacher1.validate();
			fail("Should not reach here");
		}
		catch (Exception e) {
			assertEquals(e.getMessage(), ValueRequiredException.class, e.getClass());
			assertEquals("You must enter a value for Name.", e.getLocalizedMessage());
		}

	}

	@Test
	public void test_ValueRequiredException_is_thrown_when_changing_entity_with_required_string_empty()
			throws BusinessException {

		// First make a teacher.

		Teacher teacher1 = new Teacher("T1", null, null);
		teacher1 = _workoutService.createTeacher(teacher1, false);

		// Try to change the name to empty - expect a ValueRequiredException.

		teacher1.setName("");
		try {
			_workoutService.changeTeacher(teacher1);
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);
			assertEquals(s.getMessage(), ValueRequiredException.class, s.getClass());
			assertEquals("You must enter a value for Name.", s.getLocalizedMessage());
		}

	}

	@Test
	public void test_ValueRequiredException_is_thrown_when_changing_entity_with_required_string_empty_while_detached()
			throws BusinessException {

		// First make a teacher.

		Teacher teacher1 = new Teacher("T1", null, null);
		teacher1 = _workoutService.createTeacher(teacher1, false);

		// Try to change the name to empty - expect a ValueRequiredException.

		teacher1.setName("");
		try {
			teacher1.validate();
			fail("Should not reach here");
		}
		catch (Exception e) {
			assertEquals(e.getMessage(), ValueRequiredException.class, e.getClass());
			assertEquals("You must enter a value for Name.", e.getLocalizedMessage());
		}

	}

	@Test
	public void test_ValueRequiredException_is_thrown_when_creating_entity_with_required_reference_nulled()
			throws BusinessException {

		// First make a teacher.

		Teacher teacher1 = new Teacher("T1", null, null);
		teacher1 = _workoutService.createTeacher(teacher1, false);

		// Try to make a student without a teacher - expect a ValueRequiredException.

		Student student1 = new Student("Jack", null);
		try {
			_workoutService.createStudent(student1, false);
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);
			assertEquals(s.getMessage(), ValueRequiredException.class, s.getClass());
			assertEquals("You must enter a value for Teacher.", s.getLocalizedMessage());
		}

	}

	@Test
	public void test_ValueRequiredException_is_thrown_when_creating_entity_with_required_reference_nulled_while_detached()
			throws BusinessException {

		// First make a teacher.

		Teacher teacher1 = new Teacher("T1", null, null);
		teacher1 = _workoutService.createTeacher(teacher1, false);

		// Try to make a student without a teacher - expect a ValueRequiredException.

		Student student1 = new Student("Jack", null);
		try {
			student1.validate();
			fail("Should not reach here");
		}
		catch (Exception e) {
			assertEquals(e.getMessage(), ValueRequiredException.class, e.getClass());
			assertEquals("You must enter a value for Teacher.", e.getLocalizedMessage());
		}

	}

	@Test
	public void test_ValueRequiredException_is_thrown_when_changing_entity_with_required_reference_nulled()
			throws BusinessException {

		// First make a teacher and student.

		Teacher teacher1 = new Teacher("T1", null, null);
		teacher1 = _workoutService.createTeacher(teacher1, false);
		Student student1 = new Student("Jack", teacher1);

		// Try to change the student to have no teacher - expect a ValueRequiredException.

		student1.setTeacher(null);
		try {
			_workoutService.changeStudent(student1);
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);
			assertEquals(s.getMessage(), ValueRequiredException.class, s.getClass());
			assertEquals("You must enter a value for Teacher.", s.getLocalizedMessage());
		}

	}

	@Test
	public void test_ValueRequiredException_is_thrown_when_changing_entity_with_required_reference_nulled_while_detached()
			throws BusinessException {

		// First make a teacher and student.

		Teacher teacher1 = new Teacher("T1", null, null);
		teacher1 = _workoutService.createTeacher(teacher1, false);
		Student student1 = new Student("Jack", teacher1);

		// Try to change the student to have no teacher - expect a ValueRequiredException.

		student1.setTeacher(null);
		try {
			student1.validate();
			fail("Should not reach here");
		}
		catch (Exception e) {
			assertEquals(e.getMessage(), ValueRequiredException.class, e.getClass());
			assertEquals("You must enter a value for Teacher.", e.getLocalizedMessage());
		}

	}

}
