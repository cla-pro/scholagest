Scholagest.TeachersRoute = Scholagest.AuthenticatedRoute.extend({
	model: function() {
		return this.store.find('teacher');
	},
//	actions: {
//	    openModal: function(modalName) {
//    	    return this.render(modalName, {
//    	        into: 'application',
//    	        outlet: 'modal'
//    	    });
//    	}
//	}
});
Scholagest.TeachersController = Scholagest.RoleArrayController.extend({
});

Scholagest.TeacherRoute = Scholagest.AuthenticatedRoute.extend({
	model: function(params) {
		return this.store.find('teacher', params.teacher_id);
	}
});
Scholagest.TeacherController = Scholagest.RoleObjectController.extend({
    isEditionAllowed: function() {
        if (this.get('isAdmin')) {
            return true;
        } else {
            var user = Scholagest.SessionManager.get('user');
            if (user != null && user.get('teacher').get('id') === this.get('model').get('id')) {
                return true;
            }
        }
        return false;
    }.property('model', 'isAdmin'),
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

//Scholagest.NewTeacherRoute = Ember.Route.extend({
//	model: function(params) {
//		return {};
//	}
//});
Scholagest.NewTeacherController = Ember.ObjectController.extend({
	content: null,
	
	init: function() {
		this._super();
		this.set('content', Ember.Object.create({}));
	},
	actions: {
		create: function(router, event) {
			var content = this.get('content');
//			var teacherDetail = this.store.createRecord('teacherDetail', { 
//				address: content.address, 
//				phone: content.phone,
//				email: content.email
//			});
			var teacher = this.store.createRecord('teacher', {
				firstName: content.firstName,
				lastName: content.lastName
			});
			teacher.save();
			
//			teacherDetail.save().then(function() {
//				teacher.set('detail', teacherDetail);
//				teacher.save();
//			}, function() {});
		}
	}
});