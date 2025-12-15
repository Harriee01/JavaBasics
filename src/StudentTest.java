import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


// Tests Student class and its implementations (85%+ coverage)

class StudentTest {
    private RegularStudent regularStudent;
    private HonorsStudent honorsStudent;
    private GradeManager gradeManager;

    @BeforeEach
    void setUp() {
        // Initialize test objects before each test
        // This ensures clean state for each test
        regularStudent = new RegularStudent("John Doe", "john@test.com", "john@test.com", 18);
        honorsStudent = new HonorsStudent("Jane Smith", "jane@test.com", "098-765-4321", 19);
        gradeManager = new GradeManager();

        // Connect grade manager to students
        regularStudent.setGradeManager(gradeManager);
        honorsStudent.setGradeManager(gradeManager);
    }

    @Test
    @DisplayName("Test student ID generation")
    void testStudentIDGeneration() {
        // IDs should be sequential and formatted
        assertNotNull(regularStudent.getStudentId(), "Student ID should not be null");
        assertTrue(regularStudent.getStudentId().startsWith("STU"),
                "Student ID should start with STU");
        assertEquals(6, regularStudent.getStudentId().length(),
                "Student ID should be 6 characters (STU + 3 digits)");
    }

    @Test
    @DisplayName("Test student getters")
    void testStudentGetters() {
        // Test all getter methods
        assertEquals("John Doe", regularStudent.getStudentName());
        assertEquals(18, regularStudent.getStudentAge());
        assertEquals("john@test.com", regularStudent.getStudentName());
        assertEquals("123-456-7890", regularStudent.getStudentPhone());
        assertEquals("Active", regularStudent.getStudentStatus());
    }

    @Test
    @DisplayName("Test student setters")
    void testStudentSetters() {
        // Test all setter methods
        regularStudent.setStudentName("John Smith");
        assertEquals("John Smith", regularStudent.getStudentName());

        regularStudent.setStudentAge(19);
        assertEquals(19, regularStudent.getStudentAge());

        regularStudent.setStudentEmail("johnsmith@test.com");
        assertEquals("johnsmith@test.com", regularStudent.getStudentEmail());

        regularStudent.setStudentPhone("111-222-3333");
        assertEquals("111-222-3333", regularStudent.getStudentPhone());

        regularStudent.setStudentStatus("Inactive");
        assertEquals("Inactive", regularStudent.getStudentStatus());
    }

    @Test
    @DisplayName("Test grade validation - valid grades")
    void testValidateGrade_Valid() {
        // Valid grades (0-100)
        assertTrue(regularStudent.validateGrade(0), "0 should be valid");
        assertTrue(regularStudent.validateGrade(50), "50 should be valid");
        assertTrue(regularStudent.validateGrade(100), "100 should be valid");
        assertTrue(regularStudent.validateGrade(85.5), "85.5 should be valid");
    }

    @Test
    @DisplayName("Test grade validation - invalid grades")
    void testValidateGrade_Invalid() {
        // Invalid grades (outside 0-100)
        assertFalse(regularStudent.validateGrade(-1), "-1 should be invalid");
        assertFalse(regularStudent.validateGrade(101), "101 should be invalid");
        assertFalse(regularStudent.validateGrade(150), "150 should be invalid");
    }

    @Test
    @DisplayName("Test regular student type and passing grade")
    void testRegularStudent_TypeAndPassingGrade() {
        assertEquals("Regular", regularStudent.getStudentType());
        assertEquals(50.0, regularStudent.getPassingGrade(), 0.01,
                "Regular students should have 50% passing grade");
    }

    @Test
    @DisplayName("Test honors student type and passing grade")
    void testHonorsStudent_TypeAndPassingGrade() {
        assertEquals("Honors", honorsStudent.getStudentType());
        assertEquals(60.0, honorsStudent.getPassingGrade(), 0.01,
                "Honors students should have 60% passing grade");
    }

    @Test
    @DisplayName("Test regular student isPassing - passing")
    void testRegularStudent_IsPassing_True() {
        // Add grades that average above 50%
        CoreSubject math = new CoreSubject("Mathematics", "MATH101");
        gradeManager.addGrade(new Grade(regularStudent.getStudentId(), math, 80));
        gradeManager.addGrade(new Grade(regularStudent.getStudentId(), math, 70));

        assertTrue(regularStudent.isPassing(),
                "Student with 75% average should be passing (need 50%)");
    }

    @Test
    @DisplayName("Test regular student isPassing - failing")
    void testRegularStudent_IsPassing_False() {
        // Add grades that average below 50%
        CoreSubject math = new CoreSubject("Mathematics", "MATH101");
        gradeManager.addGrade(new Grade(regularStudent.getStudentId(), math, 40));
        gradeManager.addGrade(new Grade(regularStudent.getStudentId(), math, 45));

        assertFalse(regularStudent.isPassing(),
                "Student with 42.5% average should be failing (need 50%)");
    }

    @Test
    @DisplayName("Test honors student isPassing - passing")
    void testHonorsStudent_IsPassing_True() {
        // Add grades that average above 60%
        CoreSubject math = new CoreSubject("Mathematics", "MATH101");
        gradeManager.addGrade(new Grade(honorsStudent.getStudentId(), math, 85));
        gradeManager.addGrade(new Grade(honorsStudent.getStudentId(), math, 75));

        assertTrue(honorsStudent.isPassing(),
                "Honors student with 80% average should be passing (need 60%)");
    }

    @Test
    @DisplayName("Test honors student isPassing - failing")
    void testHonorsStudent_IsPassing_False() {
        // Add grades that average below 60%
        CoreSubject math = new CoreSubject("Mathematics", "MATH101");
        gradeManager.addGrade(new Grade(honorsStudent.getStudentId(), math, 55));
        gradeManager.addGrade(new Grade(honorsStudent.getStudentId(), math, 58));

        assertFalse(honorsStudent.isPassing(),
                "Honors student with 56.5% average should be failing (need 60%)");
    }

    @Test
    @DisplayName("Test honors eligibility - eligible (85%+)")
    void testHonorsEligibility_Eligible() {
        // Add grades that average 85% or higher
        CoreSubject math = new CoreSubject("Mathematics", "MATH101");
        gradeManager.addGrade(new Grade(honorsStudent.getStudentId(), math, 90));
        gradeManager.addGrade(new Grade(honorsStudent.getStudentId(), math, 88));

        assertTrue(honorsStudent.checkHonorsEligibility(),
                "Student with 89% average should be eligible for honors (need 85%)");
    }

    @Test
    @DisplayName("Test honors eligibility - not eligible")
    void testHonorsEligibility_NotEligible() {
        // Add grades that average below 85%
        CoreSubject math = new CoreSubject("Mathematics", "MATH101");
        gradeManager.addGrade(new Grade(honorsStudent.getStudentId(), math, 80));
        gradeManager.addGrade(new Grade(honorsStudent.getStudentId(), math, 75));

        assertFalse(honorsStudent.checkHonorsEligibility(),
                "Student with 77.5% average should not be eligible for honors (need 85%)");
    }

    @Test
    @DisplayName("Test student search by name")
    void testSearchable_MatchesName() {
        // Partial name matching (case-insensitive)
        assertTrue(regularStudent.matchesName("john"),
                "Should match partial name 'john'");
        assertTrue(regularStudent.matchesName("JOHN"),
                "Should match regardless of case");
        assertTrue(regularStudent.matchesName("Doe"),
                "Should match last name");
        assertFalse(regularStudent.matchesName("Jane"),
                "Should not match different name");
    }

    @Test
    @DisplayName("Test student search by type")
    void testSearchable_MatchesType() {
        assertTrue(regularStudent.matchesType("Regular"));
        assertTrue(regularStudent.matchesType("regular"));  // Case-insensitive
        assertFalse(regularStudent.matchesType("Honors"));

        assertTrue(honorsStudent.matchesType("Honors"));
        assertFalse(honorsStudent.matchesType("Regular"));
    }

    @Test
    @DisplayName("Test calculate GPA")
    void testCalculateGPA() {
        // Add grades and test GPA calculation
        CoreSubject math = new CoreSubject("Mathematics", "MATH101");
        gradeManager.addGrade(new Grade(regularStudent.getStudentId(), math, 93));

        double gpa = regularStudent.calculateGPA();
        assertEquals(4.0, gpa, 0.01, "93% should convert to 4.0 GPA");
    }
}
