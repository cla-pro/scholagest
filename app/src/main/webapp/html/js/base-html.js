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

function createHtmlFromList(list, listId, baseDOM, buildTextFunction) {
	var ul = dojo.create("ul", {
		id: listId,
		className: 'search-list'}, base);
	for (var d in list) {
		var t = list[d];
		var text = buildTextFunction(['pTeacherFirstName', 'pTeacherLastName']);

		dojo.create("li",
				{ innerHTML: text,
			className: 'search-list-item',
			onclick: selectTeacher(d)}, ul);
	}
}