function escapeJson(json) {
	return handleJson(json, escape);
}

function unescapeJson(json) {
	return handleJson(json, unescape);
}

function handleJson(json, handleStringFunction) {
	var result = {};
	for (var key in json) {
		var value = json[key];
		var typeOfValue = typeof(value);
		
		var escaped = value;
		if (typeOfValue == "string") {
			escaped = handleStringFunction.call(this, value.replace(/\+/g, " "));
		}
		else if (typeOfValue == "object" && typeOfValue.length != null) {
			escaped = handleArray(value, handleStringFunction);
		}
		
		result[key] = escaped;
	}
	
	return result;
}

function handleArray(array, handleStringFunction) {
	var result = [];
	for (var i = 0; i < array.length; i++) {
		result.push(handleStringFunction.call(this, array[i].replace(/\+/g, " ")));
	}
	
	return result;
}

function clearDOM(domId) {
	var base = dojo.byId(domId);
	while (base.hasChildNodes()) {
		base.removeChild(base.lastChild);
	}
}

function getKeyValues(parentId) {
	//var keys = [];
	//var values = [];
	var keyValues = {};
	
	var rows = dojo.byId(parentId).childNodes;
	for (var i = 0; i < rows.length; i++) {
		var row = rows.item(i);
		var info = row.info;
		if (info != null) {
			//keys.push(info.propertyName);
			//values.push(info.infoGetter());
			keyValues[info.propertyName] = info.infoGetter();
		}
	}
	
	return keyValues;
}