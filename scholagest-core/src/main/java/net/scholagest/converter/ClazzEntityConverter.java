package net.scholagest.converter;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.BranchEntity;
import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.PeriodEntity;
import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.object.Clazz;

/**
 * Method to convert from the jpa entity {@link ClazzEntity} to the transfer object {@link Clazz} and reverse.
 * 
 * @author CLA
 * @since 0.16.0
 */
public class ClazzEntityConverter {
    /**
     * Convenient method to convert a list of {@link ClazzEntity} to a list of {@link Clazz}
     *  
     * @param clazzEntityList The list to convert
     * @return The converted list
     */
    public List<Clazz> convertToClazzList(final List<ClazzEntity> clazzEntityList) {
        final List<Clazz> clazzList = new ArrayList<>();

        for (final ClazzEntity clazzEntity : clazzEntityList) {
            clazzList.add(convertToClazz(clazzEntity));
        }

        return clazzList;
    }

    /**
     * Convert a {@link ClazzEntity} to its transfer version {@link Clazz}.
     * 
     * @param clazzEntity The clazz entity to convert
     * @return The converted clazz
     */
    public Clazz convertToClazz(final ClazzEntity clazzEntity) {
        final Clazz clazz = new Clazz();
        clazz.setId("" + clazzEntity.getId());
        clazz.setName(clazzEntity.getName());
        clazz.setYear("" + clazzEntity.getYear().getId());

        final List<String> branches = new ArrayList<>();
        for (final BranchEntity branchEntity : clazzEntity.getBranches()) {
            branches.add("" + branchEntity.getId());
        }
        clazz.setBranches(branches);

        final List<String> periods = new ArrayList<>();
        for (final PeriodEntity periodEntity : clazzEntity.getPeriods()) {
            periods.add("" + periodEntity.getId());
        }
        clazz.setPeriods(periods);

        final List<String> teachers = new ArrayList<>();
        for (final TeacherEntity teacherEntity : clazzEntity.getTeachers()) {
            teachers.add("" + teacherEntity.getId());
        }
        clazz.setTeachers(teachers);

        final List<String> students = new ArrayList<>();
        for (final StudentEntity studentEntity : clazzEntity.getStudents()) {
            students.add("" + studentEntity.getId());
        }
        clazz.setStudents(students);

        return clazz;
    }

    /**
     * Convert a {@link Clazz} to the entity {@link ClazzEntity}.
     * 
     * @param clazz The clazz to convert
     * @return The converted clazz entity
     */
    public ClazzEntity convertToClazzEntity(final Clazz clazz) {
        final ClazzEntity clazzEntity = new ClazzEntity();
        clazzEntity.setName(clazz.getName());
        clazzEntity.setBranches(new ArrayList<BranchEntity>());
        clazzEntity.setPeriods(new ArrayList<PeriodEntity>());

        return clazzEntity;
    }
}
