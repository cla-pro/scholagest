Scholagest.RoleObjectController = Ember.ObjectController.extend({
    role: function() {
        var user = Scholagest.SessionManager.get('user');
        if (user == null) {
            return null;
        } else {
            return user.get('role');
        }
    }.property('Scholagest.SessionManager.user'),

    isAdmin: function() {
        return this.get('role') === Scholagest.Role.ADMIN;
    }.property('role'),
    
    isTeacher: function() {
        return this.get('role') === Scholagest.Role.TEACHER;
    }.property('role'),
    
    isHelpTeacher: function() {
        return this.get('role') === Scholagest.Role.HELP_TEACHER;
    }.property('role')
});
Scholagest.RoleArrayController = Ember.ArrayController.extend({
    role: function() {
        var user = Scholagest.SessionManager.get('user');
        if (user == null) {
            return null;
        } else {
            return user.get('role');
        }
    }.property('Scholagest.SessionManager.user'),
    
    isAdmin: function() {
        return this.get('role') === Scholagest.Role.ADMIN;
    }.property('role'),
    
    isTeacher: function() {
        return this.get('role') === Scholagest.Role.TEACHER;
    }.property('role'),
    
    isHelpTeacher: function() {
        return this.get('role') === Scholagest.Role.HELP_TEACHER;
    }.property('role')
});

Scholagest.AuthenticatedRoute = Ember.Route.extend({
    beforeModel: function(transition) {
        if (!Scholagest.SessionManager.isAuthenticated()) {
            var self = this;
            Ember.$.post("/scholagest-app/services/login", $.toJSON({
                token: $.cookie('access_token')
            })).then(function(response) {
                Scholagest.SessionManager.setToken(response.token);
                
                self.store.find('user', response.user).then(function(user) {
                    Scholagest.SessionManager.setUser(user);
                    transition.retry();
                }, function() {});
                
            }, function() {
                Scholagest.SessionManager.reset();
                self.redirectToLogin(transition);
            }.bind(this));
            that.transitionTo('tryCookieAuthentication');
        }
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