function GetXmlHttpObject()
{
    var xmlHttp = null;
    var browser = navigator.appName;
    if(browser == "Microsoft Internet Explorer") {
    	xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else{
    	xmlHttp = new XMLHttpRequest();
    }
    return xmlHttp;
}



function dateSelected(dateText, inst) {
	// send dateText to server for persistence
	var request = GetXmlHttpObject();
	request.open("POST", "/setdate", true);
	request.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	request.onreadystatechange  = function() {
		if (request.readyState == 4) {
			if (request.status != 200) {
				alert("Varning! Något gick fel när du klickade på datum " + dateText);
			} else {
				var dateCount = 0;
				dateCount += $('#october').multiDatesPicker('getDates').length;
				dateCount += $('#november').multiDatesPicker('getDates').length;
				document.getElementById('scorefield').innerHTML = dateCount;
			}
		}
	}
	request.send("date=" + dateText);
}

function addSelectedDates() {
	var request = GetXmlHttpObject();
	request.open("POST", "/dates");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	request.onreadystatechange  = function() {
		if(request.readyState == 4) {
			// for each string in responseText, set that date in both datepickers
			var dates = JSON.parse(request.responseText);
			for (d in dates) {
				var month = dates[d].match(/\d\d\d\d-(\d\d)-\d\d/)[1];
				if (month == "10") {
					$('#october').multiDatesPicker('addDates', dates[d]);
				} else if (month == "11") {
					$('#november').multiDatesPicker('addDates', dates[d]);
				}
			}
		}
	}
	request.send();
}


window.onload = function() {
	$('#october').multiDatesPicker({
		firstDay: 1,
		numberOfMonths: 1,
		stepMonths: 0,
		changeMonth: false,
		changeYear: false,
		defaultDate: new Date(2012, 9, 1),
		dateFormat: "yy-mm-dd",
		onSelect: dateSelected
	}).focus(function() {
		  $(".ui-datepicker-prev, .ui-datepicker-next").remove();
	});
	
	$('#november').multiDatesPicker({
		firstDay: 1,
		numberOfMonths: 1,
		stepMonths: 0,
		changeMonth: false,
		changeYear: false,
		defaultDate: new Date(2012, 10, 1),
		dateFormat: "yy-mm-dd",
		onSelect: dateSelected
	}).focus(function() {
		  $(".ui-datepicker-prev, .ui-datepicker-next").remove();
	});
	
	addSelectedDates();
}