let baseUrl = "http://localhost:8080"
let partitionsHtml = '<div class="bg-dark p-2 partition-container">'+
                            '<span class="text-white">partition-0</span>'+
                            '<i class="bi bi-play-circle-fill text-primary float-right"></i>'+
                        '</div>';

let topicDiv = '<div id="topic-partition-container-1">' +
                    '<a class="pl-0 nav-link active h4" data-target="#partitions" data-toggle="collapse" href="#" id="topic-label"></a>' +
                    '<div class="collapse" id="partitions">' +
                        '<div class="bg-dark p-2 partition-container">' +
                            '<span class="text-white">partition-0</span>' +
                            '<i class="bi bi-play-circle-fill text-primary float-right"></i>' +
                        '</div>'+
                    '</div>' +
                '</div>';

$(document).ready(function () {

    getTopics();
    $("#load-topics").click(function (){
         getTopics();

    });

    $(".collapse").on("show.bs.collapse", function () {
            $(".collapse").collapse("hide");
            getPartitions(this);
    })

});

function getPartitions(ths){
    $("#spinner").show(500);
    let partitionsDiv = $(ths);
    let topic = partitionsDiv.parent().find("a").attr("id");
    $.ajax({
        method: "GET", url: baseUrl + "/topics/"+topic+"/partitions/"
    }).done(function (data) {
        updatePartitionsHTML(data,partitionsDiv,topic);
    }).fail(function (e) {
        console.log("error", e);
        onError(e);
    }).always(function (data) {
        console.log("complete");
        $("#spinner").hide(500);
    });

}

// '<div class="bg-dark p-2 partition-container">'+
// '<span class="text-white">partition-0</span>'+
// '<i class="bi bi-play-circle-fill text-primary float-right"></i>'+
// '</div>';

function updatePartitionsHTML(data,partitionsDiv,topic){
    if(data){
        for(let i=0;i<data.length;i++){
            let partitionElem = $("#partition-container").clone(true,true);
            partitionElem.find(".text-white").text(topic+"-"+data[i]);
            partitionElem.appendTo(partitionsDiv);
        }
    }
}

function getTopics() {
    $("#spinner").show(500)
    $.ajax({
        method: "GET", url: baseUrl + "/topics"
    }).done(function (data) {
        updateTopics(data);
    }).fail(function (e) {
        console.log("error", e);
        onError(e);
    }).always(function (data) {
        console.log("complete");
        $("#spinner").hide(500);
    });
}

function onError(e){
    $(".toast-body").text(e.statusText)
    $(".toast").toast("show");
}

function updateTopics(topics){
    if(topics){
        for(let i=0;i<topics.length;i++){
            let topicElement = $("#topic-partition-container").clone(true,true);
            topicElement.attr("id","topic-partition-container-"+i);
            topicElement.find("#topic-label")
                .attr("id",topics[i])
                .attr("data-target","#partitions-"+i)
                .text(topics[i]);
            topicElement.find("#partitions").attr("id","partitions-"+i)
            topicElement.appendTo("#topics-container");
            topicElement.show(1000);
        }
    }

}

