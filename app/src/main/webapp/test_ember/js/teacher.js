Scholagest.TeachersRoute = Ember.Route.extend({
	model: function() {
		return this.store.find('teacher');
	}
});
Scholagest.TeachersController = Ember.ArrayController.extend({
});
//Scholagest.TeachersView = Ember.View.extend({
//    content: null,
//    templateName: 'teachers',
//    attributes: function() {
//        console.log('called')
//        var instance = this.get('content');
//        var keys = instance.get('constructor.attributes.keys').toArray();
//        return keys.reduce(function(result, attribute) {
//            result.push({
//                name: attribute,
//                value: instance.get(attribute)    
//            });
//            return result;            
//        }, []);
//    }.property()
//});

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
		}
	}
});