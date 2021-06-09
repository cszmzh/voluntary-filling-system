// 验证是否登录
LoginRequired();

$(getAllReport = function () {

    // 如果page参数不为空再赋值
    if (getQueryVariable("page")) {
        page = getQueryVariable("page");
    }

    $.ajax({
        url: backgroundURL + 'data/get',
        type: 'get',
        datatype: 'json',
        success: function (res) {

            let peopleNum = res.data.peopleNum;
            let labelFirst = [];
            let numFirst = [];
            let colorFirst = [];
            let labelSecond = [];
            let numSecond = [];
            let colorSecond = [];
            let totalNum = 0;

            // 第一志愿
            $.each(res.data.first, function (index, obj) {
                labelFirst.push(index);
                numFirst.push(obj);
                colorFirst.push(randomRgbaColor());
                totalNum += obj;
            })

            // 第二志愿
            $.each(res.data.second, function (index, obj) {
                labelSecond.push(index);
                numSecond.push(obj);
                colorSecond.push(randomRgbaColor());
                totalNum += obj;
            })

            $('#total-report').append(peopleNum);
            $('#total-will').append(totalNum);

            var ctx = document.getElementById("myChart1").getContext('2d');
            var myChart1 = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: labelFirst,
                    datasets: [{
                        label: '# of Votes',
                        data: numFirst,
                        backgroundColor: colorFirst,
                    }]
                },
                options: {
                    responsive: true
                }
            });

            var ctx = document.getElementById("myChart2").getContext('2d');
            var myChart2 = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: labelSecond,
                    datasets: [{
                        label: '# of Votes',
                        data: numSecond,
                        backgroundColor: colorSecond,
                    }]
                },
                options: {
                    responsive: true
                }
            });
        }
    })
});

function randomRgbaColor() { // 随机生成RGBA颜色
    var r = Math.floor(Math.random() * 256); // 随机生成256以内r值
    var g = Math.floor(Math.random() * 256); // 随机生成256以内g值
    var b = Math.floor(Math.random() * 256); // 随机生成256以内b值
    var alpha = Math.random(); // 随机生成1以内a值
    return `rgb(${r},${g},${b},${alpha})`; // 返回rgba(r,g,b,a)格式颜色
}
