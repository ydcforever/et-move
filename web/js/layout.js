/**
 * Created by T440 on 2019/11/5.
 */

function loadLayout($table, objects, clsName) {
    for (var i = 0, len = objects.length; i < len; i += 1) {
        var row = new TrComponent($table.attr("id"), objects[i], i);
        row.build(clsName);
    }
}

function TrComponent(tableId, objects, rowId) {
    this.table = document.getElementById(tableId);
    this.columnSize = objects.row.length;
    this.objects = objects.row;
    this.rowId = tableId + rowId;
}

TrComponent.prototype.build = function (clsName) {
    var trElement = tr(this.rowId);
    for (var i = 0; i < this.columnSize; i++) {
        var query = new TdComponent(this.objects[i], clsName);
        query.build(trElement);
    }
    this.table.appendChild(trElement);
};

function TdComponent(object, clsName) {
    //组件类型
    this.type = object.type;
    //外部展示
    this.title = object.title;
    this.field = object.field;
    this.value = object.value;
    //占几个tr
    this.col = object.col;
    //指定宽度
    this.width = object.width;
    //是否标红
    this.markStar = object.markStar;
    //初始化url
    this.initUrl = object.initUrl;
    //初始化数据
    this.initData = object.initData;
    //使用数据列表
    this.useDataList = object.useDataList;
    this.selectedData = object.selectedData;

    //内部id
    this.id = this.field;
    this.clsName = object.classify == undefined ? clsName : clsName + " " + object.classify;
    this.dataListId = this.field + "Data";
    this.selectId = this.field + "Select";
}

TdComponent.prototype = {
    init: function () {
        this.isInit = !(isBlank(this.initUrl) && isBlank(this.initData));
        if (isBlank(this.useDataList) || !this.isInit) this.useDataList = false;
        if (isBlank(this.markStar)) this.markStar = false;
        if (isBlank(this.type)) this.type = "none";
    },
    build: function (trElement) {
        this.init();
        var tdELE = td(this.col);
        var ele;
        if (!isBlank(this.title)) {
            tdELE.appendChild(span(this.title));
            tdELE.appendChild(star(this.markStar));
        }
        if (this.type.toLowerCase() == "text") {
            ele = input(this.id, this.clsName, "text", this.width);
            tdELE.appendChild(ele);
        }
        if (this.type.toLowerCase() == "button") {
            ele = input(this.id, this.clsName + "_bnt", "button", this.width);
            ele.value = this.value;
            tdELE.appendChild(ele);
        }
        if (this.useDataList) ele.setAttribute("list", this.dataListId);
        if (this.isInit) {
            if (this.useDataList) {
                if (this.selectedData != undefined) ele.value = this.selectedData;
                var dataListElement = dataList(this.dataListId);
                var selectElement = select(this.selectId);
                if(this.initData) {
                    dataSelect(selectElement, this.initData);
                }
                if(this.initUrl) {
                    urlSelect(selectElement, this.initUrl);
                }
                dataListElement.appendChild(selectElement);
                tdELE.appendChild(dataListElement);
            } else {
                ele.value = this.initData;
            }
        }
        trElement.appendChild(tdELE);
    }
};

function tr(id) {
    var trElement = document.createElement("tr");
    if (!isBlank(id)) trElement.id = id;
    return trElement;
}

function td(colspan) {
    var td = document.createElement("td");
    if (colspan !== undefined) td.setAttribute("colspan", colspan);
    return td;
}

function dataList(id) {
    var dataListElement = document.createElement("dataList");
    dataListElement.id = id;
    return dataListElement;
}
function select(id) {
    var selectElement = document.createElement("select");
    selectElement.id = id;
    return selectElement;
}

function urlSelect(selectElement, url) {
    $.ajax({
        url: url,
        dataType: "json",
        success: function (data) {
            $(data).each(function (i) {
                var option = optionELE(this);
                if (i == 0) {
                    option.selected = true;
                }
                selectElement.options.add(option);
            });
        }
    });
}

function dataSelect(selectElement, data) {
    for (var k = 0, len = data.length; k < len; k++) {
        var option = optionELE(data[k]);
        if (k == 0) {
            option.selected = true;
        }
        selectElement.options.add(option);
    }
}

function span(title, cls) {
    var spanElement = document.createElement("span");
    spanElement.style.display = "inline-block";
    if (!isBlank(cls)) spanElement.className = cls;
    spanElement.innerText = title;
    spanElement.style.textAlign = "right";
    return spanElement;
}

function star(markStar) {
    var starElement = document.createElement("span");
    starElement.innerText = "*";
    starElement.verticalAlign = "middle";
    starElement.style.float = "right";
    if (markStar) {
        starElement.style.color = "red";
    } else {
        starElement.style.color = "rgba(255, 0, 0, 0)";
    }
    return starElement;
}

function optionELE(data) {
    var optionElement = document.createElement("option");
    optionElement.value = data;
    return optionElement;
}

function input(id, className, type, width) {
    var inputElement = document.createElement("input");
    inputElement.style.boxSizing = "border-box";
    inputElement.type = type;
    inputElement.style.textAlign = "center";
    inputElement.style.width = width == undefined ? "100%" : width;
    inputElement.id = id;
    inputElement.className = className;
    return inputElement;
}

function isBlank(obj) {
    return obj == null || obj == undefined || obj == "";
}
