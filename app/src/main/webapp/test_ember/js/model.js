Scholagest.StudentPersonal = DS.Model.extend({
	street: DS.attr(),
    city: DS.attr(),
    postcode: DS.attr(),
    religion: DS.attr()
});
Scholagest.StudentMedical = DS.Model.extend({
    doctor: DS.attr()
});
Scholagest.Student = DS.Model.extend({
	firstName: DS.attr(),
	lastName: DS.attr(),
	personal: DS.belongsTo('studentPersonal', { async: true }),
	medical: DS.belongsTo('studentMedical', { async: true }),
	
	fullName: function() {
		return this.get('firstName') + " " + this.get('lastName');
	}.property('firstName', 'lastName')
});
Scholagest.TeacherDetail = DS.Model.extend({
	address: DS.attr(),
	email: DS.attr(),
	phone: DS.attr()
});
Scholagest.Teacher = DS.Model.extend({
	firstName: DS.attr(),
	lastName: DS.attr(),
	detail: DS.belongsTo('teacherDetail', { async: true }),
	
	fullName: function() {
		return this.get('firstName') + " " + this.get('lastName');
	}.property('firstName', 'lastName')
});

Scholagest.Year = DS.Model.extend({
	name: DS.attr(),
	classes: DS.hasMany('class', { async: true })
});

Scholagest.Class = DS.Model.extend({
	name: DS.attr(),
	year: DS.belongsTo('year', { async: true }),
	periods: DS.hasMany('period', { async: true }),
	students: DS.hasMany('student', { async: true }),
	teachers: DS.hasMany('teacher', { async: true })
});

Scholagest.Period = DS.Model.extend({
	name: DS.attr(),
	clazz: DS.belongsTo('class', { async: true }),
	branches: DS.hasMany('branch', { async: true })
});
Scholagest.Branch = DS.Model.extend({
	name: DS.attr(),
	period: DS.belongsTo('period', { async: true }),
	exams: DS.hasMany('exam', { async: true }),
	studentResults: DS.hasMany('studentResult')
});
Scholagest.Exam = DS.Model.extend({
	name: DS.attr(),
	coeff: DS.attr(),
	branch: DS.belongsTo('branch', { async: true })
});

Scholagest.StudentResult = DS.Model.extend({
    branch: DS.belongsTo('branch', { async: true }),
	student: DS.belongsTo('student', { async: true }),
	results: DS.hasMany('result')
});
//Scholagest.Exam = DS.Model.extend({
//	name: DS.attr(),
//	coeff: DS.attr(),
//	branch: DS.belongsTo('branch'),
//	results: DS.hasMany('result'),
//	
//	getResult: function(student_id) {
//		var filteredResult = null;
//		this.get('results').forEach(function(result) {
//			if (result.get('student').get('id') == student_id) {
//				filteredResult = result;
//			}
//		});
//		return filteredResult;
//	}
//});
Scholagest.Result = DS.Model.extend({
	grade: DS.attr(),
	exam: DS.belongsTo('exam', { async: true }),
	studentResult: DS.belongsTo('studentResult', { async: true })
});