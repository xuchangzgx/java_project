package cpm.wonders.mapper;

import cpm.wonders.entity.LinkMan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

@Mapper
public interface LinkManMapper {

    @Select("SELECT * FROM cst_linkman WHERE lkm_cust_id = #{custId}")
    public Set<LinkMan> getLinkMansByCustId(Long custId);

}
