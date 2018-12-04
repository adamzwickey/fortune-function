//Need to check for Backend Service
var url

//check URL Param... hack for if we dont' have load balancer and we're using a NodePort
var urlParams = new URLSearchParams(window.location.search);
var backend = urlParams.get('backend');
if(backend != null) {
    console.log("backend URL para: " + backend);
    url = "http://"  + backend;
} else {
    $.ajax({
            url: "http://" + window.location.hostname + "/backend",
            cache: false,
            statusCode: {
                200: function(data) {
                  console.log("Successful config lookup!")
                  url = data;
                },
                404: function() {
                  console.log("Error on config lookup!")
                  url = "http://" + window.location.hostname + ":9080"
                }
              },
            async: false
        });
}



console.log("Backend URL: " + url);

function loadOne() {
    console.log("Getting fortunes");
    $.ajax({
        url: "http://fortune." + url,
        data: "random",
        method: "POST",
        contentType: "text/plain"
    }).then(function(data) {
        var container = $('#fortune');
        console.log("Adding Fortune: " + data);
        if(data) {
            container.append("<p>" + data.text + "</p>");
        } else {
            container.append("<p>You future is murky...</p>");
        }
    });
}

function loadAll() {
    console.log("Getting all fortunes");

    $.ajax({
        url: "http://fortunes." + url,
        data: "all",
        method: "POST",
        contentType: "text/plain"
    }).then(function(data) {
        var container = $('#fortune');
        console.log("Adding Fortunes: " + data);
        container.empty();
        if(data) {
            $.each(data, function(index, value) {
                console.log("Adding fortune " + index);
                container.append("<p>" + value.text + "</p><br>");
            });
        } else {
            container.append("<p>-- Empty --</p>");
        }
    });
}

function handle(event) {
    if (event.keyCode == 13) {
        event.preventDefault();
        var dataObject = new Object();
        dataObject.text = input_fortune.value;
        console.log("Data: " + dataObject);
        $.ajax({
            url: "http://save-fortune." + url,
            method: "POST",
            data: dataObject.text,
            contentType: "text/plain",
            success: function () {
                location.reload();
            }
        });
    }
};
