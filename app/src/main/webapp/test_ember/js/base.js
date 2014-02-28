Scholagest.AuthenticatedRoute = Ember.Route.extend({
    beforeModel: function(transition) {
//        if (!Scholagest.SessionManager.isAuthenticated()) {
//            this.redirectToLogin(transition);
//        }
    },
    redirectToLogin: function(transition) {
        // If want to display login and then go back to the requested page, see http://www.embercasts.com/episodes/client-side-authentication-part-2
        this.transitionTo('sessionexpired');
    },
    events: {
//        error: function(reason, transition) {
//            if (reason.status == 401) {
//                this.redirectToLogin(transition);
//            } else {
//                alert('Error: ' + reason.status);
//            }
//        }
    }
});

Scholagest.ModalController = Ember.ObjectController.extend({
    actions: {
        close: function() {
            return this.send('closeModal');
        }
    }
});
Scholagest.ModalDialogComponent = Ember.Component.extend({
    actions: {
        close: function() {
            return this.sendAction();
        }
    }
});


//
//metadata: function() {
//    var content = this.get('content');
//    var attributeMap = this.get('content.constructor.attributes');
//    var vals = [];
//    attributeMap.forEach(function(name, value) {
//          vals.push({
//              object: content,
//              name: name,
//              value: content.get(name)   
//          });          
//    });
//    return vals;
//}.property('content'),