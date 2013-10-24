function changeYearsButtonChange(yearActive) {
	dojo.byId('btnStartYear').disabled = yearActive;
	dojo.byId('btnStopYear').disabled = !yearActive;
	dojo.byId('btnRenameYear').disabled = !yearActive;
	dojo.byId('btnYearNewClass').disabled = !yearActive;
	dojo.byId('btnYearRemoveClass').disabled = !yearActive;
};
function startYear(closeId, txtYearNameId) {
	if (checkRequiredFieldsAndMarkAsMissing([txtYearNameId])) {
		return;
	}
	
	var yearName = dojo.byId(txtYearNameId).value;
	
	sendGetRequest("../year/start", { name: yearName }, function(info) {
		dijit.byId(closeId).hide();
		
		changeYearsButtonChange(true);
		dijit.byId('newClassDialog').currentYearKey = info.key;
		dijit.byId('renameYearDialog').currentYearKey = info.key;
		loadYears();
	}, function(errorJson) {
		if (errorJson.errorCode == errorCodesMap.OBJECT_ALREADY_EXISTS) {
			displayMessageDialog('Une année avec le même nom existe déjà')
		}
	});
};
function stopYear() {
	sendGetRequest("../year/stop", {}, function(info) { changeYearsButtonChange(false); });
};
function renameYear(closeId, txtNewYearNameId) {
	if (checkRequiredFieldsAndMarkAsMissing([txtNewYearNameId])) {
		return;
	}

	var newYearName = dojo.byId(txtNewYearNameId).value;
	var currentYearKey = dijit.byId(closeId).currentYearKey;
	
	sendPostRequest("../year/rename", { key: currentYearKey, newYearName: newYearName }, function(info) { dijit.byId(closeId).hide(); });
};