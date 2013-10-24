var MISSING_FIELD_SUFFIX = " *";

function displayMessageDialog(text) {
	dojo.byId('lblMessageDialog').innerText = text;
	dijit.byId('messageDialog').show();
};
function displayMessageConfirmDialog(text, okFunction) {
	dojo.byId('lblMessageConfirmDialog').innerText = text;
	dojo.byId('msgConfirmDialogOk').onclick = function(e) {
		dijit.byId('messageConfirmDialog').hide();
		okFunction.apply(window);
	};
	dijit.byId('messageConfirmDialog').show();
};

//Method used to get the values from the different dialogs. DO NOT REMOVE.
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

function checkRequiredFieldsAndMarkAsMissing(txtIds) {
	var foundMissingRequiredField = false;
	for (var id in txtIds) {
		var nodeId = txtIds[id];
		var txtNode = dojo.byId(nodeId);
		var txtValue = txtNode.value;
		var labelNodeAttribute = txtNode.attributes.getNamedItem('labelNodeId');
		if (isFieldRequired(txtNode) && labelNodeAttribute == null) {
			displayMessageDialog('TextNode with id ' + nodeId + ' is missing the labelNodeId attribute');
		} else if (isFieldRequired(txtNode)) {
			var labelNode = dojo.byId(labelNodeAttribute.nodeValue);
			if (isBlank(txtValue)) {
				markFieldAsMissing(labelNode);
				foundMissingRequiredField = true;
			} else {
				resetFieldMissingMark(labelNode);
			}
		}
	}
	
	return foundMissingRequiredField;
};

function isBlank(text) {
	return text == null || text == '';
};

function isFieldRequired(txtNode) {
	var requiredAttribute = txtNode.attributes.getNamedItem('required');
	return requiredAttribute != null && requiredAttribute.nodeValue == 'required';
};

function markFieldAsMissing(labelNode) {
	if (labelNode.markedMissing != undefined && labelNode.markedMissing == true) {
		return;
	}
	
	var text = labelNode.innerText;
	if (labelNode.originalText == null) {
		labelNode.originalText = text;
	}
	
	labelNode.markedMissing = true;
	labelNode.innerText = text + MISSING_FIELD_SUFFIX;
	labelNode.style.color = 'red';
};
function resetFieldMissingMark(labelNode) {
	if (labelNode.markedMissing == undefined || labelNode.markedMissing == false) {
		return;
	}
	
	var text = labelNode.originalText;

	labelNode.markedMissing = false;
	labelNode.innerText = text;
	labelNode.style.color = 'black';
};




function resetDialogDiv(dialogDiv) {
	resetDiv(dialogDiv.containerNode);
}
function resetDiv(div) {
	for (var i = 0; i < div.childNodes.length; i++) {
		var node = div.childNodes[i];
		if (isContainerNode(node)) {
			resetDiv(node);
		} else if (isTextNode(node)) {
			resetTextBox(node);
		}
	}
};
function resetTextBox(node) {
	if (node.defaultValue == undefined) {
		node.value = '';
	} else {
		node.value = node.defaultValue;
	}
};
function isContainerNode(node) {
	var nodeName = node.nodeName.toUpperCase();
	if (nodeName == 'DIV' || nodeName == 'SPAN' || nodeName == 'TABLE' || nodeName == 'TD' || nodeName == 'TR' || nodeName == 'TBODY') {
		return true;
	}
	return false;
};
function isTextNode(node) {
	return node.nodeName.toUpperCase() == 'INPUT' && node.type == 'text';
};