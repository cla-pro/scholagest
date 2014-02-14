Scholagest.AuthenticatedRoute = Ember.Route.extend({
    beforeModel: function(transition) {
        if (!Scholagest.SessionManager.isAuthenticated()) {
            this.redirectToLogin(transition);
        }
    },
    redirectToLogin: function(transition) {
        // If want to display login and then go back to the requested page, see http://www.embercasts.com/episodes/client-side-authentication-part-2
        this.transitionTo('sessionexpired');
    },
    events: {
        error: function(reason, transition) {
            if (reason.status == 401) {
                this.redirectToLogin(transition);
            } else {
                alert('Error: ' + reason.status);
            }
        }
    }
});
