define(["dojo/_base/declare", "dojo/i18n",
    "dojo/store/Memory", "dojo/_base/lang", "dojo/store/Observable", 'gridx/Grid', 'gridx/core/model/cache/Sync',
    'gridx/modules/SingleSort', 'gridx/modules/ColumnResizer', "gridx/modules/Pagination", "gridx/modules/Filter",
    "gridx/modules/Bar", "gridx/support/Summary", "gridx/support/DropDownPager", "gridx/support/QuickFilter",
    "gridx/support/LinkSizer", "gridx/support/LinkPager"],

    function (declare, i18n, Memory, lang, Observable, Grid, Cache, SingleSort, ColumnResizer, Pagination, Filter, Bar, Summary, DropDownPager, QuickFilter, LinkSizer, LinkPager) {

        var GridX = declare("GridX", null, {

            setColumnMeta:function (columnMeta) {
                this.columnMeta = columnMeta;
            },

            setData:function (gridata) {
                this.gridDataStore = Observable(new Memory({data:gridata,
                    idProperty:'id',
                    query:function (query, options) {
                        options = options || {};
                        //options.sort = [ {
                        //    attribute : [{attribute:"appName"}]
                        //} ];
                        return Memory.prototype.query.call(this, query, options);
                    }
                }));
            },

            render:function (div) {
                this.grid = new Grid({
                    cacheClass:Cache,
                    store:this.gridDataStore,
                    structure:this.columnMeta,
                    modules:[
                        SingleSort,
                        ColumnResizer,
                        Pagination,
                        Filter,
                        Bar
                    ],
                    barTop:[
                        Summary,
                        DropDownPager,
                        {pluginClass:QuickFilter, style:'text-align: right;'}
                    ],
                    barBottom:[
                        LinkSizer,
                        {pluginClass:LinkPager, style:'text-align: right;'}
                    ]
                }, div);
                this.grid.startup();
            },

            addRow:function (row) {
                this.gridDataStore.store.add(row);
            },

            getRow:function (evt) {
                //return this.grid.row(evt);
            },

            handleRowClick:function (object) {
                //this.grid.on(".dgrid-row:click", lang.hitch(object, object.handleRowClick));
            }

        });

        return GridX;
    });