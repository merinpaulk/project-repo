
var Status = "0";

function loadPopupAnyway(popupname)
{
		centerPopup(popupname);
		$("#backgroundPopup"+popupname).css({"opacity": "0.7"});
		$("#backgroundPopup"+popupname).fadeIn("slow");
		$("#popup"+popupname).fadeIn("slow");
		Status = popupname;
}

// Overload loadPopupAnyway for set Y on long list
function loadPopupAnyway(popupname, ytop)
{
		positionPopupY(popupname,ytop);
		$("#backgroundPopup"+popupname).css({"opacity": "0.7"});
		$("#backgroundPopup"+popupname).fadeIn("slow");
		$("#popup"+popupname).fadeIn("slow");
		Status = popupname;
}

//disabling popup with jQuery magic!
function disablePopup(popupname){
	//disables popup only if it is enabled
	if(Status==popupname){
		$("#backgroundPopup"+popupname).fadeOut("slow");
		$("#popup"+popupname).fadeOut("slow");
		Status = "0"; 
	}
}

//centering popup
function centerPopup(popupname){
	//request data for centering
	var windowWidth = document.documentElement.clientWidth;
	var windowHeight = document.documentElement.clientHeight;
	var popupHeight = $("#popup"+popupname).height();
	var popupWidth = $("#popup"+popupname).width();
	//centering
	$("#popup"+popupname).css({
		"position": "absolute",
		"top": windowHeight/2-popupHeight/2,
		//"top": 0,
		"left": windowWidth/2-popupWidth/2
	});
	//only need force for IE6
	
	$("#backgroundPopup"+popupname).css({
		"height": windowHeight
	});
	
}

function positionPopupY(popupname,y){
	//request data for X positioning
	var windowWidth = document.documentElement.clientWidth;
	var popupWidth = $("#popup"+popupname).width();
	$("#popup"+popupname).css({
		"position": "absolute",
		"top": y,
		"left": windowWidth/2-popupWidth/2
	});

}



//CONTROLLING EVENTS IN jQuery
$(document).ready(function(){
	
	//LOADING POPUP
	//Click the button event!
	$("#button0").click(function(){loadPopupAnyway("0");});
	$("#button1").click(function(){loadPopupAnyway("1");});
	$("#button2").click(function(){loadPopupAnyway("2");});
	$("#button3").click(function(){loadPopupAnyway("3");});
	$("#button4").click(function(){loadPopupAnyway("4");});
	$("#button5").click(function(){loadPopupAnyway("5");});
	$("#button6").click(function(){loadPopupAnyway("6");});
	$("#button7").click(function(){loadPopupAnyway("7");});
	$("#button8").click(function(){loadPopupAnyway("8");});
	$("#button9").click(function(){loadPopupAnyway("9");});
	$("#button10").click(function(){loadPopupAnyway("10");});

	//CLOSING POPUP
	//Click the x event
	$("#popupClose0").click(function(){disablePopup("0");});
	$("#popupClose1").click(function(){disablePopup("1");});
	$("#popupClose2").click(function(){disablePopup("2");});
	$("#popupClose3").click(function(){disablePopup("3");});
	$("#popupClose4").click(function(){disablePopup("4");});
	$("#popupClose5").click(function(){disablePopup("5");});
	$("#popupClose6").click(function(){disablePopup("6");});
	$("#popupClose7").click(function(){disablePopup("7");});
	$("#popupClose8").click(function(){disablePopup("8");});
	$("#popupClose9").click(function(){disablePopup("9");});
	$("#popupClose10").click(function(){disablePopup("10");});
	
	//Click out event
	$("#backgroundPopup0").click(function(){disablePopup("0");});
	$("#backgroundPopup1").click(function(){disablePopup("1");});
	$("#backgroundPopup2").click(function(){disablePopup("2");});
	$("#backgroundPopup3").click(function(){disablePopup("3");});
	$("#backgroundPopup4").click(function(){disablePopup("4");});
	$("#backgroundPopup5").click(function(){disablePopup("5");});
	$("#backgroundPopup6").click(function(){disablePopup("6");});
	$("#backgroundPopup7").click(function(){disablePopup("7");});
	$("#backgroundPopup8").click(function(){disablePopup("8");});
	$("#backgroundPopup9").click(function(){disablePopup("9");});
	$("#backgroundPopup10").click(function(){disablePopup("10");});
	
	
	//Press Escape event!
	$(document).keypress(function(e){
		if(e.keyCode==27){
			disablePopup("0");
			disablePopup("1");
			disablePopup("2");
			disablePopup("3");
			disablePopup("4");
			disablePopup("5");
			disablePopup("6");
			disablePopup("7");
			disablePopup("8");
			disablePopup("9");
			disablePopup("10");
		}
	});

});