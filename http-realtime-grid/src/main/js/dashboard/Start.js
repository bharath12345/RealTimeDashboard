define(["dojo/_base/declare", "dojo/request/xhr", "dojo/dom-construct", "dijit/layout/BorderContainer",
    "dijit/layout/ContentPane", "dashboard/GridX"],

    function (declare, xhr, domConstruct, BorderContainer, ContentPane, GridX) {

        var Start = declare("dashboard.Start", null, {

            start:function () {

                var topBC = dashboard.topBC = new BorderContainer({
                    design:"headline",
                    liveSplitters:false,
                    persist:true,
                    gutters:false,
                    style: "width: 100%; height: 100%;"
                });

                var cp = new ContentPane({
                    region:"center",
                    splitter:false,
                    style: "width: 100%; height: 100%;"
                });

                topBC.addChild(cp);
                topBC.placeAt(document.body);

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
                    
                    var json = dojo.fromJson(msg.data);
                    Start.grid.addRow(json);
                }

                socket.onclose = function () {
                    console.log('socket onclose readyState = ' + socket.readyState);
                }

            },

            createGrid:function (div) {
                var columnMeta = [];

                var columns = ["httpQuery", "responseCode", "httpContentType", "httpProtocolVersion", "httpPath", "httpMethod", "httpUri", "httpContentEncoding", "httpCharacterEncoding"];
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

                for (var i = 0; i < 1; i++) {
                    gridata.push(row);
                }

                try {
                    Start.grid = new GridX();
                    Start.grid.setColumnMeta(columnMeta);
                    Start.grid.setData(gridata);
                    Start.grid.render(div);
                    dashboard.topBC.resize();
                } catch (e) {
                    console.log("exception = " + e);
                }
                
                var testdata = {"httpQuery":null,"responseCode":0,"httpContentType":null,"httpProtocolVersion":null,"httpPath":"","httpMethod":"POST","httpUri":"/realtime","httpContentEncoding":null,"httpCharacterEncoding":null};
                Start.grid.addRow(testdata);

                this.startWebSocketReceiver();

            }

        });
        
        Start.grid = null;

        return Start;
    }
);