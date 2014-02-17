Scholagest.StudentsRoute = Ember.Route.extend({
	model: function() {
		return this.store.find('student');
	}
});
Scholagest.StudentsController = Ember.ArrayController.extend({});

Scholagest.StudentRoute = Ember.Route.extend({
	model: function(params) {
		return this.store.find('student', params.student_id);
	}
});
Scholagest.StudentController = Ember.ObjectController.extend({
	actions: {
//		delete: function() {
//			this.get('model').deleteRecord();
//			this.get('model').save();
//			this.transitionToRoute('students');
//		},
        savePersonal: function() {
            var student = this.get('model');
            if (student.get('isDirty')) {
                student.save();
            }
            var studentPersonal = student.get('personal');
            if (studentPersonal.get('isDirty')) {
                studentPersonal.get('content').save();
            }
        },
        saveMedical: function() {
            var student = this.get('model');
            var studentMedical = student.get('medical');
            if (studentMedical.get('isDirty')) {
                studentMedical.get('content').save();
            }
        }
	}
});