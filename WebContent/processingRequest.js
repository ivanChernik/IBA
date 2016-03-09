var req;
var isIE;
var completeField;
var completeTable;
var autoRow;

function init() {
	completeField = document.getElementById("complete-field");
	completeTable = document.getElementById("complete-table");
	autoRow = document.getElementById("auto-row");
	completeTable.style.top = getElementY(autoRow) + "px";
}

function doCompletion() {
	var url = "autocomplete?action=complete&id=" + escape(completeField.value);
	req = initRequest();
	req.open("POST", url, true);
	req.onreadystatechange = callback;
	req.send(null);
}

function initRequest() {
	if (window.XMLHttpRequest) {
		if (navigator.userAgent.indexOf('MSIE') != -1) {
			isIE = true;
		}
		return new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		isIE = true;
		return new ActiveXObject("Microsoft.XMLHTTP");
	}
}

function callback() {

	clearTable();

	if (req.readyState == 4) {
		if (req.status == 200) {
			var parser = new DOMParser();
			var text = req.responseText;
			var xmlDoc = parser.parseFromString(text, "text/xml");
			parseMessages(xmlDoc);
		}
	}
}

function appendStudent(firstName, lastName, partronymic, numberGroup) {

	var row;
	var cell;
	var linkElement;

	if (isIE) {
		completeTable.style.display = 'block';
		row = completeTable.insertRow(completeTable.rows.length);
		cell = row.insertCell(0);
	} else {
		completeTable.style.display = 'table';
		row = document.createElement("tr");
		cell = document.createElement("td");
		row.appendChild(cell);
		completeTable.appendChild(row);
	}

	cell.className = "popupCell";

	linkElement = document.createElement("p");
	linkElement.className = "popupItem";
	linkElement.appendChild(document.createTextNode(firstName + " " + lastName
			+ " " + partronymic + " " + numberGroup));
	cell.appendChild(linkElement);
}

function parseMessages(responseXML) {

	if (responseXML == null) {
		return false;
	} else {

		var students = responseXML.getElementsByTagName("students")[0];

		if (students.childNodes.length > 0) {
			completeTable.setAttribute("bordercolor", "black");
			completeTable.setAttribute("border", "1");

			for (var loop = 0; loop < students.childNodes.length; loop++) {
				var student = students.childNodes[loop];

				var firstName = student.getElementsByTagName("firstName")[0];
				var valueFirstName = firstName.firstChild.nodeValue;

				var lastName = student.getElementsByTagName("lastName")[0];
				var valueLastName = lastName.firstChild.nodeValue;

				var partronymic = student.getElementsByTagName("partronymic")[0];
				var valuePatronymic = partronymic.firstChild.nodeValue;

				var group = student.getElementsByTagName("group")[0];
				var valueGroup = group.firstChild.nodeValue;

				appendStudent(valueFirstName, valueLastName, valuePatronymic,
						valueGroup);
			}
		}
	}
}

function getElementY(element) {

	var targetTop = 0;

	if (element.offsetParent) {
		while (element.offsetParent) {
			targetTop += element.offsetTop;
			element = element.offsetParent;
		}
	} else if (element.y) {
		targetTop += element.y;
	}
	return targetTop;
}

function clearTable() {
	if (completeTable.getElementsByTagName("tr").length > 0) {
		completeTable.style.display = 'none';
		for (loop = completeTable.childNodes.length - 1; loop >= 0; loop--) {
			completeTable.removeChild(completeTable.childNodes[loop]);
		}
	}
}