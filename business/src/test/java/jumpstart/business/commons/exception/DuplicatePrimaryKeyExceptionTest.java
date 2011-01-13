package jumpstart.business.commons.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import jumpstart.business.BaseTest;
import jumpstart.business.domain.workout.StringThing;

import org.junit.Test;

public class DuplicatePrimaryKeyExceptionTest extends BaseTest {

	@Test
	public void test_DuplicatePrimaryKeyException_is_thrown() throws BusinessException {

		// First make two entities.

		_workoutService.createStringThing(new StringThing("ABC", "Alpha"));
		_workoutService.createStringThing(new StringThing("DEF", "Delta"));

		// Try to make an entity with the same id as the first entity - expect a DuplicatePrimaryKeyException.

		try {
			_workoutService.createStringThing(new StringThing("ABC", "Apricot"));
			fail("Should not reach here because 3rd customer has same id as 1st.");
		}
		catch (Exception e) {
			BusinessException s = _interpreter.interpret(e);

			if (s instanceof DuplicatePrimaryKeyException) {
				DuplicatePrimaryKeyException d = (DuplicatePrimaryKeyException) s;
				assertEquals(DuplicatePrimaryKeyException.INFORMATIONLEVEL_NONE, d.getInformationLevel());
				assertEquals("Already exists.", d.getLocalizedMessage());
			}
			else if (s instanceof DuplicatePrimaryOrAlternateKeyException) {
				// This occurs when the database is Derby because it cannot differentiate between primary and alternate
				// key violations.
				DuplicatePrimaryOrAlternateKeyException d = (DuplicatePrimaryOrAlternateKeyException) s;
				assertTrue(d.getLocalizedMessage().startsWith("Already exists in table \"STRINGTHING\" with same primary key or other unique key."));
			}
			else {
				fail("Wrong exception type. Found " + s);
			}
		}
	}

}
