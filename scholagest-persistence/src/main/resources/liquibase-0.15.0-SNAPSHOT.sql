-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: src/main/resources/liquibase/database.xml
-- Ran at: 02.04.14 21:01
-- Against: scholagest_dba@jdbc:postgresql://localhost/scholagest
-- Liquibase version: 3.1.1
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE public.databasechangeloglock (ID INT NOT NULL, LOCKED BOOLEAN NOT NULL, LOCKGRANTED TIMESTAMP WITH TIME ZONE, LOCKEDBY VARCHAR(255), CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID));

-- Initialize Database Lock Table
DELETE FROM public.databasechangeloglock;

INSERT INTO public.databasechangeloglock (ID, LOCKED) VALUES (1, FALSE);

-- Lock Database
-- Create Database Change Log Table
CREATE TABLE public.databasechangelog (ID VARCHAR(255) NOT NULL, AUTHOR VARCHAR(255) NOT NULL, FILENAME VARCHAR(255) NOT NULL, DATEEXECUTED TIMESTAMP WITH TIME ZONE NOT NULL, ORDEREXECUTED INT NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35), DESCRIPTION VARCHAR(255), COMMENTS VARCHAR(255), TAG VARCHAR(255), LIQUIBASE VARCHAR(20));

-- Create Database Lock Table
CREATE TABLE public.databasechangeloglock (ID INT NOT NULL, LOCKED BOOLEAN NOT NULL, LOCKGRANTED TIMESTAMP WITH TIME ZONE, LOCKEDBY VARCHAR(255), CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID));

-- Initialize Database Lock Table
DELETE FROM public.databasechangeloglock;

INSERT INTO public.databasechangeloglock (ID, LOCKED) VALUES (1, FALSE);

-- Changeset src/main/resources/liquibase/change-sets/change-set-0.15.0.xml::0.15.0-1-tables::cla
CREATE TABLE public.teacher (id SERIAL NOT NULL, "firstName" VARCHAR(50) NOT NULL, "lastName" VARCHAR(50) NOT NULL, CONSTRAINT PK_TEACHER PRIMARY KEY (id));

CREATE TABLE public.teacher_detail (id SERIAL NOT NULL, address VARCHAR(50), email VARCHAR(50), phone VARCHAR(50), teacher_id INT NOT NULL, CONSTRAINT PK_TEACHER_DETAIL PRIMARY KEY (id));

CREATE TABLE public.class_teacher (teacher_id INT, class_id INT);

CREATE TABLE public."user" (id SERIAL NOT NULL, username VARCHAR(50) NOT NULL, password VARCHAR(50) NOT NULL, role VARCHAR(50) NOT NULL, teacher_id INT NOT NULL, CONSTRAINT PK_USER PRIMARY KEY (id));

CREATE TABLE public.student (id SERIAL NOT NULL, "firstName" VARCHAR(50) NOT NULL, "lastName" VARCHAR(50) NOT NULL, CONSTRAINT PK_STUDENT PRIMARY KEY (id));

CREATE TABLE public.student_personal (id SERIAL NOT NULL, street VARCHAR(50), city VARCHAR(50), postcode VARCHAR(50), religion VARCHAR(50), student_id INT NOT NULL, CONSTRAINT PK_STUDENT_PERSONAL PRIMARY KEY (id));

CREATE TABLE public.student_medical (id SERIAL NOT NULL, doctor VARCHAR(50), student_id INT NOT NULL, CONSTRAINT PK_STUDENT_MEDICAL PRIMARY KEY (id));

CREATE TABLE public.class_student (student_id INT, class_id INT);

CREATE TABLE public.year (id SERIAL NOT NULL, name VARCHAR(50) NOT NULL, running BOOLEAN NOT NULL, CONSTRAINT PK_YEAR PRIMARY KEY (id));

CREATE TABLE public.class (id SERIAL NOT NULL, name VARCHAR(50) NOT NULL, year_id INT NOT NULL, CONSTRAINT PK_CLASS PRIMARY KEY (id));

CREATE TABLE public.branch (id SERIAL NOT NULL, name VARCHAR(50) NOT NULL, numerical BOOLEAN NOT NULL, class_id INT NOT NULL, CONSTRAINT PK_BRANCH PRIMARY KEY (id));

CREATE TABLE public.period (id SERIAL NOT NULL, name VARCHAR(50) NOT NULL, class_id INT NOT NULL, CONSTRAINT PK_PERIOD PRIMARY KEY (id));

CREATE TABLE public.branch_period (id SERIAL NOT NULL, branch_id INT NOT NULL, period_id INT NOT NULL, CONSTRAINT PK_BRANCH_PERIOD PRIMARY KEY (id));

CREATE TABLE public.exam (id SERIAL NOT NULL, name VARCHAR(50) NOT NULL, coeff INT NOT NULL, branch_period_id INT NOT NULL, CONSTRAINT PK_EXAM PRIMARY KEY (id));

CREATE TABLE public.student_result (id SERIAL NOT NULL, active BOOLEAN NOT NULL, student_id INT NOT NULL, branch_period_id INT NOT NULL, CONSTRAINT PK_STUDENT_RESULT PRIMARY KEY (id));

CREATE TABLE public.result (id SERIAL NOT NULL, grade VARCHAR(50) NOT NULL, exam_id INT NOT NULL, student_result_id INT NOT NULL, CONSTRAINT PK_RESULT PRIMARY KEY (id));

CREATE TABLE public.mean (id SERIAL NOT NULL, grade VARCHAR(50) NOT NULL, student_result_id INT NOT NULL, CONSTRAINT PK_MEAN PRIMARY KEY (id));

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('0.15.0-1-tables', 'cla', 'src/main/resources/liquibase/change-sets/change-set-0.15.0.xml', NOW(), 1, '7:8f4a5e957a31cc90017cce06834cdcff', 'createTable (x17)', '', 'EXECUTED', '3.1.1');

-- Changeset src/main/resources/liquibase/change-sets/change-set-0.15.0.xml::0.15.0-2-foreign_key_constraints::cla
ALTER TABLE public.teacher_detail ADD CONSTRAINT fk_teacher_detail_teacher FOREIGN KEY (teacher_id) REFERENCES public.teacher (id);

ALTER TABLE public."user" ADD CONSTRAINT fk_user_teacher FOREIGN KEY (teacher_id) REFERENCES public.teacher (id);

ALTER TABLE public.class_teacher ADD CONSTRAINT fk_class_teacher_teacher FOREIGN KEY (teacher_id) REFERENCES public.teacher (id);

ALTER TABLE public.class_teacher ADD CONSTRAINT fk_class_teacher_class FOREIGN KEY (class_id) REFERENCES public.class (id);

ALTER TABLE public.student_personal ADD CONSTRAINT fk_student_personal_student FOREIGN KEY (student_id) REFERENCES public.student (id);

ALTER TABLE public.student_medical ADD CONSTRAINT fk_student_medical_student FOREIGN KEY (student_id) REFERENCES public.student (id);

ALTER TABLE public.class_student ADD CONSTRAINT fk_class_student_student FOREIGN KEY (student_id) REFERENCES public.student_classes (id);

ALTER TABLE public.class_student ADD CONSTRAINT fk_class_student_class FOREIGN KEY (class_id) REFERENCES public.class (id);

ALTER TABLE public.class ADD CONSTRAINT fk_class_year FOREIGN KEY (year_id) REFERENCES public.year (id);

ALTER TABLE public.branch ADD CONSTRAINT fk_branch_class FOREIGN KEY (class_id) REFERENCES public.class (id);

ALTER TABLE public.period ADD CONSTRAINT fk_period_class FOREIGN KEY (class_id) REFERENCES public.class (id);

ALTER TABLE public.branch_period ADD CONSTRAINT fk_branch_period_period FOREIGN KEY (period_id) REFERENCES public.period (id);

ALTER TABLE public.branch_period ADD CONSTRAINT fk_branch_period_branch FOREIGN KEY (branch_id) REFERENCES public.branch (id);

ALTER TABLE public.exam ADD CONSTRAINT fk_exam_branch_period FOREIGN KEY (branch_period_id) REFERENCES public.branch_period (id);

ALTER TABLE public.student_result ADD CONSTRAINT fk_student_result_student FOREIGN KEY (student_id) REFERENCES public.student (id);

ALTER TABLE public.student_result ADD CONSTRAINT fk_student_result_branch_period FOREIGN KEY (branch_period_id) REFERENCES public.branch_period (id);

ALTER TABLE public.result ADD CONSTRAINT fk_result_exam FOREIGN KEY (exam_id) REFERENCES public.exam (id);

ALTER TABLE public.result ADD CONSTRAINT fk_result_student_result FOREIGN KEY (student_result_id) REFERENCES public.student_result (id);

ALTER TABLE public.mean ADD CONSTRAINT fk_mean_student_result FOREIGN KEY (student_result_id) REFERENCES public.student_result (id);

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('0.15.0-2-foreign_key_constraints', 'cla', 'src/main/resources/liquibase/change-sets/change-set-0.15.0.xml', NOW(), 2, '7:6ae3ae563a216caf436d66b4a52116ae', 'addForeignKeyConstraint (x19)', '', 'EXECUTED', '3.1.1');

-- Changeset src/main/resources/liquibase/change-sets/change-set-0.15.0.xml::0.15.0-3-fk-indexes::cla
CREATE INDEX idx_teacher_detail_teacher_id ON public.teacher_detail(teacher_id);

CREATE INDEX idx_class_teacher_teacher_id ON public.class_teacher(teacher_id);

CREATE INDEX idx_class_teacher_class_id ON public.class_teacher(class_id);

CREATE INDEX idx_user_teacher_id ON public."user"(teacher_id);

CREATE INDEX idx_student_personal_student_id ON public.student_personal(student_id);

CREATE INDEX idx_student_medical_student_id ON public.student_medical(student_id);

CREATE INDEX idx_class_student_student_id ON public.class_student(student_id);

CREATE INDEX idx_class_student_class_id ON public.class_student(class_id);

CREATE INDEX idx_class_year_id ON public.class(year_id);

CREATE INDEX idx_branch_class_id ON public.branch(class_id);

CREATE INDEX idx_period_class_id ON public.period(class_id);

CREATE INDEX idx_branch_period_branch_id ON public.branch_period(branch_id);

CREATE INDEX idx_branch_period_period_id ON public.branch_period(period_id);

CREATE INDEX idx_student_result_student_id ON public.student_result(student_id);

CREATE INDEX idx_student_result_branch_period_id ON public.student_result(branch_period_id);

CREATE INDEX idx_result_exam_id ON public.result(exam_id);

CREATE INDEX idx_result_student_result_id ON public.result(student_result_id);

CREATE INDEX idx_mean_student_result_id ON public.mean(student_result_id);

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('0.15.0-3-fk-indexes', 'cla', 'src/main/resources/liquibase/change-sets/change-set-0.15.0.xml', NOW(), 3, '7:23165caf37dd3c57d6359498f3d63c31', 'createIndex (x18)', '', 'EXECUTED', '3.1.1');

-- Changeset src/main/resources/liquibase/change-sets/change-set-0.15.0.xml::0.15.0-4-business-indexes::cla
CREATE INDEX idx_teacher_fullname ON public.teacher("firstName", "lastName");

CREATE INDEX idx_student_fullname ON public.student("firstName", "lastName");

INSERT INTO public.databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('0.15.0-4-business-indexes', 'cla', 'src/main/resources/liquibase/change-sets/change-set-0.15.0.xml', NOW(), 4, '7:d3b8537ecd158eb3d118c5343a45f691', 'createIndex (x2)', '', 'EXECUTED', '3.1.1');

