package com.zz.amqp1.dao.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zz.amqp1.dao.entity.TXhCodeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface TXhCodeRepository extends BaseMapper<TXhCodeEntity> {

    // 根据玺号查询记录
    @Select("SELECT * FROM t_xh_code WHERE xh_code = #{xhCode}")
    TXhCodeEntity findByXhCode(@Param("xhCode") String xhCode);

    // 查询区电话号码中的最大值
    @Select("select * from t_xh_code WHERE tel_code = #{cityCode} AND count = " +
            "(select MAX(count) from t_xh_code WHERE tel_code = #{cityCode})")
    TXhCodeEntity findMaxXhCode(@Param("cityCode") String cityCode);

}
