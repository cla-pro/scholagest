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
		delete: function() {
			this.get('model').deleteRecord();
			this.get('model').save();
			this.transitionToRoute('students');
		}
	}
});