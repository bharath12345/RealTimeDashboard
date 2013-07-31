define(["dojo/_base/declare", "dojo/request/xhr", "dojo/dom-construct", "dijit/layout/BorderContainer",
    "dijit/layout/ContentPane", "dashboard/GridX"],

    function (declare, xhr, domConstruct, BorderContainer, ContentPane, GridX) {

        var Start = declare("dashboard.Start", null, {

            grid: null,

            start:function () {

                var topBC = new BorderContainer({
                    design:"headline",
                    liveSplitters:false,
                    persist:true,
                    gutters:false
                });

                var cp = new ContentPane({
                    region:"center",
                    splitter:false
                });

                topBC.addChild(cp);
                topBC.startup();
                topBC.resize();

                var div = domConstruct.create("div");
                cp.domNode.appendChild(div, {style:'width: 100%; height: 100%;'});

                this.createGrid(div);
            },

            startWebSocketReceiver:function () {

                var host = 'ws://localhost:9090/httpJsonHeaderTopic';
                var socket = new WebSocket(host);
                socket.onopen = function () {
                    console.log('socket onopen readyState = ' + socket.readyState);
                }

                socket.onmessage = function (msg) {
                    console.log('websocket message' + msg);
                    console.log('websocket data = ' + msg.data);
                }

                socket.onclose = function () {
                    console.log('socket onclose readyState = ' + socket.readyState);
                }

            },

            createGrid:function (div) {
                var columnMeta = [];

                var columns = input.columns; // ToDo: this column info needs passing
                var gridata = [];
                var row = {};
                for (var i = 0; i < columns.length; i++) {
                    var col = {};
                    col.field = columns[i];
                    col.name = columns[i];
                    columnMeta.push(col);

                    // create blank grid row
                    row[col.field] = "-";
                }

                for (var i = 0; i < 25; i++) {
                    gridata.push(row);
                }

                try {
                    this.grid = new GridX();
                    this.grid.setColumnMeta(columnMeta);
                    this.grid.setData(gridata);
                    this.grid.render(div);
                } catch (e) {
                    console.log("exception = " + e);
                }

                this.startWebSocketReceiver();

            }

        });

        return Start;
    }
);