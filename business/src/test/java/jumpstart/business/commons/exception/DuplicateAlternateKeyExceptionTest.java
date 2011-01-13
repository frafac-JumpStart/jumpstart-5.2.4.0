package jumpstart.business.commons.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import jumpstart.business.BaseTest;
import jumpstart.business.domain.workout.Department;

import org.junit.Test;

public class DuplicateAlternateKeyExceptionTest extends BaseTest {

	@Test
	public void test_DuplicateAlternateKeyException_is_thrown() throws BusinessException {

		// First make two entities.

		_workoutService.createDepartment(new Department("science"));
		_workoutService.createDepartment(new Department("law"));

		// Try to make an entity with the same alternate key as the first entity - expect a
		// DuplicateAlternateKeyException.

		try {
			_workoutService.createDepartment(new Department("science"));
			fail("Should not reach here");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);

			if (s instanceof DuplicateAlternateKeyException) {
				DuplicateAlternateKeyException d = (DuplicateAlternateKeyException) s;
				assertEquals(DuplicateAlternateKeyException.INFORMATIONLEVEL_ENTITY_TECHMSG, d.getInformationLevel());
				assertTrue(d.getLocalizedMessage().startsWith("Department already exists"));
			}
			else if (s instanceof DuplicatePrimaryOrAlternateKeyException) {
				// This occurs when the database is Derby because it cannot differentiate between primary and alternate
				// key violations.
				DuplicatePrimaryOrAlternateKeyException d = (DuplicatePrimaryOrAlternateKeyException) s;
				assertTrue(d.getLocalizedMessage().startsWith("Already exists in table \"DEPARTMENT\" with same primary key or other unique key."));
			}
			else {
				fail("Wrong exception type. Found " + s);
			}
		}

	}

}
