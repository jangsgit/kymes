 

    window.onload = function() {   
        var view_p01tab01page;
        var view_machnmpage;
        var view_addinfopage;  
        var wtab01Grid;
        var machnmGrid;
        var addinfoGrid; 
        var machnmChart;
        var addinfoChart;
        var labels ; 
        var dataset ;
        var dataChart = [];
        // 전역 변수로 차트 객체 선언
        let chartInstance;
       //실시간현황
       view_p01tab01page = new wijmo.collections.CollectionView(getData_tab01('%', '%', '%', '%', "list"), { 
        }); 


        wtab01Grid = new wijmo.grid.FlexGrid('#wtab01Grid_div', {  
            autoGenerateColumns: false,
            showMarquee: true,
            isReadOnly: true,  
            allowSorting: 'MultiColumn',
            alternatingRowStep: 0,
            columns: [     
                { binding: 'shottime', header: '시간', align: "center" ,  width: 120 },   
                { binding: 'machine_name', 
                            header: '기계', 
                            align: "center", 
                            width: 50,
                            cellTemplate: wijmo.grid.cellmaker.CellMaker.makeLink({ 
                                text: '<b>${item.machine_name}</b>',
                                click: (e, ctx) => {
                                    chart_list(ctx.item.machine_name, ctx.item.Additional_Info_1);                    
                                }
                            }) 
                },     
                { binding: 'Additional_Info_1', 
                            header: '금형', 
                            align: "left", 
                            width: 200,
                            cellTemplate: wijmo.grid.cellmaker.CellMaker.makeLink({ 
                                text: '<b>${item.Additional_Info_1}</b>',
                                click: (e, ctx) => {
                                    chart_list(ctx.item.machine_name, ctx.item.Additional_Info_1);                    
                                }
                            }) 
                },     
                { binding: 'Shot_Number', header: '공정횟수', align: "right",  width: 80  },  
                { binding: 'Injection_Time', header: '사출시간', align: "right", format: 'n1',  width: 80  },   
                { binding: 'Filling_Time', header: '충진시간', align: "center", width: 80 },
                { binding: 'Plasticizing_Time', header: '계량시간', align: "right", format: 'n1',  width: 80  },  
                { binding: 'Cycle_Time', header: '공정시간', align: "center", width: 80 },
                { binding: 'Barrel_Temperature_1', header: '연장노즐', align: "right", format: 'n1',  width: 80  },  
                { binding: 'Barrel_Temperature_2', header: '노즐', align: "right", format: 'n1',  width: 80  },  
                { binding: 'Mold_Temperature_1', header: '이동측금형1', align: "right", format: 'n1',  width: 110  },   
                { binding: 'Mold_Temperature_2', header: '이동측금형2', align: "right", format: 'n1',  width: 110  }, 
                { binding: 'Mold_Temperature_3', header: '고정측금형1', align: "right", format: 'n1',  width: 110  }, 
                { binding: 'Mold_Temperature_4', header: '고정측금형2', align: "right", format: 'n1' ,  width: 110 }, 
                { binding: 'Mold_Temperature_5', header: '건조기', align: "right", format: 'n1',  width: 110  }, 
                { binding: 'Mold_Temperature_6', header: '온수기', align: "right", format: 'n1',  width: 110  },   
            ], 
            itemsSource: view_p01tab01page
        });   
        
        new FlexGridContextMenu(wtab01Grid);  
        setHeaderColumn(false);
        function setHeaderColumn(headerOn) {
            wtab01Grid.headersVisibility = headerOn
            ? wijmo.grid.HeadersVisibility.All
            : wijmo.grid.HeadersVisibility.Column; 
        } 



         // tab01검색 데이터 수집
       function getData_tab01(arg1, arg2, arg3, arg4, arg5){  
            var ls_machnm = arg1;
            var ls_addinfo = arg2;
            var ls_frdate = arg3;
            var ls_todate = arg4;   
            var wactnm_data = [];          
            var cnt = 1;     
            $.ajax({
                    url: '/appcms/reallist',   
                    type:"POST", 
                    data: {
                        "frdate" : ls_frdate , 
                        "todate" : ls_todate , 
                        "machnm" : ls_machnm , 
                        "addinfo" : ls_addinfo,
                        "listflag" : arg5
                    },
                    async:false,
                    success:function(data){   
                            var wtab01Dto = data; 
                            rowCount = data.length;   
                            for (var i = 0; i < rowCount; i++) {
                                    if(arg5 == "list"){
                                        wactnm_data.push({ 
                                            index: cnt,
                                            machine_name: wtab01Dto[i]["machine_name"], 
                                            Additional_Info_1: wtab01Dto[i]["additional_Info_1"], 
                                            Shot_Number: wtab01Dto[i]["shot_Number"], 
                                            Injection_Time: wtab01Dto[i]["injection_Time"], 
                                            Filling_Time: wtab01Dto[i]["filling_Time"], 
                                            Plasticizing_Time: wtab01Dto[i]["plasticizing_Time"], 
                                            Cycle_Time: wtab01Dto[i]["cycle_Time"], 
                                            Barrel_Temperature_1: wtab01Dto[i]["barrel_Temperature_1"], 
                                            Barrel_Temperature_2: wtab01Dto[i]["barrel_Temperature_2"], 
                                            Mold_Temperature_1: wtab01Dto[i]["mold_Temperature_1"], 
                                            Mold_Temperature_2: wtab01Dto[i]["mold_Temperature_2"], 
                                            Mold_Temperature_3: wtab01Dto[i]["mold_Temperature_3"], 
                                            Mold_Temperature_4: wtab01Dto[i]["mold_Temperature_4"], 
                                            Mold_Temperature_5: wtab01Dto[i]["mold_Temperature_5"], 
                                            Mold_Temperature_6: wtab01Dto[i]["mold_Temperature_6"],
                                            Cavity: wtab01Dto[i]["cavity"],  
                                            TimeStamp: wtab01Dto[i]["timeStamp"], 
                                            shottime: wtab01Dto[i]["shottime"], 
                                            shotdate: wtab01Dto[i]["shotdate"],      
                                            
                                        });

                                    }else{
                                        wactnm_data.push({ 
                                            index: cnt, 
                                            machine_name: wtab01Dto[i]["machine_name"], 
                                            Additional_Info_1: wtab01Dto[i]["additional_Info_1"], 
                                            MT01: wtab01Dto[i]["mt01"],  
                                            MT02: wtab01Dto[i]["mt02"], 
                                            MT03: wtab01Dto[i]["mt03"], 
                                            MT04: wtab01Dto[i]["mt04"], 
                                            MT05: wtab01Dto[i]["mt05"], 
                                            MT06: wtab01Dto[i]["mt06"], 
                                            shottime: wtab01Dto[i]["shottime"],       
                                            
                                        }); 
                                    }
                                    cnt++;
                              }  
                    },error:function(e){
                        console.log('error',e);
                    }
                }).done(function(fragment){           
                })   
                    return wactnm_data;  
       }  
       machnmChart = "샘플";
       addinfoChart = "Sample";
       dataC = [
            { machine_name : "1", Additional_Info_1: 'DM_RD', shottime: '21:23:54', MT01: 28.2, MT02: 28, MT03: 30.3, MT04: 30.5, MT05: 62.8, MT06: 0 },
            { machine_name : "1", Additional_Info_1: 'DM_RD', shottime: '21:23:16', MT01: 28.1, MT02: 28, MT03: 30.2, MT04: 30.4, MT05: 62.6, MT06: 0 },
            { machine_name : "1", Additional_Info_1: 'DM_RD', shottime: '21:22:38', MT01: 28, MT02: 28, MT03: 30.3, MT04: 30.5, MT05: 62.6, MT06: 0 },
            { machine_name : "1", Additional_Info_1: 'DM_RD', shottime: '21:22:01', MT01: 28.1, MT02: 28.1, MT03: 30.4, MT04: 30.5, MT05: 62.6, MT06: 0 },
            { machine_name : "1", Additional_Info_1: 'DM_RD', shottime: '21:20:45', MT01: 28.1, MT02: 28.1, MT03: 30.5, MT04: 30.5, MT05: 62.2, MT06: 0 },
            { machine_name : "1", Additional_Info_1: 'DM_RD', shottime: '21:20:08', MT01: 28.1, MT02: 28.1, MT03: 30.5, MT04: 30.6, MT05: 62.2, MT06: 0 },
            { machine_name : "1", Additional_Info_1: 'DM_RD', shottime: '21:19:30', MT01: 28, MT02: 28.1, MT03: 30.4, MT04: 30.6, MT05: 62.7, MT06: 0 },
            { machine_name : "1", Additional_Info_1: 'DM_RD', shottime: '21:18:52', MT01: 21.2, MT02: 28.1, MT03: 30.4, MT04: 30.6, MT05: 63, MT06: 0 },
            { machine_name : "1", Additional_Info_1: 'DM_RD', shottime: '21:18:15', MT01: 28.1, MT02: 28.1, MT03: 30.3, MT04: 30.5, MT05: 63.1, MT06: 0 },
            { machine_name : "1", Additional_Info_1: 'DM_RD', shottime: '21:17:37', MT01: 28.1, MT02: 28, MT03: 30.3, MT04: 30.5, MT05: 63.4, MT06: 0 },
            { machine_name : "1", Additional_Info_1: 'DM_RD', shottime: '21:17:00', MT01: 28.1, MT02: 28, MT03: 30.3, MT04: 30.4, MT05: 63.1, MT06: 0 },
            { machine_name : "1", Additional_Info_1: 'DM_RD', shottime: '21:16:22', MT01: 28.1, MT02: 28, MT03: 30.3, MT04: 30.4, MT05: 62.2, MT06: 0 }
        ];

        labels = dataC.map(entry => entry.shottime);
        dataset = [
            { label: '이동측금형1', data: dataC.map(entry => entry.MT01) },
            { label: '이동측금형2', data: dataC.map(entry => entry.MT02) },
            { label: '고정측금형1', data: dataC.map(entry => entry.MT03) },
            { label: '고정측금형2', data: dataC.map(entry => entry.MT04) },
            { label: '건조기', data: dataC.map(entry => entry.MT05) },
            { label: '온수기', data: dataC.map(entry => entry.MT06) }
        ]; 
        //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
        //기계구분 popup list
        view_machnmpage = new wijmo.collections.CollectionView(getData_machnm(), {
            // pageSize: 6
        });      
        machnmGrid = new wijmo.grid.FlexGrid('#machnmGrid_div', {  
            autoGenerateColumns: false, 
            selectionMode: 'RowRange',
            alternatingRowStep: 0,
            columns: [   
                {
                    binding: 'machine_name',
                    width: '1*',
                    header: '기계번호', 
                    align:"left", 
                } 

            ],       
                itemsSource: view_machnmpage
        });  
        // 데이터 수집
        function getData_machnm(){  
            var data05 = []; 
            return data05                                          
        } 
 

       //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
        //금형구분 popup list
        view_addinfopage = new wijmo.collections.CollectionView(getData_machnm(), {
            // pageSize: 6
        });      
        addinfoGrid = new wijmo.grid.FlexGrid('#addinfoGrid_div', {  
            autoGenerateColumns: false, 
            selectionMode: 'RowRange',
            alternatingRowStep: 0,
            columns: [   
                {
                    binding: 'additional_Info_1',
                    width: '1*',
                    header: '금형명', 
                    align:"left", 
                } 

            ],       
                itemsSource: view_addinfopage
        });   

 
        function machnm_list(arg){ 
            var wactnm_data = [];          
            var cnt = 1;    
            $.ajax({
                    url: '/appcms/machnmlist',   
                    type:"POST", 
                    data: { 
                        "machnm" : arg , 
                    },
                    async:false,
                    success:function(data){   
                            var wtab01Dto = data; 
                            rowCount = data.length;   
                            for (var i = 0; i < rowCount; i++) {
                                    wactnm_data.push({ 
                                        index: cnt,
                                        machine_name: wtab01Dto[i]["machine_name"] 
                                    });
                                    cnt++;
                              }  
                    },error:function(e){
                        console.log('error',e);
                    }
            }).done(function(fragment){           
            })  
            
            machnmGrid.columns.clear();  
            machnmGrid.autoGenerateColumns = false; 
            view_machnmpage.sourceCollection  = wactnm_data;
            
            var cols = new wijmo.grid.Column(); 
            cols.binding = 'machine_name';
            cols.header = '기계번호';
            cols.align = "center";
            cols.width = 200;  
            cols.cellTemplate = wijmo.grid.cellmaker.CellMaker.makeLink({ 
                text: '<b>${item.machine_name}</b>',
                click: (e, ctx) => {
                    machnmChk(ctx.item.machine_name);  
                }
            });
            machnmGrid.columns.push(cols);   
                
        }
        function machnmChk(arg1){    
            var ls_tab = document.getElementById('tabvalue').value; 
            if(ls_tab == "A"){
                document.getElementById('inmachname').value= arg1;  
                search_rtn()  
                var ls_inaddinfo = document.getElementById('inaddinfo').value; 
                    ls_inaddinfo = ls_inaddinfo.trim(); 
                    if(ls_inaddinfo == null || ls_inaddinfo.length == 0 || ls_inaddinfo == ""){ 
                        ls_inaddinfo    = "%"
                    }    
                chart_list(arg1, ls_inaddinfo); 
            }  
            $("#machnmmodal_btn").trigger("click"); 
        }  
        



        function addinfo_list(arg){ 
            var wactnm_data = [];          
            var cnt = 1;    
            $.ajax({
                    url: '/appcms/addinfolist',   
                    type:"POST", 
                    data: { 
                        "addinfo" : arg , 
                    },
                    async:false,
                    success:function(data){   
                            var wtab01Dto = data; 
                            rowCount = data.length;   
                            for (var i = 0; i < rowCount; i++) {
                                    wactnm_data.push({ 
                                        index: cnt,
                                        addinfo_name: wtab01Dto[i]["additional_Info_1"] 
                                    });
                                    cnt++;
                              }  
                    },error:function(e){
                        console.log('error',e);
                    }
            }).done(function(fragment){           
            })  
            
            addinfoGrid.columns.clear();  
            addinfoGrid.autoGenerateColumns = false; 
            view_addinfopage.sourceCollection  = wactnm_data;
            
            var cols = new wijmo.grid.Column(); 
            cols.binding = 'addinfo_name';
            cols.header = '금형자료';
            cols.align = "center";
            cols.width = 200;  
            cols.cellTemplate = wijmo.grid.cellmaker.CellMaker.makeLink({ 
                text: '<b>${item.addinfo_name}</b>',
                click: (e, ctx) => {
                    addinfoChk(ctx.item.addinfo_name);  
                }
            });
            addinfoGrid.columns.push(cols);   
                
        }
        function addinfoChk(arg1){    
            var ls_tab = document.getElementById('tabvalue').value; 
            if(ls_tab == "A"){
                document.getElementById('inaddinfo').value= arg1.trim(); 
                search_rtn()   
            var ls_inmachname = document.getElementById('inmachname').value; 
                ls_inmachname = ls_inmachname.trim(); 
                if(ls_inmachname == null || ls_inmachname.length == 0 || ls_inmachname == ""){ 
                    ls_inmachname    = "%"
                }    
                chart_list(ls_inmachname, arg1);  
            }  
            $("#addinfomodal_btn").trigger("click"); 
        }  
        const ctx = document.getElementById('temperatureChart').getContext('2d');
        function chart_list(arg1, arg2){
            var  ls_frdate     =  convert_date(wfrdate.value);                    
            var  ls_todate     =  convert_date(wtodate.value);  
            arg1 = arg1.trim()
            arg2 = arg2.trim() 
            const newdataChart = getData_tab01(arg1, arg2, ls_frdate, ls_todate, "chart")   
            dataChart = [...newdataChart] 
            machnmChart  = arg1
            addinfoChart = arg2
            labels = dataChart.map(entry => entry.shottime);
            dataset = [
                { label: '이동측금형1', data: dataChart.map(entry => entry.MT01) },
                { label: '이동측금형2', data: dataChart.map(entry => entry.MT02) },
                { label: '고정측금형1', data: dataChart.map(entry => entry.MT03) },
                { label: '고정측금형2', data: dataChart.map(entry => entry.MT04) },
                { label: '건조기', data: dataChart.map(entry => entry.MT05) },
                { label: '온수기', data: dataChart.map(entry => entry.MT06) }
            ];
  
            const canvasElement = document.getElementById('temperatureChart'); 
            
            //차트 업데이트
            if (chartInstance) {
                chartInstance.data.labels = labels;
                chartInstance.data.datasets = dataset.map((set, index) => ({
                    label: set.label,
                    data: set.data,
                    borderColor: `hsl(${index * 60}, 70%, 50%)`,
                    backgroundColor: `hsl(${index * 60}, 70%, 70%, 0.4)`,
                    fill: false,
                    tension: 0.1
                }));
                chartInstance.options.plugins.title.text = `기계: ${arg1} (금형: ${arg2}) 온도변화`;
                chartInstance.update(); // 차트 업데이트
            } 
        }
        //text: '기계:' + data.map(item => item.machine_name) + '(금형:' + data.map(item => item.additional_Info_1) + ') 온도변화'
        



        chartInstance = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: dataset.map((set, index) => ({
                    label: set.label,
                    data: set.data,
                    borderColor: `hsl(${index * 60}, 70%, 50%)`,
                    backgroundColor: `hsl(${index * 60}, 70%, 70%, 0.4)`,
                    fill: false,
                    tension: 0.1
                }))
            }, 
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: '기계:' + machnmChart + '(금형:' +  addinfoChart + ') 온도변화'
                    },
                    legend: {
                        display: true,
                        position: 'top'
                    }
                },
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: '시간'
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: '온도 (°C)'
                        },
                        beginAtZero: true
                    }
                }
            }
        });
             
          
          
        // tab01 검색   
        document.getElementById("retrieveBtn").addEventListener("click",function(){ 
            search_rtn() 
        })  

        document.getElementById("inmachname").addEventListener("keyup", (e)=>{ 
            if(e.keyCode === 13){  
                machnm_list("%")
                document.getElementById("inbtn01").click();
            }
        });  
        document.getElementById("inaddinfo").addEventListener("keyup", (e)=>{ 
            if(e.keyCode === 13){  
                addinfo_list("%")
                document.getElementById("inbtn02").click();
            }
        });  
        document.getElementById("inmachname").addEventListener("click", (e)=>{  
                machnm_list("%")
                document.getElementById("inbtn01").click(); 
        }); 
        document.getElementById("inaddinfo").addEventListener("click", (e)=>{  
                addinfo_list("%")
                document.getElementById("inbtn02").click(); 
        });




        function convert_date(arg_date){  
            var ls_arg_date = String(arg_date); 
            if(ls_arg_date == null || ls_arg_date.length == 0 || ls_arg_date == ""|| ls_arg_date == "null"){  
                return "";
            }  
            // Date 객체로 변환
            const dateObj = new Date(arg_date);

            // 년, 월, 일 추출 (월은 0부터 시작하므로 +1 필요)
            const year = dateObj.getFullYear();
            const month = String(dateObj.getMonth() + 1).padStart(2, '0');
            const day = String(dateObj.getDate()).padStart(2, '0');


            // 시간과 분 추출
            const hours = String(dateObj.getHours()).padStart(2, '0');
            const minutes = String(dateObj.getMinutes()).padStart(2, '0');

            // "YYYY-MM-DD" 형식으로 변환
            const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}`;
            arg_date = formattedDate; 
            return arg_date; 
        }



        // Initialize charts
        createPieChart(document.getElementById('chart7').getContext('2d'), 125, 75);
        createPieChart(document.getElementById('chart8').getContext('2d'), 125, 75);


    }

 


 
    function init(){ 
            
            wfrdate = new wijmo.input.InputDateTime('#frdate', {
                isRequired: false,
                value: null,
                placeholder: '',
                format: 'yyyy-MM-dd HH:mm', 
            });
            wtodate = new wijmo.input.InputDateTime('#todate', {
                isRequired: false,
                value: null,
                placeholder: '',
                format: 'yyyy-MM-dd HH:mm', 
            }); 

            var result = new Date();
            result.setHours(result.getHours() + 9); 
            var ls_date01 = result.toISOString().substring(0,10);    
            
            // ls_date02 값을 날짜와 시간을 "23:59"로 설정
            
            // result.setHours(result.getHours() + 10); 
            var today = new Date();
            today.setHours(23, 59, 0, 0); // 시간: 23, 분: 59, 초: 0, 밀리초: 0 
            var ls_date02 = today.toISOString().substring(0, 10) + " " + today.toTimeString().substring(0, 5); 
                          
            wfrdate.value = ls_date01;   
            wtodate.value = ls_date02;  
            
    } 
    


    function getToday(){
        let today = new Date();   

        let year = today.getFullYear(); // 년도
        let month = today.getMonth() + 1;  // 월
        let date = today.getDate();  // 날짜
        let day = today.getDay();  // 요일
        if(month > 0 && month < 10) {month = "0" + month};
        if(date > 0 && date < 10) {date = "0" + date};
        let ls_frday = year + '-' + month + '-01'  
        let ls_today = year + '-' + month + '-' + date 
        document.getElementById('frdate').value = ls_frday;  
        document.getElementById('todate').value = ls_today;
        
        // document.getElementById('todate').value = ls_today;
    }




    function convert_time(timestamp){ 
        //현재시간
        var date = new Date(timestamp); 
        var year = date.getFullYear();
        var month = ("0" + (1 + date.getMonth())).slice(-2);
        var day = ("0" + date.getDate()).slice(-2); 
        var hh = date.getHours();  
        var min = ('0' + date.getMinutes()).slice(-2) 
        time = year +  month +  day   ;
        return time;  
    } 


 

        class FlexGridContextMenu {
                        constructor(grid) {
                            let host = grid.hostElement, menu = this._buildMenu(grid);
                            host.addEventListener('contextmenu', (e) => {
                                // 클릭된 셀/열 선택
                                let sel = grid.selection, ht = grid.hitTest(e), row = ht.getRow();
                
                                switch (ht.panel) {
                                    case grid.cells:
                                    let colIndex = ht.col;
                                    // 만약 그룹 헤더이면, 그룹 열 선택
                                    if (row instanceof wijmo.grid.GroupRow && row.dataItem instanceof wijmo.collections.CollectionViewGroup) {
                                        let gd = row.dataItem.groupDescription;
                                        if (gd instanceof wijmo.collections.PropertyGroupDescription) {
                                        let col = grid.getColumn(gd.propertyName);
                                        if (col && col.index > -1) {
                                            colIndex = col.index;
                                        }
                                        }
                                    }
                                    grid.select(ht.row, colIndex);
                                    break;
                                    case grid.columnHeaders:
                                    grid.select(sel.row, ht.col);
                                    break;
                                    case grid.rowHeaders:
                                    grid.select(ht.row, sel.col);
                                    break;
                                    default:
                                    return; // 유효하지 않는 패널
                                }
                                // 현재 열에 대한 메뉴 표시
                                if (grid.selection.col > -1) {
                                    e.preventDefault(); // 브라우저의 기본 메뉴 취소
                                    menu.show(e); // Wijmo 메뉴 표시
                                }
                            }, true);
                        }
                        _buildMenu(grid) {
                            let menu = new wijmo.input.Menu(document.createElement('div'), {
                            owner: grid.hostElement,
                            displayMemberPath: 'header',
                            subItemsPath: 'items',
                            commandParameterPath: 'cmd',
                            dropDownCssClass: 'ctx-menu',
                            openOnHover: true,
                            closeOnLeave: true,
                            itemsSource: [
                                {
                                header: '정렬', items: [
                                    { header: 'Ascending', cmd: 'SRT_ASC' },
                                    { header: 'Descending', cmd: 'SRT_DESC' },
                                    { header: 'No Sort', cmd: 'SRT_NONE' },
                                    { header: '-' },
                                    { header: 'Clear All Sorts', cmd: 'SRT_CLR' }
                                ]
                                },
                                { header: '-' },
                                { header: 'Pin/Unpin', cmd: 'PIN' },
                                { header: '-' },
                                { header: 'AutoSize', cmd: 'ASZ' },
                                { header: 'AutoSize All', cmd: 'ASZ_ALL' },
                                { header: '-' },
                                { header: 'Group/Ungroup', cmd: 'GRP' },
                                { header: 'Clear All Groups', cmd: 'GRP_CLR' },
                                { header: '-' },
                                {
                                header: '내려받기', items: [
                                    { header: 'CSV', cmd: 'X_CSV' },
                                    { header: 'XLSX', cmd: 'X_XLSX' },
                                    { header: 'PDF', cmd: 'X_PDF' },
                                ]
                                }
                            ],
                            command: {
                                // 메뉴 명령 활성화/비활성화
                                canExecuteCommand: (cmd) => {
                                let view = grid.collectionView, col = grid.columns[grid.selection.col];
                                switch (cmd) {
                                    case 'SRT_ASC':
                                    return col.currentSort != '+';
                                    case 'SRT_DESC':
                                    return col.currentSort != '-';
                                    case 'SRT_NONE':
                                    return col.currentSort != null;
                                    case 'SRT_CLR':
                                    return view.sortDescriptions.length > 0;
                                    case 'PIN':
                                    return true; // toggle pin
                                    case 'ASZ':
                                    case 'ASZ_ALL':
                                    return true;
                                    case 'GRP':
                                    return col.dataType != wijmo.DataType.Number; // don't group numbers
                                    case 'GRP_CLR':
                                    return view.groupDescriptions.length > 0;
                                }
                                return true;
                                },
                                // 메뉴 명령 실행
                                executeCommand: (cmd) => {
                                    let view = grid.collectionView, cols = grid.columns, col = cols[grid.selection.col], sd = view.sortDescriptions, gd = view.groupDescriptions;
                                    switch (cmd) {
                                        case 'SRT_ASC':
                                        case 'SRT_DESC':
                                        case 'SRT_NONE':
                                        if (grid.allowSorting != wijmo.grid.AllowSorting.MultiColumn) {
                                            sd.clear();
                                        }
                                        else {
                                            for (let i = 0; i < sd.length; i++) {
                                            if (sd[i].property == col.binding) {
                                                sd.removeAt(i);
                                                break;
                                            }
                                            }
                                        }
                                        if (cmd != 'SRT_NONE') {
                                            sd.push(new wijmo.collections.SortDescription(col.binding, cmd == 'SRT_ASC'));
                                        }
                                            break;
                                        case 'SRT_CLR':
                                            sd.clear();
                                            break;
                                        case 'PIN':
                                            let fCols = grid.frozenColumns;
                                            if (col.index >= fCols) { // pinning
                                                cols.moveElement(col.index, fCols, false);
                                                cols.frozen++;
                                            }
                                            else { // unpinning
                                                cols.moveElement(col.index, fCols - 1, false);
                                                cols.frozen--;
                                            }
                                            break;
                                        case 'ASZ':
                                            grid.autoSizeColumn(col.index);
                                            break;
                                        case 'ASZ_ALL':
                                            grid.autoSizeColumns(0, grid.columns.length - 1);
                                            break;
                                        case 'GRP':
                                        // 그룹 삭제
                                            for (let i = 0; i < gd.length; i++) {
                                                if (gd[i].propertyName == col.binding) {
                                                gd.removeAt(i);
                                                return; // we're done
                                                }
                                            }
                                            // 그룹 추가
                                            gd.push(new wijmo.collections.PropertyGroupDescription(col.binding));
                                            break;
                                        case 'GRP_CLR':
                                            gd.clear();
                                            break;
                                        // 내보내기
                                        case 'X_CSV':
                                            let rng = new wijmo.grid.CellRange(0, 0, grid.rows.length - 1, grid.columns.length - 1), csv = grid.getClipString(rng, wijmo.grid.ClipStringOptions.CSV, true, false);
                                            wijmo.saveFile(csv, '재고등록.csv');
                                            break;
                                        case 'X_XLSX':
                                            const gridView = grid.collectionView;
                                            let oldPgSize = gridView.pageSize, oldPgIndex = gridView.pageIndex;
                
                                            grid.beginUpdate();
                                            gridView.pageSize = 0;
                
                                            wijmo.grid.xlsx.FlexGridXlsxConverter.saveAsync(grid, {includeCellStyles: true, includeColumnHeaders: true}, '재고등록.xlsx',
                                            saved => {
                                            gridView.pageSize = oldPgSize;
                                            gridView.moveToPage(oldPgIndex);
                                            grid.endUpdate();
                                            }, null
                                            ); 
                                            break;
                                        case 'X_PDF':
                                            wijmo.grid.pdf.FlexGridPdfConverter.export(grid, '재고등록.pdf', {
                                                maxPages: 10,
                                                scaleMode: wijmo.grid.pdf.ScaleMode.PageWidth,
                                                documentOptions: {
                                                compress: true,
                                                header: { declarative: { text: '\t&[Page] of &[Pages]' } },
                                                footer: { declarative: { text: '\t&[Page] of &[Pages]' } },
                                                info: { author: 'GrapeCity', title: '재고등록' }
                                                },
                                                //내장된 폰트 정보
                                                embeddedFonts: [
                                                    {
                                                    source: "https://assets.codepen.io/975719/BMHANNAPro.ttf",
                                                    name: "BMHANNAPro",
                                                    style: "normal",
                                                    weight: "normal",
                                                    sansSerif: true
                                                    }
                                                ],
                                                styles: {
                                                    cellStyle: { backgroundColor: '#ffffff', borderColor: '#c6c6c6', font: {family: "BMHANNAPro"} },
                                                    altCellStyle: { backgroundColor: '#f9f9f9' },
                                                    groupCellStyle: { backgroundColor: '#dddddd' },
                                                    headerCellStyle: { backgroundColor: '#eaeaea' }
                                                }
                                            });
                                            break;
                                    }
                                    // 활성화 그리드 셀에 포커스 복원
                                    grid.refresh();
                                    let sel = grid.selection, cell = grid.cells.getCellElement(sel.row, sel.col);
                                    if (cell) {
                                        cell.focus();
                                    }
                                }
                            }
                            });
                            // 완료
                            return menu;
                        }
                    }        
                
                    
                
 
 