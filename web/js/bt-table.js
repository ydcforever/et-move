/**
 * Created by T440 on 2020/6/8.
 */
/**
 * btTable在不侵入bootstrapTable原则下，扩展自定义属性，命名遵循非原有属性。
 * 开发者在上述前提下，可自由扩展。
 *
 * 已经完成：
 * 禁用onclickRow,已完成selectOnCheck,复写选择行 提供补偿接口eonClickRow
 * 1.可免除手写通用属性align: "center" , valign: "middle"，不影响原有列属性，如width,sortable
 * 2.form参数入参 或 qParams追加额外参数映射（一般二选一即可）
 * 3.分页参数映射，当前页根据后台起始页修改，此版后台起始页序号是1
 * 4.取消手写checkbox， 控制参数详见useCheck, checkAll
 * 5.columns自动填充及追加，如操作列的补充列。控制参数详见openFiller，cFiller，extendField, rejectFields
 * 6.增加行号rownumbers
 * 7.增加selectOnCheck，选中行同时触发checkbox
 * 8.增加rejectFields，主要用于屏蔽columns自动加载的列
 * --以下操作禁用onclickCell，已完成选择性编辑列，和暂存功能,
 * --禁用responseHandler,,提供补偿接口eResponseHandler
 * 9.增加editable编辑控制，isEditFields接受或拒绝编辑指定列， editFields指定编辑列
 * 10.增加编辑后的处理函数operator,提供insertHandler， updateHandler, deleteHandler接口
 * 11.内置函数
 *    1.getBtSelectRows 取代原有获取选中行
 *    2.onEditableSave(config) 保存编辑修改
 *    3.insert(config) 插入行
 *    4.del(config, keyField) 删除行
 *    5.onEditableCell(config) 编辑单元格
 *    6.getBtFieldSelection(field) return String[]
 *    7.getBtSelection(fields) return Object[]
 * 未完成：
 * 1.服务端分页，如何前端排序？
 * Created by ydc on 2018/7/4.
 */
(function ($) {
    var btTools = {
        getConfig: function (config) {
            var _config = {
                height: 480,
                cache: false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
                //striped: true,
                //undefinedText: '-',
                //toolbar: "#toolbar",
                //sortable: true,
                //sortOrder: "asc",
                //sortName: "列名",
                //onSort: function (name, order) {
                //    $('#sql_area').bootstrapTable('refreshOptions', {
                //        sortName: name,
                //        sortOrder: order
                //    });
                //},
                //showToggle: true,
                //showColumns: true,
                singleSelect: false,//多选
                pagination: false,
                pageList: [10, 20, 30, 1000, 10000],
                pageSize: 10,
                pageNumber: 1,
                paginationPreText: "上一页",
                paginationNextText: "下一页",
                sidePagination: "server",
                onLoadSuccess: function () {

                },
                onLoadError: function () {
                    console.info("加载数据失败");
                },
                //自定义属性
                tTitle: "",//表题，目前无效
                refreshBtn: null, //刷新按钮
                queryForm: null, //查询条件表单
                formField: "name", //input对象属性
                qParams: {}, //查询补充传参
                pPage: "page", //后台分页参数映射
                pPageSize: "pageSize", //后台分页参数映射
                openFiller: true, //是否启用补全,
                cFiller: {
                    align: "center",
                    valign: "middle"
                    //editable: {
                    //type:"text"
                    //canEdit: true,
                    //mode:"popup"
                    //}
                }, //默认columns属性补全
                rownumbers: false,//行号显示
                rownumbersField: "bt-line-no",
                useCheck: false, //columns追加checkbox
                checkField: "check",
                checkAll: false, //是否默认全选
                selectOnCheck: false,//选择时选中check
                eonClickRow: function (row, $element) {
                }, //扩展行点击事件，选中同时选中checkbox，和onClickRow用法一样
                eResponseHandler:function(data){
                },//作为responseHandler补偿接口
                extendField: function (columns) {
                    return columns;
                },//仅限自动columns扩展，如操作列
                rejectFields: [],//屏蔽列
                editable: false,//是否可编辑
                isEditFields: false, //拒绝或者接受编辑
                editFields: [],//指定编辑列或不可编辑列，和isEditFields连用
                operator: function ($table, config) {
                },//用于表格操作
                tableName: "",
                insertHandler: function (tableName, rows) {
                },//插入操作
                updateHandler: function (tableName, rows) {
                },//更新操作
                deleteHandler: function (tableName, rows) {
                },//删除操作
                save: {
                    update: {},
                    insert: {},
                    delete: []
                },//记录操作
                cancel: function () {
                    _config.save = {
                        update: {},
                        insert: {},
                        delete: []
                    }
                }//清除暂存操作
            };
            $.extend(true, _config, config);
            if(!_config.striped){
                _config.striped = !_config.editable;
            }
            _config.rejectFields = _config.rejectFields.concat(["ROW_ID", "RN"]);
            if (!_config.isEditFields) {
                _config.editFields = _config.editFields.concat(["RN", _config.checkField, _config.rownumbersField]);
            }
            if (_config.columns) {
                var a = [];
                if (_config.rownumbers) {
                    a.push(btTools.lineNo(_config.rownumbersField));
                }
                if (_config.useCheck) {
                    a.push(btTools.ck(_config.checkField, _config.checkAll));
                }
                if (_config.openFiller) {
                    _config.columns[0] = a.concat(btTools.fillColumns(_config.columns[0], _config.cFiller));
                } else {
                    _config.columns[0] = a.concat(_config.columns[0]);
                }
            }
            return _config;
        },
        /**
         * generate columns depended on fields which user provided
         *
         * @param fields  [field1,field2,field3]
         * @param config
         * @returns {*[]} [{ title: field1, field: field1}]
         */
        defineColumns: function (fields, config) {
            var columns = [[]];
            if (config.useCheck) columns[0][0] = btTools.ck(config.checkField, config.checkAll);
            $(fields).each(function (i, val) {
                var index = config.useCheck ? i + 1 : i;
                columns[0][index] = btTools.col(val, val);
            });
            return columns;
        },
        /**
         * common property fill all columns
         *
         * @param columns
         * @param filler
         * @returns {*[]}
         */
        fillColumns: function (columns, filler) {
            var cols = [];
            $(columns).each(function (i) {
                cols[i] = $.extend(true, columns[i], filler);
            });
            return cols;
        },
        /**
         * generate columns depended on object fields
         * config {rejectFields,useCheck,checkField,checkAll}
         * @param object
         * @param config
         * @returns {*[]}
         */
        autoColumns: function (object, config) {
            var columns = [[]];
            var i = 0;
            if (config.rownumbers) {
                columns[0][i] = btTools.lineNo(config.rownumbersField);
                i++;
            }
            if (config.useCheck) {
                columns[0][i] = btTools.ck(config.checkField, config.checkAll);
                i++;
            }
            if (object) {
                $.each(object, function (field) {
                    if (!$.fn.findField(field, config.rejectFields)) {
                        columns[0][i] = btTools.col(field, field);
                        i++;
                    }
                });
            }
            return columns;
        },
        ck: function (checkField, checkAll) {
            return {
                field: checkField,
                checkbox: true,
                formatter: function () {
                    return {
                        checked: checkAll
                    };
                }
            }
        },
        lineNo: function (field) {
            return {
                title: "",
                field: field,
                formatter: function (value, row, index) {
                    return index + 1;
                }
            };
        },
        col: function (title, field) {
            return {
                title: title,
                field: field
                //align: "center",
                //valign: "middle"
            };
        },
        serializeFormPage: function (config, params) {
            var obj = {};
            if (config.queryForm !== null) {
                obj = btTools.serializeForm($(config.queryForm), config.formField);
            }
            if (config.pagination) {
                obj[config.pPage] = params.offset / params.limit + 1;
                obj[config.pPageSize] = params.limit;
            }
            $.extend(true, obj, config.qParams);
            return obj;
        },
        serializeForm: function ($form, formField) {
            var obj = {};
            $.each($form.serializeArray(), function () {
                if (obj[this[formField]]) {
                    obj[this[formField]] = obj[this[formField]] + ',' + this["value"];
                } else {
                    obj[this[formField]] = this["value"];
                }
            });
            return obj;
        }
    };

    $.fn.extend({
        resultHandler: function (config, data, callback) {
            var $table = this;
            if ($table.bootstrapTable("getOptions").columns[0].length == 0) {
                var col = btTools.autoColumns(config.pagination ? data.rows[0] : data[0], config);
                $table.bootstrapTable("refreshOptions", {columns: config.extendField(col)});
            }
            callback(data);
            return config.pagination ? data : {total: data.length, rows: data};
        },
        onEditableCell: function (config) {
            var $table = this;
            config.onClickCell = function (field, value, row, $element) {
                var accept = config.isEditFields ? $.fn.findField(field, config.editFields) : !$.fn.findField(field, config.editFields);
                if (accept) {
                    $element.attr("contenteditable", true);
                    $element.blur(function () {
                        var index = $element.parent().data("index");
                        row[field] = $element.text();
                        $table.bootstrapTable("updateCell", {field: field, index: index, value: $element.html()});
                        if (config.save.insert[index]) {
                            config.save.insert[index] = row;
                        } else {
                            config.save.update[index] = row;
                        }
                    });
                }
            };
        },
        insert: function (config) {
            var $table = this;
            var count = $table.bootstrapTable("getData").length;
            var newRow = {};
            newRow[config.rownumbersField] = count + 1;
            $table.bootstrapTable("insertRow", {index: count, row: newRow});
            config.save.insert[count] = {};
        },
        del: function (config, keyField) {
            var $table = this;
            var rows = $table.getBtSelectRows();
            config.save.delete = rows;
            var index = $.fn.getFieldSelection(rows, keyField);
            $table.bootstrapTable("remove", {field: keyField, values: index});
        },
        onEditableSave: function (config) {
            var $table = this;
            var insertRow = [];
            for (var key in config.save.insert) {
                insertRow.push(config.save.insert[key]);
            }
            insertRow = $.fn.getSelectionNoFields(insertRow, [config.checkField, config.rownumbersField]);
            if (insertRow.length > 0) {
                config.insertHandler(config.tableName, insertRow);
            }

            var updateRow = [];
            for (var k  in config.save.update) {
                updateRow.push(config.save.update[k]);
            }
            updateRow = $.fn.getSelectionNoFields(updateRow, [config.checkField, config.rownumbersField]);
            if (updateRow.length > 0) {
                config.updateHandler(config.tableName, updateRow);
            }

            if (config.save.delete.length > 0) {
                config.deleteHandler(config.tableName, config.save.delete);
            }
            //$table.bootstrapTable("destroy").bootstrapTable(config);
            config.cancel();
        },
        getBtSelectRows: function () {
            var $table = this;
            var data = $table.bootstrapTable("getData");
            var rows = [];
            $table.find("tr.selected").each(function (i) {
                var index = $(this).data("index");
                rows.push(data[index]);
            });
            return rows;
        },
        getBtFieldSelection: function (field) {
            var $table = this;
            var rows = $table.bootstrapTable("getAllSelections");
            return $.fn.getFieldSelection(rows, field);
        },
        getBtSelection: function (fields) {
            var $table = this;
            var rows = $table.bootstrapTable("getAllSelections");
            return $.fn.getSelection(rows, fields);
        },
        findField : function(field, fields){
            for(var i = 0, len = fields.length ; i < len; i++) {
                if(fields[i] == field){
                    return true;
                }
            }
            return false;
        },
        findObject : function(field, objects){
            for(var i = 0, len = objects.length ; i < len; i++) {
                if(objects[i].field == field){
                    return true;
                }
            }
            return false;
        },
        getFieldSelection: function(rows, field){
            var val = [];
            $.each(rows, function (i) {
                val.push(rows[i][field]);
            });
            return val;
        },
        getSelectionNoFields:function(rows, fields){
            if(fields === undefined) {
                return rows;
            }else {
                var rebuild = [];
                $.each(rows, function(k){
                    var row = {};
                    $.each(rows[k], function(field){
                        if(!$.fn.findField(field, fields)){
                            row[field] = rows[k][field];
                        }
                    });
                    rebuild.push(row);
                });
                return rebuild;
            }
        },
        getSelection: function(rows, fields){
            if(fields === undefined) {
                return rows;
            }else {
                var rebuild = [];
                $.each(rows, function(k){
                    var row = {};
                    $.each(fields, function(){
                        row[this] = rows[k][this];
                    });
                    rebuild.push(row);
                });
                return rebuild;
            }
        }
    });
    $.fn.btTable = function (config) {
        var $table = this;
        //if(config.tTitle){
        //    $table.before($("<h4>" + config.tTitle +"</h4>"));
        //}
        var _config = btTools.getConfig(config);
        _config.onClickRow = function (row, $element) {
            if (_config.selectOnCheck) {
                $element.find("[type='checkbox']").click();
            }
            $element.toggleClass("selected");
            //if (_config.editable) {
            //    $element.attr("contenteditable", true);
            //}
            _config.eonClickRow(row, $element);
        };

        if (_config.editable) {
            $table.onEditableCell(_config);
            _config.operator($table, _config);
        }

        if (_config.data === undefined) {
            _config.queryParams = function (params) {
                return btTools.serializeFormPage(_config, params);
            };
            _config.responseHandler = function (data) {
                return $table.resultHandler(_config, data, _config.eResponseHandler);
            };
        } else{
            if (_config.columns === undefined) {
                var col = btTools.autoColumns(_config.pagination ? _config.data.rows[0] : _config.data[0], _config);
                _config.columns = _config.extendField(col);
            }
        }
        $table.bootstrapTable("destroy").bootstrapTable(_config);
        if (_config.refreshBtn) {
            $(_config.refreshBtn).click(function () {
                $table.bootstrapTable("refresh");
            });
        }
    }
})(jQuery);