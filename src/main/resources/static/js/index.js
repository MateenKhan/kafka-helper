let baseUrl = "http://localhost:8080"
let treeData = [];

$(document).ready(function () {
    getPartitions();
});

function getPartitions() {
    $.ajax({
        method: "GET", url: baseUrl + "/topics/partitions"
    }).done(function (data) {
        updateTopics(data);
    }).fail(function (e) {
        console.log("error", e);
    }).always(function (data) {
        console.log("complete");
    });
}

function buildTreeWithData() {
    $('#tree').bstreeview({
        data: treeData, indent: 1.25, parentsMarginLeft: '1.25rem', openNodeLinkOnNewTab: true
    });
}


function updateTopics(data) {
    prepareData(data);
    buildTreeWithData();
}

function prepareData(data) {
    let result = [];
    if (data) for (const [key, value] of Object.entries(data)) {
        let nodes = [];
        let item = {"text": key, "icon": "fa fa-inbox fa-fw", "nodes": nodes}
        for (let i = 0; i < value.length; i++) {
            let node = {"text": key + "-" + value[i], "icon": "fa fa-inbox fa-fw"};
            nodes[i] = node;
        }
        result[result.length] = item;
    }
    treeData = result;
}