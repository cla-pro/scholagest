package net.scholagest.dao;

import java.util.Arrays;
import java.util.UUID;

import net.scholagest.db.entity.BranchEntity;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.ExamEntity;
import net.scholagest.db.entity.MeanEntity;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.db.entity.ResultEntity;
import net.scholagest.db.entity.SessionEntity;
import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentMedicalEntity;
import net.scholagest.db.entity.StudentPersonalEntity;
import net.scholagest.db.entity.StudentResultEntity;
import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.db.entity.UserEntity;
import net.scholagest.db.entity.YearEntity;
import net.scholagest.test.util.TransactionalHelper;

import org.joda.time.DateTime;

public class EntityContextSaver {
    private YearEntity yearEntity;
    private ClazzEntity clazzEntity;
    private BranchEntity branchEntity;
    private PeriodEntity periodEntity;
    private BranchPeriodEntity branchPeriodEntity;
    private StudentResultEntity studentResultEntity;
    private ExamEntity examEntity;
    private ResultEntity resultEntity;
    private MeanEntity meanEntity;
    private StudentEntity studentEntity;
    private StudentPersonalEntity studentPersonalEntity;
    private StudentMedicalEntity studentMedicalEntity;
    private TeacherEntity teacherEntity;
    private TeacherDetailEntity teacherDetailEntity;
    private UserEntity userEntity;
    private SessionEntity sessionEntity;

    public void createAndPersistEntityContext(final TransactionalHelper transactionalHelper) {
        createAndPersistStudentEntity(transactionalHelper);
        createAndPersistStudentPersonalEntity(transactionalHelper);
        createAndPersistStudentMedicalEntity(transactionalHelper);
        createAndPersistTeacherEntity(transactionalHelper);
        createAndPersistTeacherDetailEntity(transactionalHelper);
        createAndPersistUserEntity(transactionalHelper);
        createAndPersistSessionEntity(transactionalHelper);

        createAndPersistYearEntity(transactionalHelper);
        createAndPersistClazzEntity(transactionalHelper);
        createAndPersistBranchEntity(transactionalHelper);
        createAndPersistPeriodEntity(transactionalHelper);
        createAndPersistBranchPeriodEntity(transactionalHelper);
        createAndPersistStudentResultEntity(transactionalHelper);
        createAndPersistExamEntity(transactionalHelper);
        createAndPersistResultEntity(transactionalHelper);
        createAndPersistMeanEntity(transactionalHelper);
    }

    private StudentEntity createAndPersistStudentEntity(final TransactionalHelper transactionalHelper) {
        studentEntity = new StudentEntity();
        studentEntity.setFirstname("studentFirstname");
        studentEntity.setLastname("studentLastname");

        return transactionalHelper.persistEntity(studentEntity);
    }

    private StudentPersonalEntity createAndPersistStudentPersonalEntity(final TransactionalHelper transactionalHelper) {
        studentPersonalEntity = new StudentPersonalEntity();
        studentPersonalEntity.setStudent(studentEntity);

        return transactionalHelper.persistEntity(studentPersonalEntity);
    }

    private StudentMedicalEntity createAndPersistStudentMedicalEntity(final TransactionalHelper transactionalHelper) {
        studentMedicalEntity = new StudentMedicalEntity();
        studentMedicalEntity.setStudent(studentEntity);

        return transactionalHelper.persistEntity(studentMedicalEntity);
    }

    private TeacherEntity createAndPersistTeacherEntity(final TransactionalHelper transactionalHelper) {
        teacherEntity = new TeacherEntity();
        teacherEntity.setFirstname("teacherFirstname");
        teacherEntity.setLastname("teacherLastname");

        return transactionalHelper.persistEntity(teacherEntity);
    }

    private TeacherDetailEntity createAndPersistTeacherDetailEntity(final TransactionalHelper transactionalHelper) {
        teacherDetailEntity = new TeacherDetailEntity();
        teacherDetailEntity.setTeacher(teacherEntity);
        teacherEntity.setTeacherDetail(teacherDetailEntity);

        return transactionalHelper.persistEntity(teacherDetailEntity);
    }

    private UserEntity createAndPersistUserEntity(final TransactionalHelper transactionalHelper) {
        userEntity = new UserEntity();
        userEntity.setUsername("username");
        userEntity.setPassword("password");
        userEntity.setRole("admin");
        userEntity.setTeacher(teacherEntity);

        return transactionalHelper.persistEntity(userEntity);
    }

    private SessionEntity createAndPersistSessionEntity(final TransactionalHelper transactionalHelper) {
        sessionEntity = new SessionEntity();
        sessionEntity.setExpirationDate(new DateTime().plusHours(2));
        sessionEntity.setId(UUID.randomUUID().toString());
        sessionEntity.setUser(userEntity);

        return transactionalHelper.persistEntity(sessionEntity);
    }

    private YearEntity createAndPersistYearEntity(final TransactionalHelper transactionalHelper) {
        yearEntity = new YearEntity();
        yearEntity.setName("yearName");

        return transactionalHelper.persistEntity(yearEntity);
    }

    private ClazzEntity createAndPersistClazzEntity(final TransactionalHelper transactionalHelper) {
        clazzEntity = new ClazzEntity();
        clazzEntity.setName("clazzName");
        clazzEntity.setYear(yearEntity);
        clazzEntity.setStudents(Arrays.asList(studentEntity));
        clazzEntity.setTeachers(Arrays.asList(teacherEntity));

        return transactionalHelper.persistEntity(clazzEntity);
    }

    private BranchEntity createAndPersistBranchEntity(final TransactionalHelper transactionalHelper) {
        branchEntity = new BranchEntity();
        branchEntity.setName("branchName");
        branchEntity.setClazz(clazzEntity);
        branchEntity.setNumerical(true);

        return transactionalHelper.persistEntity(branchEntity);
    }

    private PeriodEntity createAndPersistPeriodEntity(final TransactionalHelper transactionalHelper) {
        periodEntity = new PeriodEntity();
        periodEntity.setName("periodName");
        periodEntity.setClazz(clazzEntity);

        return transactionalHelper.persistEntity(periodEntity);
    }

    private BranchPeriodEntity createAndPersistBranchPeriodEntity(final TransactionalHelper transactionalHelper) {
        branchPeriodEntity = new BranchPeriodEntity();
        branchPeriodEntity.setBranch(branchEntity);
        branchPeriodEntity.setPeriod(periodEntity);

        return transactionalHelper.persistEntity(branchPeriodEntity);
    }

    private StudentResultEntity createAndPersistStudentResultEntity(final TransactionalHelper transactionalHelper) {
        studentResultEntity = new StudentResultEntity();
        studentResultEntity.setActive(true);
        studentResultEntity.setBranchPeriod(branchPeriodEntity);
        studentResultEntity.setStudent(studentEntity);

        return transactionalHelper.persistEntity(studentResultEntity);
    }

    private ExamEntity createAndPersistExamEntity(final TransactionalHelper transactionalHelper) {
        examEntity = new ExamEntity();
        examEntity.setName("examName");
        examEntity.setBranchPeriod(branchPeriodEntity);
        examEntity.setCoeff(3);

        return transactionalHelper.persistEntity(examEntity);
    }

    private ResultEntity createAndPersistResultEntity(final TransactionalHelper transactionalHelper) {
        resultEntity = new ResultEntity();
        resultEntity.setGrade("suffisant");
        resultEntity.setExam(examEntity);
        resultEntity.setStudentResult(studentResultEntity);

        return transactionalHelper.persistEntity(resultEntity);
    }

    private MeanEntity createAndPersistMeanEntity(final TransactionalHelper transactionalHelper) {
        meanEntity = new MeanEntity();
        meanEntity.setGrade("atteint");
        meanEntity.setStudentResult(studentResultEntity);

        return transactionalHelper.persistEntity(meanEntity);
    }

    public YearEntity getYearEntity() {
        return yearEntity;
    }

    public ClazzEntity getClazzEntity() {
        return clazzEntity;
    }

    public BranchEntity getBranchEntity() {
        return branchEntity;
    }

    public PeriodEntity getPeriodEntity() {
        return periodEntity;
    }

    public BranchPeriodEntity getBranchPeriodEntity() {
        return branchPeriodEntity;
    }

    public StudentResultEntity getStudentResultEntity() {
        return studentResultEntity;
    }

    public ExamEntity getExamEntity() {
        return examEntity;
    }

    public ResultEntity getResultEntity() {
        return resultEntity;
    }

    public MeanEntity getMeanEntity() {
        return meanEntity;
    }

    public StudentEntity getStudentEntity() {
        return studentEntity;
    }

    public StudentPersonalEntity getStudentPersonalEntity() {
        return studentPersonalEntity;
    }

    public StudentMedicalEntity getStudentMedicalEntity() {
        return studentMedicalEntity;
    }

    public TeacherEntity getTeacherEntity() {
        return teacherEntity;
    }

    public TeacherDetailEntity getTeacherDetailEntity() {
        return teacherDetailEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public SessionEntity getSessionEntity() {
        return sessionEntity;
    }
}
