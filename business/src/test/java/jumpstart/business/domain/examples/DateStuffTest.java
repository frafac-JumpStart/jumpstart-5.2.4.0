package jumpstart.business.domain.examples;

import static org.junit.Assert.assertEquals;
import jumpstart.business.BaseTest;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

public class DateStuffTest extends BaseTest {

	@Test
	public void test_dateTime_get_set_doesnt_mutate() {
		DateStuff ds = new DateStuff();

		DateTime dt = new DateTime(2007, 12, 31, 18, 19, 20, 123);
		ds.setADateTime(dt);
		DateTime dt1 = ds.getADateTime();
		assertEquals(dt, dt1);
	}

	@Test
	public void test_dateMidnight_get_set_doesnt_mutate() {
		DateStuff ds = new DateStuff();

		DateMidnight dm = new DateMidnight(2007, 12, 31);
		ds.setADateMidnight(dm);
		DateMidnight dm1 = ds.getADateMidnight();
		assertEquals(dm, dm1);
	}

	@Test
	public void test_localDateTime_get_set_doesnt_mutate() {
		DateStuff ds = new DateStuff();

		LocalDateTime ldt = new LocalDateTime(2007, 12, 31, 18, 19, 20, 123);
		ds.setALocalDateTime(ldt);
		LocalDateTime dt1 = ds.getALocalDateTime();
		assertEquals(ldt, dt1);
	}

	@Test
	public void test_localDate_get_set_doesnt_mutate() {
		DateStuff ds = new DateStuff();

		LocalDate ld = new LocalDate(2007, 12, 31);
		ds.setALocalDate(ld);
		LocalDate dt1 = ds.getALocalDate();
		assertEquals(ld, dt1);
	}

	@Test
	public void test_localTimeAsMillis_get_set_doesnt_mutate() {
		DateStuff ds = new DateStuff();

		LocalTime lt = new LocalTime(18, 19, 20, 123);
		ds.setALocalTimeAsMillis(lt);
		LocalTime dt1 = ds.getALocalTimeAsMillis();
		assertEquals(lt, dt1);
	}

	@Test
	public void test_localTimeAsString_get_set_doesnt_mutate() {
		DateStuff ds = new DateStuff();

		LocalTime lt = new LocalTime(18, 19, 20, 123);
		ds.setALocalTimeAsString(lt);
		LocalTime dt1 = ds.getALocalTimeAsString();
		assertEquals(lt, dt1);
	}

	@Test
	public void test_localTimeAsTime_get_set_doesnt_mutate() {
		DateStuff ds = new DateStuff();

		LocalTime lt = new LocalTime(18, 19, 20, 123);
		ds.setALocalTimeAsTime(lt);
		LocalTime dt1 = ds.getALocalTimeAsTime();
		assertEquals(lt, dt1);
	}

}
