package com.cmazxiaoma.mallSeckill.controller;

import com.cmazxiaoma.core.BaseController;
import com.cmazxiaoma.core.ResultVo;
import com.cmazxiaoma.core.ResultVoGenerator;
import com.cmazxiaoma.mallSeckill.model.Seckill;
import com.cmazxiaoma.mallSeckill.service.ISeckillService;
import com.cmazxiaoma.mallSeckill.vo.SeckillExecutionVo;
import com.cmazxiaoma.mallSeckill.vo.SeckillExposerVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author cmazxiaoma
 * @version V1.0
 * @Description: TODO
 * @date 2018/4/28 18:57
 */
@Slf4j
@Controller
@RequestMapping("/seckill")
public class SeckillController extends BaseController {

    @Resource
    private ISeckillService seckillService;

    /**
     * 秒杀列表
     * @return
     */
    @GetMapping(value = "/list")
    public String list() {
        List<Seckill> list = seckillService.findAll();
        modelMap.addAttribute("list", list);
        return "list";
    }

    /**
     * 秒杀详情
     * @param seckillId
     * @return
     */
    @GetMapping(value = "/detail/{seckillId}")
    public String detail(@PathVariable Long seckillId) {
        if (Objects.isNull(seckillId)) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.findById(seckillId);

        if (Objects.isNull(seckill)) {
            return "redirect:/seckill/list";
        }
        modelMap.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 秒杀地址暴露
     * @param seckillId
     * @return
     */
    @PostMapping(value = "/exposer/{seckillId}")
    @ResponseBody
    public ResultVo seckillExposer(@PathVariable Long seckillId) {
        ResultVo resultVo;

        try {
            SeckillExposerVo seckillExposerVo = seckillService.exportSeckillUrl(seckillId);
            resultVo = ResultVoGenerator.genSuccessResult(seckillExposerVo);
        } catch (Exception e) {
            log.error(e.getMessage());
            resultVo = ResultVoGenerator.genFailResult(e.getMessage());
        }
        return resultVo;
    }

    /**
     * 执行秒杀
     * @param seckillId
     * @param md5
     * @param userPhone
     * @return
     */
    @PostMapping(value = "/execution/{seckillId}/{md5}")
    @ResponseBody
    public ResultVo execute(@PathVariable Long seckillId,
                            @PathVariable String md5,
                            @CookieValue(value = "killPhone") Long userPhone) {
        if (Objects.isNull(userPhone)) {
            return ResultVoGenerator.genFailResult("未注册");
        }

        SeckillExecutionVo seckillExecutionVo = seckillService
                .executeSeckillProcedure(seckillId, userPhone, md5);
        return ResultVoGenerator.genSuccessResult(seckillExecutionVo);
    }

    @GetMapping(value = "/time/now")
    @ResponseBody
    public ResultVo now() {
        return ResultVoGenerator.genSuccessResult(new Date());
    }


}
