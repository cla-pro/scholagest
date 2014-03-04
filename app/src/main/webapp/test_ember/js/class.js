// Code from http://stackoverflow.com/questions/19618602/ember-js-hasmany-as-list-of-checkboxes
Scholagest.CheckboxSelectComponent = Ember.Component.extend({   
    /* The property to be used as label */
    labelPath: null,
    /* The model */
    model: null,
    /* The has many property from the model */
    propertyPath: null,
    /* All possible elements, to be selected */
    elements: null,
    elementsOfProperty: function() {
        return this.get('model.' + this.get('propertyPath'));
    }.property()
});
Scholagest.CheckboxItemController = Ember.ObjectController.extend({    
    selected: function() {        
        var activity = this.get('content');
        var children = this.get('parentController.elementsOfProperty');        
        return children.contains(activity);
    }.property(),
    label: function() {    
        return this.get('model.' + this.get('parentController.labelPath'));
    }.property(),
    selectedChanged: function() {
        var activity = this.get('content');
        var children = this.get('parentController.elementsOfProperty');
        if (this.get('selected')) {                                    
            children.pushObject(activity);            
        } else {                                    
            children.removeObject(activity);                                                    
        }        
    }.observes('selected')
});

Scholagest.ClassAssignController = Ember.ObjectController.extend({
	allElements: null,
	clazz: null,
	clazzProperty: null,
	
	selectedAssigned: null,
	selectedUnassigned: null,

	unassigned: function() {
		var assigned = this.get('assigned');
		return this.get('allElements').filter(function(e) {
			return !assigned.contains(e);
		});
	}.property('allElements.@each', 'assigned.@each'),
	
	init: function() {
		this._super();
		this.set('selectedAssigned', []);
		this.set('selectedUnassigned', []);
	},
	
	actions: {
		addToClass: function() {
			var clazz = this.get('clazz');
			var clazzElements = clazz.get(this.get('clazzProperty'));
			var selected = this.get('selectedUnassigned');
			var unassigned = Ember.copy(selected);
			selected.removeObjects(unassigned);
			
			unassigned.forEach(function(element) {
				clazzElements.pushObject(element);
				clazz.send('becomeDirty');
			});
			clazz.notifyPropertyChange(this.get('clazzProperty'));
		},
		removeFromClass: function() {
			var clazz = this.get('clazz');
			var clazzElements = clazz.get(this.get('clazzProperty'));
			var selected = this.get('selectedAssigned');
			var assigned = Ember.copy(selected);
			selected.removeObjects(assigned);
			
			assigned.forEach(function(element) {
				clazzElements.removeObject(element);
				clazz.send('becomeDirty');
			});
			clazz.notifyPropertyChange(this.get('clazzProperty'));
		}
	}
});

Scholagest.ClassAssignStudentsRoute = Scholagest.AuthenticatedRoute.extend({
	model: function(params) {
		return this.store.find('class', params.class_id);
	},
	setupController: function(controller, model) {
		controller.set('clazz', model);
		controller.set('allElements', this.store.find('student'));
	}
});
Scholagest.ClassAssignStudentsController = Scholagest.ClassAssignController.extend({
	assigned: function() {
		return this.get('clazz').get('students');
	}.property('clazz.students.@each'),
	
	init: function() {
		this.set('clazzProperty', 'students');
		this._super();
	}
});

Scholagest.ClassAssignTeachersRoute = Scholagest.AuthenticatedRoute.extend({
	model: function(params) {
		return this.store.find('class', params.class_id);
	},
	setupController: function(controller, model) {
		controller.set('clazz', model);
		controller.set('allElements', this.store.find('teacher'));
	}
});
Scholagest.ClassAssignTeachersController = Scholagest.ClassAssignController.extend({
	assignedTeachers: null,
	
	assigned: function() {
		return this.get('clazz').get('teachers');
	}.property('clazz.teachers.@each'),
	
	init: function() {
		this.set('clazzProperty', 'teachers');
		this._super();
	},
});

Scholagest.ClassRoute = Scholagest.AuthenticatedRoute.extend({
	model: function(params) {
		return this.store.find('class', params.class_id);
	}
});
Scholagest.ClassController = Scholagest.RoleObjectController.extend({
	actions: {
		save: function() {
			var clazz = this.get('model');
			if (clazz.get('isDirty') || clazz.get('students').get('isDirty') || clazz.get('teachers').get('isDirty')) {
				clazz.save();
			}
		}
	}
});