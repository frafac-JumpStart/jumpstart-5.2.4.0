package jumpstart.business;

import jumpstart.business.commons.exception.CannotDeleteIsNotEmptyExceptionTest;
import jumpstart.business.commons.exception.CannotDeleteIsReferencedExceptionTest;
import jumpstart.business.commons.exception.DoesNotExistExceptionTest;
import jumpstart.business.commons.exception.DuplicateAlternateKeyExceptionTest;
import jumpstart.business.commons.exception.DuplicatePrimaryKeyExceptionTest;
import jumpstart.business.commons.exception.GenericBusinessExceptionTest;
import jumpstart.business.commons.exception.NotNullableExceptionTest;
import jumpstart.business.commons.exception.OptimisticLockExceptionTest;
import jumpstart.business.commons.exception.ReferenceNotMutableExceptionTest;
import jumpstart.business.commons.exception.ReferencesWrongSubsetExceptionTest;
import jumpstart.business.commons.exception.ValueRequiredExceptionTest;
import jumpstart.business.domain.security.UserPasswordTest;
import jumpstart.business.domain.workout.WorkoutServiceTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( { WorkoutServiceTest.class, CannotDeleteIsNotEmptyExceptionTest.class,
		CannotDeleteIsReferencedExceptionTest.class, DoesNotExistExceptionTest.class,
		DuplicateAlternateKeyExceptionTest.class, DuplicatePrimaryKeyExceptionTest.class,
		GenericBusinessExceptionTest.class, OptimisticLockExceptionTest.class, NotNullableExceptionTest.class,
		ReferenceNotMutableExceptionTest.class, ReferencesWrongSubsetExceptionTest.class,
		ValueRequiredExceptionTest.class, UserPasswordTest.class })
public class BusinessIntegrationTestSuite {
}
