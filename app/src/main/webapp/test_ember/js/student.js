Scholagest.StudentsRoute = Ember.Route.extend({
	model: function() {
		return this.store.find('student');
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

//Scholagest.NewStudentRoute = Ember.Route.extend({
//	model: function(params) {
//		return {};
//	}
//});
Scholagest.NewStudentController = Ember.ObjectController.extend({
	content: null,
	
	init: function() {
		this._super();
		this.set('content', Ember.Object.create({}));
	},
	actions: {
		create: function(router, event) {
			var content = this.get('content');
//			var studentPersonal = this.store.createRecord('studentPersonal', {
//				street: "Test street"
//			});
//			var studentMedical = this.store.createRecord('studentMedical', {});
			var student = this.store.createRecord('student', {
				firstName: content.firstName,
				lastName: content.lastName
			});
			
//			studentPersonal.save().then(function() {
				//studentMedical.save().then(function() {
//					student.set('personal', studentPersonal);
					//student.set('medical', studentMedical);
					student.save();
				//}, function() {});
//        	}, function() {
        	  // Error callback
//        	});
		}
	}
});