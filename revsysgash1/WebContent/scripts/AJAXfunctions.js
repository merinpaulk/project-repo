function getSerializedData(form){
	alert('getSerializedData running');
	var data = $(form).find('input checkbox select').serialize();
	alert('data: ' + data);
	return data;
}

function AJAXPost(serializedData, serverTarget){
    	alert('SerializedData: ' + serializedData);
    	alert('servertarget: ' + serverTarget);
    	//Do AJAX post
    	// abort any pending request
        if (request) {
           request.abort();
        }

        // fire off the request to the serverTarget
        var request = $.ajax({
            url: serverTarget,
            type: "post",
            data: serializedData 
        });

        // callback handler that will be called on success
        request.done(function (response, textStatus, jqXHR){
            // log a message to the console
            console.log("Hooray, it worked!");
        });
     // callback handler that will be called on failure
        request.fail(function (jqXHR, textStatus, errorThrown){
            // log the error to the console
            console.error(
                "The following error occured: "+
                textStatus, errorThrown
            );
        });
     // callback handler that will be called regardless
        // if the request failed or succeeded
        request.always(function () {
            // reenable the inputs
            
        });
    	//End of AJAX
    }