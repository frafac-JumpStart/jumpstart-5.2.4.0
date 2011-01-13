package jumpstart.business.domain.workout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.NumberFormat;
import java.util.List;

import jumpstart.business.BaseTest;
import jumpstart.business.commons.exception.BusinessException;
import jumpstart.business.commons.exception.DoesNotExistException;

import org.junit.Test;

public class WorkoutServiceTest extends BaseTest {

	@Test
	public void test_creating_everything_one_by_one() throws BusinessException {

		// Create a building and add 2 rooms, one by one.

		Building bigBuilding = new Building("Big");
		bigBuilding = _workoutService.createBuilding(bigBuilding);

		Room room1 = new Room("West1", bigBuilding);
		room1 = _workoutService.addRoom(room1, false);

		Room room2 = new Room("West2", bigBuilding);
		room2 = _workoutService.addRoom(room2, false);

		// Create a department and add 2 teachers, one by one.

		Department mathsDepartment = new Department("Maths");
		mathsDepartment = _workoutService.createDepartment(mathsDepartment);

		Teacher teacher1 = new Teacher("T1", mathsDepartment, room1);
		teacher1 = _workoutService.createTeacher(teacher1, false);

		Teacher teacher2 = new Teacher("T2", mathsDepartment, null);
		teacher2 = _workoutService.createTeacher(teacher2, false);

		// Create 2 students for teacher 1.

		Student student1 = new Student("Jack", teacher1);
		student1 = _workoutService.createStudent(student1, false);

		Student student2 = new Student("Jill", teacher1);
		student2 = _workoutService.createStudent(student2, false);

		// Check the first building is what we expect.

		Building building = _workoutService.findBuildingAndItsRooms(bigBuilding.getId());
		assertEquals(2, building.getRooms().size());
		assertTrue(building.getRooms().contains(room1));
		assertTrue(building.getRooms().contains(room2));

		// Check the department is what we expect.

		Department department = _workoutService.findDepartmentAndItsTeachers(mathsDepartment.getId());
		assertEquals(2, department.getTeachers().size());
		assertTrue(department.getTeachers().contains(teacher1));
		assertTrue(department.getTeachers().contains(teacher2));
	}

	@Test
	public void test_creating_everything_in_bigger_chunks() throws BusinessException {

		// Create a building and add 2 rooms, all at once.

		Building bigBuilding = new Building("Big");
		Room room1 = new Room("West1", bigBuilding);
		bigBuilding.addRoom(room1);
		Room room2 = new Room("West2", bigBuilding);
		bigBuilding.addRoom(room2);

		bigBuilding = _workoutService.createBuilding(bigBuilding);

		// We might not know the id of any rooms (eg. if Hibernate has batched it), so we have to find them

		if (room1.getId() == null) {
			List<Room> rooms = _workoutService.findRooms();
			for (Room room : rooms) {
				if (room.getName().equals("West1")) {
					room1 = room;
				}
				else if (room.getName().equals("West2")) {
					room2 = room;
				}
			}
		}
		assertNotNull(room1.getId());
		Room favouriteRoom = room1;

		// Create a department and add 2 teachers, all at once.

		Department mathsDepartment = new Department("Maths");
		Teacher teacher1 = new Teacher("T1", mathsDepartment, favouriteRoom);
		mathsDepartment.addTeacher(teacher1);
		Teacher teacher2 = new Teacher("T2", mathsDepartment, null);
		mathsDepartment.addTeacher(teacher2);

		mathsDepartment = _workoutService.createDepartment(mathsDepartment);

		// We might not know the id of any teachers (eg. if Hibernate has batched it), so we have to find them

		if (teacher1.getId() == null) {
			List<Teacher> teachers = _workoutService.findTeachers();
			for (Teacher teacher : teachers) {
				if (teacher.getName().equals("T1")) {
					teacher1 = teacher;
				}
				else if (teacher.getName().equals("T2")) {
					teacher2 = teacher;
				}
			}
		}
		assertNotNull(teacher1.getId());

		// Create 2 students for teacher 1.

		Student student1 = new Student("Jack", teacher1);
		student1 = _workoutService.createStudent(student1, false);

		Student student2 = new Student("Jill", teacher1);
		student2 = _workoutService.createStudent(student2, false);

		// Check the first building is what we expect.

		Building building = _workoutService.findBuildingAndItsRooms(bigBuilding.getId());
		assertEquals(2, building.getRooms().size());
		assertTrue(building.getRooms().contains(room1));
		assertTrue(building.getRooms().contains(room2));

		// Check the department is what we expect.

		Department department = _workoutService.findDepartmentAndItsTeachers(mathsDepartment.getId());
		assertEquals(2, department.getTeachers().size());
		assertTrue(department.getTeachers().contains(teacher1));
		assertTrue(department.getTeachers().contains(teacher2));
	}

	@Test
	public void test_removing_with_an_aggregation_relationship() throws BusinessException {

		// Create a department and add 2 teachers, all at once.

		Department mathsDepartment = new Department("Maths");
		mathsDepartment = _workoutService.createDepartment(mathsDepartment);

		Teacher teacher1 = new Teacher("T1", mathsDepartment, null);
		teacher1 = _workoutService.createTeacher(teacher1, false);

		Teacher teacher2 = new Teacher("T2", mathsDepartment, null);
		teacher2 = _workoutService.createTeacher(teacher2, false);

		// Create 2 students for teacher 1.

		Student student1 = new Student("Jack", teacher1);
		student1 = _workoutService.createStudent(student1, false);

		Student student2 = new Student("Jill", teacher1);
		student2 = _workoutService.createStudent(student2, false);

		// Check the department is what we expect.

		Department department = _workoutService.findDepartmentAndItsTeachers(mathsDepartment.getId());
		assertEquals(2, department.getTeachers().size());
		assertTrue(department.getTeachers().contains(teacher1));
		assertTrue(department.getTeachers().contains(teacher2));

		// Delete the students, then check the department still has 2 teachers.

		_workoutService.deleteStudent(student1);
		_workoutService.deleteStudent(student2);
		department = _workoutService.findDepartmentAndItsTeachers(mathsDepartment.getId());
		assertEquals(2, department.getTeachers().size());

		// Remove teacher 1, then check the department is down to 1 teacher but teacher 1 still exists.

		department.removeTeacher(teacher1);
		// JPA requires merging the "many" end (Teacher) because the "one" end (Department) no longer references it.
		_workoutService.changeTeacher(teacher1);
		department = _workoutService.findDepartmentAndItsTeachers(mathsDepartment.getId());
		assertEquals(1, department.getTeachers().size());
		teacher1 = _workoutService.findTeacher(teacher1.getId());
		assertNotNull(teacher1);

		// Move teacher 2 from one department to another.

		Department scienceDepartment = new Department("Science");
		scienceDepartment = _workoutService.createDepartment(scienceDepartment);
		teacher2.setDepartment(scienceDepartment);
		_workoutService.changeTeacher(teacher2);
		department = _workoutService.findDepartmentAndItsTeachers(mathsDepartment.getId());
		assertEquals(0, department.getTeachers().size());
		department = _workoutService.findDepartmentAndItsTeachers(scienceDepartment.getId());
		assertEquals(1, department.getTeachers().size());

		// Delete teacher 2, then check the new department is down to 0 teachers, and that the teacher has gone.

		teacher2 = _workoutService.findTeacher(teacher2.getId());
		_workoutService.deleteTeacher(teacher2);
		department = _workoutService.findDepartmentAndItsTeachers(scienceDepartment.getId());
		assertEquals(0, department.getTeachers().size());
		try {
			teacher2 = _workoutService.findTeacher(teacher2.getId());
		}
		catch (DoesNotExistException e) {
			assertEquals("Teacher \"" + NumberFormat.getIntegerInstance().format(teacher2.getId())
					+ "\" does not exist.", e.getMessage());
		}
		assertNotNull(teacher2);
	}

	@Test
	public void test_removing_with_a_composition_relationship() throws BusinessException {

		// Create a building and add 2 rooms, one by one.

		Building bigBuilding = new Building("Big");
		bigBuilding = _workoutService.createBuilding(bigBuilding);

		Room room1 = new Room("West1", bigBuilding);
		room1 = _workoutService.addRoom(room1, false);

		Room room2 = new Room("West2", bigBuilding);
		room2 = _workoutService.addRoom(room2, false);

		Room room3 = new Room("West3", bigBuilding);
		room3 = _workoutService.addRoom(room3, false);

		// Check we have 3 rooms.

		List<Room> rooms = _workoutService.findRooms();
		assertEquals(3, rooms.size());

		// Remove a room and check we have 2 rooms.

		_workoutService.removeRoom(room1);
		rooms = _workoutService.findRooms();
		assertEquals(2, rooms.size());

		// Delete the building and check we have 0 rooms.

		bigBuilding = _workoutService.findBuilding(bigBuilding.getId());
		_workoutService.deleteBuilding(bigBuilding);
		rooms = _workoutService.findRooms();
		assertEquals(0, rooms.size());

	}

}
