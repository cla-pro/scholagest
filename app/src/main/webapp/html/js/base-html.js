function buildListItemTextClosure(propertyNames) {
	return function(element, elementId) {
		var text = '';
		
		var seen = false;
		for (var i = 0; i < propertyNames.length; i++) {
			var property = propertyNames[i];
			if (element[property] != null) {
				if (text != '') {
					text += ' ';
				}
				text += element[property];
				seen = true;
			}
		}
		
		if (!seen) {
			text = elementId;
		}
		
		return text;
	};
};

function createHtmlListFromList(list, listId, base, buildTextFunction, onclickClosure) {
	var ul = dojo.create("ul", {
		id: listId,
		className: 'search-list'}, base);
	for (var id in list) {
		var t = list[id];
		var text = buildTextFunction(t);

		dojo.create("li",
				{ innerHTML: text,
			className: 'search-list-item',
			onclick: onclickClosure(id)}, ul);
	}
}

function getKeysAndValues(txtIds) {
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
	
	return {keys: keys, values: values};
};