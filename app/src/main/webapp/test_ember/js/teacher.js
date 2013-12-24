Scholagest.TeachersRoute = Ember.Route.extend({
	model: function() {
		return this.store.find('teacher');
	}
});
Scholagest.TeachersController = Ember.ArrayController.extend({});

Scholagest.TeacherRoute = Ember.Route.extend({
	model: function(params) {
		return this.store.find('teacher', params.teacher_id);
	}
});
Scholagest.TeacherController = Ember.ObjectController.extend({
	actions: {
		delete: function() {
			this.get('model').deleteRecord();
			this.get('model').save();
			this.transitionToRoute('teachers');
		}
	}
});