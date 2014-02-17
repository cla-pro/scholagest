Scholagest.TeachersRoute = Scholagest.AuthenticatedRoute.extend({
	model: function() {
		return this.store.find('teacher');
	},
	actions: {
	    openModal: function(modalName) {
    	    return this.render(modalName, {
    	        into: 'application',
    	        outlet: 'modal'
    	    });
    	}
	}
});
Scholagest.TeachersController = Ember.ArrayController.extend({
});

Scholagest.TeacherRoute = Ember.Route.extend({
	model: function(params) {
		return this.store.find('teacher', params.teacher_id);
	}
});
Scholagest.TeacherController = Ember.ObjectController.extend({
    metadata: function() {
        var content = this.get('content');
        var attributeMap = this.get('content.constructor.attributes');
        var vals = [];
        attributeMap.forEach(function(name, value) {
              vals.push({
                  object: content,
                  name: name,
                  value: content.get(name)   
              });          
        });
        return vals;
    }.property('content'),
	actions: {
		delete: function() {
			this.get('model').deleteRecord();
			this.get('model').save();
			this.transitionToRoute('teachers');
		},
		save: function() {
			var teacher = this.get('model');
			if (teacher.get('isDirty')) {
				teacher.save();
			}
			var teacherDetail = teacher.get('detail');
			if (teacherDetail.get('isDirty')) {
				teacherDetail.get('content').save();
			}
		}
	}
});

Scholagest.NewTeacherRoute = Ember.Route.extend({
	model: function(params) {
		return {};
	}
});
Scholagest.NewTeacherController = Ember.ObjectController.extend({
	actions: {
		create: function(router, event) {
			var content = this.get('content');
			var teacherDetail = this.store.createRecord('teacherDetail', { 
				address: content.address, 
				phone: content.phone,
				email: content.email
			});
			var teacher = this.store.createRecord('teacher', {
				firstName: content.firstName,
				lastName: content.lastName,
				detail: teacherDetail
			});
			teacherDetail.save();
			teacher.save();
		}
	}
});