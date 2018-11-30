package com.zz.amqp1.service;


import com.zz.amqp1.bean.XhCodeIModel;
import com.zz.amqp1.bean.XhCodeOModel;
import com.zz.amqp1.dao.entity.TXhCodeEntity;
import com.zz.amqp1.dao.repository.TXhCodeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 玺号专属服务
 * User: zhouzhou
 * Date: 2018-09-19
 * Time: 14:12
 */
@Service
public class XhCodeService {

    @Autowired
    private TXhCodeRepository tXhCodeRepository;

    /**
     * 根据城市区号获取最大玺号
     *
     * @param cityCode 城市区号
     * @return 玺号
     */
    public XhCodeOModel findMaxSignatureNo(String cityCode) {
        XhCodeOModel xhCodeOModel = new XhCodeOModel();
        TXhCodeEntity codeEntity = tXhCodeRepository.findMaxXhCode(cityCode);
        if (codeEntity != null) {
            BeanUtils.copyProperties(codeEntity, xhCodeOModel);
            return xhCodeOModel;
        } else {
            return null;
        }
    }

    /**
     * 创建玺号
     *
     * @param xhCode       玺号
     * @param xhCodeIModel 玺号model
     * @return 创建好的玺号
     */
    public XhCodeOModel createSignatureNo(String xhCode, XhCodeIModel xhCodeIModel) {
        // 幂等判断
        TXhCodeEntity byXhCode = tXhCodeRepository.findByXhCode(xhCode);
        if (byXhCode != null) {
            throw new RuntimeException(String.format("1玺号{%s}已存在记录", xhCode));
        } else {
            TXhCodeEntity entity = new TXhCodeEntity();
            BeanUtils.copyProperties(xhCodeIModel, entity);
            int save = tXhCodeRepository.insert(entity);
            Assert.notNull(save, String.format("1保存玺号{%s}失败", xhCode));
            XhCodeOModel xhCodeOModel = new XhCodeOModel();
            BeanUtils.copyProperties(xhCodeIModel, xhCodeOModel);
            return xhCodeOModel;
        }
    }

    /**
     * 根据城市电话code生成最大玺号
     * <p>
     * 细粒度锁, 锁的是城市区号,支持高并发
     * 先获取锁, 获取不到则继续等待300ms, 失败次数5次后跳出, 消费降级
     * </p>
     *
     * @param cityCode 城市电话区号
     * @return 玺号
     */
    public XhCodeOModel generateMaxXhCode(String cityCode) {
        // 先查最大玺号
        XhCodeOModel xhCodeOModel = findMaxSignatureNo(cityCode);
        XhCodeIModel xhCodeIModel = new XhCodeIModel();
        xhCodeIModel.setTelCode(cityCode);
        int newCount;
        // 如果是该电话区号第一个玺号则初始化
        if (xhCodeOModel != null) {
            newCount = xhCodeOModel.getCount() + 1;
        } else {
            newCount = 1;
        }
        xhCodeIModel.setCount(newCount);
        String newXhCode = cityCode + String.format("%07d", newCount);
        xhCodeIModel.setXhCode(newXhCode);
        // 创建玺号
        XhCodeOModel signatureNo = createSignatureNo(newXhCode, xhCodeIModel);
        return signatureNo;
    }

}
