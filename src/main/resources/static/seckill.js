var seckill = {
    URL: {
        now: function () {
            return "/seckill/time/now"
        },
        exposer: function (seckillId) {
            return "/seckill/exposer/" + seckillId;
        },
        killUrl: function (seckillId, md5) {
            return "/seckill/execution/" + seckillId + "/" + md5;
        }
    },
    validatePhone: function (phone) {
        if (phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },

    handleSeckill: function (seckillId, node) {
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result.code && result.code === 200) {
                var seckillExposerVo = result['data'];
                if (seckillExposerVo['exposed']) {
                    var md5 = seckillExposerVo['md5'];
                    var killUrl = seckill.URL.killUrl(seckillId, md5);
                    console.log("killUrl:" + killUrl);

                    //绑定一次点击事件
                    $("#killBtn").one('click', function () {
                        $(this).addClass("disable");
                        $.post(killUrl, {}, function (result) {
                            if (result && result.code && result.code === 200) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }

                        });
                    });
                    node.show();
                } else {
                    //未开启秒杀
                    var now = seckillExposerVo['now'];
                    var start = seckillExposerVo['startTime'];
                    var end = seckillExposerVo['endTime'];
                    seckill.countdown();
                }
            } else {
                console.log("result:" + result);
            }
        })
    },

    //计时
    countDown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $("#seckill-box");
        //时间判断
        if (nowTime > endTime) {
            seckillBox.html("秒杀结束");
        } else if (nowTime < startTime) {
            //秒杀未开始
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                //控制时间格式
                var format = event.strftime("秒杀倒计时: %D天 %H时 %M分 %S秒");
                seckillBox.html(format);
            }).on("finish.countdown", function () {
                seckill.handleSeckill(seckillId, seckillBox);
            })
        } else {
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },

    //详情页秒杀
    detail: {
        init: function (params) {
            var killPhone = $.cookie("killPhone");

            if (!killPhone) {
                var killPhoneModal = $("#killPhoneModal");
                killPhoneModal.modal({
                    show: true,
                    backdrop: 'static',
                    keyboard: false
                });

                $("#killPhoneBtn").click(function () {
                    var inputPhone = $("#killPhoneKey").val();
                    //验证输入的手机号是否正确
                    if (seckill.validatePhone(inputPhone)) {
                        $.cookie("killPhone", inputPhone, {expires: 7, path:"/seckill"});
                        window.location.reload();
                    } else {
                        $("#killPhoneMessage").hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                });
            }
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(), {}, function(result) {
                if (result && result.code && result.code === 200) {
                    var nowTime = result['data'];
                    seckill.countDown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log("result:" + result);
                }
            });
        }
    }
}
