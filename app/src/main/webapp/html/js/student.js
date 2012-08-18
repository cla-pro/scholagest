function createStudent(closeId, txtIds) {
	var keys = [];
	var values = [];
	for (var id in txtIds) {
		var node = dojo.byId(txtIds[id]);

		var value = node.value;
		var name = node.attributes.getNamedItem('propertyName').nodeValue;

		if (name != null && value != null) {
			keys.push(name);
			values.push(value);
		}
	}

	var xhrArgs = {
			url: "http://localhost:8080/scholagest-app/services/student/create",
			preventCash: true,
			content: {token: dojo.cookie("scholagest-token"),
				keys: keys, values: values},
				handleAs: "json",
				load: function(data) {
					if (data.errorCode == null) {
						loadTeachers();
					}
					else
						alert("Error during createStudents: ("
								+ data.errorCode + ") " + data.message);
				},
				error: function(error) {
					alert("error = " + error);
				}
	}

	var deferred = dojo.xhrGet(xhrArgs);

	if (closeId != null) {
		dijit.byId(closeId).hide();
	}
}