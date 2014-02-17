$.postJSON = function(url, data, callback) {
    return $.ajax({
        'type': 'POST',
        'url': url,
        'contentType': 'application/json',
        'data': $.toJson(data),
        'dataType': 'json',
        'success': callback
    });
};

Scholagest.SessionManager = Ember.Object.create({
    token: null,
    user: null,
    
    isAuthenticated: function() {
        return this.get('token');
    },
    setToken: function(token) {
        this.set('token', token);
        
        var token = this.get('token');
        $.ajaxSetup({
            headers: {
                "Authorization": token
            }
        });
    }
});

Scholagest.LoginRoute = Ember.Route.extend({

});
Scholagest.LoginController = Ember.Controller.extend({
    loginFailed: false,
    isProcessing: false,

    reset: function() {
        this.setProperties({
            loginFailed: false,
            isProcessing: false,
            username: "",
            password: ""
        });
    },
    actions: {
        login: function() {
            this.setProperties({
                loginFailed: false,
                isProcessing: true
            });
            
            var self = this;
            Ember.$.post("/scholagest-app/services/login", $.toJSON({
                username: this.get("username"),
                password: this.get("password")
            })).then(function(response) {
                self.reset(); //set("isProcessing", false);

                Scholagest.SessionManager.setToken(response.token);
                
                var user = self.store.find('user', response.user);
                Scholagest.SessionManager.set("user", user);
                
                self.transitionToRoute('teachers');
            }, function() {
                self.set("isProcessing", false);
                self.set("loginFailed", true);
            }.bind(this));
        }
    }
});