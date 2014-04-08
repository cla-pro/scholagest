function createExam(closeId, txtIds) {
	if (checkRequiredFieldsAndMarkAsMissing(closeId, txtIds)) {
		return;
	}
	
	var dialog = dijit.byId(closeId);
	var yearKey = dialog.yearKey;
	var classKey = dialog.classKey;
	var branchKey = dialog.branchKey;
	var periodKey = dialog.periodKey;
	var keyValues = getKeysAndValues(txtIds);
	
	var parameters = {
			keys: keyValues.keys,
			values: keyValues.values,
			yearKey: yearKey,
			classKey: classKey,
			branchKey: branchKey,
			periodKey: periodKey
		};
	sendPostRequest("../exam/create", parameters, function(info) {
		dialog.hide();
		loadStudents(); 
	}, function(errorJson) {
		if (errorJson.errorCode == errorCodesMap.OBJECT_ALREADY_EXISTS) {
			displayMessageDialog('Un examen avec le même nom existe déjà dans cette période')
		}
	});
};

function getExamsInfo(examList, properties, callback) {
	sendPostRequest("../exam/getExamsInfo", { keys: examList, properties: properties }, callback);
};
