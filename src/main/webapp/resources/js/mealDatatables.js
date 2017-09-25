var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

$("#dateTime").datetimepicker({
    format: 'Y-m-d H:m:s',
});
$("#startDate").datetimepicker({
    timepicker:false,
    format:'Y-m-d',
});
$("#endDate").datetimepicker({
    timepicker:false,
    format:'Y-m-d',
});
$("#startTime").datetimepicker({
    datepicker:false,
    format:'H:i'
});
$("#endTime").datetimepicker({
    datepicker:false,
    format:'H:i'
});

function updateTable() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize(),
        success: updateTableByData
    });
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}

$(function () {
    datatableApi = $("#datatable").DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime",
                "render": function (date, type, row) {
                    if (type === "display") {
                        return date.substring(0, 10) + " " + date.substring(11, 20);
                    }
                    return date;
                }
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "orderable": false,
                "defaultContent": "",
                "render": renderEditBtn
            },
            {
                "orderable": false,
                "defaultContent": "",
                "render": renderDeleteBtn
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ],
        "createdRow": function (row, data, dataIndex) {
            if (data.exceed) {
                $(row).addClass("exceeded");
            } else {
                $(row).addClass("normal");
            }
        },
        "initComplete": makeEditable
    });
});